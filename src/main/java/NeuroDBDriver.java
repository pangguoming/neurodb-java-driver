import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class NeuroDBDriver {

    private Socket s;
    private InputStream is;
    private OutputStream os;
    private BufferedWriter bw;
    private BufferedReader br;

    public NeuroDBDriver(String address,int port) {
        try {
            s = new Socket(address, port);
            is = s.getInputStream();
            os = s.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os));
            br = new BufferedReader(new InputStreamReader(is));
        } catch(UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw.close();
            br.close();
            is.close();
            os.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(String query){
        try {
            bw.write(query);
            bw.flush();

            String mess = br.readLine();
            char type=mess.charAt(0);
            switch (type)
            {
                case '@':
                   // printf("OK\n");
                case '$':
                   // printf("ERROR:\n");
                   // return cliReadMsg(fd);
                case '#':
                   // printf("INFO:\n");
                   // return cliReadMsg(fd);
                case '*':
                   // return cliReadBulkReply(fd);
                default:
                    //printf("protocol error, got '%c' as reply type byte\n", type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
