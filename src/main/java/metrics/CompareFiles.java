package metrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;

@SuppressWarnings("unused")
public class CompareFiles {

	private class Helper {

		private String fileName;
		private String columnTitle;

		public Helper(String fileName, String columnTitle){
			this.fileName = fileName;
			this.columnTitle = columnTitle;
		}

		public String getFileName() {
			return fileName;
		}

		public String getColumnTitle() {
			return columnTitle;
		}

	}
	
	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;
	private ExcelDealer excelDealerDefault;
	private ExcelDealer excelDealerCreated;
	HashMap<String,Indicator> saveIndsPerMethod = new HashMap<>();
	HashMap<String,Indicator> saveIndsPerClass = new HashMap<>();
	
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
	

	public LinkedHashMap<Helper,Integer> getColIndexesByTitle(String[] titles) {
		LinkedHashMap<Helper,Integer> map = new LinkedHashMap<>();

		for(String title: titles){
			for(int i=0; i<excelDealerDefault.getExcelHeader(0).length; i++){
				if(String.valueOf(excelDealerDefault.getExcelHeader(0)[i]).equalsIgnoreCase(title)){
					map.put(new Helper(excelDealerDefault.getExcel_file(),String.valueOf(excelDealerDefault.getExcelHeader(0)[i])), i);
				}
			}
			for(int i=0; i<excelDealerCreated.getExcelHeader(0).length; i++){
				if(String.valueOf(excelDealerCreated.getExcelHeader(0)[i]).equalsIgnoreCase(title)){
					map.put(new Helper(excelDealerCreated.getExcel_file(),String.valueOf(excelDealerCreated.getExcelHeader(0)[i])), i);
				}
			}
		}
		return map;
	}
		
	
	public void testQuality(String[] strs) {
		HashMap<Helper,Integer> helpersList = getColIndexesByTitle(strs);

		for(Object[] objDefaultExcel: excelDealerDefault.getAllRows(0)){
			for(Object[] objCreatedExcel: excelDealerCreated.getAllRows(0)){

				String packageDefaultExcel = String.valueOf(objDefaultExcel[1]);
				String pacote = String.valueOf(objCreatedExcel[1]);
				String classDefaultExcel = String.valueOf(objDefaultExcel[2]);
				String classe = String.valueOf(objCreatedExcel[2]);
				String metodoDefaultExcel = String.valueOf(objDefaultExcel[3]);
				String metodo = String.valueOf(objCreatedExcel[3]);

				if(packageDefaultExcel.equals(pacote) && classDefaultExcel.equals(classe) && metodoDefaultExcel.equals(metodo)){
					int arrayIndex = 0;
					for(int i = 0; i < strs.length; i++){
						String cellTextDefault = String.valueOf(objDefaultExcel[(int)helpersList.values().toArray()[arrayIndex]]);
						String cellTextCreated = String.valueOf(objCreatedExcel[(int)helpersList.values().toArray()[arrayIndex+1]]);

						if(cellTextDefault.equals("TRUE") && cellTextCreated.equals("TRUE")){	
							System.out.println(Indicator.VP.getName()+" no metodo: " + metodo+" da classe: " + classe);
							saveIndsPerMethod.put(metodo, Indicator.VP);
							saveIndsPerClass.put(classe, Indicator.VP);
						}

						else if(cellTextDefault.equals("FALSE") && cellTextCreated.equals("TRUE")){
							System.out.println(Indicator.FP.getName()+" no metodo: " + metodo+" da classe: " + classe);
							saveIndsPerMethod.put(metodo, Indicator.FP);
							saveIndsPerClass.put(classe, Indicator.FP);
						}

						else if(cellTextDefault.equals("FALSE") && cellTextCreated.equals("FALSE")){						
							System.out.println(Indicator.VN.getName()+" no metodo: " + metodo +" da classe: "+ classe);
							saveIndsPerMethod.put(metodo, Indicator.VN);
							saveIndsPerClass.put(classe, Indicator.VN);
						}

						else if(cellTextDefault.equals("TRUE") && cellTextCreated.equals("FALSE")){					
							System.out.println(Indicator.FN.getName()+" no metodo: " + metodo +" da classe: "+ classe);
							saveIndsPerMethod.put(metodo, Indicator.FN);
							saveIndsPerClass.put(classe, Indicator.FN);
						}	
						arrayIndex+=2;
					}
				}
			}
		}
		System.out.println(saveIndsPerMethod.size());
		System.out.println(saveIndsPerMethod.entrySet());
		System.out.println(saveIndsPerClass.size());
		System.out.println(saveIndsPerClass.entrySet());
	}
			

	public static void main(String[] args) throws IOException {
		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
		cf.testQuality(new String[]{"is_god_class","is_long_method"});
		
		//CompareFiles cf = new CompareFiles("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx", "C:\\Users\\Pedro Pinheiro\\Desktop\\jasml_0.10_metrics.xlsx");
		//ArrayList<Helper> yolllo = cf.get_Targets_ColIndexes(new String[]{"is_god_class"});
		//System.out.println(yolllo.size());
		//FileInputStream file = new FileInputStream("C:\\Users\\Pedro Pinheiro\\Downloads\\Code_Smells.xlsx");
		//XSSFSheet sheet = new XSSFWorkbook(file).getSheet("Code Smells");
		//System.out.println(get_Target_ColIndex(sheet,"is_God_Class"));
	}

}
