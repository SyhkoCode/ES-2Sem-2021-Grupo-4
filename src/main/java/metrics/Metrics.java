package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Metrics {


	/**
	 * Represents LOC_class metric.
	 * @param file This is the given Java file from which this metric is extracted. 
	 * @return The Number of Lines of Code in Java file, between class declaration and last bracket, including blank lines and comments inside.
	 * @throws IOException If an I/O error occurs.
	 */
	public static int getLOC_class(File file) throws IOException {
		if(file == null) {
			throw new NullPointerException("Ficheiro nao pode ser nulo.");
		}
		Integer min = null;
		Integer max = null;
		
		List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
		String primaryClassName = file.getName().replaceFirst("\\.java", "");
		
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.indexOf("class "+primaryClassName) != -1 && min == null) 
				min = i;
			if (line.indexOf("}") != -1)
				max = i;
		}
		
		if (min == null || max == null) {
			return 0;
		}
		
		return 1 + max - min;
	}

	/**
	 * Auxiliary method to get all the names of methods a class has in given Java file.
	 * Useful for the extraction of NOM_class metric and the getLinesOfMethods method (which will be used for the extraction of WMC_class, CYCLO_method and LOC_method). 
	 * @param file This is the given Java file. 
	 * @return The LinkedHashSet<String> methods which contains the names of methods a class has in given Java file.
	 */
	public static LinkedHashSet<String> methods(File file) {
		String regex = "((public|protected|private|static)+\\n*\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+)\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;]))|(^(?!\\s*(public|private|protected))\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;]))";
		String regex2 = "(if|else|for|while|switch|catch)\\n* \\s*(\\w+) \\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])|((^|\\s*)return )|((^|\\s*)(new ))";

		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		
		Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);

		LinkedHashSet<String> nomMethod = new LinkedHashSet<String>();

		try {
			Scanner scanner = new Scanner(file);
			String text = null;
			if (scanner.useDelimiter("\\A").hasNext()) {
				text = scanner.useDelimiter("\\A").next();
				String cleanText = text.replaceAll("\\/\\/(.*)|\\/\\*([\\s\\S]*?)\\*\\/", "");
				Matcher matcher = pattern.matcher(cleanText);
				
				while (matcher.find()) {
					Matcher matcher2 = pattern2.matcher(matcher.group());
					if (!matcher2.find()) {
						nomMethod.add(matcher.group().trim().replace("{", ""));
					}
				}

			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Ficheiro especificado nao existe.");
		}

		return nomMethod;
	}
	
	
	/**
	 * Allows to extract NOM_class metric.
	 * @param file This is the given Java file from which this metric is extracted. 
	 * @return The Number of Methods a class has in given Java file.
	 */
	public static int getNOM_class(File file) {
		return methods(file).size();
	}


	/**
	 * Auxiliary method that stores the methods names and corresponding lines of code.
	 * Useful method for the extraction of LOC_method, CYCLO_method and WMC_class metrics.
	 * @param file This is the given Java file.
	 * @param methods This is the given LinkedHashSet<String> methods which should contain the methods names in the class from the given file.
	 * @return The LinkedHashMap<String, String> getLinesOfMethods which contains the methods names and corresponding lines of code.
	 * @throws FileNotFoundException
	 */
	public static LinkedHashMap<String, String> getLinesOfMethods(File file, LinkedHashSet<String> methods)
			throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		Scanner scanner = new Scanner(file);
		ArrayList<String> descartar = new ArrayList<>();
		for (String m : methods) {
			String method = new String("");
			boolean foundMethod = false;
			String toCheck = m;
			if (m.contains(System.lineSeparator())) {
				toCheck = m.substring(0, m.indexOf(System.lineSeparator()));
			}
			while (scanner.hasNext() && !foundMethod) {
				String checkIfStart = scanner.nextLine();

				if (checkIfStart.contains(toCheck)) {
					foundMethod = true;
					boolean endedMethod = false;
					method = method + checkIfStart;
					int openCurly = (checkIfStart.contains("{")) ? 1 : 0;

					while (openCurly == 0) {
						String got1stCurly = scanner.nextLine().trim();
						openCurly += got1stCurly.contains("{") ? 1 : 0;
						if (got1stCurly.contains("}")) {
							endedMethod = true;
							break;
						}
					}
					while (scanner.hasNext() && !endedMethod) {
						String check4Curlies = scanner.nextLine();
						method = method + "\n" + check4Curlies;
						openCurly += (check4Curlies.contains("{") && !check4Curlies.contains("'{'")) ? 1 : 0;
						openCurly -= (check4Curlies.contains("}") && !check4Curlies.contains("'}'")) ? 1 : 0;
						if (openCurly == 0)
							endedMethod = true;
					}
					map.put(m, method);
				}
			}
			if (!foundMethod) {
				descartar.add(m);
				scanner.close();
				scanner = new Scanner(file);
			}

		}
		scanner.close();
		
		for (String s : descartar)
			methods.remove(s);
		
		return map;
	}

	
	 /**
	 * Auxiliary method that calculates the sum of the Cyclomatic Complexity for the required method.
	 * Useful for the extraction of CYCLO_method metric.
	 * @param method This is the given String that represents the method name.
	 * @return The Sum of the Cyclomatic Complexity for the method.
	 */
	private static int nOfCyclo(String method) {
		if(method == null || method.length() == 0) {
			throw new IllegalArgumentException("Empty or null String");
		}
		int n = 1;
		Pattern pattern = Pattern.compile(
				"(\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;))",
				Pattern.MULTILINE);
		String cleanText = method.replaceAll("\\/\\/(.*)|\\/\\*([\\s\\S]*?)\\*\\/", "");
		Matcher matcher = pattern.matcher(cleanText);
		while (matcher.find()) {
			n++;
		}
		return n;
	}
	
	/**
	 * Auxiliary method that allows to store the sum of the Cyclomatic Complexity for the each method present in a class.
	 * Useful to extract the CYCLO_method and WMC_class metrics.
	 * @param linesOfMethods linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @return The Sum of the Cyclomatic Complexity for the corresponding method.
	 */
	private static ArrayList<Integer> getCycloOfAllMethods(LinkedHashMap<String, String> linesOfMethods) {
		ArrayList<Integer> cycloOfAllMethods = new ArrayList<Integer>();
		for (String key : linesOfMethods.keySet())
			cycloOfAllMethods.add(nOfCyclo(linesOfMethods.get(key)));
		return cycloOfAllMethods;
	}
	
	/**
	 * Allows to extract the CYCLO_method metric.
	 * @param linesOfMethods linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @param methodIndex This is the receiving Counter in ReadJavaProject which represents the present method.
	 * @return The Sum of the Cyclomatic Complexity for the corresponding method.
	 */
	public static int getCYCLO_method(LinkedHashMap<String, String> linesOfMethods, int methodIndex) {
		return getCycloOfAllMethods(linesOfMethods).get(methodIndex);
	}
	

	/**
	 * Represents the WMC_class metric.
	 * @param linesOfMethods linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @return The Total of the Cyclomatic Complexity in a class, by getting the total sum of cyclomatic complexity of each method in the class.
	 */
	public static int getWMC_class(LinkedHashMap<String, String> linesOfMethods) {
		int i = 0;
		for (int f : getCycloOfAllMethods(linesOfMethods))
			i += f;
		return i;
	}
	
	/**
	 * Allows to extract the LOC_method metric.
	 * @param linesOfMethods linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @param methodIndex This is the receiving Counter in ReadJavaProject which represents the present method.
	 * @return The Number of Lines of Code in the corresponding method.
	 * @throws FileNotFoundException
	 */
	public static int getLOC_method(LinkedHashMap<String, String> linesOfMethods, int methodIndex) throws FileNotFoundException {
		ArrayList<Integer> getLines = new ArrayList<Integer>();
		for (String key : linesOfMethods.keySet()) {
			getLines.add(linesOfMethods.get(key).split("\r?\n").length);
		}
		return getLines.get(methodIndex);
	}

}
