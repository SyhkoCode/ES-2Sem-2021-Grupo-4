package classes;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.CompareFiles;

class CompareFilesTest {
		
	File excelDefault = new File(getClass().getResource("/jasml_0.10_teorico.xlsx").getFile()); // Teórico, tb podia ser o Code_Smells.xlsx da UC
	String csFileDefault = excelDefault.getAbsolutePath();
	
	File excelCreatedCodeSmells = new File(getClass().getResource("/jasml_CodeSmells.xlsx").getFile()); // Code Smells gerado pela app
	String csFileCreated = excelCreatedCodeSmells.getAbsolutePath();
	
	File metricsFile = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String metricsFilePath = metricsFile.getAbsolutePath();
	
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	String testeregrasPath = testeregras.getAbsolutePath();
	
	File excel_MalFormatado = new File(getClass().getResource("/Code_Smells_MalFormatado.xlsx").getFile());
	String csFile_MalFormatado = excel_MalFormatado.getAbsolutePath();
	
	CompareFiles cf2Files = new CompareFiles(csFileDefault, csFileCreated);
	CompareFiles cf3Files = new CompareFiles(csFileDefault, metricsFilePath, testeregrasPath);
	CompareFiles cf2MalFormatado = new CompareFiles(csFileDefault, csFile_MalFormatado);

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
	final void testTestQuality() throws Exception {
		assertNotNull(cf2Files);
		assertNotNull(cf2Files.testQuality());
		
		assertNotNull(cf3Files);
		assertNotNull(cf3Files.testQuality());
		
		Exception exception = assertThrows(IllegalStateException.class, ()->{cf2MalFormatado.testQuality();});
		String expectedMessage = "Ficheiro mal formatado";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

}
