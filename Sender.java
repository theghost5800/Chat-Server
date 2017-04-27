package chat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class Sender implements Runnable {

	
	
	void sendMsg(Vector<PrintStream> streams) {
		Vector<Socket> found = new Vector<Socket>();
		
		for (Socket client : HandleClients.clientStreams.keySet()) {
			PrintStream out = HandleClients.clientStreams.get(client).getOutputStreamWriter();
			for (String msg : HandleClients.message) {
				out.println(msg);
			}
			
			if (out.checkError()) {
				found.add(client);
			}
		}
		if (!found.isEmpty()) {
			try {
				for (Socket delClient : found) {
					HandleClients.clientStreams.get(delClient).getInputStreamReader().close();
					HandleClients.clientStreams.get(delClient).getOutputStreamWriter().close();
					HandleClients.clientStreams.remove(delClient);
					System.out.println(delClient + " has been disconnected!");
					delClient.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HandleClients.message.clear();
	}
	
	public void run() {
		
		//new Thread(new CheckClients()).start();
		Vector<PrintStream> streams = new Vector<PrintStream>();
		long mapState = 0;
		
		while(!Thread.currentThread().isInterrupted()) {
	
			
			if (mapState != HandleClients.clientStreams.hashCode()) {
				for (StreamsWrapper wrapper : HandleClients.clientStreams.values()) {
					if (!streams.contains(wrapper.getOutputStreamWriter())) {
						streams.add(wrapper.getOutputStreamWriter());
					}
				}
				mapState = HandleClients.clientStreams.hashCode();
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
