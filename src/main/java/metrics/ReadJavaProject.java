package metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
				if (packageFile.isDirectory()) {
					folders.push(packageFile);
				} else {
					if (packageFile.getName().endsWith(".java")) {
						try {
							LinkedHashSet<String> methods = Metrics.methods(packageFile);
							LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(packageFile,methods);

							int methodIndex = 0;
							if (current.getAbsolutePath().contains("\\src")) {

								for (String method : methods) {
									String strFormatted = "";
									String str2 = method.replaceAll(".*\\(|\\n|\\r|\\)", "");
									String[] strs = str2.split(",");
									for (String substr : strs) {
										strFormatted += substr.trim().split("\\s+")[0] + ",";
									}
									String[] lines = new String[11];
									if (current.getAbsolutePath().contains("\\src\\")) {
										lines[0] = current.getAbsolutePath().replaceFirst(".*\\\\src\\\\", "").replace("\\", "."); 
									} else {	//package name
										lines[0] = "package default";
									}

									lines[1] = packageFile.getName().substring(0,packageFile.getName().lastIndexOf('.')); // class name

									String finalstr = method.replaceAll("\\s*\\((.|\\n|\\r)*\\)\\s*", "") + "("
											+ strFormatted.substring(0, strFormatted.length() - 1) + ")";
									String[] words = method.replaceAll("\\([^(]*$", "").split("\\s+");
									
									if (words.length == 2) {
										lines[2] = finalstr.replaceFirst("^\\s*\\S+\\s*", "");
									} else {	// method name
										lines[2] = finalstr.replaceFirst("^\\s*\\S+\\s+\\S+\\s*", "");
									}
									lines[3] = "" + Metrics.getNOM_class(packageFile); 
									lines[4] = "" + Metrics.getLOC_class(packageFile); 
									lines[5] = "" + Metrics.getWMC_class(linesOfMethods); 
									lines[7] = "" + Metrics.getLOC_method(linesOfMethods, methodIndex); 
									lines[8] = "" + Metrics.getCYCLO_method(linesOfMethods, methodIndex);
									result.add(lines);
									methodIndex++;
								}
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
