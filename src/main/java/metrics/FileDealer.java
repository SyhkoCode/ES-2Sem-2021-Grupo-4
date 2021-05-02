package metrics;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class FileDealer {

	public static void createFile(String path, List<String> write) {
		try {
			FileWriter myWriter = new FileWriter(path);
			for (String str : write)
				myWriter.write(str + "\n");
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
				String nextLine = myReader.nextLine();
				if (nextLine.equals("is_Long_Method"))
					result[0] = myReader.nextLine();
				else if (nextLine.equals("is_God_Class"))
					result[1] = myReader.nextLine();
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Ficheiro nao existe.");
		}
		return result;
	}

}
