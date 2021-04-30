package metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * This class allows the user to obtain boolean results based on the calculated
 * metrics and the chosen rule
 * 
 * @author Susana Polido, Tiago Mendes, Pedro Pinheiro
 *
 */
public class MetricsRuleAnalysis {
	private HashMap<String, ArrayList<Boolean>> codeSmellDetectedMap = new HashMap<>();
	private ArrayList<MethodData> methodsData;
//	HashMap => <is_God_Class, [true,false,true]>

	/**
	 * MetricsRuleAnalysis constructor
	 * <p>
	 * Analyzes and stores the detection of Code Smells based on an ArrayList of
	 * Rules and another of MethodData
	 * 
	 * 
	 * @param methodsData ArrayList of MethodData objects
	 * @param rules       ArrayList of Rule objects
	 * @see MethodData
	 * @see Rule
	 */
	public MetricsRuleAnalysis(ArrayList<MethodData> methodsData, ArrayList<Rule> rules) {
		this.methodsData = methodsData;
		for (Rule r : rules) {
			codeSmellDetectedMap.put(r.getName(), new ArrayList<Boolean>());
			for (MethodData md : methodsData) {
				codeSmellDetectedMap.get(r.getName()).add(r.smellDetected(md));
			}
		}
	}

	/**
	 * Gets the codeSmellDetectedMap
	 * 
	 * @return the MetricsRuleAnalysis' map
	 */
	public HashMap<String, ArrayList<Boolean>> getCodeSmellDetectedMap() {
		return codeSmellDetectedMap;
	}

	/**
	 * Gets the methodsData ArrayList
	 * 
	 * @return the MetricsRuleAnalysis' ArrayList
	 */
	public ArrayList<MethodData> getMethodsData() {
		return methodsData;
	}

	/**
	 * Organizes the information in the MetricsRuleAnalysis object so it can be
	 * written in an Excel file
	 * <p>
	 * Creates an ArrayList of String[] which the first position is the Excel
	 * header, and the remaining positions will be each method data plus the results
	 * of all the detected Code Smells
	 * 
	 * @return an ArrayList of lines for an Excel file creation
	 */
	public ArrayList<String[]> getResults() {
		ArrayList<String[]> result = new ArrayList<>();
		String[] codeSmellsNames = new String[codeSmellDetectedMap.keySet().size()];
		int counterHeader = 0;
		for (String s : codeSmellDetectedMap.keySet()) {
			codeSmellsNames[counterHeader++] = s;
		}
		String[] auxHeader = Stream.concat(Arrays.stream(new String[] { "MethodID", "package", "class", "method" }),
				Arrays.stream(codeSmellsNames)).toArray(String[]::new);
		
		result.add(auxHeader);

		for (int i = 0; i < getMethodsData().size(); i++) {
			String[] methodLine = new String[3 + codeSmellDetectedMap.size()];
			methodLine[0] = getMethodsData().get(i).getPackageName();
			methodLine[1] = getMethodsData().get(i).getClassName();
			methodLine[2] = getMethodsData().get(i).getMethodName();
			int indexMethodLine = 3;
			for (String codeSmell : getCodeSmellDetectedMap().keySet()) {
				methodLine[indexMethodLine] = getCodeSmellDetectedMap().get(codeSmell).get(i).toString();
				indexMethodLine++;
			}
			result.add(methodLine);
		}

		return result;

	}

	/**
	 * Organizes the information in the MetricsRuleAnalysis object separating class
	 * Code Smells and method Code Smells
	 * <p>
	 * Creates an ArrayList with two ArrayLists of String[] where the first
	 * ArrayList refers only to class Code Smells and the second ArrayList to the
	 * method Code Smells Each of these ArrayList contains the respective header
	 * followed by the results of all the respective detected Code Smells
	 * 
	 * @return an ArrayList of ArrayLists of lines to create two tables
	 */
	public ArrayList<ArrayList<String[]>> getCodeSmellResults() {
		ArrayList<String> classesAndCodeSmellsFound = new ArrayList<>();
		ArrayList<ArrayList<String[]>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		result.add(new ArrayList<>());
		result.get(0).add(getClassHeader());
		result.get(1).add(getMethodHeader());

		for (int i = 0; i < getMethodsData().size(); i++) {
			String[] classLine = new String[getClassSmells().size() + 1];
			String[] methodLine = new String[getMethodSmells().size() + 2];

			if (!classesAndCodeSmellsFound.contains(getMethodsData().get(i).getClassName())) {
				classLine[0] = getMethodsData().get(i).getClassName();
				for (int j = 1; j < classLine.length; j++) {
					classLine[j] = codeSmellDetectedMap.get(getClassSmells().get(j - 1)).get(i).toString();

					if (j == classLine.length - 1) {
						classesAndCodeSmellsFound.add(getMethodsData().get(i).getClassName());
						result.get(0).add(classLine);
					}
				}
			}

			methodLine[0] = Integer.toString(i + 1);
			methodLine[1] = getMethodsData().get(i).getMethodName();
			for (int j = 2; j < methodLine.length; j++) {
				methodLine[j] = codeSmellDetectedMap.get(getMethodSmells().get(j - 2)).get(i).toString();
				if (j == methodLine.length - 1) {
					result.get(1).add(methodLine);
				}
			}
		}
		return result;
	}

	/**
	 * Gets the ArrayList of all class Code Smells names present in the map
	 * 
	 * @return ArrayList of the names of the class Code Smells
	 */
	private ArrayList<String> getClassSmells() {
		ArrayList<String> classSmellsNames = new ArrayList<String>();
		for (String s : codeSmellDetectedMap.keySet()) {
			if (s.toLowerCase().contains("class"))
				classSmellsNames.add(s);
		}
		return classSmellsNames;
	}

	/**
	 * Gets the ArrayList of all method Code Smells names present in the map
	 * 
	 * @return ArrayList of the names of the method Code Smells
	 */
	private ArrayList<String> getMethodSmells() {
		ArrayList<String> methodSmellsNames = new ArrayList<String>();
		for (String s : codeSmellDetectedMap.keySet()) {
			if (s.toLowerCase().contains("method"))
				methodSmellsNames.add(s);
		}
		return methodSmellsNames;
	}

	/**
	 * Gets the header for the getCodeSmellResults' first ArrayList
	 * 
	 * @return String array of "Class" plus the names of all class Code Smells
	 */
	private String[] getClassHeader() {
		String[] header = new String[1 + getClassSmells().size()];
		header[0] = "Class";
		for (int i = 1; i < header.length; i++) {
			header[i] = getClassSmells().get(i - 1);
		}
		return header;
	}

	/**
	 * Gets the header for the getCodeSmellResults' second ArrayList
	 * 
	 * @return String array of "ID","Method" plus the names of all method Code
	 *         Smells
	 */
	private String[] getMethodHeader() {
		String[] header = new String[2 + getMethodSmells().size()];
		header[0] = "ID";
		header[1] = "Method";
		for (int i = 2; i < header.length; i++) {
			header[i] = getMethodSmells().get(i - 2);
		}
		return header;
	}

//	public static void main(String[] args) {
//		String path = "C:\\Users\\sophi\\Desktop\\CoordenacaoIII_metrics.xlsx";
//
//		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(path),Rule.allRules(new File("testeregras")));
//		mra.getCodeSmellResults();
////		for (int i = 0; i < mra.getMethods().size(); i++) {
////			System.out.println(mra.getMethods().get(i).getMethodName());
////			for (String nome : mra.getMap().keySet()) {
////				System.out.println(mra.getMap().get(nome).get(i));
////			}
////		}
//	}

}
