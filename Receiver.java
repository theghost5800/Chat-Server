package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver implements Runnable {
	
	private Socket socket;
	private BufferedReader socketReader;
	
	public Receiver(Socket socket) throws IOException {
		this.socket = socket;
		socketReader = new BufferedReader(
				new InputStreamReader(this.socket.getInputStream()));
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			String msg = null;
			try {
				msg = socketReader.readLine();
				if (msg == null) {
					Thread.sleep(1000);
				}else {
					HandleClients.message.add(socket.getRemoteSocketAddress() + " " + msg);
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			
		}
	}

}
