package chat;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class CheckClients implements Runnable{


	
	
	@Override
	public void run() {
		Vector<Socket> found = new Vector<>();
		DateTime currTime = DateTime.now();
		Seconds maxInterval = Seconds.seconds(240);

		for (Socket socket : HandleClients.clientStreams.keySet()) {
			PrintStream out = HandleClients.clientStreams.get(socket).getOutputStreamWriter();
			out.println("PING:" + Server.getExternalIp().toString());

			Seconds interval = Seconds.secondsBetween(HandleClients.clientStreams.get(socket).getActiveTime(),
					currTime); //calculate interval between last pong and current time
			if (interval.isGreaterThan(maxInterval)) {
				found.add(socket);
			}
		}

		if (!found.isEmpty()) {
			try {
				for (Socket delClient : found) {
					HandleClients.clientStreams.remove(delClient);
					System.out.println(delClient + " has been disconnected!");
					delClient.close();
				}

			} catch (IOException e) {
				System.out.println("Problem with close socket " + e.getMessage());
			}
		}

	}

}
