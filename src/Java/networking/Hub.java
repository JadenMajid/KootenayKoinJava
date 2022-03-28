package Java.networking;

import java.util.ArrayList;
import java.net.InetAddress;

public class Hub {
    static ArrayList<InetAddress> connectedAddresses;
    static final int PORT = 49666;
    protected static ClientAcceptingThread mainServer;
    //private ClientThreadFactory mainClient;

    public Hub() {
        mainServer = new ClientAcceptingThread();
        mainServer.start();
    }
}