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
 * of code smell detection
 * 
 * @author Sofia Chaves, Pedro Pinheiro
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
	 * Gets the corresponding Column Indexes of given Strings that represent Column
	 * Titles in two Excel files
	 * 
	 * @param titles These are the Title Columns required to get their indexes.
	 * @return LinkedHashMap<String, Integer> in which the String is the Column
	 *         Title and the Integer is the corresponding Column Index per Excel
	 *         file
	 * @throws Exception
	 */
	private LinkedHashMap<String, Integer> getColIndexesByTitles(String[] titles) throws Exception {
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
	 */
	private static Indicator parseIndicator(String defaultText, String createdText) {
		List<String> valid = Arrays.asList("TRUE", "FALSE");
		if (!valid.contains(defaultText) || !valid.contains(defaultText)) {
			throw new IllegalStateException("Ficheiro mal formatado");
		}
		return (defaultText.equalsIgnoreCase("TRUE") ? (createdText.equalsIgnoreCase("TRUE") ? Indicator.VP : Indicator.FN)
				: (createdText.equalsIgnoreCase("TRUE") ? Indicator.FP : Indicator.VN));
	}

	/**
	 * When CompareFiles receives 2 Excel files, this is the method that allows the
	 * comparison between them and the code smell detection quality evaluation.
	 * 
	 * @param titles These are the Title Columns required to get their indexes.
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class
	 * @throws Exception
	 */
	private Quality compareWith2Files(String[] titles) throws Exception {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles(titles);
		HashMap<String, Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String, Indicator> saveIndsPerClass = new HashMap<>();

		for (Object[] objDefaultExcel : ExcelDealer.getAllRows(csFileDefault, 0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);

			for (Object[] objCreatedExcel : ExcelDealer.getAllRows(csFileCreated, 0)) {

				MethodData dataCreatedExcel = new MethodData(objCreatedExcel);

				String created_package = dataCreatedExcel.getPackageName();
				String created_class = dataCreatedExcel.getClassName();
				String created_method = dataCreatedExcel.getMethodName();

				if (dataDefaultExcel.getPackageName().equals(created_package)
						&& dataDefaultExcel.getClassName().contains(created_class)
						&& dataDefaultExcel.getMethodName().replaceFirst("\\(\\w+\\.", "(")
								.replaceAll("\\,\\w+\\.", ",")
								.equals(created_method.replaceFirst("\\(\\w+\\.", "(").replaceAll("\\,\\w+\\.", ","))) {

					for (String title : titles) {

						String cellTextDefault = String
								.valueOf(objDefaultExcel[(int) indexesMap.get("default " + title.toLowerCase())]);
						String cellTextCreated = String
								.valueOf(objCreatedExcel[(int) indexesMap.get(title.toLowerCase())]);

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
	 * When CompareFiles receives 3 files, this is the method that allows the
	 * comparison between them and the code smell detection quality evaluation.
	 * 
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class
	 * @throws Exception 
	 */
	private Quality compareWith3Files() throws Exception {
		HashMap<String, Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String, Indicator> saveIndsPerClass = new HashMap<>();
		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),
				Rule.allRules(new File(rulesFile)));

		for (Object[] objDefaultExcel : ExcelDealer.getAllRows(csFileDefault, 0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);

			for (int i = 0; i < mra.getMethods().size(); i++) {
				String god_class = String.valueOf(mra.getMap().get("is_God_Class").get(i)).toUpperCase();
				String long_method = String.valueOf(mra.getMap().get("is_Long_Method").get(i)).toUpperCase();

				String cellTextDefault_godclass = String.valueOf(objDefaultExcel[7]);
				String cellTextDefault_longmethod = String.valueOf(objDefaultExcel[10]);

				String created_package = mra.getMethods().get(i).getPackageName();
				String created_class = mra.getMethods().get(i).getClassName();
				String created_method = mra.getMethods().get(i).getMethodName();

				if (dataDefaultExcel.getPackageName().equals(created_package)
						&& dataDefaultExcel.getClassName().contains(created_class)
						&& dataDefaultExcel.getMethodName().replaceFirst("\\(\\w+\\.", "(")
								.replaceAll("\\,\\w+\\.", ",")
								.equals(created_method.replaceFirst("\\(\\w+\\.", "(").replaceAll("\\,\\w+\\.", ","))) {
					if (!saveIndsPerClass.containsKey(created_package + " " + created_class)) {
						Indicator indicator_godclass = parseIndicator(cellTextDefault_godclass, god_class);
						saveIndsPerClass.put(created_package + " " + created_class, indicator_godclass);
					}

					Indicator indicator_longmethod = parseIndicator(cellTextDefault_longmethod, long_method);
					saveIndsPerMethod.put(created_package + " " + created_class + " " + created_method,
							indicator_longmethod);
				}
			}
		}
		return new Quality(saveIndsPerMethod, saveIndsPerClass);
	}

	/**
	 * Allows to evaluate the quality of code smell detection by choosing the right
	 * comparison method.
	 * 
	 * @param titles These are the Column Titles required to evaluate.
	 * @return Quality object that contains both HashMaps with Indicators per Method
	 *         and per Class
	 * @throws Exception
	 */
	public Quality testQuality(String[] titles) throws Exception {
		if (booleanColumnsFilled) {
			return compareWith2Files(titles);
		} else {
			return compareWith3Files();
		}
	}

	/**
	 * este main vai desaparecer
	 */
//	public static void main(String[] args) throws IOException {
////		CompareFiles cf = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx", "C:\\Users\\Pedro Pinheiro\\Pictures\\jasml_0.10_metrics.xlsx","C:\\Users\\Pedro Pinheiro\\Desktop\\rules.txt" );
////		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
//		CompareFiles cf2 = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx", "testeregras.txt");		
//		Quality result = cf2.testQuality(new String[]{"is_god_class","is_long_method"});
//		result.getIndicatorsPerClass();	
//		result.getIndicatorsPerMethod();	
//		System.out.println(result.getIndicatorsPerMethod());
//		System.out.println(result.getIndicatorsPerClass());
//		System.out.println("No de FPs: "+result.countIndicatorInMethods(Indicator.FP)+" em "+ result.getIndicatorsPerMethod().size());
//	}
}
