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
	 * Constructor given two files This is for the situation when User chose to save
	 * an Excel file with his rules.
	 * 
	 * @param csFileDefault Path of Excel file after User submits project.
	 * @param csFileCreated Path of Excel file created when User also submitted and
	 *                      saved rules, which will have code smells columns filled
	 *                      accordingly.
	 */
	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
		this.booleanColumnsFilled = true;
	}

	/**
	 * Constructor given three files This is for the situation when User chose not
	 * to save an Excel file with his rules.
	 * 
	 * @param csFileDefault Path of Excel file after User submits project.
	 * @param metricsFile   Path of Excel file with code smells columns not filled.
	 * @param rulesFile     Path of text file after User submits rules.
	 */
	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		this.csFileDefault = csFileDefault;
		this.metricsFile = metricsFile;
		this.rulesFile = rulesFile;
		this.booleanColumnsFilled = false;
	}

	/**
	 * Gets the corresponding Column Indexes of titles (code smells) that represent Column
	 * Titles in two Excel files
	 * 
	 * @return LinkedHashMap<String, Integer> in which the String is the Column
	 *         Title and the Integer is the corresponding Column Index per Excel
	 *         file
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	private LinkedHashMap<String, Integer> getColIndexesByTitles() throws Exception {
		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		Object[] headerDefault = ExcelDealer.getRow(csFileDefault, 0, 0);
		Object[] headerCreated = ExcelDealer.getRow(csFileCreated, 0, 0);

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
		return (defaultText.equalsIgnoreCase("TRUE") ? (createdText.equalsIgnoreCase("TRUE") ? Indicator.VP : Indicator.FN)
				: (createdText.equalsIgnoreCase("TRUE") ? Indicator.FP : Indicator.VN));
	}

	/**
	 * When CompareFiles receives 2 Excel files, this is the method that allows the
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
				if(matchesFields(dataDefaultExcel,dataCreatedExcel,null,dataCreatedExcel.getPackageName(),dataCreatedExcel.getClassName(),dataCreatedExcel.getMethodName())) {
					for (String title : titles) {
						String cellTextDefault = String.valueOf(objDefaultExcel[(int) indexesMap.get("default " + title.toLowerCase())]);
						String cellTextCreated = String.valueOf(objCreatedExcel[(int) indexesMap.get(title.toLowerCase())]);
						if (title.equalsIgnoreCase("is_god_class")
								&& !saveIndsPerClass.containsKey(dataCreatedExcel.getPackageName() + " " + dataCreatedExcel.getClassName())) {
							System.out.println("hhhh");
							Indicator indicator_godclass = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerClass.put(dataCreatedExcel.getPackageName() + " " + dataCreatedExcel.getClassName(), indicator_godclass);
						} else if (title.equalsIgnoreCase("is_long_method")) {
							Indicator indicator_longmethod = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerMethod.put(dataCreatedExcel.getPackageName() + " " + dataCreatedExcel.getClassName() + " " + dataCreatedExcel.getMethodName(),
									indicator_longmethod);
						}
					}	
				}
			}
		}
		return new Quality(saveIndsPerMethod, saveIndsPerClass);
	}

	
	/**
	 * When CompareFiles receives 3 files, this is the method that allows the
	 * comparison between them and the code smell detection quality evaluation.
	 * 
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class.
	 * @throws Exception Propagated Exception from ExcelDealer to be dealt with on the GUI.
	 */
	private Quality compareWith3Files() throws Exception {
		//HashMap<String, Integer> indexesMap = getColIndexesByTitles();
		HashMap<String, Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String, Indicator> saveIndsPerClass = new HashMap<>();
		MetricsRuleAnalysis mra = new MetricsRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),Rule.allRules(rulesFile));

		for (Object[] objDefaultExcel : ExcelDealer.getAllRows(csFileDefault, 0)) {
			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);
				for (int i = 0; i < mra.getMethodsData().size(); i++) {
					String god_class = String.valueOf(mra.getCodeSmellDetectedMap().get("is_God_Class").get(i)).toUpperCase();
					String long_method = String.valueOf(mra.getCodeSmellDetectedMap().get("is_Long_Method").get(i)).toUpperCase();
					String cellTextDefault_godclass = String.valueOf(objDefaultExcel[7]);
					String cellTextDefault_longmethod = String.valueOf(objDefaultExcel[10]);
					if(matchesFields(dataDefaultExcel,mra.getMethodsData().get(i),mra,mra.getMethodsData().get(i).getPackageName(),mra.getMethodsData().get(i).getClassName(),mra.getMethodsData().get(i).getMethodName())) {
						if (!saveIndsPerClass.containsKey(mra.getMethodsData().get(i).getPackageName() + " " + mra.getMethodsData().get(i).getClassName())) {
							Indicator indicator_godclass = parseIndicator(cellTextDefault_godclass, god_class);
							saveIndsPerClass.put(mra.getMethodsData().get(i).getPackageName() + " " + mra.getMethodsData().get(i).getClassName(), indicator_godclass);
						}
							Indicator indicator_longmethod = parseIndicator(cellTextDefault_longmethod, long_method);
							saveIndsPerMethod.put(mra.getMethodsData().get(i).getPackageName() + " " + mra.getMethodsData().get(i).getClassName() + " " + mra.getMethodsData().get(i).getMethodName(),indicator_longmethod);
					}
				}
			}
		return new Quality(saveIndsPerMethod, saveIndsPerClass);
	}
	
	
	private boolean matchesFields(MethodData dataDefaultExcel, MethodData dataCreatedExcel, MetricsRuleAnalysis mra,String created_package,String created_class,String created_method) {
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
