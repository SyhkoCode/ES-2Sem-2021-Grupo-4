package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

class ExcelDealerTest {

	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	File excel = new File(getClass().getResource("/Code_Smells.xlsx").getFile());
	String excelPath = excel.getAbsolutePath();
	File excelMetrics = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String excelMetricsPath = excelMetrics.getAbsolutePath();
	static TemporaryFolder tempFolder;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tempFolder = new TemporaryFolder();
		tempFolder.create();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testCreateExcelFile() throws Exception {
		File input = tempFolder.newFile("jasml_0.10_metrics");
		String inputPath = input.getAbsolutePath();
		File output = tempFolder.newFile("jasml_0.10_metrics.xlsx");
		output.delete();
		
		List<String[]> rows = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
		ExcelDealer.createExcelFile(inputPath, rows, "jasml_0.10_metrics.xlsx");
		assertTrue(output.exists());	
	}
	
	@Test
	final void testGetAllCellsOfColumn() throws Exception {
		assertNotNull(ExcelDealer.getAllCellsOfColumn(excelMetricsPath,0,0,false));
		assertEquals(246,ExcelDealer.getAllCellsOfColumn(excelMetricsPath,0,0,false).size()); // Col[0]=MethodID=246 rows
	}	


	@Test
	final void testGetAllRows() throws Exception {
		assertNotNull(ExcelDealer.getAllRows(excelMetricsPath,0));
		assertEquals(11,ExcelDealer.getRow(excelMetricsPath, 0, 0).length); // Total Number of Columns in this excel
	}

	@Test
	final void testGetRow() throws Exception {
		assertNotNull(ExcelDealer.getRow(excelMetricsPath, 0, 0));
		Object[] row = new Object[]{"MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class", "is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method"};
		assertEquals(row[0].toString(),ExcelDealer.getRow(excelMetricsPath, 0, 0)[0].toString()); // MethodID
		assertEquals(row.length,ExcelDealer.getRow(excelMetricsPath, 0, 0).length);
	}

	@Test
	final void testsumAllColumn() throws Exception {
		assertNotNull(ExcelDealer.sumAllColumn(excelMetricsPath,0,5));
		assertEquals(129240,ExcelDealer.sumAllColumn(excelMetricsPath,0,5)); // Total Sum of col LOC_class in this excel
	}

}
