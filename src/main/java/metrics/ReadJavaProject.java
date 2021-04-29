package metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

public class ReadJavaProject {
	
	public static String formatString(String method) {
		String strFormatted = "";
		String strTemp = method.replaceAll(".*\\(|\\n|\\r|\\)", "");
		String[] arrayTemp = strTemp.split(",");
		for (String substrTemp : arrayTemp) {
			strFormatted += substrTemp.trim().split("\\s+")[0] + ",";
		}
		return strFormatted;
	}
	
	
	public static void getMetricsForExcel(File packageFile, String[] lines, LinkedHashSet<String> methods, int wmc,
			int i, ArrayList<Integer> countLinesOfMethods, ArrayList<Integer> cycloOfAllMethods) throws IOException{
		lines[3] = "" + methods.size(); // NOM_class
		lines[4] = Metrics.getLines(packageFile) + ""; // LOC_class
		lines[5] = "" + wmc; // WMC_class
		lines[6] = "" + countLinesOfMethods.get(i); // LOC_method
		lines[7] = "" + cycloOfAllMethods.get(i); // CYCLO_method
	}
	
	
	public static void getInformationForExcel(File current, File packageFile, LinkedHashSet<String> methods, int wmc,
			int i, ArrayList<Integer> countLinesOfMethods, ArrayList<Integer> cycloOfAllMethods, List<String[]> result)
			throws IOException {
		for (String method : methods) {
			String argumentsFormatted = formatString(method);
			String[] lines = new String[8];
			if (current.getAbsolutePath().contains("\\src\\")) {
				lines[0] = current.getAbsolutePath().replaceFirst(".*\\\\src\\\\", "").replace("\\", ".");
			} else { // package name
				lines[0] = "package default";
			}
			lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.')); // class name
			String methodFormatted = method.replaceAll("\\s*\\((.|\\n|\\r)*\\)\\s*", "") + "("
					+ argumentsFormatted.substring(0, argumentsFormatted.length() - 1) + ")";
			
			int regexWordsCaught = method.replaceAll("\\([^(]*$", "").split("\\s+").length;
			if (regexWordsCaught == 2) {
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s*", "");
			} else if ((regexWordsCaught == 4)) {
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s+\\S+\\s+\\S+\\s*", "");
			} else { // method name
				lines[2] = methodFormatted.replaceFirst("^\\s*\\S+\\s+\\S+\\s*", "");
			}
			getMetricsForExcel(packageFile,lines,methods,wmc,i,countLinesOfMethods,cycloOfAllMethods);
			result.add(lines);
			i++;
		}
	}

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
							int i = 0;
							if (current.getAbsolutePath().contains("\\src")) {
								getInformationForExcel(current,packageFile,methods,wmc,i,countLinesOfMethods,cycloOfAllMethods,result);
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