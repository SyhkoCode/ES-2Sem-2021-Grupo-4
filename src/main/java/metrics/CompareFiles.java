package metrics;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CompareFiles {

	private String csFileDefault;
	private String csFileCreated;
	private String metricsFile;
	private String rulesFile;

	public CompareFiles(String csFileDefault, String csFileCreated) {
		this.csFileDefault = csFileDefault;
		this.csFileCreated = csFileCreated;
	}


	public CompareFiles(String csFileDefault, String metricsFile, String rulesFile) {
		csFileDefault = csFileDefault;
		metricsFile = metricsFile;
		rulesFile = rulesFile;
	}



	private int get_isGodClass_ColIndex(XSSFSheet sheet) {
		int isGodClassIndex = 0;
		for (Cell s : sheet.getRow(0)) {
			if (s.getStringCellValue().toLowerCase().equals("is_god_class")) {
				isGodClassIndex = s.getColumnIndex();
			}
		}
		return isGodClassIndex;
	}
	
	private int get_isLongMethod_ColIndex(XSSFSheet sheet) {
		int isLongMethodIndex = 0;
		for (Cell s : sheet.getRow(0)) {
			if (s.getStringCellValue().toLowerCase().equals("is_long_method")) {
				isLongMethodIndex = s.getColumnIndex();
			}
		}
		return isLongMethodIndex;
	}
	
	
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

	public static void main(String[] args) {
		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
	}

}
