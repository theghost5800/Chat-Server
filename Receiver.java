package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Receiver implements Runnable {
	
	private Socket socket;
	private BufferedReader socketReader;
	
	public Receiver(Socket s,BufferedReader buf) throws IOException {
		this.socket = s;
		this.socketReader = buf;
	}
	
	@Override
	public void run() {
		String msg;
		while(!(Thread.currentThread().isInterrupted()) ) {
			
			
			try {
				msg = socketReader.readLine();
				
				if (msg == null) {
					Thread.sleep(1000);
				}else {
					HandleClients.message.add(socket.getRemoteSocketAddress() + " " + msg);
					
				}
			} catch (IOException e) {
				break;
				//e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			
		}
	}

}
