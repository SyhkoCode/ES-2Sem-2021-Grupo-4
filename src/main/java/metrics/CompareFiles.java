package metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

	public LinkedHashMap<String, Integer> get_Targets_ColIndexes(String[] targets) {

		LinkedHashMap<String, Integer> indexesMap = new LinkedHashMap<>();

		for (String target : targets) {
			for (int i = 0; i < excelDealerDefault.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(target)) {
					indexesMap.put("default " + String.valueOf(excelDealerDefault.getExcelHeader(0)[i]), i);
					System.out.println(i);
				}
			}
			for (int i = 0; i < excelDealerCreated.getExcelHeader(0).length; i++) {
				if (String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(target)) {
					indexesMap.put("created " + String.valueOf(excelDealerCreated.getExcelHeader(0)[i]), i);
					System.out.println(i);
				}
			}
		}
		System.out.println(indexesMap.size());
		return indexesMap;
	}

	public void testQuality(String[] strs) {

		HashMap<String, Integer> indexesMap = get_Targets_ColIndexes(strs);

		for (Object[] objDefaultExcel : excelDealerDefault.getAllRows(0)) {
			for (Object[] objCreatedExcel : excelDealerCreated.getAllRows(0)) {

				MethodData methodDataDefaultExcel = new MethodData(objDefaultExcel);
				MethodData methodDataCreatedExcel = new MethodData(objCreatedExcel);
				
				
				if (methodDataDefaultExcel.getPackageName().equals(methodDataCreatedExcel.getPackageName())
						&& methodDataDefaultExcel.getClassName().equals(methodDataCreatedExcel.getClassName())
						&& methodDataDefaultExcel.getMethodName().equals(methodDataCreatedExcel.getMethodName())) {

					if (booleanColumnsFilled) {

						int a = 0;
						for (int i = 0; i < strs.length; i++) {
							Object[] indexArray = indexesMap.values().toArray();
							Object[] targetColumnsNames = indexesMap.keySet().toArray();
							
							if (String.valueOf(objDefaultExcel[(int) indexArray[a]]).equals("TRUE")
									&& String.valueOf(objCreatedExcel[(int) indexArray[a + 1]])
											.equals("TRUE")) {

								System.out.println("Verdadeiro Positivo em:  " + targetColumnsNames[a + 1]
										+ "  " + String.valueOf(objCreatedExcel[3]));

							}

							else if (String.valueOf(objDefaultExcel[(int) indexesMap.values().toArray()[a]]).equals("FALSE")
									&& String.valueOf(objCreatedExcel[(int) indexesMap.values().toArray()[a + 1]])
											.equals("TRUE")) {

								System.out.println("Falso Positivo em:  " + targetColumnsNames[a + 1] + "  "
										+ String.valueOf(objCreatedExcel[3]));

							}

							else if (String.valueOf(objDefaultExcel[(int) indexesMap.values().toArray()[a]]).equals("FALSE") 
									&& String.valueOf(objCreatedExcel[(int) indexesMap.values().toArray()[a + 1]])
											.equals("FALSE")) {

								System.out.println("Verdadeiro Negativo em:  " + targetColumnsNames[a + 1]
										+ "  " + String.valueOf(objCreatedExcel[3]));
							}

							else if (String.valueOf(objDefaultExcel[(int) indexesMap.values().toArray()[a]]).equals("TRUE")
									&& String.valueOf(objCreatedExcel[(int) indexesMap.values().toArray()[a + 1]])
											.equals("FALSE")) {

								System.out.println("Falso Negativo em:  " + targetColumnsNames[a + 1] + "  "
										+ String.valueOf(objCreatedExcel[3]));

							}
							a += 2;
						}
					} 
					//else {
//						int b = 0;
//						try {
//							String[] content = new String(Files.readAllBytes(Paths.get(rulesFile))).split("\\n");
//
//							for (int i = 0; i < content.length; i += 2) {
//								if (String.valueOf(objDefaultExcel[(int) indexesList.values().toArray()[b]])
//										.equals("TRUE") &&
//
//										String.valueOf(objCreatedExcel[(int) indexesList.values().toArray()[b + 1]])
//												.equals("TRUE")) {
//								}
//							}
//
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}

				}

			}
		}
	}

	public ArrayList<Indicator> compareCS(XSSFSheet csFileDefault, XSSFSheet csFileCreated) {

		ArrayList<Indicator> indicators = new ArrayList<>(); // ou HashMap<String,Indicator> indicators = new
																// HashMap<>();

		/*
		 * iterar rows das sheets csFileDefault e csFileCreated obter os booleans das
		 * colunas index obtidas pelos getters acima comparar:
		 * if(csFileDefault_isgodclass && csFileCreated_isgodclass)
		 * indicators.add(Indicator.VP); // temos de ver o q queremos fazer..criar uma
		 * coluna so pa isto e escrever Indicator.VP.getName na row correspondente ou
		 * pintar esta celula mesmo else if(!csFileDefault_isgodclass &&
		 * csFileCreated_isgodclass) indicators.add(Indicator.FP); else
		 * if(csFileDefault_isgodclass && !csFileCreated_isgodclass)
		 * indicators.add(Indicator.FN); else indicators.add(Indicator.VN);
		 * 
		 * if(csFileDefault_islongmethod && csFileCreated_islongmethod)
		 * indicators.add(Indicator.VP); else if(!csFileDefault_islongmethod &&
		 * csFileCreated_islongmethod) indicators.add(Indicator.FP); else
		 * if(csFileDefault_islongmethod && !csFileCreated_islongmethod)
		 * indicators.add(Indicator.FN); else indicators.add(Indicator.VN);
		 */
		return indicators;

	}

	public static void main(String[] args) throws IOException {
		CompareFiles cf = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx",
				"C:\\Users\\Pedro Pinheiro\\Desktop\\jasml_0.10_metrics.xlsx");
		// ArrayList<Helper> yolllo = cf.get_Targets_ColIndexes(new
		// String[]{"is_god_class"});
		// System.out.println(yolllo.size());
		cf.testQuality(new String[] { "is_god_class", "is_long_method" });
		// FileInputStream file = new FileInputStream("C:\\Users\\Pedro
		// Pinheiro\\Downloads\\Code_Smells.xlsx");
		// XSSFSheet sheet = new XSSFWorkbook(file).getSheet("Code Smells");
		// System.out.println(get_Target_ColIndex(sheet,"is_God_Class"));

	}

}
