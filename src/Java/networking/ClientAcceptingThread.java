package Java.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * This is the main class for the networking system. Server and client connections are created and managed from this class. Pending rename to MainServer.
 */
public class ClientAcceptingThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private LinkedList<ServerThread> serverThreads;
    private LinkedList<ClientThread> clientThreads;
    private boolean shouldRun;

    public ClientAcceptingThread() {
        shouldRun = true;
    }

    // Main thread loop. Call start() submethod to start thread instead of run().
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Hub.PORT);
        } catch (IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Hub.PORT);
            e.printStackTrace();
        }

        while(shouldRun) {
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

    // Remove a server thread from the register.
    protected void removeServerThread(ServerThread t) {
        serverThreads.remove(t);
    }

    // Begin a new connection and propagate this connection through the network.
    protected void newConnection(String ip) {
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

    // Call this method to shut down all network threads (including this one).
    public void end() {
        // Close all server threads
        for (ServerThread clientHandler : Hub.mainServer.serverThreads)
            clientHandler.stopConnection();
        // Close all client threads
        for (ClientThread client : Hub.mainServer.clientThreads)
            client.stopConnection();

        shouldRun = false; // Stop main loop

        // Close sockets
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