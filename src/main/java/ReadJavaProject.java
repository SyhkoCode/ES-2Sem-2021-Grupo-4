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
							
							//String[] methodsArray = (String[]) methods.toArray();
							
							int i = 0;
							
							System.out.println("Novo for");
							System.out.println(methods);
							System.out.println("---------------------");
							System.out.println(linesOfMethods.keySet());
					
						//	for(int i=0; i < countLinesOfMethods.size(); i++) {
							for(String s: methods) {
								String[] lines = new String[11];
								lines[0] = current.getName();
								lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.'));
								lines[2] = s;
								lines[3] = "" + methods.size();
								lines[4] = Metrics.getLines(packageFile) + "";
								lines[5] = "" + wmc;
								lines[7] = "" + countLinesOfMethods.get(i);
								lines[8] = "" + cycloOfAllMethods.get(i);
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
