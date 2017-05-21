package chat;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class Sender implements Runnable {
	private volatile boolean shutdown = false;
	
	public void shutdownSenderThread() {
		this.shutdown = true;
	}
	
	void sendMsg() {
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

		HandleClients.message.clear();
	}
	
	public void run() {
		
		//new Thread(new CheckClients()).start();
		Vector<PrintStream> streams = new Vector<PrintStream>();
		long mapState = 0;
		
		while(!this.shutdown) {
	
			
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
					Thread.currentThread().interrupt();
				}
			}else {
				sendMsg();
			}
		}
	}
}
