package chat;

import org.joda.time.DateTime;

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
import java.util.concurrent.*;

public class HandleClients implements Runnable {
	private ServerSocket server;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	public static List<Socket> clients = new Vector<Socket>();
	public static List<String> message = new Vector<String>();
	
	public static Map<Socket, StreamsWrapper> clientStreams = 
			Collections.synchronizedMap(new HashMap<Socket, StreamsWrapper>());
	
	
	public HandleClients(ServerSocket server) {
		this.server = server;
	}
	
	
	public void run() {
		Receiver receiver = null;
		Sender sender = null;
		ScheduledFuture checkClientsTask = null;
		try {
			boolean flagSender = false;
			checkClientsTask = scheduledExecutorService.scheduleWithFixedDelay(new CheckClients(),
					240, 240, TimeUnit.SECONDS);
			while(!server.isClosed()) {
				Socket socket = server.accept();
				clientStreams.put(socket, 
						new StreamsWrapper(new BufferedReader(
								new InputStreamReader(socket.getInputStream())),
								new PrintStream(socket.getOutputStream()),
								DateTime.now()));
				receiver = new Receiver(socket,clientStreams.get(socket).getInputStreamReader());
				
				clients.add(socket);
				executor.execute(receiver);
				if (flagSender == false) {
					sender = new Sender();
					new Thread(sender).start();
					flagSender = true;
				}
				System.out.println(socket.toString() + " has been connected!");
			}
		} catch (IOException e) {
			executor.shutdownNow();
			System.out.println("The Server is Stopped!");
		} 
		if (receiver != null) {
			receiver.shutdownReceiverThread();
			sender.shutdownSenderThread();
			scheduledExecutorService.shutdownNow();
			checkClientsTask.cancel(true);
			for (Socket client : clientStreams.keySet()) {
				try {
					client.close();
				} catch (IOException e) {
					System.out.println("Problem with close socket " + e.getMessage());
				}
				
			}
		}

	}
}
