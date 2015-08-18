package hmm.build.console;

import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleReader extends Thread {
	
	private BufferedReader reader;
	
	public ConsoleReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	public void run() {
        try {
        	char[] buf = new char[1024];
        	int len = 0;
        	while(len != -1) {
        		len = reader.read(buf);
        		if(len != -1) {
        			String str = String.valueOf(buf, 0, len);
        			int pos = str.indexOf("exit\n");
        			if(pos != -1)
        				str = str.substring(0, pos);
        			Console.getCommandInstance().write(str);
        		}
        	}
        	reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
