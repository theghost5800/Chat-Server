package chat;

import java.io.BufferedReader;
import java.io.PrintStream;

public class StreamsWrapper {
	private BufferedReader inputStreamReader;
	private PrintStream outputStreamWriter;
	
	
	public StreamsWrapper(BufferedReader inputStream, PrintStream outputStream) {
		super();
		this.inputStreamReader = inputStream;
		this.outputStreamWriter = outputStream;
	}


	public BufferedReader getInputStreamReader() {
		return inputStreamReader;
	}


	public PrintStream getOutputStreamWriter() {
		return outputStreamWriter;
	}
	

}
