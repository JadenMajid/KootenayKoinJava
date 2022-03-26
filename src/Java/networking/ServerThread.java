package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends NetworkingThread {
    private Socket clientSocket;
    private BufferedReader instream;
    private PrintWriter outstream;

    public ServerThread(Socket socket) {
        super(socket, true);
    }

    @Override
    public void run() {
        String receivedLine;
        try {
            while((receivedLine = instream.readLine()) != null) { // Read new line
                switch(receivedLine) { // Process received line
                    case "Exit":
                        stopConnection();
                        break;
                    default:
                        System.err.println("Unprocessed message from client: " + receivedLine);
                }
            }
        } catch(IOException e) {
            System.err.println("Problem receiving message from client.");
            e.printStackTrace();
        } finally {
            stopConnection();
        }
    }

    public void stopConnection() {
        try {
            sendMessage("exit");

            if (this.outstream != null)
                this.outstream.close();
            if (this.instream != null) {
                this.instream.close();
                this.clientSocket.close();
            }
            this.setConnectionStatus(false);
        } catch (IOException e) {
            System.err.println("Unable to properly close client connection.");
        }
    }
}