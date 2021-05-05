package metrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Allows to compare between different files to be able to evaluate the quality
 * of code smell detection.
 * 
 * @author Sofia Chaves
 * @author Pedro Pinheiro
 * @version 2.0
 * @since 2021-04-15
 */
@SuppressWarnings("unused")
public class CompareFiles {

	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;
	private boolean booleanColumnsFilled;
	private final String[] titles = new String[]{"is_god_class","is_long_method"};

	/**
	 * Constructor given two files.
	 * <p>
	 * This is for the situation when User already has chosen to save an Excel file with his rules.
	 * 
	 * @param csFileDefault Path of Excel file which contains code smells columns filled. We call this "Teórico" on the GUI. 
	 * @param csFileCreated Path of Excel file created when User already submitted metricsFile and
	 *                      saved rules, which will have code smells columns filled
	 *                      accordingly. We call this "Code Smells" on the GUI.
	 */
	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
		this.booleanColumnsFilled = true;
	}

	/**
	 * Constructor given three files. 
	 * <p>
	 * This is for the situation when User chose not to save an Excel file with his rules.
	 * 
	 * @param csFileDefault Path of Excel file which contains code smells columns filled. We call this "Teórico" on the GUI. 
	 * @param metricsFile   Path of Excel file after User submits project to be analysed which won't have any code smells columns. We call this "Métricas" on the GUI.
	 * @param rulesFile     Path of text file after User submits and saves rules.  We call this "Regras" on the GUI.
	 */
	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		this.csFileDefault = csFileDefault;
		this.metricsFile = metricsFile;
		this.rulesFile = rulesFile;
		this.booleanColumnsFilled = false;
	}

	/**
	 * Gets the corresponding Column Indexes of titles (code smells) that represent Column
	 * Titles in two Excel files.
	 * 
	 * @return LinkedHashMap<String, Integer> in which the String is the Column
	 *         Title and the Integer is the corresponding Column Index per Excel
	 *         file.
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	private LinkedHashMap<String, Integer> getColIndexesByTitles() throws Exception {
		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		Object[] headerDefault = ExcelDealer.getRow(csFileDefault, 0, 0);
		Object[] headerCreated;
		if(booleanColumnsFilled){
			headerCreated = ExcelDealer.getRow(csFileCreated, 0, 0);
		}else{
			headerCreated = ExcelDealer.getRow(metricsFile, 0, 0);
		}

		for (String title : titles) {

			for (int i = 0; i < headerDefault.length; i++) {
				if (String.valueOf(headerDefault[i]).equalsIgnoreCase(title)) {
					indexesMap.put("default " + String.valueOf(headerDefault[i]).toLowerCase(), i);
				}
			}
			for (int i = 0; i < headerCreated.length; i++) {
				if (String.valueOf(headerCreated[i]).equalsIgnoreCase(title)) {
					indexesMap.put(String.valueOf(headerCreated[i]).toLowerCase(), i);
				}
			}
		}
		return indexesMap;
	}

	/**
	 * Compares between two strings to return the right Indicator for each
	 * situation. Is used to compare String values of two Excel cells in method
	 * compareWith2Files and to compare String values of one Excel cell and one
	 * MethodRuleAnalysis map value in method compareWith3Files.
	 * 
	 * @param defaultText Given string.
	 * @param createdText Given string.
	 * @return Indicator This is the right Indicator after comparison of given
	 *         strings.
	 *@throws IllegalStateException If given strings do not correspond to valid values.
	 */
	private static Indicator parseIndicator(String defaultText, String createdText) {
		List<String> valid = Arrays.asList("TRUE", "FALSE","true","false");
		if (!valid.contains(defaultText) || !valid.contains(createdText)) {
			throw new IllegalStateException("Ficheiro mal formatado");
		}
		return (defaultText.equalsIgnoreCase("TRUE") ? (createdText.equalsIgnoreCase("TRUE") ? Indicator.VP : Indicator.FN) : (createdText.equalsIgnoreCase("TRUE") ? Indicator.FP : Indicator.VN));
	}

	/**
	 * When CompareFiles receives 2 Excel files ("Teórico" and "Code Smells"), this is the method that allows the
	 * comparison between them and the code smell detection quality evaluation.
	 * 
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class.
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	private Quality compareWith2Files() throws Exception {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles();
		HashMap<String, Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String, Indicator> saveIndsPerClass = new HashMap<>();

		for (Object[] objDefaultExcel : ExcelDealer.getAllRows(csFileDefault, 0)) {
			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);
			for (Object[] objCreatedExcel : ExcelDealer.getAllRows(csFileCreated, 0)) {
				MethodData dataCreatedExcel = new MethodData(objCreatedExcel);
				String created_package = dataCreatedExcel.getPackageName();
				String created_class = dataCreatedExcel.getClassName();
				String created_method = dataCreatedExcel.getMethodName();
				if(matchesFields(dataDefaultExcel,created_package,created_class,created_method)) {
					for (String title : titles) {
						String cellTextDefault = String.valueOf(objDefaultExcel[(int) indexesMap.get("default " + title.toLowerCase())]);
						String cellTextCreated = String.valueOf(objCreatedExcel[(int) indexesMap.get(title.toLowerCase())]);
						if (title.equalsIgnoreCase("is_god_class")
								&& !saveIndsPerClass.containsKey(created_package + " " + created_class)) {
							Indicator indicator_godclass = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerClass.put(created_package + " " + created_class, indicator_godclass);
						} else if (title.equalsIgnoreCase("is_long_method")) {
							Indicator indicator_longmethod = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerMethod.put(created_package + " " + created_class + " " + created_method,
									indicator_longmethod);
						}
					}	
				}
			}
		}
		return new Quality(saveIndsPerMethod, saveIndsPerClass);
	}

	
	/**
	 * When CompareFiles receives 3 files ("Teórico","Métricas" and "Regras"), this is the method that allows the
	 * comparison between them and the code smell detection quality evaluation.
	 * 
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class.
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	private Quality compareWith3Files() throws Exception {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles();
		HashMap<String, Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String, Indicator> saveIndsPerClass = new HashMap<>();
		MetricsRuleAnalysis mra = new MetricsRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),Rule.allRules(rulesFile));

		for (Object[] objDefaultExcel : ExcelDealer.getAllRows(csFileDefault, 0)) {
			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);
				for (int i = 0; i < mra.getMethodsData().size(); i++) {
					String god_class = String.valueOf(mra.getCodeSmellDetectedMap().get("is_God_Class").get(i)).toUpperCase();
					String long_method = String.valueOf(mra.getCodeSmellDetectedMap().get("is_Long_Method").get(i)).toUpperCase();
					String cellTextDefault_godclass = String.valueOf(objDefaultExcel[(int) indexesMap.get("default " + "is_god_class")]);
					String cellTextDefault_longmethod = String.valueOf(objDefaultExcel[(int) indexesMap.get("default " + "is_long_method")]);
					String created_package = mra.getMethodsData().get(i).getPackageName();
					String created_class = mra.getMethodsData().get(i).getClassName();
					String created_method = mra.getMethodsData().get(i).getMethodName();
					if(matchesFields(dataDefaultExcel,created_package,created_class,created_method)) {
						if (!saveIndsPerClass.containsKey(created_package + " " + created_class)) {
							Indicator indicator_godclass = parseIndicator(cellTextDefault_godclass, god_class);
							saveIndsPerClass.put(created_package + " " + created_class, indicator_godclass);
						}
							Indicator indicator_longmethod = parseIndicator(cellTextDefault_longmethod, long_method);
							saveIndsPerMethod.put(created_package + " " + created_class + " " + created_method,indicator_longmethod);
					}
				}
			}
		return new Quality(saveIndsPerMethod, saveIndsPerClass);
	}
	
	
	/**
	 * Overall, allows to know if package, class and method are the same between two files. 
	 * This is an auxiliary method to avoid code repetition on CompareWith2Files and CompareWith3Files.
	 * @param dataDefaultExcel Given MethodData object from DefaultExcel
	 * @param created_package Given String that should be the package name from CreatedExcel
	 * @param created_class Given String that should be the class name from CreatedExcel
	 * @param created_method Given String that should be the method name from CreatedExcel
	 * @return Boolean
	 */
	private boolean matchesFields(MethodData dataDefaultExcel,String created_package,String created_class,String created_method) {
		boolean matches = false;
		String default_package = dataDefaultExcel.getPackageName();
		String default_class = dataDefaultExcel.getClassName();
		String default_method = dataDefaultExcel.getMethodName();
		if (default_package.equals(created_package) && default_class.contains(created_class)
					&& default_method.replaceFirst("\\(\\w+\\.", "(").replaceAll("\\,\\w+\\.", ",")
							.equals(created_method.replaceFirst("\\(\\w+\\.", "(").replaceAll("\\,\\w+\\.", ","))) {
				matches = true;
			}
		return matches;
	}

	/**
	 * Allows to evaluate the quality of code smell detection by choosing the right
	 * comparison method.
	 * 
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	public Quality testQuality() throws Exception {
		if (booleanColumnsFilled) {
			return compareWith2Files();
		} else {
			return compareWith3Files();
		}
	}
}
