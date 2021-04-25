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
		String expectedMessage = "Ficheiro nÃ£o pode ser nulo.";
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
		String expectedMessage = "Ficheiro especificado nÃ£o existe.";
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
		
		map.clear();
		assertEquals(map, Metrics.getLinesOfMethods(emptyTestFile, Metrics.countMethods(emptyTestFile)));
		
		assertEquals(6, (Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.countMethods(ParsingExceptionTestFile))).size());
		assertEquals(32, (Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.countMethods(SourceCodeParserTestFile))).size());	
		
//		map.clear();
//		map.put("public ParsingException(int offset, int line, int column, String msg) ", "	public ParsingException(int offset, int line, int column, String msg) {\n" + 
//				"		super(msg);\n" + System.lineSeparator() + 
//				"		this.offset = offset;\n" + 
//				"		this.line = line;n" + 
//				"		this.column = column;\n" + 
//				"	}");
//		
//		assertEquals(map.get(0).contains(System.lineSeparator()), (Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.countMethods(ParsingExceptionTestFile))).get(0).contains(System.lineSeparator()));
	}

	@Test
	final void testNOfClyclo() {
		String teste = "";
		
		Exception exceptionStringNull = assertThrows(IllegalArgumentException.class, ()->{Metrics.nOfCyclo(null);});
		Exception exceptionStringEmpty = assertThrows(IllegalArgumentException.class, ()->{Metrics.nOfCyclo("");});
		String expectedMessage = "Empty or null String";
		String actualMessageStringNull = exceptionStringNull.getMessage();
		assertTrue(actualMessageStringNull.contains(expectedMessage));
		String actualMessageStringEmpty = exceptionStringEmpty.getMessage();
		assertTrue(actualMessageStringEmpty.contains(expectedMessage));
		assertEquals(actualMessageStringEmpty,actualMessageStringNull);

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
	final void testWmc() throws FileNotFoundException {
		ArrayList<Integer> numbersToSum = new ArrayList<>();
		assertEquals(0, Metrics.wmc(numbersToSum));
		
		numbersToSum.add(5);
		numbersToSum.add(10);
		assertEquals(15, Metrics.wmc(numbersToSum));
		
		LinkedHashMap<String, String> map = new LinkedHashMap<>();	
		map.putAll(Metrics.getLinesOfMethods(emptyTestFile, Metrics.countMethods(emptyTestFile)));		
		ArrayList<Integer> none = Metrics.allCyclos(map);
		assertEquals(0, Metrics.wmc(none));
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));		
		ArrayList<Integer> GrammerExceptionCyclos = Metrics.allCyclos(map);
		assertEquals(4, Metrics.wmc(GrammerExceptionCyclos)); // WMC_class[GrammerException]=4
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.countMethods(ParsingExceptionTestFile)));		
		ArrayList<Integer> ParsingExceptionCyclos = Metrics.allCyclos(map);
		assertEquals(13, Metrics.wmc(ParsingExceptionCyclos)); // WMC_class[ParsingException]=13
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.countMethods(SourceCodeParserTestFile)));		
		ArrayList<Integer> SourceCodeParserCyclos = Metrics.allCyclos(map);
		assertEquals(331, Metrics.wmc(SourceCodeParserCyclos)); // WMC_class[SourceCodeParser]=328 + 3 inner classes = 331		
	}

	@Test
	final void testCountLinesOfMethods() throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();	
		map.putAll(Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));
		assertEquals(3, Metrics.countLinesOfMethods(map).get(0)); // LOC_method[GrammerException(int,String)]=3
		assertEquals(3, Metrics.countLinesOfMethods(map).get(1)); // LOC_method[GrammerException(int,int,int,String)]=3
		assertEquals(3, Metrics.countLinesOfMethods(map).get(2)); // LOC_method[GrammerException(String,Exception)]=3
		assertEquals(3, Metrics.countLinesOfMethods(map).get(3)); // LOC_method[GrammerException(int,int,String)]=3
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.countMethods(ParsingExceptionTestFile)));
		assertEquals(6, Metrics.countLinesOfMethods(map).get(0)); // LOC_method[ParsingException(int,int,int,String)]=6
		assertEquals(21, Metrics.countLinesOfMethods(map).get(5)); // LOC_method[getMessage()]=21
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.countMethods(SourceCodeParserTestFile)));
		assertEquals(40, Metrics.countLinesOfMethods(map).get(5)); // LOC_method[parseClassSignature()]=40
		assertEquals(76, Metrics.countLinesOfMethods(map).get(7)); // LOC_method[parseField()]=76
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(emptyTestFile, Metrics.countMethods(emptyTestFile)));
		ArrayList<Integer> empty = new ArrayList<Integer>();
		assertEquals(empty, Metrics.countLinesOfMethods(map));
	}

	@Test
	final void testAllCyclos() throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();	
		map.putAll(Metrics.getLinesOfMethods(GrammerExceptionTestFile, Metrics.countMethods(GrammerExceptionTestFile)));
		assertEquals(1, Metrics.allCyclos(map).get(0)); // CYCLO_method[GrammerException(int,String)]=1
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(ParsingExceptionTestFile, Metrics.countMethods(ParsingExceptionTestFile)));
		assertEquals(1, Metrics.allCyclos(map).get(0)); // CYCLO_method[ParsingException(int,int,int,String)]=1
		assertEquals(8, Metrics.allCyclos(map).get(5)); // CYCLO_method[getMessage()]=8
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(SourceCodeParserTestFile, Metrics.countMethods(SourceCodeParserTestFile)));
		assertEquals(10, Metrics.allCyclos(map).get(5)); // CYCLO_method[parseClassSignature()]=10
		assertEquals(19, Metrics.allCyclos(map).get(7)); // CYCLO_method[parseField()]=19
		
		map.clear();
		map.putAll(Metrics.getLinesOfMethods(emptyTestFile, Metrics.countMethods(emptyTestFile)));
		ArrayList<Integer> empty = new ArrayList<Integer>();
		assertEquals(empty, Metrics.allCyclos(map));
	}

}
