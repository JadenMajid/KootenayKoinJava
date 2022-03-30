package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends NetworkingThread {
    private BufferedReader instream;
    private String ip;

    public ClientThread(String ip) {
        this.ip = ip;
    }

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
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to establish clientside IO streams.");
            e.printStackTrace();
        }
    }
}