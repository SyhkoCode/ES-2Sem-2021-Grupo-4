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

@SuppressWarnings("unused")
public class CompareFiles {

	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;
	private ExcelDealer excelDealerDefault;
	private ExcelDealer excelDealerCreated;
	private boolean booleanColumnsFilled;

	HashMap<String,Indicator> saveIndsPerMethod = new HashMap<>();
	HashMap<String,Indicator> saveIndsPerClass = new HashMap<>();

	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
		this.excelDealerDefault = new ExcelDealer(csFileDefault, true, new int[] {});
		this.excelDealerCreated = new ExcelDealer(csFileCreated, true, new int[] {});
		this.booleanColumnsFilled = true;
	}

	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		this.csFileDefault = csFileDefault;
		this.metricsFile = metricsFile;
		this.rulesFile = rulesFile;
		this.excelDealerDefault = new ExcelDealer(csFileDefault, true, new int[] {});
		this.excelDealerCreated = new ExcelDealer(metricsFile, true, new int[] {7,10});
		this.booleanColumnsFilled = false;
	}

	public LinkedHashMap<String, Integer> getColIndexesByTitles(String[] titles) {
		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		for (String title : titles) {
			for (int i = 0; i < excelDealerDefault.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put("default " + String.valueOf(excelDealerDefault.getExcelHeader(0)[i]), i);
				}
			}
			for (int i = 0; i < excelDealerCreated.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put(String.valueOf(excelDealerCreated.getExcelHeader(0)[i]), i);
				}
			}
		}
		return indexesMap;
	}
	
	private static Indicator parseIndicator(String defaultText, String createdText) {
		List<String> valid = Arrays.asList("TRUE", "FALSE");
		if (!valid.contains(defaultText) || !valid.contains(defaultText)) {
			throw new IllegalStateException("Ficheiro mal formatado");
		}
		return (defaultText.equals("TRUE") ? (createdText.equals("TRUE") ? Indicator.VP : Indicator.FN) : (createdText.equals("TRUE") ? Indicator.FP : Indicator.VN));
	}

	public void compareWith2Files(String[] titles) {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles(titles);
		
		for(Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);

				for (Object[] objCreatedExcel : excelDealerCreated.getAllRows(0)) {

					MethodData dataCreatedExcel = new MethodData(objCreatedExcel);

					if (dataDefaultExcel.getPackageName().equals(dataCreatedExcel.getPackageName())
							&& dataDefaultExcel.getClassName().equals(dataCreatedExcel.getClassName())
							&& dataDefaultExcel.getMethodName().equals(dataCreatedExcel.getMethodName())) {

						int arrayIndex = 0;
						for (int i = 0; i < titles.length; i++) {
							String cellTextDefault = String.valueOf(objDefaultExcel[(int) indexesMap.values().toArray()[arrayIndex]]);
							String cellTextCreated = String.valueOf(objCreatedExcel[(int) indexesMap.values().toArray()[arrayIndex + 1]]);
							String classe = String.valueOf(objCreatedExcel[2]);
							String metodo = String.valueOf(objCreatedExcel[3]);

							Indicator indicator = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerMethod.put(metodo, indicator);
							saveIndsPerClass.put(classe, indicator);

							arrayIndex += 2;
						}
					}
				}
			}	
	}
	
	public void compareWith3Files() {
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
	}
	
	public List<HashMap<String, Indicator>> testQuality(String[] titles) {
		List<HashMap<String, Indicator>> indicators = new ArrayList<>();
		if (booleanColumnsFilled){
			compareWith2Files(titles);
		} else {
			compareWith3Files();
		}
		indicators.add(saveIndsPerClass);
		indicators.add(saveIndsPerMethod);
		return indicators;
	}
	
	public List<HashMap<String, Indicator>> testQualityAntigo(String[] titles) {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles(titles);

		for(Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {

			MethodData dataDefaultExcel = new MethodData(objDefaultExcel);

			if (booleanColumnsFilled){

				for (Object[] objCreatedExcel : excelDealerCreated.getAllRows(0)) {

					MethodData dataCreatedExcel = new MethodData(objCreatedExcel);

					if (dataDefaultExcel.getPackageName().equals(dataCreatedExcel.getPackageName())
							&& dataDefaultExcel.getClassName().equals(dataCreatedExcel.getClassName())
							&& dataDefaultExcel.getMethodName().equals(dataCreatedExcel.getMethodName())) {

						int arrayIndex = 0;
						for (int i = 0; i < titles.length; i++) {
							String cellTextDefault = String.valueOf(objDefaultExcel[(int) indexesMap.values().toArray()[arrayIndex]]);
							String cellTextCreated = String.valueOf(objCreatedExcel[(int) indexesMap.values().toArray()[arrayIndex + 1]]);
							String classe = String.valueOf(objCreatedExcel[2]);
							String metodo = String.valueOf(objCreatedExcel[3]);

							Indicator indicator = parseIndicator(cellTextDefault, cellTextCreated);
							saveIndsPerMethod.put(metodo, indicator);
							saveIndsPerClass.put(classe, indicator);

							arrayIndex += 2;
						}
					}
				}
			} else {
				/* o mra estar dentro de cada iteracao por row do objDefaultExcel era o q estava a fazer demorar imenso tempo*/
				MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),Rule.allRules(new File(rulesFile)));
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
		}
		List<HashMap<String, Indicator>> indicators = new ArrayList<>();
		indicators.add(saveIndsPerClass);
		indicators.add(saveIndsPerMethod);
		return indicators;
	}
	
	public HashMap<String,Indicator> getIndicatorsPerClass(List<HashMap<String, Indicator>> indicators) {
		return indicators.get(0);
	}
	
	public HashMap<String,Indicator> getIndicatorsPerMethod(List<HashMap<String, Indicator>> indicators) {
		return indicators.get(1);
	}
	
	public long countIndicatorInMap(Indicator indicator, HashMap<String,Indicator> indicatorsMap) {
		return indicatorsMap.values().stream().filter(v -> v.equals(indicator)).count();		
	}

	public static void main(String[] args) throws IOException {
//		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
//		
//		CompareFiles cf = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx", "C:\\Users\\Pedro Pinheiro\\Pictures\\jasml_0.10_metrics.xlsx","C:\\Users\\Pedro Pinheiro\\Desktop\\rules.txt" );
//
//		
//		List<HashMap<String, Indicator>> indicators = cf.testQuality(new String[]{"is_god_class","is_long_method"});	
//		HashMap<String,Indicator> indicatorsPerClassMap = cf.getIndicatorsPerClass(indicators);
//		HashMap<String,Indicator> indicatorsPerMethodMap = cf.getIndicatorsPerMethod(indicators);
//		
//		System.out.println(indicatorsPerClassMap.entrySet());
//		System.out.println(indicatorsPerMethodMap.entrySet());
//		System.out.println("No de FPs: "+cf.countIndicatorInMap(Indicator.FP,indicatorsPerClassMap)+ " em "+indicatorsPerClassMap.size()+" classes");
//		System.out.println("No de FPs: "+cf.countIndicatorInMap(Indicator.FP,indicatorsPerMethodMap)+ " em "+indicatorsPerMethodMap.size()+" metodos");

		CompareFiles cf2 = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx", "testeregras");
		
		List<HashMap<String, Indicator>> indicators = cf2.testQuality(new String[]{"is_god_class","is_long_method"});	
		HashMap<String,Indicator> indicatorsPerClassMap = cf2.getIndicatorsPerClass(indicators);
		HashMap<String,Indicator> indicatorsPerMethodMap = cf2.getIndicatorsPerMethod(indicators);
		
		System.out.println(indicatorsPerClassMap.entrySet());
		System.out.println(indicatorsPerMethodMap.entrySet());
		System.out.println("No de FPs: "+cf2.countIndicatorInMap(Indicator.FP,indicatorsPerClassMap)+ " em "+indicatorsPerClassMap.size()+" classes");
		System.out.println("No de FPs: "+cf2.countIndicatorInMap(Indicator.FP,indicatorsPerMethodMap)+ " em "+indicatorsPerMethodMap.size()+" metodos");


	}

}
