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
	
	File excel = new File("/src/test/resources/Code_Smells.xlsx");
	String excelName = excel.getName();
	boolean read = true;
	int[] arrayint = new int[]{7,10};
	ExcelDealer ed = new ExcelDealer(excelName, read, arrayint);

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
        File output = tempFolder.newFile(tempFolder.getRoot() + "\\" + excelName + ".xlsx");
        String outputPath = output.getPath();
        
        List<String[]> rows = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
        
        ed.createExcelFile(excelName, outputPath, rows);
        assertTrue(output.exists());
	}

	@Test
	final void testCreateCodeSmellsExcelFile() {
		fail("Not yet implemented"); // TODO
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
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetAllRows() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetExcelHeader() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetAllCellsOfColumn() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testSumLinesOfCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetExcel_file() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAddAllToIgnoredIndexes() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAddToIgnoredIndexes() {
		fail("Not yet implemented"); // TODO
	}

}
