package Java.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream instream;
    private DataOutputStream outstream;

    public ServerThread(Socket clientSocket, DataInputStream instream, DataOutputStream outstream) {
        this.clientSocket = clientSocket;
        this.instream = instream;
        this.outstream = outstream;
    }

    @Override
    public void run() {
        String received;
        while(true) {
            try {
                received = instream.readUTF();

                switch(received) { // Process received messages
                    case "Exit":
                        end();
                        break;
                    default:
                        System.err.println("Unprocessed message from client: " + received);
                }
            } catch(IOException e) {
                System.err.println("Problem receiving message from client.");
                e.printStackTrace();
            }
        }
    }

    public void end() {
        try {
            this.outstream.close();
            this.instream.close();
            this.clientSocket.close();
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Unable to properly close ServerThread sockets/streams.");
        }
    }
}