package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompareFilesTest {
		
	File excelDefault = new File(getClass().getResource("/Code_Smells.xlsx").getFile());
	String csFileDefault = excelDefault.getAbsolutePath();
	
	File excelCreated = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String csFileCreated = excelCreated.getAbsolutePath();
	
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	String testeregrasPath = testeregras.getAbsolutePath();
	
	File excelDefault_MalFormatado = new File(getClass().getResource("/Code_Smells_MalFormatado.xlsx").getFile());
	String csFileDefault_MalFormatado = excelDefault_MalFormatado.getAbsolutePath();
	
	CompareFiles cfSS = new CompareFiles(csFileDefault, csFileCreated);
	CompareFiles cfSSS = new CompareFiles(csFileDefault, csFileCreated, testeregrasPath);
	CompareFiles cfSSMau = new CompareFiles(csFileDefault, csFileDefault_MalFormatado);

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
	final void testTestQuality() {
		assertNotNull(cfSS.testQuality(new String[]{"is_god_class","is_long_method"}));
		assertNotNull(cfSSS.testQuality(new String[]{"is_god_class","is_long_method"}));
		
		Exception exception = assertThrows(IllegalStateException.class, ()->{cfSSMau.testQuality(new String[]{"is_god_class","is_long_method"});});
		String expectedMessage = "Ficheiro mal formatado";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

}
