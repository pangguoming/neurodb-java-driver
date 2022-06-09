//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;

class Test {
    public static void main(String[] args) {
        NeuroDBDriver neuroDBDriver=new NeuroDBDriver("127.0.0.1",8839);
        neuroDBDriver.executeQuery("match (n) return n");
        neuroDBDriver.close();
//        try {
//            Socket s = new Socket("127.0.0.1",8839);
//            //构建IO
//            InputStream is = s.getInputStream();
//            OutputStream os = s.getOutputStream();
//
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//            //向服务器端发送一条消息
//            bw.write("match (n) return n\n");
//            bw.flush();
//
//            //读取服务器返回的消息
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String mess = br.readLine();
//
//            char type=mess.charAt(0);
//            switch (type)
//            {
//                case '@':
//                    printf("OK\n");
//                case '$':
//                    printf("ERROR:\n");
//                    return cliReadMsg(fd);
//                case '#':
//                    printf("INFO:\n");
//                    return cliReadMsg(fd);
//                case '*':
//                    return cliReadBulkReply(fd);
//                default:
//                    //printf("protocol error, got '%c' as reply type byte\n", type);
//            }
//
//            System.out.println("服务器："+mess);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
