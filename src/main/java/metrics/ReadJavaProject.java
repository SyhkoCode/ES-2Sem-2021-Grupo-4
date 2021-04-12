package metrics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

public class ReadJavaProject {
	public static List<String[]> readJavaProject(String pathProject) {
		List<String[]> result = new ArrayList<>();

		Stack<File> folders = new Stack<>();
		folders.push(new File(pathProject + "\\src"));
		do {
			File current = folders.pop();

			for (File packageFile : current.listFiles()) {
				if (packageFile.isDirectory())
					folders.push(packageFile);
				else {
					if (packageFile.getName().endsWith(".java")) {
						try {
							LinkedHashSet<String> methods = Metrics.countMethods(packageFile);
							
							LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(packageFile,methods);
							
							ArrayList<Integer> countLinesOfMethods = Metrics.countLinesOfMethods(linesOfMethods);
							
							ArrayList<Integer> cycloOfAllMethods = Metrics.allCyclos(linesOfMethods);
							
							int wmc = Metrics.wmc(cycloOfAllMethods);
							
							
							int i = 0;
							
							
							for (String s : methods) {

								String strFormatted = "";
								String str2 = s.replaceAll(".*\\(|\\n|\\r|\\)", "");
								String[] strs = str2.split(",");

								for (String substr : strs) {
									strFormatted += substr.trim().split("\\s+")[0] + ",";

								}

								String[] lines = new String[11];
								lines[0] = current.getName();					//package
								lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.')); //class
								lines[2] = s.replaceAll("\\((.|\\n|\\r)*\\)", "") + "("
										+ strFormatted.substring(0, strFormatted.length() - 1) + ")"; //method
								lines[3] = "" + methods.size(); 				//NOM_class
								lines[4] = Metrics.getLines(packageFile) + ""; 	//LOC_class
								lines[5] = "" + wmc;  							//WMC_class
								lines[7] = "" + countLinesOfMethods.get(i); 	//LOC_method
								lines[8] = "" + cycloOfAllMethods.get(i); 		//CYCLO_method
								result.add(lines);
								i++;
								
							}
						} catch (IOException e) {
							System.out.println("sou mongoloide!");
						}
					}

				}

			}

		} while (!folders.isEmpty());

		return result;
	}

}
