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





}
