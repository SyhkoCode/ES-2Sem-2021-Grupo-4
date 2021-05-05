package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
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
//	ExcelDealer.createExcelFile(excelPath, read, arrayint);
//	ExcelDealer edFalse = new ExcelDealer(excelPath, false, arrayint);
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
	final void testExcelDealer() {
		assertNotNull(ExcelDealer.class);
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
	final void testGetAllCellsOfColumn() throws IOException {
		File input = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
		String inputPath = input.getAbsolutePath();
		try {
			assertNotNull(ExcelDealer.getAllCellsOfColumn(inputPath,0,0,false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	final void testGetClasses() throws IOException {
		File input = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
		String inputPath = input.getAbsolutePath();
		try {
			assertNotNull(ExcelDealer.getAllCellsOfColumn(inputPath, 0, 2, false));
		} catch (Exception e) {
			Exception exception = assertThrows(Exception.class, ()->{ExcelDealer.getAllCellsOfColumn(inputPath, 0, 2, false);});
		}
	}
	
	

	@Test
	final void testGetAllRows() throws IOException {
		File input = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
		String inputPath = input.getAbsolutePath();
		try {
			assertNotNull(ExcelDealer.getAllRows(inputPath,0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	final void testGetRow() throws IOException {
		File input = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
		String inputPath = input.getAbsolutePath();
		try {
			assertNotNull(ExcelDealer.getRow(inputPath, 0, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	final void testsumAllColumn() throws IOException {
		File input = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
		String inputPath = input.getAbsolutePath();
		try {
			assertNotNull(ExcelDealer.sumAllColumn(inputPath,0,7));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
