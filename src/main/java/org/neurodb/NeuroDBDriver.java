package org.neurodb;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeuroDBDriver {
    private Socket s;
    private InputStream is;
    private OutputStream os;
    private BufferedWriter bw;
    private BufferedReader br;

    public NeuroDBDriver(String address, int port) {
        try {
            s = new Socket(address, port);
            is = s.getInputStream();
            os = s.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os));
            br = new BufferedReader(new InputStreamReader(is));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
            bw.close();
            br.close();
            is.close();
            os.close();
            s.close();
    }

    public ResultSet executeQuery(String query) throws Exception {
        try {
            bw.write(query);
            bw.flush();

            ResultSet resultSet = new ResultSet();
            char type = (char) br.read();
            switch (type) {
                case '@':
                    resultSet.setStatus(ResultStatus.PARSER_OK.getType());
                    break;
                case '$':
                    resultSet.setMsg(br.readLine());
                    break;
                case '#':
                    resultSet.setMsg(br.readLine());
                    break;
                case '*':
                    String line = br.readLine();
                    String[] head = line.split(",");
                    resultSet.setStatus(Integer.valueOf(head[0]));
                    resultSet.setCursor(Integer.valueOf(head[1]));
                    resultSet.setResults(Integer.valueOf(head[2]));
                    resultSet.setAddNodes(Integer.valueOf(head[3]));
                    resultSet.setAddLinks(Integer.valueOf(head[4]));
                    resultSet.setModifyNodes(Integer.valueOf(head[5]));
                    resultSet.setModifyLinks(Integer.valueOf(head[6]));
                    resultSet.setDeleteNodes(Integer.valueOf(head[7]));
                    resultSet.setDeleteLinks(Integer.valueOf(head[8]));

                    int bodyLen = Integer.parseInt(head[9]);
                    char[] body = new char[bodyLen];
                    int len = br.read(body);
                    br.readLine();
                    String bodyStr = String.valueOf(body);
                    RecordSet recordSet = deserializeReturnData(bodyStr);
                    resultSet.setRecordSet(recordSet);
                    break;
                default:
                    throw new Exception("reply type error");
            }
            return resultSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    final static char NEURODB_EXIST = 65527;
    final static char NEURODB_NIL = 65528;
    final static char NEURODB_RECORD = 65529;
    final static char NEURODB_RECORDS = 65530;
    final static char NEURODB_NODES = 65531;
    final static char NEURODB_LINKS = 65532;
    final static char NEURODB_RETURNDATA = 65533;
    final static char NEURODB_SELECTDB = 65534;
    final static char NEURODB_EOF = 65535;

    final static char NDB_6BITLEN = 0;
    final static char NDB_14BITLEN = 1;
    final static char NDB_32BITLEN = 2;
    final static char NDB_ENCVAL = 3;
    //final static char NDB_LENERR =UINT_MAX;

    final static char VO_STRING = 1;
    final static char VO_NUM = 2;
    final static char VO_STRING_ARRY = 3;
    final static char VO_NUM_ARRY = 4;
    final static char VO_NODE = 5;
    final static char VO_LINK = 6;
    final static char VO_PATH = 7;
    final static char VO_VAR = 8;
    final static char VO_VAR_PATTERN = 9;

    class StringCur {
        String s;
        int cur;

        public StringCur(String s) {
            this.s = s;
            this.cur = 0;
        }

        public String get(int size) {
            String subStr = s.substring(cur, cur+size);
            cur += size;
            return subStr;
        }

        public char getType() {
            char type = s.charAt(cur);
            cur += 1;
            return type;
        }
    }

    static char deserializeType(StringCur cur) {
        return cur.getType();
    }

    static int deserializeUint(StringCur cur) throws Exception {
        int []buf =new int[2];
        buf[0] = cur.get(1).charAt(0);
        int type;
        type = (buf[0] & 0xC0) >> 6;
        if (type == NDB_6BITLEN) {
            /* Read a 6 bit len */
            return buf[0] & 0x3F;
        } else if (type == NDB_14BITLEN) {
            buf[1] = cur.get(1).charAt(0);
            return ((buf[0] & 0x3F) << 8) | buf[1];
        } else if (type == NDB_32BITLEN) {
            /* Read a 32 bit len */
//            if (( * ss)[0] =='\0' || ( * ss)[1] =='\0' || ( * ss)[2] =='\0' || ( * ss)[3] =='\0')
//            return NDB_LENERR;
//            memcpy( & len, ( * ss),4);
//            ( * ss) +=4;
//            return len; // ntohl(len);

            int i = cur.get(1).charAt(0) & 0xFF | //
                    (cur.get(1).charAt(0) & 0xFF) << 8 | //
                    (cur.get(1).charAt(0) & 0xFF) << 16 | //
                    (cur.get(1).charAt(0) & 0xFF) << 24; //
            return i;
        }
        else {
            throw new Exception("Unknown type");
        }

    }

    static String deserializeString(StringCur cur) throws Exception {
        int len;
        String val;
        len = deserializeUint(cur);
        val = cur.get(len);
        return val;
    }

    static List deserializeStringList(StringCur cur) throws Exception {
        int listlen;
        listlen = deserializeUint(cur);
        List<String> l = new ArrayList<String>();
        while (listlen-- > 0) {
            String s = deserializeString(cur);
            l.add(s);
        }
        return l;
    }

    static List deserializeLabels(StringCur cur, List labeList) throws Exception {
        int listlen;
        listlen = deserializeUint(cur);
        List<String> l = new ArrayList<String>();
        while (listlen-- > 0) {
            int i = deserializeUint(cur);
            l.add(labeList.get(i).toString());
        }
        return l;
    }

    static Map<String, ColVal> deserializeKVList(StringCur cur, List keyNames) throws Exception {
        int listlen;
        listlen = deserializeUint(cur);
        Map<String, ColVal> properties = new HashMap<String, ColVal>();
        while (listlen-- > 0) {
            int i = deserializeUint(cur);
            String key = keyNames.get(i).toString();
            int type;
            type = deserializeUint(cur);
            int aryLen = 0;
            ColVal val = new ColVal();
            val.setType(type);
            if (type == VO_STRING) {
                val.setVal(deserializeString(cur));
            } else if (type == VO_NUM) {
                String doubleStr = deserializeString(cur);
                val.setVal(Double.parseDouble(doubleStr));
            } else if (type == VO_STRING_ARRY) {
                aryLen = deserializeUint(cur);
                String[] valAry = new String[aryLen];
                for (i = 0; i < aryLen; i++) {
                    valAry[i] = deserializeString(cur);
                }
                val.setVal(valAry);
            } else if (type == VO_NUM_ARRY) {
                aryLen = deserializeUint(cur);
                double[] valAry = new double[aryLen];
                for (i = 0; i < aryLen; i++) {
                    String doubleStr = deserializeString(cur);
                    valAry[i] = Double.parseDouble(doubleStr);
                }
                val.setVal(valAry);
            } else {
                //printf("loading pkvType ERROR ");
                throw new Exception("Error Type");
            }
            properties.put(key, val);
        }
        return properties;
    }

    static Node deserializeCNode(StringCur cur, List labels, List keyNames) throws Exception {
        long id;
        List nlabels = null;
        Map<String, ColVal> kvs = null;
        id = deserializeUint(cur);
        nlabels = deserializeLabels(cur, labels);
        kvs = deserializeKVList(cur, keyNames);
        Node n = new Node(id, nlabels, kvs);
        return n;
    }

    static Link deserializeCLink(StringCur cur, List types, List keyNames) throws Exception {
        long id, hid, tid;
        int typeIndex;
        String type = null;
        Map<String, ColVal> kvs = null;
        id = deserializeUint(cur);
        hid = deserializeUint(cur);
        tid = deserializeUint(cur);
        int ty;
        ty = deserializeType(cur);
        if (ty == NEURODB_RETURNDATA) {//NEURODB_EXIST
            typeIndex = deserializeUint(cur);
            type = types.get(typeIndex).toString();
        } else if (ty == NEURODB_NIL) {
        }
        kvs = deserializeKVList(cur, keyNames);
        Link l = new Link(id, hid, tid, type, kvs);
        return l;
    }
    Node getNodeById(List<Node> nodes,long id){
        for(Node node:nodes){
            if(node.getId()==id)
                return node;
        }
        return null;
    }
    Link getLinkById(List<Link> links,long id){
        for(Link link:links){
            if(link.getId()==id)
                return link;
        }
        return null;
    }

    RecordSet deserializeReturnData(String body) throws Exception {
        StringCur cur = new StringCur(body);
        RecordSet rd = new RecordSet();
        List path = null;
        /*读取labels、types、keyNames列表*/
        if (deserializeType(cur) != NEURODB_RETURNDATA)
            throw new Exception("Error Type");
        rd.setLabels(deserializeStringList(cur));
        rd.setTypes(deserializeStringList(cur));
        rd.setKeyNames(deserializeStringList(cur));
        /*读取节点列表*/
        if (deserializeType(cur) != NEURODB_RETURNDATA) //NEURODB_NODES
            throw new Exception("Error Type");
        int cnt_nodes;
        cnt_nodes = deserializeUint(cur);
        for (int i = 0; i < cnt_nodes; i++) {
            Node n = deserializeCNode(cur, rd.getLabels(), rd.getKeyNames());
            rd.getNodes().add(n);
        }
        /*读取关系列表*/
        if (deserializeType(cur) != NEURODB_RETURNDATA) //NEURODB_LINKS
            throw new Exception("Error Type");
        int cnt_links;
        cnt_links = deserializeUint(cur);
        for (int i = 0; i < cnt_links; i++) {
            Link l = deserializeCLink(cur, rd.getTypes(), rd.getKeyNames());
            rd.getLinks().add(l);
        }
        /*读取return结果集列表*/
        if (deserializeType(cur) != NEURODB_RETURNDATA)//NEURODB_RECORDS
            throw new Exception("Error Type");
        int cnt_records;
        cnt_records = deserializeUint(cur);
        for (int i = 0; i < cnt_records; i++) {
            int type, cnt_column;
            type = deserializeType(cur);
            cnt_column = deserializeUint(cur);
            List record = new ArrayList();
            for (int j = 0; j < cnt_column; j++) {
                int aryLen = 0;
                type = deserializeType(cur);
                ColVal val = new ColVal();
                val.setType(type);
                if (type == NEURODB_NIL) {
                    /*val =NULL;*/
                } else if (type == VO_NODE) {
                    int id;
                    id = deserializeUint(cur);
                    Node n=getNodeById(rd.getNodes(), id);
                    val.setVal(n);
                } else if (type == VO_LINK) {
                    int id;
                    id = deserializeUint(cur);
                    Link l = getLinkById(rd.getLinks(), id);
                    val.setVal(l);
                } else if (type == VO_PATH) {
                    int len;
                    len = deserializeUint(cur);
                    path = new ArrayList();
                    for (i = 0; i < len; i++) {
                        int id;
                        id = deserializeUint(cur);
                        if (i % 2 == 0) {
                            Node nd = getNodeById(rd.getNodes(), id);
                            path.add(nd);
                        } else {
                            Link lk = getLinkById(rd.getLinks(), id);
                            path.add(lk);
                        }
                    }
                    val.setVal(path);
                } else if (type == VO_STRING) {
                    val.setVal(deserializeString(cur));
                } else if (type == VO_NUM) {
                    String doubleStr = deserializeString(cur);
                    val.setVal(Double.parseDouble(doubleStr));
                } else if (type == VO_STRING_ARRY) {
                    aryLen = deserializeUint(cur);
                    String[] valAry = new String[aryLen];
                    for (i = 0; i < aryLen; i++) {
                        valAry[i] = deserializeString(cur);
                    }
                    val.setVal(valAry);
                } else if (type == VO_NUM_ARRY) {
                    aryLen = deserializeUint(cur);
                    double[] valAry = new double[aryLen];
                    for (i = 0; i < aryLen; i++) {
                        String doubleStr = deserializeString(cur);
                        valAry[i] = Double.parseDouble(doubleStr);
                    }
                    val.setVal(valAry);
                } else {
                    throw new Exception("Error Type");
                }
                record.add(val);
            }
            rd.getRecords().add(record);
        }
        /*读取结束标志*/
       if (deserializeType(cur) != NEURODB_RETURNDATA)//NEURODB_EOF
            throw new Exception("Error Type");
        return rd;
    }
}
