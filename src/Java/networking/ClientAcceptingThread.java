package Java.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class ClientAcceptingThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(Client.PORT);
        } catch(IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Client.PORT);
            e.printStackTrace();
        }

        while(true) {
            try {
                this.clientSocket = this.serverSocket.accept();

                // Debug
                System.out.println("New client connected: " + this.clientSocket);
    
                Thread t = new ServerThread(this.clientSocket, new DataInputStream(clientSocket.getInputStream()),  new DataOutputStream(clientSocket.getOutputStream()));

                t.start();
            } catch(IOException e) {
                System.err.println("Unable to accept incoming connection to ServerThread.");
                e.printStackTrace();
            }
        }
    }

    public void end() {
        try {
            this.clientSocket.close();
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ClientAcceptingThread socekts.");
        }
    }
}