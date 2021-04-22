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
 * Allows to compare between different files to be able to evaluate the quality of code smell detection
 * @author Sofia Chaves, Pedro Pinheiro
 * @version 2.0
 * @since   2021-04-15 
 */
@SuppressWarnings("unused")
public class CompareFiles {

	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;
	private ExcelDealer excelDealerDefault;
	private ExcelDealer excelDealerCreated;
	private boolean booleanColumnsFilled;

	/**
	 * Constructor given two files
	 * This is for the situation when User chose to save an Excel file with his rules.
	 * @param csFileDefault Path of Excel file after User submits project.
	 * @param csFileCreated Path of Excel file created when User also submitted and saved rules, which will have code smells columns filled accordingly.
	 */
	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
		this.excelDealerDefault = new ExcelDealer(csFileDefault, true, new int[] {});
		this.excelDealerCreated = new ExcelDealer(csFileCreated, true, new int[] {});
		this.booleanColumnsFilled = true;
	}

	/**
	 * Constructor given three files
	 * This is for the situation when User chose not to save an Excel file with his rules.
	 * @param csFileDefault Path of Excel file after User submits project.
	 * @param metricsFile Path of Excel file with code smells columns not filled.
	 * @param rulesFile Path of text file after User submits rules.
	 */
	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		this.csFileDefault = csFileDefault;
		this.metricsFile = metricsFile;
		this.rulesFile = rulesFile;
		this.excelDealerDefault = new ExcelDealer(csFileDefault, true, new int[] {});
		this.excelDealerCreated = new ExcelDealer(metricsFile, true, new int[] {7,10});
		this.booleanColumnsFilled = false;
	}

	/**
	 * Gets the corresponding Column Indexes of given Strings that represent Column Titles in two Excel files
	 * @param titles These are the Title Columns required to get their indexes.
	 * @return LinkedHashMap<String, Integer> in which the String is the Column Title and the Integer is the corresponding Column Index per Excel file
	 */
	private LinkedHashMap<String, Integer> getColIndexesByTitles(String[] titles) {
		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		for (String title : titles) {
			for (int i = 0; i < excelDealerDefault.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put("default " + String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).toLowerCase(), i);
				}
			}
			for (int i = 0; i < excelDealerCreated.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put(String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).toLowerCase(), i);
				}
			}
		}
		return indexesMap;
	}
	
	/**
	 * Compares between two strings to return the right Indicator for each situation.
	 * Is used to compare String values of two Excel cells in method compareWith2Files and to compare String values of one Excel cell and one MethodRuleAnalysis map value in method compareWith3Files.
	 * @param defaultText Given string.
	 * @param createdText Given string.
	 * @return Indicator This is the right Indicator after comparison of given strings.
	 */
	private static Indicator parseIndicator(String defaultText, String createdText) {
		List<String> valid = Arrays.asList("TRUE", "FALSE");
		if (!valid.contains(defaultText) || !valid.contains(defaultText)) {
			throw new IllegalStateException("Ficheiro mal formatado");
		}
		return (defaultText.equals("TRUE") ? (createdText.equals("TRUE") ? Indicator.VP : Indicator.FN) : (createdText.equals("TRUE") ? Indicator.FP : Indicator.VN));
	}

	/**
	 * When CompareFiles receives 2 Excel files, this is the method that allows the comparison between them and the code smell detection quality evaluation.
	 * @param titles These are the Title Columns required to get their indexes.
	 * @return Quality object that contains both HashMaps with Indicators per Method and per Class
	 */
	private Quality compareWith2Files(String[] titles) {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles(titles);
		HashMap<String,Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String,Indicator> saveIndsPerClass = new HashMap<>();
		
		for(Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);

				for (Object[] objCreatedExcel : excelDealerCreated.getAllRows(0)) {

					MethodData dataCreatedExcel = new MethodData(objCreatedExcel);

					if (dataDefaultExcel.getPackageName().equals(dataCreatedExcel.getPackageName())
							&& dataDefaultExcel.getClassName().equals(dataCreatedExcel.getClassName())
							&& dataDefaultExcel.getMethodName().equals(dataCreatedExcel.getMethodName())) {

						for (String title: titles) {
							String cellTextDefault = String.valueOf(objDefaultExcel[(int) indexesMap.get("default " + title.toLowerCase())]);
							String cellTextCreated = String.valueOf(objCreatedExcel[(int) indexesMap.get(title.toLowerCase())]);
							String classe = String.valueOf(objCreatedExcel[2]);
							String metodo = String.valueOf(objCreatedExcel[3]);
							
							if(title.equalsIgnoreCase("is_god_class")){
								Indicator indicator_godclass = parseIndicator(cellTextDefault, cellTextCreated);
								saveIndsPerClass.put(classe, indicator_godclass);
							} else if(title.equalsIgnoreCase("is_long_method")){
								Indicator indicator_longmethod = parseIndicator(cellTextDefault, cellTextCreated);
								saveIndsPerMethod.put(metodo, indicator_longmethod);
							}

						}
					}
				}
			}
		return new Quality(saveIndsPerMethod,saveIndsPerClass);	
	}
	
	/**
	 * When CompareFiles receives 3 files, this is the method that allows the comparison between them and the code smell detection quality evaluation.
	 * @return Quality object that contains both HashMaps with Indicators per Method and per Class
	 */
	private Quality compareWith3Files() {
		HashMap<String,Indicator> saveIndsPerMethod = new HashMap<>();
		HashMap<String,Indicator> saveIndsPerClass = new HashMap<>();
		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),Rule.allRules(new File(rulesFile)));
		
		for(Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);
			
			for (int i = 0; i < mra.getMethods().size(); i++) {
				String god_class = String.valueOf(mra.getMap().get("is_God_Class").get(i)).toUpperCase();
				String long_method = String.valueOf(mra.getMap().get("is_Long_Method").get(i)).toUpperCase();
				
				String cellTextDefault_godclass = String.valueOf(objDefaultExcel[7]);
				String cellTextDefault_longmethod = String.valueOf(objDefaultExcel[10]);
				String classe = dataDefaultExcel.getClassName();
				String metodo = dataDefaultExcel.getMethodName();

				if(dataDefaultExcel.getPackageName().equals(mra.getMethods().get(i).getPackageName()) 
						&& dataDefaultExcel.getClassName().equals(mra.getMethods().get(i).getClassName())
						&& dataDefaultExcel.getMethodName().equals(mra.getMethods().get(i).getMethodName())) {
					
					Indicator indicator_godclass = parseIndicator(cellTextDefault_godclass, god_class);
					saveIndsPerClass.put(classe, indicator_godclass);
					
					Indicator indicator_longmethod = parseIndicator(cellTextDefault_longmethod, long_method);
					saveIndsPerMethod.put(metodo, indicator_longmethod);
				}
			}
		}
		return new Quality(saveIndsPerMethod,saveIndsPerClass);
	}
	
	/**
	 * Allows to evaluate the quality of code smell detection by choosing the right comparison method.
	 * @param titles These are the Column Titles required to evaluate.
	 * @return Quality object that contains both HashMaps with Indicators per Method and per Class
	 */
	public Quality testQuality(String[] titles) {
		if (booleanColumnsFilled){
			return compareWith2Files(titles);
		} else {
			return compareWith3Files();
		}
	}

	/**
	 * este main vai desaparecer
	 */
	public static void main(String[] args) throws IOException {
		CompareFiles cf2 = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Desktop\\Snake_test.xlsx", "C:\\Users\\Pedro Pinheiro\\Downloads\\boop.xlsx");
		//CompareFiles cf2 = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx", "testeregras");		
		Quality result = cf2.testQuality(new String[] {"is_god_class", "is_long_method"});
		//result.getIndicatorsPerClass();	
		System.out.println(result.getIndicatorsPerClass());
		System.out.println("No de FPs: "+result.countIndicatorInClasses(Indicator.FP)+" em "+ result.getIndicatorsPerClass().size());
		System.out.println(result.getIndicatorsPerMethod());
		System.out.println("No de FPs: "+result.countIndicatorInMethods(Indicator.FP)+" em "+ result.getIndicatorsPerMethod().size());
	}

}
