package Java.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class ClientAcceptingThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private LinkedList<ServerThread> serverThreads;
    private LinkedList<ClientThread> clientThreads;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(Hub.PORT);
        } catch (IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Hub.PORT);
            e.printStackTrace();
        }

        while(true) {
            try {
                this.clientSocket = this.serverSocket.accept();

                // Debug
                System.out.println("New client connected: " + this.clientSocket);

                this.serverThreads.add(new ServerThread(this.clientSocket));
                this.serverThreads.getLast().start(); // Start the new server thread

                this.newConnection(this.serverThreads.getLast().getConnectedIP());
            } catch (IOException e) {
                System.err.println("Unable to accept incoming connection to ServerThread.");
                e.printStackTrace();
            }
        }
    }

    public void removeServerThread(ServerThread t) {
        this.serverThreads.remove(t);
    }

    public void newConnection(String ip) {
        if (ip.equals(Hub.localhostIP))
            return; // ip is our own ip, no need to broadcast

        for (ServerThread clientHandler : Hub.mainServer.serverThreads) {
            if (clientHandler.getConnectedIP().equals(ip))
                return; // Avoid creating duplicate connections
            
            // Alert all connected addresses of the new ip
            clientHandler.sendMessage("NewConnection\n" + ip); // The newline splits the message into two lines for interpretation by the client

            // Create our own connection to the new address
            this.clientThreads.add(new ClientThread(ip));
            this.clientThreads.getLast().start(); // Start the new client thread
        }
    }

    public void end() {
        try {
            if (this.clientSocket != null)
                this.clientSocket.close();
            if (this.serverSocket != null)
                this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ClientAcceptingThread sockets.");
        }
    }
}