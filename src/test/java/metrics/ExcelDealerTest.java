package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
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
	
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	File excel = new File(getClass().getResource("/Code_Smells.xlsx").getFile());
	String excelPath = excel.getAbsolutePath();
	boolean read = true;
	int[] arrayint = new int[]{7,10};
	ExcelDealer ed = new ExcelDealer(excelPath, read, arrayint);
	ExcelDealer edFalse = new ExcelDealer(excelPath, false, arrayint);
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
	final void testCreateExcelFile() throws IOException {
        File output = tempFolder.newFile("jasml_0.10.xlsx");
        String outputPath = output.getAbsolutePath();
        
        List<String[]> rows = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
        ed.createExcelFile(excel.getName(), outputPath, rows);
        assertTrue(output.exists());
        
        edFalse.createExcelFile(excel.getName(), outputPath, rows);
        assertTrue(output.exists());
	}

	@Test
	final void testCreateCodeSmellsExcelFile() throws IOException {
        File output = tempFolder.newFile("output.xlsx");
        String outputPath = output.getAbsolutePath();
        
        ArrayList<String []> a = new ArrayList<>();
        String b[] = {"not here","not here2"};
        String c[] = {"not here3","i'm here"};
        a.add(b);
        a.add(c);

		ed.createCodeSmellsExcelFile(outputPath, a);
		assertTrue(output.exists());

	}

	@Test
	final void testGetClassMethods() {
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
		assertNotNull(ed.getAllRows(2));
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
