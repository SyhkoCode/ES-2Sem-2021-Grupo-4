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
		this.booleanColumnsFilled = false;
	}

	public LinkedHashMap<String, Integer> getColIndexesByTitles(String[] titles) {
		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		for (String title : titles) {
			for (int i = 0; i < excelDealerDefault.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put("default " + String.valueOf(excelDealerDefault.getExcelHeader(0)[i]), i);
//					System.out.println(i);
				}
			}
			for (int i = 0; i < excelDealerCreated.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(title)) {
					indexesMap.put(String.valueOf(excelDealerCreated.getExcelHeader(0)[i]), i);
//					System.out.println(i);
				}
			}
		}
//		System.out.println(indexesMap.size());
		return indexesMap;
	}
	
	
	private static Indicator parseIndicator(String defaultText, String createdText) {
		List<String> valid = Arrays.asList("TRUE", "FALSE");
		if (!valid.contains(defaultText) || !valid.contains(defaultText)) {
			throw new IllegalStateException("Ficheiro mal formatado");
		}
		return (defaultText.equals("TRUE") ? (createdText.equals("TRUE") ? Indicator.VP : Indicator.VN) : (createdText.equals("TRUE") ? Indicator.FP : Indicator.FN));
		
//		if (defaultText.equals("TRUE")) {
//			return createdText.equals("TRUE") ? Indicator.VP : Indicator.VN;
//		} else {
//			return createdText.equals("TRUE") ? Indicator.FP : Indicator.FN;
//		}
	}

	public List<HashMap<String, Indicator>> testQuality(String[] titles) {
		HashMap<String, Integer> indexesMap = getColIndexesByTitles(titles);

		for (Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {
			for (Object[] objCreatedExcel : excelDealerCreated.getAllRows(0)) {			

				MethodData dataDefaultExcel = new MethodData(objDefaultExcel);
				MethodData dataCreatedExcel = new MethodData(objCreatedExcel);

				if (dataDefaultExcel.getPackageName().equals(dataCreatedExcel.getPackageName())
						&& dataDefaultExcel.getClassName().equals(dataCreatedExcel.getClassName())
						&& dataDefaultExcel.getMethodName().equals(dataCreatedExcel.getMethodName())) {

					if (booleanColumnsFilled) {

						int arrayIndex = 0;
						for (int i = 0; i < titles.length; i++) {
//							String colTitle = String.valueOf(indexesMap.keySet().toArray()[arrayIndex + 1]);
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
					else {			
						
//						MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(metricsFile),Rule.allRules(new File(rulesFile)));
//						for (int i = 0; i < mra.getCodeSmellResults().get(0).get(0).length; i++) {
//							System.out.println(mra.getCodeSmellResults());
//						}
//						readCodeSmells(mra.getCodeSmellResults());
//											int b = 0;
//											try {
//												String[] content = new String(Files.readAllBytes(Paths.get(rulesFile))).split("\\n");
//					
//												for (int i = 0; i < content.length; i += 2) {
//													if (String.valueOf(objDefaultExcel[(int) indexesList.values().toArray()[b]])
//															.equals("TRUE") &&
//					
//															String.valueOf(objCreatedExcel[(int) indexesList.values().toArray()[b + 1]])
//																	.equals("TRUE")) {
//													}
//												}
//					
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//										}
					}

				}

			}
		}
//		System.out.println(saveIndsPerMethod.size());
//		System.out.println(saveIndsPerMethod.entrySet());
//		System.out.println(saveIndsPerClass.size());
//		System.out.println(saveIndsPerClass.entrySet());

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
		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
		
		List<HashMap<String, Indicator>> indicators = cf.testQuality(new String[]{"is_god_class","is_long_method"});	
		HashMap<String,Indicator> indicatorsPerClassMap = cf.getIndicatorsPerClass(indicators);
		HashMap<String,Indicator> indicatorsPerMethodMap = cf.getIndicatorsPerMethod(indicators);
		
		System.out.println(indicatorsPerClassMap.entrySet());
		System.out.println(indicatorsPerMethodMap.entrySet());
		System.out.println("Nº de FPs: "+cf.countIndicatorInMap(Indicator.FP,indicatorsPerClassMap)+ " em "+indicatorsPerClassMap.size()+" classes");
		System.out.println("Nº de FPs: "+cf.countIndicatorInMap(Indicator.FP,indicatorsPerMethodMap)+ " em "+indicatorsPerMethodMap.size()+" metodos");

//		CompareFiles cf2 = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics - Copy.xlsx", "testeregras");
//		cf2.testQuality(new String[]{"is_god_class","is_long_method"});


	}

}
