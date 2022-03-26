package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstraction of both network communication handling threads
 */
public class NetworkingThread extends Thread {
	private Socket socket;
    private PrintWriter outstream;
    private BufferedReader instream;
    private boolean connected;

    public NetworkingThread(Socket clientSocket, boolean isServer) {
        this.socket = clientSocket;
        this.connected = false;

		if (isServer) {
			try {
				this.instream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				this.outstream = new PrintWriter(this.socket.getOutputStream(), true);

				this.connected = true;
			} catch (IOException e) {
				System.err.println("Unable to establish client IO streams.");
				e.printStackTrace();
			}
		}
    }

	public void setIOStreams() throws IOException {
		this.instream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.outstream = new PrintWriter(this.socket.getOutputStream(), true);
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
	}
	
	public boolean connectionStatus() {
        return this.connected;
    }

	public void setConnectionStatus(boolean state) {
		this.connected = state;
	}

    public void sendMessage(String msg) {
        this.outstream.println(msg);
        this.outstream.flush();
    }

    public void stopConnection() {
        try {
            sendMessage("exit");
            
            if (this.outstream != null)
                this.outstream.close();
            if (this.instream != null) {
                this.instream.close();
                this.socket.close();
            }
            this.connected = false;
        } catch (IOException e) {
            System.err.println("Unable to properly close client connection.");
        }
    }
}
