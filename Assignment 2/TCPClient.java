import java.net.*;
import java.io.*;

class TCPClient {
    static BufferedReader in;
    static Socket s;
    static DataOutputStream dout;
    static String recentMessage;


//Constructors for sending and receiving text from client and servers respectively.
public static void sendText(String text)  {
    try{
    dout.write((text+"\n").getBytes());
    dout.flush();
    }catch(IOException e) {
        e.printStackTrace();
    }


}
public static String readText(){ //
    String msg = "";
    try{
        msg = in.readLine();
    }
    catch(IOException e) {
        e.printStackTrace();
    }
    return msg;
}
public static String handshakeProtocol(){
    sendText("HELO");
    String message = readText();
    if (message.equals("OK")) {//checking if the message from the server is OK or not
        String us = System.getProperty("user.name"); // reference for username: https://stackoverflow.com/questions/18353856/system-getpropertyuser-name-returns-hostname-instead-of-currently-logged-use
        sendText("AUTH " +us); //sending AUTH with account's username
        message = readText(); //receiving the message from the server
        return message;
    } else {//if message from server not OK then do this part
    return "ERROR: cannot establish a connection between SERVER & CLIENT";
    }

  
   
}

static void SCHDcommand(String command, int jobID, String serverType, int serverID) {

    if(command.equals("JOBN")){ //checking that command is JOBN to perform SCHD
        String schd_job = "SCHD " + jobID + " " + serverType + " " + serverID; //compiling all job details //assigning serverID by computing the remainder of jobID and serverTypeNumber that corresponds to the pattern of serverID
         sendText(schd_job); //sending it to client 
         readText(); //receiving the message from the server
    }    
}
    public static void main(String[] args) throws IOException {

        String serverType = ""; 

            s = new Socket("localhost",50000);
            dout = new DataOutputStream(s.getOutputStream());
            in  = new BufferedReader(new InputStreamReader(s.getInputStream()));

     
        recentMessage = handshakeProtocol();

   
        sendText("REDY");
        recentMessage = readText(); 

        while(!recentMessage.equals("NONE")){

            String[] jobList = recentMessage.split(" ");


            if(jobList[0].equals("JOBN")){
              
                int coreNumbers = Integer.parseInt(String.valueOf(jobList[4]));
                int memoryNum = Integer.parseInt(String.valueOf(jobList[5]));
                int diskNum = Integer.parseInt(String.valueOf(jobList[6]));
                int jobID = Integer.parseInt(jobList[2]); //retrieving the jobID
                String meseg = "GETS Available" + " " + coreNumbers + " " + memoryNum + " " + diskNum + "\n".getBytes();
                sendText(meseg);
                recentMessage = readText();   
                String[] allserverList = recentMessage.split(" ");
                int nRecs = 0;
                
                if (Integer.parseInt(allserverList[1]) ==0)
            {
                sendText("OK");
                recentMessage = readText();
                meseg = "GETS Capable" + " " + coreNumbers + " " + memoryNum + " " + diskNum + "\n".getBytes();
                sendText(meseg);
                recentMessage = readText();   
                
                
    
            } 
                allserverList = recentMessage.split(" ");
                nRecs = Integer.parseInt(allserverList[1]);
                sendText("OK");
                
    
                recentMessage = readText(); 
                String Server = recentMessage;
                String [] selectedServerDetails = Server.split(" ");
                serverType = selectedServerDetails[0]; 
                coreNumbers = Integer.parseInt(jobList[4]);
                
                recentMessage = traverseRemain(nRecs, recentMessage);
    
            
                sendText("OK");
                recentMessage = readText(); 
                SCHDcommand(jobList[0],jobID,serverType,Integer.parseInt((selectedServerDetails[1])));
            }
            
            sendText("REDY");
            recentMessage = readText(); 
        }

 

        sendText("QUIT");
        recentMessage = readText(); 
        in.close();
        dout.close();
        s.close();
    }
}

  


 
