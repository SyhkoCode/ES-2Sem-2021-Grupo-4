package metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;


@SuppressWarnings("unused")
public class CompareFiles {

	
	
private class Helper {
		
		private String file_name;
		private String target;
		
		public Helper(String file_name, String target){
			this.file_name = file_name;
			this.target = target;
		}

		public String getFile_name() {
			return file_name;
		}

		public String getTarget() {
			return target;
		}
		
	}
	
	
	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;
	private ExcelDealer excelDealerDefault;
	private ExcelDealer excelDealerCreated;
	
	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
		this.excelDealerDefault  = new ExcelDealer(csFileDefault,true, new int[]{});
		this.excelDealerCreated = new ExcelDealer(csFileCreated,true, new int[]{});
	}


	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		this.csFileDefault = csFileDefault;
		this.metricsFile = metricsFile;
		this.rulesFile = rulesFile;
	}


	
	public LinkedHashMap<Helper,Integer> get_Targets_ColIndexes(String[] targets) {
		
		LinkedHashMap<Helper,Integer> map = new LinkedHashMap<>();
		
		for(String target: targets){
			for(int i=0; i<excelDealerDefault.getExcelHeader(0).length; i++){
					if(String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(target)){
						map.put(new Helper(excelDealerDefault.getExcel_file(),String.valueOf(excelDealerDefault.getExcelHeader(0)[i])), i);
					}
				}
			for(int i=0; i<excelDealerCreated.getExcelHeader(0).length; i++){
					if(String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(target)){
						map.put(new Helper(excelDealerCreated.getExcel_file(),String.valueOf(excelDealerCreated.getExcelHeader(0)[i])), i);
					}
				}
			}
			
			return map;
		}
		
	
public void testQuality(String[] strs) {
	
	HashMap<Helper,Integer> helpersList = get_Targets_ColIndexes(strs);
	
		
		for(Object[] objDefaultExcel: excelDealerDefault.getAllRows(0)){
			for(Object[] objCreatedExcel: excelDealerCreated.getAllRows(0)){
			
				if(String.valueOf(objDefaultExcel[1]).equals(String.valueOf(objCreatedExcel[1])) && String.valueOf(objDefaultExcel[2]).equals(String.valueOf(objCreatedExcel[2])) && String.valueOf(objDefaultExcel[3]).equals(String.valueOf(objCreatedExcel[3]))){
					int a = 0;
					for(int i = 0; i < strs.length; i++){
						
//						System.out.println(String.valueOf(objDefaultExcel[3]) + "  " + String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]) + "    " +   String.valueOf(objCreatedExcel[3])    +  "  " + String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]));
//						System.out.println((int)helpersList.values().toArray()[a]);
//						System.out.println((int)helpersList.values().toArray()[a+1]);
					//	if(String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]).equals(String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]))){
							
							//System.out.println(String.valueOf(objDefaultExcel[3]) + "  " + String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]) + "    " +   String.valueOf(objCreatedExcel[3])    +  "  " + String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]));
					//	System.out.println("Found one :)");
							
					//	}
						
						
						if(String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]).equals("TRUE") &&
								
								String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]).equals("TRUE")){
							
							System.out.println("Verdadeiro Positivo em :" + String.valueOf(objCreatedExcel[3]));
							
						
						
						}
						
						
						else if(String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]).equals("FALSE") &&
								
								String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]).equals("TRUE")){
							
							System.out.println("Falso Positivo em :" + String.valueOf(objCreatedExcel[3]));
							
						
						
						}
						
						
						else if(String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]).equals("FALSE") &&
								
								String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]).equals("FALSE")){
							
							System.out.println("Verdadeiro Negativo em :" + String.valueOf(objCreatedExcel[3]));
							
						
						
						}
						
						
						else if(String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[a]]).equals("TRUE") &&
								
								String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[a+1]]).equals("FALSE")){
							
							System.out.println("Falso Negativo em :" + String.valueOf(objCreatedExcel[3]));
							
						
						
						}
						
						
						
						a+=2;
					}
					
					
		//			System.out.println(String.valueOf(objDefaultExcel[columnIndexDefault]));
//						System.out.println("yolo");
					}
					
						
				}
			}
		}
		
		
		
		
	//	System.out.println(counter);

//}
		
	
	
//		int index = 0;
//		for (Cell s : sheet.getRow(0)) {
//			if (s.getStringCellValue().equalsIgnoreCase(target)) {
//				index = s.getColumnIndex();
//				break;
//			}
//		}
//		
//		return index;	
	
	
	
//	private static String get_Value_Of_Cell(XSSFSheet csFileDefault, XSSFSheet csFileCreated) {
//		
//		List<Object[]> testingY = 
//		
//		
//		return "";	
//	}
	
	
	
	
	
	
	
	
	public ArrayList<Indicator> compareCS(XSSFSheet csFileDefault, XSSFSheet csFileCreated) {
		
		ArrayList<Indicator> indicators = new ArrayList<>(); // ou HashMap<String,Indicator> indicators = new HashMap<>();
		
		/*
		iterar rows das sheets csFileDefault e csFileCreated
			obter os booleans das colunas index obtidas pelos getters acima
			comparar: 
				if(csFileDefault_isgodclass && csFileCreated_isgodclass) indicators.add(Indicator.VP); // temos de ver o q queremos fazer..criar uma coluna so pa isto e escrever Indicator.VP.getName na row correspondente ou pintar esta celula mesmo
				else if(!csFileDefault_isgodclass && csFileCreated_isgodclass) indicators.add(Indicator.FP);
				else if(csFileDefault_isgodclass && !csFileCreated_isgodclass) indicators.add(Indicator.FN);
				else indicators.add(Indicator.VN);
	
				if(csFileDefault_islongmethod && csFileCreated_islongmethod) indicators.add(Indicator.VP);
				else if(!csFileDefault_islongmethod && csFileCreated_islongmethod) indicators.add(Indicator.FP);
				else if(csFileDefault_islongmethod && !csFileCreated_islongmethod) indicators.add(Indicator.FN);
				else indicators.add(Indicator.VN);
				*/	
			return indicators;
		
	}

	public static void main(String[] args) throws IOException {
		CompareFiles cf = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx", "C:\\Users\\Pedro Pinheiro\\Desktop\\jasml_0.10_metrics.xlsx");
		//ArrayList<Helper> yolllo = cf.get_Targets_ColIndexes(new String[]{"is_god_class"});
		//System.out.println(yolllo.size());
		cf.testQuality(new String[]{"is_god_class","is_long_method"});
		//FileInputStream file = new FileInputStream("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx");
		//XSSFSheet sheet = new XSSFWorkbook(file).getSheet("Code Smells");
		//System.out.println(get_Target_ColIndex(sheet,"is_God_Class"));
		
	

	}

}
