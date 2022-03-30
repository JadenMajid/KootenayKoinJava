package Java.networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * TODO:
 * - Implement blockchain validation through the network
 * - Encrypt network communication
 * - Move network related variables to ClientAcceptingThread (possibly rename it to MainServer),
 *  update all references to this class and delete this file
 */

/**
 * Pending removal, serves no real purpose.
 */
@Deprecated(forRemoval = true)
public class Hub {
    static final int PORT = 49666;
    static String localhostIP;
    protected static ClientAcceptingThread mainServer;

    protected static final String NETExit = "Exit";
    protected static final String NETNewConnection = "New Connect";

    public Hub() {
        try {
            localhostIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Unable to get localhost IP.");
            e.printStackTrace();
        }

        mainServer = new ClientAcceptingThread();
        mainServer.start();
    }
}