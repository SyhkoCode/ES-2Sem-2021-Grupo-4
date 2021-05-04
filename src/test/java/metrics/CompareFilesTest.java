package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompareFilesTest {
		
	File excelDefault = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String csFileDefault = excelDefault.getAbsolutePath();
	
	File excelCreatedCSBooleans = new File(getClass().getResource("/jasml_csbooleans.xlsx").getFile());
	String csFileCreatedBooleans = excelCreatedCSBooleans.getAbsolutePath();
	
	File excelCreated = new File(getClass().getResource("/jasml_0.10_metrics_created.xlsx").getFile());
	String csFileCreated_metrics = excelCreated.getAbsolutePath();
	
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	String testeregrasPath = testeregras.getAbsolutePath();
	
	File excel_MalFormatado = new File(getClass().getResource("/Code_Smells_MalFormatado.xlsx").getFile());
	String csFile_MalFormatado = excel_MalFormatado.getAbsolutePath();
	
	CompareFiles cfSS = new CompareFiles(csFileDefault, csFileCreatedBooleans);
	CompareFiles cfSSS = new CompareFiles(csFileDefault, csFileCreated_metrics, testeregrasPath);
	CompareFiles cfSSMalFormatado = new CompareFiles(csFileDefault, csFile_MalFormatado);

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
	final void testCompareFilesStringString() {
		assertNotNull(cfSS);
	}

	@Test
	final void testCompareFilesStringStringString() {
		assertNotNull(cfSSS);
	}

	@Test
	final void testTestQuality() throws Exception {
		assertNotNull(cfSS.testQuality());
		assertNotNull(cfSSS.testQuality());
		
		Exception exception = assertThrows(IllegalStateException.class, ()->{cfSSMalFormatado.testQuality();});
		String expectedMessage = "Ficheiro mal formatado";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

}
