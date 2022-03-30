package Java.networking;

import java.io.IOException;
import java.net.Socket;

/**
 * Objects of this class are created to handle the client side of the connections in our network.
 */
class ClientThread extends NetworkingThread {
    private String ip;

    protected ClientThread(String ip) {
        this.ip = ip;
    }

    // Main thread loop. Call start() submethod to start thread instead of run().
    @Override
    public void run() {
        try {
            setSocket(new Socket(ip, Hub.PORT));
        } catch (IOException e) {
            System.err.println("Unable to establish socket connection."
                    + "(Has the IP for this thread been set?) Thread IP: " + ip);
            e.printStackTrace();
        }

        try {
            setIOStreams();

            String receivedLine;
            while((receivedLine = instream.readLine()) != null) { // Read new line
                switch(receivedLine) { // Process received line
                    case Hub.NETExit:
                        stopConnection();
                        return; // Stop running
                    case Hub.NETNewConnection:
                        Hub.mainServer.newConnection(instream.readLine());
                        break;
                    default:
                        System.err.println("Unprocessed message from client: " + receivedLine);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to establish clientside IO streams.");
            e.printStackTrace();
        }
    }
}