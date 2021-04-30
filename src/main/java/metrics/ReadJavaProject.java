package metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

/**
 * @author Pedro Pereira, Pedro Pinheiro, Tiago Mendes
 * @version 2.0
 * 
 */
public class ReadJavaProject {
	
	/**
	 * @param method is a String with the name, arguments and modifier of a given method  
	 * @return String formatted containing just the arguments of the method
	 */
	public static String formatString(String method) {
		String argumentsFormatted = "";
		String strTemp = method.replaceAll(".*\\(|\\n|\\r|\\)", "");
		String[] arrayTemp = strTemp.split(",");
		for (String substrTemp : arrayTemp) {
			argumentsFormatted += substrTemp.trim().split("\\s+")[0] + ",";
		}
		return argumentsFormatted;
	}
	
	
	/**
	 * @param packageFile is the java file that will by analysed
	 * @param lines is a String array that contains the information to be inserted in each row of 
	 * 		  the excel sheet by the ExcelDealer 
	 * @param counter used to get the LOC_method and CYCLO_method metrics
	 * @param linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @param cycloOfAllMethods is an arrayList of Integer that given a map of methods and their
	 * 		  lines of code, returns the cyclo value of each of them 
	 * @throws IOException If an I/O error occurs.
	 */
	public static void getMetricsForExcel(File packageFile, String[] lines, int counter, LinkedHashMap<String, String> linesOfMethods, ArrayList<Integer> cycloOfAllMethods) throws IOException{
		lines[3] = "" + Metrics.getNOM_class(packageFile); 
		lines[4] = "" + Metrics.getLOC_class(packageFile); 
		lines[5] = "" + Metrics.getWMC_class(cycloOfAllMethods); 
		lines[6] = "" + Metrics.getLOC_method(linesOfMethods, counter);
		lines[7] = "" + Metrics.getCYCLO_method(cycloOfAllMethods, counter);
	}
	
	
	/**
	 * @param current represents the project directory
	 * @param packageFile is the java file that will by analysed
	 * @param methods is a LinkedHashSet that contains the names, arguments and modifiers of the methods of the java file in Strings
	 * @param linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @param counter used to get the LOC_method and CYCLO_method metrics
	 * @param cycloOfAllMethods is an arrayList of Integer that given a map of methods and their
	 * 		  lines of code, returns the cyclo value of each of them
	 * @param result contains the information that will later be used to fill in
	 * 		  each row of the excel sheet (this will be done with the ExcelDealer)
	 * @throws IOException If an I/O error occurs.
	 */
	public static void getInformationForExcel(File current, File packageFile, LinkedHashSet<String> methods,  LinkedHashMap<String, String> linesOfMethods, ArrayList<Integer> cycloOfAllMethods,
			int counter, List<String[]> result)
			throws IOException {

		for (String method : methods) {
			String argumentsFormatted = formatString(method);
			String[] lines = new String[8];
			if (current.getAbsolutePath().contains("\\src\\")) {
				lines[0] = current.getAbsolutePath().replaceFirst(".*\\\\src\\\\", "").replace("\\", "."); // package name
			} else { 
				lines[0] = "package default";
			}
			lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.')); // class name
			String methodFormatted = method.replaceAll("\\s*\\((.|\\n|\\r)*\\)\\s*", "") + "("
					+ argumentsFormatted.substring(0, argumentsFormatted.length() - 1) + ")";
			
			int regexWordsCaught = method.replaceAll("\\([^(]*$", "").split("\\s+").length;
			if (regexWordsCaught == 2) {
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s*", ""); // method name
			} else if ((regexWordsCaught == 4)) {
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s+\\S+\\s+\\S+\\s*", "");
			} else { 
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s+\\S+\\s*", "");
			}
			getMetricsForExcel(packageFile,lines,counter,linesOfMethods,cycloOfAllMethods);
			result.add(lines);
			counter++;
		}
	}

	/**
	 * @param pathProject This is the string representation of the path to the project chosen in the GUI
	 * @return List<String[]> where each String array contains the information that will later be used to fill in
	 * 		   each row of the excel sheet (this will be done with the ExcelDealer)
	 */
	public static List<String[]> readJavaProject(String pathProject) {
		List<String[]> result = new ArrayList<>();
		Stack<File> folders = new Stack<>();
		folders.push(new File(pathProject + "\\src"));
		do {
			File current = folders.pop();
			for (File packageFile : current.listFiles()) {
				if (packageFile.isDirectory()) {
					folders.push(packageFile);
				} else {
					if (packageFile.getName().endsWith(".java")) {
						try {
							LinkedHashSet<String> methods = Metrics.methods(packageFile);
							LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(packageFile,methods);
							ArrayList<Integer> cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
							int counter = 0;
							if (current.getAbsolutePath().contains("\\src")) {
								getInformationForExcel(current,packageFile,methods,linesOfMethods,cycloOfAllMethods,counter,result);

							}
						} catch (IOException e) {
							System.out.println("File not found!");
						}
					}
				}
			}
		} while (!folders.isEmpty());
		return result;
	}
}