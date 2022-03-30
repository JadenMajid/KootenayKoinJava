package Java.networking;

import java.io.IOException;
import java.net.Socket;

/**
 * Objects of this class are created to handle the server side of the connections in our network.
 */
class ServerThread extends NetworkingThread {

    protected ServerThread(Socket socket) {
        super(socket);
    }

    // Main thread loop. Call start() submethod to start thread instead of run().
    @Override
    public void run() {
        String receivedLine;
        try {
            while((receivedLine = instream.readLine()) != null) { // Read new line
                switch(receivedLine) { // Process received line
                    case Hub.NETExit:
                        stopConnection();
                        Hub.mainServer.removeServerThread(this);
                        return; // Stop running
                    default:
                        System.err.println("Unprocessed message from client: " + receivedLine);
                        break;
                }
            }
        } catch(IOException e) {
            System.err.println("Problem receiving message from client.");
            e.printStackTrace();
        } finally {
            stopConnection();
        }
    }

    // Returns the IP address this object is connected to.
    protected String getConnectedIP() {
        return socket.getInetAddress().getHostAddress();
    }
}