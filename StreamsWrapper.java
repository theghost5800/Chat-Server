package chat;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.PrintStream;

public class StreamsWrapper {
	private BufferedReader inputStreamReader;
	private PrintStream outputStreamWriter;
	private DateTime activeTime;

	public StreamsWrapper(BufferedReader inputStream, PrintStream outputStream, DateTime activeTime) {
		super();
		this.inputStreamReader = inputStream;
		this.outputStreamWriter = outputStream;
		this.activeTime = activeTime;
	}

	public void setActiveTime(DateTime activeTime) {
		this.activeTime = activeTime;
	}

	public DateTime getActiveTime() {
		return activeTime;
	}

	public BufferedReader getInputStreamReader() {
		return inputStreamReader;
	}


	public PrintStream getOutputStreamWriter() {
		return outputStreamWriter;
	}
	

}
