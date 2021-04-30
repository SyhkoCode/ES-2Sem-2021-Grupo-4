package metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

/**
 * Contains all methods required for Metrics extraction. 
 * @author Sofia Chaves 
 * @author Pedro Pinheiro
 * @author Susana Polido
 * @author Diogo Graça
 */
public class Metrics {


	/**
	 * Represents LOC_class metric.
	 * @param file This is the given Java file from which this metric is extracted. 
	 * @return The Number of Lines of Code in Java file, between class declaration and last bracket, including blank lines and comments inside.
	 * @throws IOException If an I/O error occurs.
	 * @throws NullPointerException If given file is null.
	 */
	public static int getLOC_class(File file) throws IOException {
		if(file == null) {
			throw new NullPointerException("Ficheiro nao pode ser nulo.");
		}
		Integer min = null;
		Integer max = null;	
//		List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList()); //o jasml dava bem mas dava java.nio.charset.MalformedInputException em alguns outros projetos q testei pq esta funcao tem um enconding inbuilt.. 
//		List<String> lines = FileUtils.readLines(file, "UTF-8"); //funcionou com tds os projetos q testei mas n sei ate q ponto funcionara pa projetos sem serem UTF-8
		
		// abaixo tb funciona mas sao mais linhas, mas parece ser o mais seguro
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    lines = br.lines().collect(Collectors.toList());    
		}
		
		String classRegExp = "(((|public|final|abstract|private|static|protected)(\\s+))?(class)(\\s+)(\\w+)(<.*>)?(\\s+extends\\s+\\w+)?(<.*>)?(\\s+implements\\s+)?(.*)?(<.*>)?(\\s*))\\{$";
        Pattern classDeclarationPattern = Pattern.compile(classRegExp);
        
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			Matcher classDeclarationMatcher = classDeclarationPattern.matcher(line);
			if (classDeclarationMatcher.matches() && min == null) 
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
	 * @throws IllegalArgumentException If file not found.
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
	 * @throws FileNotFoundException If file not found.
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
	 * @throws IllegalArgumentException If given String is empty or null.
	 */
	private static int nOfCyclo(String method) {
		if(method == null || method.length() == 0) {
			throw new IllegalArgumentException("String nao pode ser vazia ou nula.");
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
	 * @param linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @return The Sum of the Cyclomatic Complexity for the corresponding method.
	 */
	public static ArrayList<Integer> getCycloOfAllMethods(LinkedHashMap<String, String> linesOfMethods) {
		ArrayList<Integer> cycloOfAllMethods = new ArrayList<Integer>();
		for (String key : linesOfMethods.keySet())
			cycloOfAllMethods.add(nOfCyclo(linesOfMethods.get(key)));
		return cycloOfAllMethods;
	}
	
	/**
	 * Allows to extract the CYCLO_method metric.
	 * @param cycloOfAllMethods This is the given ArrayList<Integer> which contains the Sum of the Cyclomatic Complexity of each method by order.
	 * @param methodIndex This is the receiving counter which represents the index for the wanted method.
	 * @return The Sum of the Cyclomatic Complexity for the corresponding wanted method.
	 * @throws IllegalArgumentException If given ArrayList<Integer> cycloOfAllMethods is empty.
	 */
	public static int getCYCLO_method(ArrayList<Integer> cycloOfAllMethods, int methodIndex) {
		if(cycloOfAllMethods.isEmpty()) {
			throw new IllegalArgumentException("Nao deve receber uma ArrayList vazia.");
		}
		return cycloOfAllMethods.get(methodIndex);
	}
	

	/**
	 * Represents the WMC_class metric.
	 * @param cycloOfAllMethods This is the given ArrayList<Integer> which contains the Sum of the Cyclomatic Complexity of each method by order.
	 * @return The Total of the Cyclomatic Complexity in a class, by getting the total sum of cyclomatic complexity of each method in the class.
	 */
	public static int getWMC_class(ArrayList<Integer> cycloOfAllMethods) {
		return cycloOfAllMethods.stream().mapToInt(Integer::intValue).sum();
	}
	
	/**
	 * Allows to extract the LOC_method metric.
	 * @param linesOfMethods This is the given LinkedHashMap<String, String> which contains the methods names and corresponding lines of code.
	 * @param methodIndex This is the receiving counter which represents the index for the wanted method.
	 * @return The Number of Lines of Code in the corresponding method.
	 * @throws IllegalArgumentException If given LinkedHashMap<String, String> linesOfMethods is empty.
	 */
	public static int getLOC_method(LinkedHashMap<String, String> linesOfMethods, int methodIndex) {
		if(linesOfMethods.isEmpty()) {
			throw new IllegalArgumentException("Nao deve receber um LinkedHashMap vazio.");
		}
		ArrayList<Integer> getLines = new ArrayList<Integer>();
		for (String key : linesOfMethods.keySet()) {
			getLines.add(linesOfMethods.get(key).split("\r?\n").length);
		}
		return getLines.get(methodIndex);
	}

}
