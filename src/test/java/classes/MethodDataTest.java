package classes;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.MethodData;

class MethodDataTest {

	String[] metrics = {"NOM_class","LOC_class","WMC_class","LOC_method","CYCLO_method"};
	File excelCreated = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String csFileCreated_metrics = excelCreated.getAbsolutePath();
	
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
	final void testMethodDataObjectArray() {
		
		Object[] parameter = new Object[9];
		parameter[1] = "metrics";
		parameter[2] = "ExcelDealer";
		parameter[3] = "main";
		parameter[4] = 1;
		parameter[5] = 2;
		parameter[6] = 3;
		parameter[7] = 4;
		parameter[8] = 5;
		
		MethodData md = new MethodData(parameter);
		assertNotNull(md);
		HashMap<String, Integer> map = new LinkedHashMap<>();
		assertEquals(map,md.getMetrics());
		assertEquals(parameter[1],md.getPackageName());
		assertEquals(parameter[2],md.getClassName());
		assertEquals(parameter[3],md.getMethodName());
		
		
		MethodData test = new MethodData(parameter,metrics);		
		assertEquals("metrics", test.getPackageName());
		assertEquals("ExcelDealer", test.getClassName());
		assertEquals("main", test.getMethodName());
		assertEquals(1, test.getMetric("NOM_class"));
		assertEquals(2, test.getMetric("LOC_class"));
		assertEquals(3, test.getMetric("WMC_class"));
		assertEquals(4, test.getMetric("LOC_method"));
		assertEquals(5, test.getMetric("CYCLO_method"));
		
		
		parameter[1] = "boop";
		parameter[2] = "Dealer";
		parameter[3] = "matrix";
		parameter[4] = 10;
		parameter[5] = 5;
		parameter[6] = 40;
		parameter[7] = 3;
		parameter[8] = 1;
		
		test = new MethodData(parameter, metrics);
		
		assertEquals("boop", test.getPackageName());
		assertEquals("Dealer", test.getClassName());
		assertEquals("matrix", test.getMethodName());
		assertEquals(10, test.getMetric("NOM_class"));
		assertEquals(5, test.getMetric("LOC_class"));
		assertEquals(40, test.getMetric("WMC_class"));
		assertEquals(3, test.getMetric("LOC_method"));
		assertEquals(1, test.getMetric("CYCLO_method"));
		
	}

	@Test
	final void testMethodData() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		assertEquals("metrics", test.getPackageName());
		assertEquals("ExcelDealer", test.getClassName());
		assertEquals("main", test.getMethodName());	
		
		test = new MethodData("package", "class", "method");
		assertEquals("package", test.getPackageName());
		assertEquals("class", test.getClassName());
		assertEquals("method", test.getMethodName());	
	}


	@Test
	final void testExcelToMetricsMap() throws Exception {
		assertNotNull(MethodData.excelToMetricsMap(csFileCreated_metrics));

		Object[] parameter = new Object[9];
		parameter[1] = "package default";
		parameter[2] = "jasml";
		parameter[3] = "main(String[])";
		parameter[4] = 8; //NOM_class
		parameter[5] = 204; //LOC_class
		parameter[6] = 49; //WMC_class
		parameter[7] = 8; //LOC_method
		parameter[8] = 2; //CYCLO_method
		
		MethodData test = new MethodData(parameter,metrics);
		assertEquals(test.toString(),MethodData.excelToMetricsMap(csFileCreated_metrics).get(0).toString());
	}

	@Test
	final void testGetMetrics() {
		Object[] parameter = new Object[9];
		parameter[1] = "metrics";
		parameter[2] = "ExcelDealer";
		parameter[3] = "main";
		parameter[4] = 1;
		parameter[5] = 2;
		parameter[6] = 3;
		parameter[7] = 4;
		parameter[8] = 5;
		
		HashMap<String, Integer> map = new LinkedHashMap<>();
		map.put(metrics[0], 1); //NOM_class
		map.put(metrics[1], 2); //LOC_class
		map.put(metrics[2], 3); //WMC_class
		map.put(metrics[3], 4); //LOC_method
		map.put(metrics[4], 5); //CYCLO_method
		
		MethodData test = new MethodData(parameter,metrics);
		assertEquals(map, test.getMetrics());
	}

	@Test
	final void testGetMetric() {
		Object[] parameter = new Object[9];
		parameter[1] = "metrics";
		parameter[2] = "ExcelDealer";
		parameter[3] = "main";
		parameter[4] = 1;
		parameter[5] = 2;
		parameter[6] = 3;
		parameter[7] = 4;
		parameter[8] = 5;
			
		MethodData test = new MethodData(parameter,metrics);
		assertEquals(1, test.getMetric("NOM_class"));
		assertEquals(1, test.getMetric(metrics[0]));
		assertEquals(2, test.getMetric("LOC_class"));
		assertEquals(2, test.getMetric(metrics[1]));
		assertEquals(3, test.getMetric("WMC_class"));
		assertEquals(4, test.getMetric("LOC_method"));
		assertEquals(5, test.getMetric("CYCLO_method"));		
	}

	@Test
	final void testGetPackageName() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		assertEquals("metrics", test.getPackageName());
		
		test = new MethodData("package", "class", "method");
		assertEquals("package", test.getPackageName());
	}

	@Test
	final void testGetClassName() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		assertEquals("ExcelDealer", test.getClassName());
		
		test = new MethodData("package", "class", "method");
		assertEquals("class", test.getClassName());
	}

	@Test
	final void testGetMethodName() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		assertEquals("main", test.getMethodName());	
		
		test = new MethodData("package", "class", "method");
		assertEquals("method", test.getMethodName());
	}

	@Test
	final void testToString() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		HashMap<String, Integer> map = new HashMap<>();
		String teorical = "MethodData [map=" + map + ", packageName=metrics, className=ExcelDealer, methodName=main]";
		assertEquals(teorical, test.toString());
		
		test = new MethodData("package", "class", "method");
		teorical = "MethodData [map=" + map + ", packageName=package, className=class, methodName=method]";

		assertEquals(teorical, test.toString());	
	}

}
