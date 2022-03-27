package Java.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * TO-DO:
 * - Share connected clients with connections (so that the network propagates through each new connection)
 * - Program ClientThreadFactory.java (needs to create new clients for each new possible connection and connect them)
 * - Implement blockchain validation through the network
 */

class ClientAcceptingThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(Client.PORT);

            Client.connectedAddresses.add(InetAddress.getLocalHost());
        } catch (IOException e) {
            System.err.println("Unable to start ServerSocket on port " + Client.PORT);
            e.printStackTrace();
        }

        while (true) {
            try {
                this.clientSocket = this.serverSocket.accept();

                // Debug
                System.out.println("New client connected: " + this.clientSocket);

                Thread t = new Thread(new ServerThread(this.clientSocket));

                t.start();
            } catch (IOException e) {
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
            System.err.println("Unable to properly close ClientAcceptingThread sockets.");
        }
    }
}