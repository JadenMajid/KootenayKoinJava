package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread extends NetworkingThread {
    private BufferedReader instream;
    private InetAddress ip;

    public ClientThread(Socket socket) {
        super(socket, false);
    }

    @Override
    public void run() {
        try {
            this.setSocket(new Socket(this.ip.getHostAddress(), Client.PORT));
        } catch (IOException e) {
            System.err.println("Unable to establish socket connection."
                    + "(Has the IP for this thread been set?) Thread IP: " + this.ip.toString());
            e.printStackTrace();
        }

        try {
            setIOStreams();

            String receivedLine;
            while((receivedLine = instream.readLine()) != null) { // Read new line
                switch(receivedLine) { // Process received line
                    case "Exit":
                        stopConnection();
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

    public void startNewThreadedConnection(InetAddress ip) {
        this.ip = ip;
        this.start();
    }
}