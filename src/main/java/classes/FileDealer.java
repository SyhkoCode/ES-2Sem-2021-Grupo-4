package classes;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Auxiliary class to deal with the rules files
 * @author Tiago Mendes
 * @author Pedro Pereira
 * @version 1.0
 * @since  2021-04-14 
 */

public class FileDealer {

	/**
	 * Writes a new file and places it on the given path
	 * @param path Where the user wants to save the file
	 * @param write ArrayList of Strings to create the file
	 */
	
	public static void createFile(String path, List<String> write) {
		try {
			FileWriter myWriter = new FileWriter(path);
			for (String str : write)
				myWriter.write(str + "\n");
			myWriter.close();
		} catch (IOException e) {
		}

	}

	/**
	 * Reads the given rules file and returns it as an array of Strings
	 * @param path Location of the rule file
	 * @return Rule file as an array of Strings
	 */
	
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
