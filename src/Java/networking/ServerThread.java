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
        super(socket);
    }

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
                    case Hub.NETNewConnection:
                        Hub.mainServer.newConnection(instream.readLine());
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

    public String getConnectedIP() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    public void stopConnection() {
        try {
            sendMessage("exit");

            if (outstream != null)
                outstream.close();
            if (instream != null) {
                instream.close();
                clientSocket.close();
            }
            setConnectionStatus(false);
        } catch (IOException e) {
            System.err.println("Unable to properly close client connection.");
        }
    }
}