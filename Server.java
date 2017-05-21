package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;



public class Server {
	private static InetAddress externalIp;

	public static InetAddress getExternalIp() {
		return externalIp;
	}

	public static String getWebServerIp() {
		String ip = null;
		BufferedReader in = null;
		try {
			URL whatismyip = new URL ("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			ip = in.readLine(); //you get the IP as a String
			System.out.println(ip);
		} catch (IOException e1) {
			System.out.println("Problem with connection to server " + e1.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Problem with closing connection " + e.getMessage());
				}
			}

		}

		return ip;
	}

	public static void main(String[] args)  {
		Calendar time = null;
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName("0.0.0.0");
			externalIp = InetAddress.getByName(getWebServerIp());
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
