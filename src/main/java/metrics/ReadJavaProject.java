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
							
							//String[] methodsArray = (String[]) methods.toArray();
							
							int i = 0;
							
							/*
							System.out.println("Novo for");
							System.out.println(methods);
							System.out.println("---------------------");
							System.out.println(linesOfMethods.keySet());
							*/
					
						//	for(int i=0; i < countLinesOfMethods.size(); i++) {
							for(String s: methods) {
								String[] lines = new String[11];
								lines[0] = current.getName();					//package
								lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.')); //class
								lines[2] = s;									//method
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
