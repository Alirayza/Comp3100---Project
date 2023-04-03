public class ServerClass{

    String serverType; //[0]
    int serverID; //[1]
    int serverTypeNumber; //[2]
    int CPUCoreNo; //[3]

   
    public ServerClass(String serverType, int serverID, int serverTypeNumber, int CPUCoreNo ) {
        this.serverType = serverType;
        this.serverID = serverID;
        this.serverTypeNumber = serverTypeNumber;
        this.CPUCoreNo = CPUCoreNo;

    }
}