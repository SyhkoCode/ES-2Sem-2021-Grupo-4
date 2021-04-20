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
			boolean testing = false;
//			String strtest = "";

			for (File packageFile : current.listFiles()) {
				if (packageFile.isDirectory()) {
					folders.push(packageFile);
					
					
//						if(testing) {
//							strtest += packageFile.getName() + ".";
//							System.out.println(strtest);
//						}
//						if(packageFile.getName().equals("src")) {
//							System.out.println("yuyiy");
//							testing = true;
//						}
				} else {
					if (packageFile.getName().endsWith(".java")) {
						try {
							LinkedHashSet<String> methods = Metrics.countMethods(packageFile);
							
							LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(packageFile,methods);
							
							ArrayList<Integer> countLinesOfMethods = Metrics.countLinesOfMethods(linesOfMethods);
							
							ArrayList<Integer> cycloOfAllMethods = Metrics.allCyclos(linesOfMethods);
							
							int wmc = Metrics.wmc(cycloOfAllMethods);
							
							
							int i = 0;
							System.out.println(current.getAbsolutePath());
							if(current.getAbsolutePath().contains("\\src\\")) {
								
							
							for (String s : methods) {

								String strFormatted = "";
								
								String str2 = s.replaceAll(".*\\(|\\n|\\r|\\)", "");
								
								
								String[] strs = str2.split(",");

								for (String substr : strs) {
									strFormatted += substr.trim().split("\\s+")[0] + ",";

								}

								String[] lines = new String[11];
//								lines[0] = packageFile.getName();					//package
//								lines[0] = strtest;
								lines[0] = current.getAbsolutePath().replaceFirst(".*\\\\src\\\\","").replace("\\",".");
							//	System.out.println(current.getAbsolutePath().replaceFirst(".*\\\\src\\\\",""));
								lines[1] = packageFile.getName().substring(0, packageFile.getName().lastIndexOf('.')); //class
							
							    String finalstr = s.replaceAll("\\s*\\((.|\\n|\\r)*\\)\\s*", "") + "("
										+ strFormatted.substring(0, strFormatted.length() - 1) + ")";
							    String[] words = s.replaceAll("\\([^(]*$","").split("\\s+");
							    //System.out.println(Arrays.toString(words) + "     " + words.length);
							    if(words.length==2){
							    	lines[2] = finalstr.replaceFirst("^\\s*\\S+\\s*","");
							    } else {
							    	lines[2] = finalstr.replaceFirst("^\\s*\\S+\\s+\\S+\\s*","");
							    }
							//	lines[2] = finalstr.replaceAll("^\\s*\\S+\\s+\\S+\\s*",""); //method
								lines[3] = "" + methods.size(); 				//NOM_class
								lines[4] = Metrics.getLines(packageFile) + ""; 	//LOC_class
								lines[5] = "" + wmc;  							//WMC_class
								lines[7] = "" + countLinesOfMethods.get(i); 	//LOC_method
								lines[8] = "" + cycloOfAllMethods.get(i); 		//CYCLO_method
								result.add(lines);
								i++;
								
							}
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
