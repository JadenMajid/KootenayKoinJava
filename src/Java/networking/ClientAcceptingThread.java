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
            serverSocket = new ServerSocket(Hub.PORT);
        } catch (IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Hub.PORT);
            e.printStackTrace();
        }

        while(true) {
            try {
                clientSocket = serverSocket.accept();

                // Debug
                System.out.println("New client connected: " + clientSocket);

                serverThreads.add(new ServerThread(clientSocket));
                serverThreads.getLast().start(); // Start the new server thread

                newConnection(serverThreads.getLast().getConnectedIP());
            } catch (IOException e) {
                System.err.println("Unable to accept incoming connection to ServerThread.");
                e.printStackTrace();
            }
        }
    }

    public void removeServerThread(ServerThread t) {
        serverThreads.remove(t);
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
            clientThreads.add(new ClientThread(ip));
            clientThreads.getLast().start(); // Start the new client thread
        }
    }

    public void end() {
        try {
            if (clientSocket != null)
                clientSocket.close();
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ClientAcceptingThread sockets.");
        }
    }
}