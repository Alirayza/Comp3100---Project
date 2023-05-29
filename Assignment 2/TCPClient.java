
/* Reference for code: 
 * 
 * 
 * Yuzhe Tian - Week 9 & 10 Practical for FC &FF
 * https://stackoverflow.com/questions/3982550/creating-an-arraylist-of-objects
 * 
 */
import java.net.*;
import java.io.*;

class TCPClient{
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
        recentMessage = readText(); //sending REDY message

        while(!recentMessage.equals("NONE")){

            String[] jobList = recentMessage.split(" ");


            if(jobList[0].equals("JOBN")){ //checking if the server wants to schedule jobs
              
                int coreNumbers = Integer.parseInt(String.valueOf(jobList[4]));//coreNumbers retreive
                int memoryNum = Integer.parseInt(String.valueOf(jobList[5]));//memoryNum retreive
                int diskNum = Integer.parseInt(String.valueOf(jobList[6])); //diskNum retreive
                int jobID = Integer.parseInt(jobList[2]); //retrieving the jobID
                String meseg = "GETS Available" + " " + coreNumbers + " " + memoryNum + " " + diskNum + "\n".getBytes();//checking for available servers
                sendText(meseg);
                recentMessage = readText();   
                String[] allserverList = recentMessage.split(" ");//storing available servers
                int nRecs = 0;
                
                if (Integer.parseInt(allserverList[1]) ==0) //checking if from GETS Avail, there were any servers
            {
                sendText("OK");
                recentMessage = readText();
                meseg = "GETS Capable" + " " + coreNumbers + " " + memoryNum + " " + diskNum + "\n".getBytes(); //do GETS capable then to find servers
                sendText(meseg);
                recentMessage = readText();   
                
                
    
            } 
                allserverList = recentMessage.split(" ");
                nRecs = Integer.parseInt(allserverList[1]); //store GETS Cap or GETS Avail servers details
                sendText("OK");
                
    
                recentMessage = readText(); 

                String [] selectedServerDetails = recentMessage.split(" "); //detach first server from the list of collected server
                serverType = selectedServerDetails[0]; //serverType recording
                coreNumbers = Integer.parseInt(jobList[4]); //recording coreNumbers
                
                recentMessage = traverseRemain(nRecs, recentMessage); //traversing through rest of the servers
    
            
                sendText("OK");
                recentMessage = readText(); 
                SCHDcommand(jobList[0],jobID,serverType,Integer.parseInt((selectedServerDetails[1]))); //scheduing jobs to selected first servers
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

    public static String traverseRemain(int nRecs, String RecentMessage){ //traversing rest of the servers in the list.
        for(int i = 1; i < nRecs; i++){
            recentMessage = readText();
        }
        return RecentMessage;
    }
}

  


 
