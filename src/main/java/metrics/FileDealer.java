package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDealer {

	public static void createFile(String path, ArrayList<String> write) {
		try {
			FileWriter myWriter = new FileWriter(path);
			for (int i = 0; i < write.size(); i++) {
				myWriter.write(write.get(i));
				myWriter.write(System.getProperty("line.separator"));
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String[] readFile(String path) {
		String[] result = new String[2];
		try {
			Scanner myReader = new Scanner(new File(path));
			while (myReader.hasNextLine()) {
				if(myReader.nextLine().equals("is_Long_Method"))
					result[0] = myReader.nextLine();
				else 
					result[1] = myReader.nextLine();
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Ficheiro nao existe.");
		}
		return result;
	}

}
