package chat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Sender implements Runnable {

	static void checkClients(Vector<PrintStream> st) {
		List<Socket> found = new ArrayList<Socket>();
		//int i=0;
		if (!st.isEmpty()) {
			for (Socket socket : HandleClients.clients) {
				if (socket.isClosed()) {
					found.add(socket);
				}
			}
			
			HandleClients.clients.removeAll(found);
		}
	}
	
	Vector<PrintStream> openStreams() throws IOException {
		Vector<PrintStream> streams = new Vector<PrintStream>();
		for (Socket client : HandleClients.clients) {
			if (!streams.contains(client)) {
				streams.add(new PrintStream(client.getOutputStream()));
			}
		}	
		return streams;
	}
	
	void sendMsg(Vector<PrintStream> streams) {
		for (PrintStream out : streams) 
			for (String msg : HandleClients.message) {
				out.println(msg);
		}
		HandleClients.message.clear();
	}
	
	public void run() {
		Vector<PrintStream> streams = null;
		try {
			streams = openStreams();
		} catch (IOException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		while(!Thread.currentThread().isInterrupted()) {
			checkClients(streams);
			try {
				streams = openStreams();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			if (HandleClients.message.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}else {
				sendMsg(streams);
			}
		}
	}
}
