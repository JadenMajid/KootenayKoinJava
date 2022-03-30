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

    // For ClientThreads
    public NetworkingThread() {
        connected = false;
    }

    // For ServerThreads
    public NetworkingThread(Socket clientSocket) {
        socket = clientSocket;
        connected = false;

		try {
			instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outstream = new PrintWriter(socket.getOutputStream(), true);

			connected = true;
		} catch (IOException e) {
			System.err.println("Unable to establish client IO streams.");
			e.printStackTrace();
		}
    }

	public void setIOStreams() throws IOException {
		instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outstream = new PrintWriter(socket.getOutputStream(), true);
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
	}
	
	public boolean connectionStatus() {
        return connected;
    }

	public void setConnectionStatus(boolean state) {
		connected = state;
	}

    public void sendMessage(String msg) {
        outstream.println(msg);
        outstream.flush();
    }

    public void stopConnection() {
        try {
            sendMessage("exit");
            
            if (outstream != null)
                outstream.close();
            if (instream != null) {
                instream.close();
                socket.close();
            }
            connected = false;
        } catch (IOException e) {
            System.err.println("Unable to properly close client connection.");
        }
    }
}
