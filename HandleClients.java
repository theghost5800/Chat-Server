package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class HandleClients implements Runnable {
	private ServerSocket server;
	public static List<Socket> clients = new Vector<Socket>();
	public static List<String> message = new Vector<String>();
	
	public HandleClients(ServerSocket server) {
		this.server = server;
	}
	
	
	public void run() {
		try {
			boolean flagSender = false;
			
			while(!Thread.currentThread().isInterrupted()) {
				Socket socket = server.accept();
				Receiver receiver = new Receiver(socket);
				clients.add(socket);
				new Thread(receiver).start();
				if (flagSender == false) {
					Sender sender = new Sender();
					new Thread(sender).start();
					flagSender = true;
				}
				System.out.println(socket.toString() + " has been connected!");
			}
		} catch (IOException e) {
			System.out.println("The Server is Stopped!");
		}
	}
}
