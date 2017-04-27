package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandleClients implements Runnable {
	private ServerSocket server;
	public static List<Socket> clients = new Vector<Socket>();
	public static List<String> message = new Vector<String>();
	
	public static Map<Socket, StreamsWrapper> clientStreams = 
			Collections.synchronizedMap(new HashMap<Socket, StreamsWrapper>());
	
	public HandleClients(ServerSocket server) {
		this.server = server;
	}
	
	
	public void run() {
		try {
			boolean flagSender = false;
			ExecutorService executor = Executors.newCachedThreadPool();
			
			while(!Thread.currentThread().isInterrupted()) {
				Socket socket = server.accept();
				clientStreams.put(socket, 
						new StreamsWrapper(new BufferedReader(
								new InputStreamReader(socket.getInputStream())),
								new PrintStream(socket.getOutputStream())));
				Receiver receiver = new Receiver(socket,clientStreams.get(socket).getInputStreamReader());
				
				clients.add(socket);
				executor.execute(receiver);
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
