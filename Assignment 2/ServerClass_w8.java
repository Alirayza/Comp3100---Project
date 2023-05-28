public class ServerClass{

    String serverType; //[0]
    int serverTypeNumber; //[1]
    int availableCores;
    int availableMemory;
    int availableDisk;

   
    public ServerClass(String serverType,int serverTypeNumber, int availableCores, int availableMemory, int availableDisk ) {
        this.serverType = serverType;
        this.serverTypeNumber = serverTypeNumber;
        this.availableDisk = availableDisk;
        this.availableCores = availableCores;
        this.availableMemory = availableMemory;


    }
}
