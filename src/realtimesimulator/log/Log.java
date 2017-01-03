package realtimesimulator.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Log {

	PrintWriter output;
	
	public Log() throws FileNotFoundException {
		output = new PrintWriter(new File("log.txt"));
	}
	
	public void close() {
		output.close();
	}
	
	public void log(LogEvent event) {
		output.println(event);
		output.flush();
	}
	
}