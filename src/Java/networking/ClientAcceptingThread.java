package Java.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/*
 * TO-DO:
 * - Program ClientThreadFactory.java (needs to create new clients for each new possible connection and connect them)
 * - Implement blockchain validation through the network
 */

class ClientAcceptingThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private LinkedList<ServerThread> clientThreads;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(Hub.PORT);

            Hub.connectedAddresses.add(InetAddress.getLocalHost());
        } catch (IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Hub.PORT);
            e.printStackTrace();
        }

        while(true) {
            try {
                this.clientSocket = this.serverSocket.accept();

                // Debug
                System.out.println("New client connected: " + this.clientSocket);

                ServerThread t = new ServerThread(this.clientSocket);
                this.clientThreads.add(t);
                this.clientThreads.getLast().start(); // Start the new server thread

                this.broadcastNewConnection(this.clientThreads.getLast().getConnectedIP());
            } catch (IOException e) {
                System.err.println("Unable to accept incoming connection to ServerThread.");
                e.printStackTrace();
            }
        }
    }

    public void broadcastNewConnection(String ip) {
        boolean alreadyConnected = false;

        for (ServerThread clientHandler : Hub.mainServer.clientThreads) {
            if (clientHandler.getConnectedIP().equals(ip))
                alreadyConnected = true;
            else // Avoid creating duplicate connections
                clientHandler.sendMessage("NewConnection\n" + ip); // The newline splits the message into two lines for interpretation by the client

            if (!alreadyConnected)
                continue; // TODO: Create new connection
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