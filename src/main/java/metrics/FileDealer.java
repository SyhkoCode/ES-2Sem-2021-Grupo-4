package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileDealer {
	
	public static void createFile(String path, ArrayList<String> write) {
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(path);
			for(int i=0;i<write.size();i++) {
				myWriter.write(write.get(i));
				myWriter.write(System.getProperty( "line.separator" ));
			}
		    myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
