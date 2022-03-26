package Java.networking;

import java.util.ArrayList;
import java.net.InetAddress;

public class Client {
    static ArrayList<InetAddress> connectedAddresses;
    static final int PORT = 49666;
    private ClientAcceptingThread mainServer;
    //private ClientThreadFactory mainClient;

    public Client() {
        this.mainServer = new ClientAcceptingThread();
        this.mainServer.start();
    public Client() {

=======
>>>>>>> Stashed changes
    }
}