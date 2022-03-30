package Java.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstraction of both network communication handling threads
 */
class NetworkingThread extends Thread {
	Socket socket;
    PrintWriter outstream;
    BufferedReader instream;
    private boolean connected;

    // For ClientThreads
    protected NetworkingThread() {
        connected = false;
    }

    // For ServerThreads
    protected NetworkingThread(Socket socket) {
        this.socket = socket;
        connected = false;

		try {
			setIOStreams();
			connected = true;
		} catch (IOException e) {
			System.err.println("Unable to establish client IO streams.");
			e.printStackTrace();
		}
    }

    // Set the input and output streams for the thread.
	protected void setIOStreams() throws IOException {
		instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outstream = new PrintWriter(socket.getOutputStream(), true);
	}

    // Set the socket for the thread
	protected void setSocket(Socket socket) throws IOException {
		this.socket = socket;
	}
	
    // Retrieve the connection status of the thread
	protected boolean connectionStatus() {
        return connected;
    }

    // Set the connection status of the thread
	protected void setConnectionStatus(boolean state) {
		connected = state;
	}

    // Send a message via the network to the connected party
    protected void sendMessage(String msg) {
        outstream.println(msg);
        outstream.flush();
    }

    // Close this thread's connection
    protected void stopConnection() {
        try {
            sendMessage(Hub.NETExit);
            
            if (outstream != null)
                outstream.close();
            if (instream != null) {
                instream.close();
                socket.close();
            }
            connected = false;
        } catch (IOException e) {
            System.err.println("Unable to properly close connection.");
            e.printStackTrace();
        }
    }
}
