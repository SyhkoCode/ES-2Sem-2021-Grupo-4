package metrics;

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

	public static void main(String[] args) {
		CompareFiles cf = new CompareFiles("F:\\Google Drive\\ISCTE\\ANO 3\\ES\\Code_Smells.xlsx", "C:\\Users\\sophi\\Desktop\\jasml_0.10_metrics.xlsx");
	}

}
