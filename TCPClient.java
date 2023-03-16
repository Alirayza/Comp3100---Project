// Lmport Java.1o. I0Exception;
// import Java.net.ServerSocket;
// Lmport Java.net.SocketException;

import java.net.Socket;
import java.nio.Buffer;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.*;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

public class TCPClient{
static Socket s;
                    static DataOutputStream dout;
                    // din = new DataInputStream(s.getInputStream());
                    static BufferedReader in ;


static void sentText(String msg) throws IOException {

        dout.write((msg+"\n").getBytes());
        dout.flush();
        System.out.println("C Sent: "  + msg);
    }

  static String readText() throws IOException {
        String msg = in.readLine();
        System.out.println("S Received: " + msg);
        return msg;
    }
        public static void main(String[] args){
           // while(true){
                try{
                    s = new Socket("localhost",50000);
                    dout = new DataOutputStream(s.getOutputStream());
                    in  = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    //InetAddress aHost = InetAddress.getByName(args[0]);
                    //int aPort = Integer.parseInt(args[1]);
                    

                    System.out.println("Target IP: " + s.getInetAddress() + "Target Port: "+ s.getPort());
                    System.out.println("local IP: " + s.getLocalAddress() + "Local Port: "+ s.getLocalPort());
                    try {TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e){System.out.println(e);}

                    String text_string = "";

        sentText("HELO");

   
        text_string = readText();
        if (text_string.equals("OK")) {
            sentText("AUTH ALI");
        } else {
            System.out.println("not OK");
            dout.close();
            s.close();
            return;
        }

        
        text_string = readText();
        if (text_string.equals("OK")) {
            sentText("REDY");
        } else {
            System.out.println("Server not OK");
            dout.close();
            s.close();
            return;
        }

        readText();


        sentText("QUIT");

        readText();
      

        in.close();
        dout.close();
        s.close();
                }
                catch(Exception e){System.out.println(e);}
                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e){System.out.println(e);}


               }
           // }
        }
