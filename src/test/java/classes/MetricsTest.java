package classes;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.Metrics;

class MetricsTest {
	
	File GrammerExceptionTestFile = new File(getClass().getResource("/GrammerException.java").getFile());
	File ParsingExceptionTestFile = new File(getClass().getResource("/ParsingException.java").getFile());
	File SourceCodeParserTestFile = new File(getClass().getResource("/SourceCodeParser.java").getFile());
	File emptyTestFile = new File(getClass().getResource("/empty.java").getFile());
	
	File GrammerExceptionExtraTestFile = new File(getClass().getResource("/GrammerExceptionExtra.java").getFile());
	File ClassOnlyOpeningTest = new File(getClass().getResource("/ClassOnlyOpening.java").getFile());
	File DoubleClassDeclarationTest = new File(getClass().getResource("/DoubleClassDeclarationTest.java").getFile());


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
	final void testGetLOC_class() throws IOException {
		Exception exception = assertThrows(NullPointerException.class, ()->{Metrics.getLOC_class(null);});
		String expectedMessage = "Ficheiro nao pode ser nulo.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		assertEquals(0, Metrics.getLOC_class(emptyTestFile));		
		assertEquals(18, Metrics.getLOC_class(GrammerExceptionTestFile)); // LOC_class[GrammerException]=18
		assertEquals(50, Metrics.getLOC_class(ParsingExceptionTestFile)); // LOC_class[ParsingException]=50
		assertEquals(1371, Metrics.getLOC_class(SourceCodeParserTestFile)); // LOC_class[SourceCodeParser]=1371	
		
		assertEquals(18, Metrics.getLOC_class(GrammerExceptionExtraTestFile)); // LOC_class[GrammerException]=18
		assertEquals(0, Metrics.getLOC_class(ClassOnlyOpeningTest));
		assertEquals(3, Metrics.getLOC_class(DoubleClassDeclarationTest)); 
	}

