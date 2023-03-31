
import java.net.Socket;
import java.io.*;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

public class TCPClient_w5{
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

    static String readTextJob() throws IOException {
        String msg = in.readLine();
        System.out.println("S Received: " + msg);
        return msg;
    }
        public static void main(String[] args){
            
            
           // while(true){
                try{
                    String serverType = "";
            int serverID = 0;
            int serverCore = 0;
            int jobID = 0;
            String str;
                    s = new Socket("localhost",50001);
                    dout = new DataOutputStream(s.getOutputStream());
                    in  = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    //InetAddress aHost = InetAddress.getByName(args[0]);
                    //int aPort = Integer.parseInt(args[1]);
                   

                    System.out.println("Target IP: " + s.getInetAddress() + "Target Port: "+ s.getPort());
                    System.out.println("local IP: " + s.getLocalAddress() + "Local Port: _"+ s.getLocalPort());
                    //try {TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e){System.out.println(e);}
                    str = in.readLine();
                    String text_string = new String(str);
                    System.out.println(text_string);

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

        str = in.readLine();
        System.out.println("RCVD:" + str);
        String[] jobInfo = str.split(" ");
        jobID = Integer.parseInt(jobInfo[2]);

        System.out.println("SENT: GETS All");
        dout.write(("GETS All\n").getBytes());
        
        
        int nRecs = Integer.parseInt(jobInfo[1]);

        dout.write(("OK\n").getBytes());
        dout.flush();
        
        //variables to store largest server core and largetst server DATA
        int j = 0;
        String[] large = new String[10];
        //int nCores = 0;
        String sType = "";
        String[] arrSpecs;

        for(int i = 0; i < nRecs; i++) {
            readText();
            
            arrSpecs = str.split(" ", 9);
            int coreNum = Integer.parseInt(String.valueOf(arrSpecs[4]));
            
            if (j < coreNum) {
                j = coreNum;
                large = arrSpecs;
            } 
        }	
        
        //sentText("OK");
        String info1 = ("SCHD " + jobInfo[2] + " " + large[0] + " " + large[1]).toString();
        sentText(info1);
        //dout.flush();
        
        sentText("OK");



        String[] jobArr = text_string.split(" ");
        System.out.println(text_string);
        if(jobArr[0].equals("JOBN")){
            String info2 = "SCHD " + jobArr[2] + " " + sType + " 0";
            sentText(info2);
            readText();
        }




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
