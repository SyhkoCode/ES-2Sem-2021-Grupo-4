package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

class ExcelDealerTest {
	
	File excel = new File(getClass().getResource("/Code_Smells.xlsx").getFile());
	String excelPath = excel.getAbsolutePath();
	boolean read = true;
	int[] arrayint = new int[]{7,10};
	ExcelDealer ed = new ExcelDealer(excelPath, read, arrayint);

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
	final void testCreateExcelFile() throws IOException {
		
		TemporaryFolder tempFolder = new TemporaryFolder();
        tempFolder.create();
        File output = tempFolder.newFile(excelPath);
        String outputPath = output.getPath();
        
        List<String[]> rows = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
        
        ed.createExcelFile(excelPath, outputPath, rows);
        assertTrue(output.exists());
	}

	@Test
	final void testCreateCodeSmellsExcelFile() {
//		ArrayList<String[]> rows = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
//		assertNotNull(ed.createCodeSmellsExcelFile(excelPath, rows));
	}

	@Test
	final void testGetClassMethods() {
		List<String> list = new ArrayList<>();	
		int col_index = 6;
		String classname = "LOC_method";
		
		assertNotNull(ed.getClassMethods(col_index, classname));
	}

	@Test
	final void testGetClasses() {
		assertNotNull(ed.getClasses());
	}

	@Test
	final void testGetAllRows() {
		assertNotNull(ed.getExcelHeader(2));
	}

	@Test
	final void testGetExcelHeader() {
		assertNotNull(ed.getExcelHeader(0));
	}

	@Test
	final void testGetAllCellsOfColumn() {
		assertNotNull(ed.getAllCellsOfColumn(0));
	}

	@Test
	final void testSumLinesOfCode() {
		assertNotNull(ed.sumLinesOfCode());
	}

	@Test
	final void testGetExcel_file() {
		assertEquals(excelPath,ed.getExcel_file());
	}

	@Test
	final void testAddAllToIgnoredIndexes() {
		List<Integer> ignoredIndexes = new ArrayList<>();
		ignoredIndexes.add(7);
		ignoredIndexes.add(10);
		ed.addAllToIgnoredIndexes(ignoredIndexes);
	}

	@Test
	final void testAddToIgnoredIndexes() {
		List<Integer> ignoredIndexes = new ArrayList<>();
		ignoredIndexes.add(7);
		ed.addToIgnoredIndexes(0);

	}

}
