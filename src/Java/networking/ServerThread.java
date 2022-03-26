package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outstream;
    private BufferedReader instream;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Client.PORT); // Random port
        } catch (IOException e) {
            System.err.println("Unable to start ServerThread on port " + Client.PORT);
            e.printStackTrace();
        }

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Unable to accept incoming connection to ServerThread.");
            e.printStackTrace();
        }

        try {
            outstream = new PrintWriter(clientSocket.getOutputStream(), true);
            instream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Unable to establish serverside IO streams.");
            e.printStackTrace();
        }
    }

    public void end() {
        try {
            outstream.close();
            instream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ServerThread sockets/streams.");
        }
    }
}