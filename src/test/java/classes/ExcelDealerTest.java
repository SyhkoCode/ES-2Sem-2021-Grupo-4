package classes;

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

import classes.ExcelDealer;
import classes.ReadJavaProject;

class ExcelDealerTest {

	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
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
		assertEquals(254,ExcelDealer.getAllCellsOfColumn(excelMetricsPath,0,0,false).size()); // Col[0]=MethodID=254 rows including title
		assertEquals(0,ExcelDealer.getAllCellsOfColumn(excelMetricsPath,0,15,false).size()); // Col[15]=nada
	}	


	@Test
	final void testGetAllRows() throws Exception {
		assertNotNull(ExcelDealer.getAllRows(excelMetricsPath,0));
		assertEquals(253,ExcelDealer.getAllRows(excelMetricsPath, 0).size()); // Number of Rows in this excel excluding title
	}

	@Test
	final void testGetRow() throws Exception {
		assertNotNull(ExcelDealer.getRow(excelMetricsPath, 0, 0));
		Object[] row = new Object[]{"MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class", "LOC_method", "CYCLO_method"};
		assertEquals(row[0].toString(),ExcelDealer.getRow(excelMetricsPath, 0, 0)[0].toString()); // MethodID
		assertEquals(row.length,ExcelDealer.getRow(excelMetricsPath, 0, 0).length); //9 colunas na row 0
	}

	@Test
	final void testsumAllColumn() throws Exception {
		assertNotNull(ExcelDealer.sumAllColumn(excelMetricsPath,0,5));
		assertEquals(130872,ExcelDealer.sumAllColumn(excelMetricsPath,0,5)); // Total Sum of col LOC_class in this excel
		assertEquals(0,ExcelDealer.sumAllColumn(excelMetricsPath,0,15)); // Col[15]=nada
	}

}
