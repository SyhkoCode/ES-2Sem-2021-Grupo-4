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
	 * @param methods is a LinkedHashSet that contains the names, arguments and modifiers of the methods of 
	 * 		  the java file in Strings
	 * @param wmc corresponds to the Weighted Methods per Class metric value
	 * @param counter used to get the LOC_method and CYCLO_method metrics
	 * @param countLinesOfMethods is an ArrayList with the number of lines of each method
	 * @param cycloOfAllMethods is an arrayList of Integer that given a map of methods and their
	 * 		  lines of code, returns the cyclo value of each of them 
	 * @throws IOException
	 */
	public static void getMetricsForExcel(File packageFile, String[] lines, LinkedHashSet<String> methods, int wmc,
			int counter, ArrayList<Integer> countLinesOfMethods, ArrayList<Integer> cycloOfAllMethods) throws IOException{
		lines[3] = "" + methods.size(); // NOM_class
		lines[4] = Metrics.getLines(packageFile) + ""; // LOC_class
		lines[5] = "" + wmc; // WMC_class
		lines[6] = "" + countLinesOfMethods.get(counter); // LOC_method
		lines[7] = "" + cycloOfAllMethods.get(counter); // CYCLO_method
	}
	
	
	/**
	 * @param current represents the project directory
	 * @param packageFile (same as getMetricsForExcel)
	 * @param methods (same as getMetricsForExcel)
	 * @param wmc (same as getMetricsForExcel)
	 * @param counter (same as getMetricsForExcel)
	 * @param countLinesOfMethods (same as getMetricsForExcel)
	 * @param cycloOfAllMethods (same as getMetricsForExcel)
	 * @param result contains the information that will later be used to fill in
	 * 		  each row of the excel sheet (this will be done with the ExcelDealer)
	 * @throws IOException
	 */
	public static void getInformationForExcel(File current, File packageFile, LinkedHashSet<String> methods, int wmc,
			int counter, ArrayList<Integer> countLinesOfMethods, ArrayList<Integer> cycloOfAllMethods, List<String[]> result)
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
			getMetricsForExcel(packageFile,lines,methods,wmc,counter,countLinesOfMethods,cycloOfAllMethods);
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
							LinkedHashSet<String> methods = Metrics.countMethods(packageFile);
							LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(packageFile,methods);
							ArrayList<Integer> countLinesOfMethods = Metrics.countLinesOfMethods(linesOfMethods);
							ArrayList<Integer> cycloOfAllMethods = Metrics.allCyclos(linesOfMethods);

							int wmc = Metrics.wmc(cycloOfAllMethods);
							int counter = 0;
							if (current.getAbsolutePath().contains("\\src")) {
								getInformationForExcel(current,packageFile,methods,wmc,counter,countLinesOfMethods,cycloOfAllMethods,result);
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