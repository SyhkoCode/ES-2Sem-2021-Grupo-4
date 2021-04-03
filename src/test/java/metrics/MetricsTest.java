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
	
	File myTestFile = new File(getClass().getResource("/GrammerException.java").getFile());

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
		assertEquals(18, Metrics.getLines(myTestFile));
	}

	@Test
	final void testCountMethods() {
		assertEquals(4, Metrics.countMethods(myTestFile).size());
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
		
		assertEquals(map, Metrics.getLinesOfMethods(myTestFile, Metrics.countMethods(myTestFile)));
		
	}

	@Test
	final void testNOfClyclo() {
		String teste = "";
		//Não sei como testar q dá excepção
		
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
	final void testCountLinesOfMethods() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAllCyclos() {
		fail("Not yet implemented"); // TODO
	}

}