	@Test
	final void testMethods() {
		assertThrows(NullPointerException.class, ()->{Metrics.methods(null);});
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{Metrics.methods(new File("/naoexiste"));});
		String expectedMessage = "Ficheiro especificado nao existe.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		
		assertEquals(0, Metrics.methods(emptyTestFile).size());
		assertEquals(4, Metrics.methods(GrammerExceptionTestFile).size()); // NOM_class[GrammerException]=4
		assertEquals(6, Metrics.methods(ParsingExceptionTestFile).size()); // NOM_class[ParsingException]=6
		assertEquals(32, Metrics.methods(SourceCodeParserTestFile).size()); // NOM_class[SourceCodeParser]=29 + 3 inner classes = 32
	}

	@Test
	final void testGetNOM_class() {	
		assertEquals(0, Metrics.getNOM_class(emptyTestFile));
		assertEquals(4, Metrics.getNOM_class(GrammerExceptionTestFile)); // NOM_class[GrammerException]=4
		assertEquals(6, Metrics.getNOM_class(ParsingExceptionTestFile)); // NOM_class[ParsingException]=6
		assertEquals(32, Metrics.getNOM_class(SourceCodeParserTestFile)); // NOM_class[SourceCodeParser]=29 + 3 inner classes = 32
	}

	@Test
	final void testGetLinesOfMethods() throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("public GrammerException(int offset, String msg) ", "	public GrammerException(int offset, String msg) {\n" + 
				"		super(offset, msg);\n" + 
				"	}");
		map.put("public GrammerException(int offset, int line, int column, String msg) ", "	public GrammerException(int offset, int line, int column, String msg) {\n" + 
				"		super(offset, line, column, msg);\n" + 
				"	}");
		map.put("public GrammerException(String msg, Exception e) ", "	public GrammerException(String msg, Exception e) {\n" + 
				"		super(msg, e);\n" + 
				"	}");
		map.put("public GrammerException(int line, int column, String msg) ", "	public GrammerException(int line, int column, String msg) {\n" + 
				"		super(line, column, msg);\n" + 
				"	}");
		
		assertEquals(map, Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.methods(GrammerExceptionTestFile)));
		
		map.clear();
		assertEquals(map, Metrics.getLinesOfMethods(emptyTestFile, Metrics.methods(emptyTestFile)));
		
		assertEquals(6, (Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.methods(ParsingExceptionTestFile))).size()); //[ParsingException]=6
		assertEquals(32, (Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.methods(SourceCodeParserTestFile))).size());	// [SourceCodeParser]=29 + 3 inner classes = 32
	}

	@Test
	final void testGetCYCLO_method() throws FileNotFoundException {

		LinkedHashMap<String, String> emptylinesOfMethods = new LinkedHashMap<>();
		emptylinesOfMethods.put("", "");
		assertThrows(IllegalArgumentException.class, ()->{Metrics.getCYCLO_method(Metrics.getCycloOfAllMethods(emptylinesOfMethods), 0);});
		
		LinkedHashMap<String, String> nulllinesOfMethods = new LinkedHashMap<>();
		nulllinesOfMethods.put(null, null);

		assertThrows(IllegalArgumentException.class, ()->{Metrics.getCYCLO_method(Metrics.getCycloOfAllMethods(nulllinesOfMethods), 0);});

		LinkedHashMap<String, String> linesOfMethodsEmptyFile = Metrics.getLinesOfMethods(emptyTestFile, Metrics.methods(emptyTestFile));
		assertThrows(IllegalArgumentException.class, ()->{Metrics.getCYCLO_method(Metrics.getCycloOfAllMethods(linesOfMethodsEmptyFile), 0);});


		LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.methods(SourceCodeParserTestFile));
		ArrayList<Integer> cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
		assertEquals(1, Metrics.getCYCLO_method(cycloOfAllMethods , 0)); // CYCLO_method[SourceCodeParser(File)] = 1
		assertEquals(19, Metrics.getCYCLO_method(cycloOfAllMethods , 7)); // CYCLO_method[parseField()] = 19
	}

	@Test
	final void testGetWMC_class() throws FileNotFoundException {
		LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(emptyTestFile, Metrics.methods(emptyTestFile));
		ArrayList<Integer> cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
		assertEquals(0, Metrics.getWMC_class(cycloOfAllMethods)); //empty
		
		linesOfMethods.clear();
		cycloOfAllMethods.clear();
		linesOfMethods = Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.methods(GrammerExceptionTestFile));
		cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
		assertEquals(4, Metrics.getWMC_class(cycloOfAllMethods)); // WMC_class[GrammerException]=4
		
		linesOfMethods.clear();
		cycloOfAllMethods.clear();
		linesOfMethods = Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.methods(ParsingExceptionTestFile));	
		cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
		assertEquals(13, Metrics.getWMC_class(cycloOfAllMethods)); // WMC_class[ParsingException]=13
		
		linesOfMethods.clear();
		cycloOfAllMethods.clear();
		linesOfMethods = Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.methods(SourceCodeParserTestFile));
		cycloOfAllMethods = Metrics.getCycloOfAllMethods(linesOfMethods);
		assertEquals(331, Metrics.getWMC_class(cycloOfAllMethods)); // WMC_class[SourceCodeParser]=328+ 3 inner classes=331
	}

	@Test
	final void testGetLOC_method() throws FileNotFoundException {	
		assertThrows(IllegalArgumentException.class, ()->{Metrics.getLOC_method(Metrics.getLinesOfMethods(emptyTestFile, Metrics.methods(emptyTestFile)), 0);});

		LinkedHashMap<String, String> linesOfMethods = Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.methods(GrammerExceptionTestFile));
		assertEquals(3, Metrics.getLOC_method(linesOfMethods, 0)); // LOC_method[GrammerException(int,String)]=3
		
		linesOfMethods.clear();
		linesOfMethods = Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.methods(ParsingExceptionTestFile));
		assertEquals(21, Metrics.getLOC_method(linesOfMethods, 5)); // LOC_method[getMessage()]=21
		
		linesOfMethods.clear();
		linesOfMethods = Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.methods(SourceCodeParserTestFile));
		assertEquals(40, Metrics.getLOC_method(linesOfMethods, 5)); // LOC_method[parseClassSignature()]=40
	}

}

