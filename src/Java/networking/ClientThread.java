package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private PrintWriter outstream;
    private BufferedReader instream;
    private InetAddress ip;

    @Override
    public void run() {
        try {
            socket = new Socket(ip.getHostAddress(), Client.PORT);
        } catch (IOException e) {
            System.err.println("Unable to establish socket connection."
                    + "(Has the IP for this thread been set?) Thread IP: " + ip.toString());
            e.printStackTrace();
        }

        try {
            outstream = new PrintWriter(socket.getOutputStream(), true);
            instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to establish clientside IO streams.");
            e.printStackTrace();
        }
    }

    public void startNewThreadedConnection(InetAddress ip) {
        this.ip = ip;
        this.start();
    }

    public void stopConnection() {
        try {
            instream.close();
            outstream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ClientThread socket/streams.");
            e.printStackTrace();
        }
    }
}