package chat;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Receiver implements Runnable {
	
	private Socket socket;
	private BufferedReader socketReader;
	volatile boolean shutdown = false;
	
	public Receiver(Socket s,BufferedReader buf) throws IOException {
		this.socket = s;
		this.socketReader = buf;
	}
	
	public void shutdownReceiverThread() {
		this.shutdown = true;
	}
	@Override
	public void run() {
		String msg;
		while(!this.shutdown) {
			
			
			try {
				msg = socketReader.readLine();
				
				if (msg == null) {
					break;
				}else {
					if (msg.equals("PONG:" + Server.getExternalIp().toString())) {
						HandleClients.clientStreams.get(socket).setActiveTime(DateTime.now());
					}else {
						HandleClients.message.add(socket.getRemoteSocketAddress() + " " + msg);
					}
				}
			} catch (IOException e) {
				break;
			}
			
			
		}
	}

}
