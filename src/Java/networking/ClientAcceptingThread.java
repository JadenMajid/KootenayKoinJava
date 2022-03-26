package Java.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Client.PORT); // Random port
        } catch (IOException e) {
            System.err.println("Unable to start ClientAcceptingThread on port " + Client.PORT);
            e.printStackTrace();
        }

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Unable to accept incoming connection to ClientAcceptingThread.");
            e.printStackTrace();
        }
    }

    public void end() {
        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ClientAcceptingThread socekts.");
        }
    }
}