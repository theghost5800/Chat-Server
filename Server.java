package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;


public class Server {

	public static void main(String[] args)  {
		Calendar time = null;
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName("0.0.0.0");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(1216,10,ip);
			HandleClients handler = new HandleClients(server);
			new Thread(handler).start();
			
			Scanner input = new Scanner(System.in);
			String console = null;
			while(true) {
				console = input.nextLine();
				
				if (console.equals("stop")) {
					for (Thread t : Thread.getAllStackTraces().keySet()) {  
//						if (t.getState()==Thread.State.RUNNABLE ||
//								t.getState() == Thread.State.BLOCKED || 
//								t.getState() == Thread.State.TIMED_WAITING) 
						t.interrupt(); 
					}
					break;
				}
				
				if (console.equals("time")) {
					time = Calendar.getInstance();
					System.out.println(time.getTime());
				}
			}
			if (input != null) {
				input.close();
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
