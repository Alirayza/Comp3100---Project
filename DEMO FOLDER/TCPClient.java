/* Reference for code: 
 * 
 * Stage 1: Pseudo code for Largest Round Robin (LRR) https://content.ilearn.mq.edu.au/0b/1d/0b1dfdc5d985202002e9de6cbee2c743d3e1be3c?response-content-disposition=inline%3Bfilename%3D%22COMP3100_2023___Stage1_Pseudo_code.pdf%22&response-content-type=application%2Fpdf&Expires=1680293040&Signature=NeRuvp7efsftZEnroXEQGGgukvDZCdNiEty0RtfHblXH2JnPaq81E3Ou3BIaauKa0hH5sbzyrj4QnZDx716ZljGYF96dhs64zhjAZXGLdtESKBBqKaNzWlzyB5a7-eL8IacfgC4sYUwJJWd0S-DZDdbIIixz4zD282dBhqk79Mva7xJCHyyRzEL6CmbLIAWXpPXSOVXKLwwLttyPN-mm6U3JcU-ywuZyXfAFuq3ufMGieINvQGZN-Ca-KtI-lISHSjADCL7XTZW3SY-WKYXKdnCQFCKyzhgzGqKPWtKFJw~j7UjzXqVkA1IP7EzYOIAcAACMhDE~KPZ8O-mCfxcHEA__&Key-Pair-Id=APKAJAEFMXVVB5Z7N4TA
 * Yuzhe Tian - Week 6 Practical for LRR Protocol
 * https://stackoverflow.com/questions/3982550/creating-an-arraylist-of-objects
 * 
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPClient{
static Socket s;
static DataOutputStream dout;
static BufferedReader in ;

//Constructors for sending and receiving text from client and servers respectively.
public static void sendText(String text) throws IOException {
        dout.write((text+"\n").getBytes());
        dout.flush();

    }
public static String readText() throws Exception{ //
        String msg = in.readLine();
        return msg;
 
}


//seperate function for handshakeProtocol, returns the last received text from server.
public static String handshakeProtocol() throws Exception{
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
static void SCHDcommand(String command, int jobID, String serverType, int serverID) throws Exception{

        if(command.equals("JOBN")){ //checking that command is JOBN to perform SCHD
            String schd_job = "SCHD " + jobID + " " + serverType + " " + serverID; //compiling all job details
             sendText(schd_job); //sending it to client 
             readText(); //receiving the message from the server
        }    
}

        public static void main(String[] args) {

                try{
            s = new Socket("localhost",50000);
            dout = new DataOutputStream(s.getOutputStream());
            in  = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String serverType = ""; //serverType to keep track of the iteractive changes within code
            int serverID = 0; //serverID to keep track of the iteractive changes within code
            int coreNumbers = 0;//coreNumbers of the server to keep track of the iteractive changes within code
            int jobID = 0; //jobID retrieved from the jobList that needs to be assigned.
            int serverTypeNumber =1; //type number of the server
            boolean LLRStart = true; //boolean variable to determinie whether LRR code should be executed or not
            int flagcounter = 0; //flagcounter to determine whether LRR protocol was executed successfully or not.
            String recentMessage =" "; //to record recent message from the server.

            
            
   
        recentMessage = handshakeProtocol(); //returns the recent from server after Handshake protocol
       
                while (!recentMessage.equals("NONE")){ //checking that last message from the server is not NONE
                    
                    sendText("REDY");
                    recentMessage = readText();
                    if(recentMessage.equals("NONE")){ //after receiving message from the server after REDY, need to check again that it is not sending NONE
                        break;
                    }
                   
                    String [] jobList = recentMessage.split(" "); //recording the job info
                    
                    
                    jobID = Integer.parseInt(jobList[2]); //retrieving the jobID
                    
                    if (LLRStart){ //starting LRR Protocol 
                    sendText("GETS All"); //client
                    
                    recentMessage = readText();//server mesage
                
    
				    String[] allserverList = recentMessage.split(" "); //recording the all server info that are on the system
				    Integer nRecs = Integer.parseInt(allserverList[1]); //number of servers/records from the retrived information
                   
                    sendText("OK");

                    for(int i = 0; i < nRecs; i++) { //looping through all servers to find the largest servers 
                        String msg = readText();
                        // if(msg.equals(".")){ break;} //checking that '.' is being sent at the start of the
                      
                        String [] LRRServer = msg.split(" ");//recording all largest server information
                        ArrayList<ServerClass> Server = new ArrayList<ServerClass>();
                        Server.add(new ServerClass(LRRServer[0], serverID, serverTypeNumber, Integer.parseInt(LRRServer[4]))); //recording all server information into an arrayList
                        if  (Server.get(0).serverType.equals(serverType)){ //checking if server 
                            serverTypeNumber++; //incrementing the number of servers with same type by 1

                        }
                        if (Server.get(0).CPUCoreNo > coreNumbers) {
                            serverTypeNumber = 1; //resetting the server number of type
                            serverType = Server.get(0).serverType; //recording the servertype
                            coreNumbers = Server.get(0).CPUCoreNo; //recording number of cores for the server
                        } 

                    }
                    sendText("OK");
                    recentMessage = readText();
                    LLRStart = false; //setting the LRR flag to false meaning that LRR Protocol is done
                    flagcounter = 1; //setting the flagcount to 1 meaning that the LRR is successfully done indicating we can more on to SCHD
                    
                } else if (flagcounter == 0){ //checking if flag counter meaning the LRR protocol was  successful or not 
                    LLRStart = true; //then set LRRflag to true meaning we have to conduct LRR protocol again.

                
       
                }
                serverID=jobID%serverTypeNumber; //assigning serverID by using number of server types and jobID.
                    SCHDcommand(jobList[0], jobID, serverType, serverID);
                    

                }
        sendText("QUIT");
        readText();
        in.close();
        dout.close();
        s.close();
                }
                catch(Exception e){System.out.println(e);}
         
               }
           // }
        }
