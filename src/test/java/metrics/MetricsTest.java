package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricsTest {
	
	File GrammerExceptionTestFile = new File(getClass().getResource("/GrammerException.java").getFile());
	File ParsingExceptionTestFile = new File(getClass().getResource("/ParsingException.java").getFile());
	File SourceCodeParserTestFile = new File(getClass().getResource("/SourceCodeParser.java").getFile());
	File emptyTestFile = new File(getClass().getResource("/empty.java").getFile());

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
	final void testGetLines() throws Exception {
		Exception exception = assertThrows(NullPointerException.class, ()->{Metrics.getLines(null);});
		String expectedMessage = "Ficheiro não pode ser nulo.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

		assertEquals(0, Metrics.getLines(emptyTestFile));		
		assertEquals(18, Metrics.getLines(GrammerExceptionTestFile)); // LOC_class[GrammerException]=18
		assertEquals(50, Metrics.getLines(ParsingExceptionTestFile)); // LOC_class[ParsingException]=50
		assertEquals(1371, Metrics.getLines(SourceCodeParserTestFile)); // LOC_class[SourceCodeParser]=1371	
	}

	@Test
	final void testCountMethods() {
		assertThrows(NullPointerException.class, ()->{Metrics.countMethods(null);});
		Exception exception = assertThrows(IllegalArgumentException.class, ()->{Metrics.countMethods(new File("/naoexiste"));});
		String expectedMessage = "Ficheiro especificado não existe.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		assertEquals(0, Metrics.countMethods(emptyTestFile).size());
		assertEquals(4, Metrics.countMethods(GrammerExceptionTestFile).size()); // NOM_class[GrammerException]=4
		assertEquals(6, Metrics.countMethods(ParsingExceptionTestFile).size()); // NOM_class[ParsingException]=6
		assertEquals(32, Metrics.countMethods(SourceCodeParserTestFile).size()); // NOM_class[SourceCodeParser]=29 + 3 inner classes = 32
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
		
		assertEquals(map, Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));
		
	}

	@Test
	final void testNOfClyclo() {
		String teste = "";
		
		assertThrows(IllegalArgumentException.class, ()->{Metrics.nOfCyclo("");});
		assertThrows(NullPointerException.class, ()->{Metrics.nOfCyclo(null);});

		teste = "public int getConstantPoolCount() {\r\n"
				+ "        return poolItems.length;\r\n"
				+ "    }";
		assertEquals(1, Metrics.nOfCyclo(teste));
		
		teste = "JavaClass ret = null;\r\n"
				+ "		try {\r\n"
				+ "			FileInputStream fsin = new FileInputStream(classFile);\r\n"
				+ "			in = new DataInputStream(fsin);\r\n"
				+ "\r\n"
				+ "			readMagic();\r\n"
				+ "			readVersion();\r\n"
				+ "			readConstant_Pool_Count();\r\n"
				+ "			readConstantPool();\r\n"
				+ "			// prt(constantPool); // \r\n"
				+ "			readAccess_flags();\r\n"
				+ "			readThis_class();\r\n"
				+ "			readSuper_class();\r\n"
				+ "			readInterfaces();\r\n"
				+ "			readFields();\r\n"
				+ "			readMethods();\r\n"
				+ "			readAttributes();\r\n"
				+ "\r\n"
				+ "			ret = new JavaClass();\r\n"
				+ "			ret.magic = magic;\r\n"
				+ "			ret.minor_version = minor_Version;\r\n"
				+ "			ret.major_version = major_Version;\r\n"
				+ "			ret.constant_pool_count = constant_Pool_Count;\r\n"
				+ "			ret.constantPool = constantPool;\r\n"
				+ "			ret.access_flags = access_flags;\r\n"
				+ "			ret.this_class = this_class;\r\n"
				+ "			ret.super_class = super_class;\r\n"
				+ "			ret.interfaces_count = interfaces_count;\r\n"
				+ "			ret.interfaces = interfaces;\r\n"
				+ "			ret.fields_count = fields_count;\r\n"
				+ "			ret.fields = fields;\r\n"
				+ "			ret.methods_count = methods_count;\r\n"
				+ "			ret.methods = methods;\r\n"
				+ "			ret.attributes_count = attributes_count;\r\n"
				+ "			ret.attributes = attributes;\r\n"
				+ "		} finally {\r\n"
				+ "			try {\r\n"
				+ "				in.close();\r\n"
				+ "			} catch (Exception e) {\r\n"
				+ "\r\n"
				+ "			}\r\n"
				+ "		}\r\n"
				+ "		return ret;\r\n"
				+ "	}";
		assertEquals(2, Metrics.nOfCyclo(teste));
		
		teste = "public String toString() {\r\n"
				+ "        StringBuffer buf = new StringBuffer();\r\n"
				+ "        ConstantPoolItem item;\r\n"
				+ "        for (int i = 0; i < poolItems.length; i++) {\r\n"
				+ "            item = poolItems[i];\r\n"
				+ "            if (item != null) {\r\n"
				+ "                buf.append(i + \" : \" + item.toString() + \"\\r\\n\");\r\n"
				+ "                if (item instanceof Constant_Double || item instanceof Constant_Long) {\r\n"
				+ "                    i++;\r\n"
				+ "                }\r\n"
				+ "            } else {\r\n"
				+ "                buf.append(i + \" : N/A\\r\\n\");\r\n"
				+ "            }\r\n"
				+ "        }\r\n"
				+ "        return buf.toString();\r\n"
				+ "    }";
		assertEquals(5, Metrics.nOfCyclo(teste));
	}

	@Test
	final void testWmc() {
		ArrayList<Integer> numbersToSum = new ArrayList<>();
		assertEquals(0, Metrics.wmc(numbersToSum));
		
		numbersToSum.add(5);
		numbersToSum.add(10);
		assertEquals(15, Metrics.wmc(numbersToSum));
	}

	@Test
	final void testCountLinesOfMethods() throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();	
		map.putAll(Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));
		assertEquals(3, Metrics.countLinesOfMethods(map).get(0)); // LOC_method[GrammerException(int,String)]=3
	}

	@Test
	final void testAllCyclos() throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();	
		map.putAll(Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));
		assertEquals(1, Metrics.allCyclos(map).get(0)); // CYCLO_method[GrammerException(int,String)]=1
	}

}
