package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodDataTest {

	String[] metrics = {"NOM_class","LOC_class","WMC_class","LOC_method","CYCLO_method"};
	File excelMetrics = new File(getClass().getResource("/Snake_metrics.xlsx").getFile());
	String csFileMetrics = excelMetrics.getAbsolutePath();
	
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
		/*
		 *  Não fiz teste para o array ser null
		 *  pk com isso ñ posso chamar o outro construtor
		 *  
		 */
		
		/*
		 *  Não fiz teste para o array ser menor que 9
		 *  pk com isso ñ posso chamar o outro construtor
		 *  
		 */
		
		/*
		 *  Não fiz teste para as strings serem null ou vazias
		 *  pk ñ está implementado no outro construtor
		 *  
		 */
		
		/*
		 *  Não fiz teste para os valores serem negativos ou as strings vazias/null
		 *  pk ñ está implementado no addMetrics
		 *  
		 */
		
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
		/*
		 *  Não fiz teste para as strings serem null ou vazias
		 *  pk ñ está implementado
		 *  
		 */
		
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
	final void testAddMetric() {
		MethodData test = new MethodData("metrics", "ExcelDealer", "main");
		HashMap<String, Integer> teorical = new HashMap<>();
		
		assertEquals(teorical, test.getMetrics());
		
//		test.addMetric("boop", 1);
//		teorical.put("boop", 1);
//		assertEquals(teorical, test.getMetrics());
		
		/*
		 *  Não fiz teste para os valores serem negativos ou as strings vazias/null
		 *  pk ñ está implementado no addMetrics
		 *  
		 */
		
		/*
		 *  Se o mapa já tiver uma metrica, altera valor ou
		 *  deve dar exception?
		 *  
		 */

	}

	@Test
	final void testExcelToMetricsMap() throws Exception {
		
		File excelCreated = new File(getClass().getResource("/jasml_0.10_metrics_created.xlsx").getFile());
		String csFileCreated_metrics = excelCreated.getAbsolutePath();
		
		assertNotNull(MethodData.excelToMetricsMap(csFileCreated_metrics));
//		
//		ArrayList<MethodData> test = MethodData.excelToMetricsMap(csFileMetrics);
//		
//		ArrayList<MethodData> compareTo = new ArrayList<>();
//		MethodData md1 = new MethodData("src", "Binomial", "static int getBinomial (int,double)");
//		md1.addMetric("NOM_class", 2);
//		md1.addMetric("LOC_class", 37);
//		md1.addMetric("WMC_class", 10);
//		md1.addMetric("LOC_method", 9);
//		md1.addMetric("CYCLO_method", 5);
//		compareTo.add(md1);
//		MethodData md2 = new MethodData("src", "Binomial", "static void main(String[])");
//		md2.addMetric("NOM_class", 2);
//		md2.addMetric("LOC_class", 37);
//		md2.addMetric("WMC_class", 10);
//		md2.addMetric("LOC_method", 21); 
//		md2.addMetric("CYCLO_method", 5);
//		compareTo.add(md2);
//		MethodData md3 = new MethodData("src", "Disform", "static Fruit disform ()");
//		md3.addMetric("NOM_class", 2);
//		md3.addMetric("LOC_class", 44);
//		md3.addMetric("WMC_class", 10);
//		md3.addMetric("LOC_method", 16); 
//		md3.addMetric("CYCLO_method", 6); 
//		compareTo.add(md3);
//		MethodData md4 = new MethodData("src", "Disform", "static void main (String[])");
//		md4.addMetric("NOM_class", 2);
//		md4.addMetric("LOC_class", 44);
//		md4.addMetric("WMC_class", 10);
//		md4.addMetric("LOC_method", 17);
//		md4.addMetric("CYCLO_method", 4);
//		compareTo.add(md4);
//		MethodData md5 = new MethodData("src", "DistributedNumberGenerator", "public DistributedNumberGenerator ()");
//		md5.addMetric("NOM_class", 3);
//		md5.addMetric("LOC_class", 69);
//		md5.addMetric("WMC_class", 5);
//		md5.addMetric("LOC_method", 4);
//		md5.addMetric("CYCLO_method", 1);
//		compareTo.add(md5);
//		MethodData md6 = new MethodData("src", "DistributedNumberGenerator", "public void addNumberDouble (double,double)");
//		md6.addMetric("NOM_class", 3);
//		md6.addMetric("LOC_class", 69);
//		md6.addMetric("WMC_class", 5);
//		md6.addMetric("LOC_method", 7);
//		md6.addMetric("CYCLO_method", 2);
//		compareTo.add(md6);
//		MethodData md7 = new MethodData("src", "DistributedNumberGenerator", "public void addNumberInteger (int,double)");
//		md7.addMetric("NOM_class", 3);
//		md7.addMetric("LOC_class", 69);
//		md7.addMetric("WMC_class", 5);
//		md7.addMetric("LOC_method", 7);
//		md7.addMetric("CYCLO_method", 2);
//		compareTo.add(md7);
//		MethodData md8 = new MethodData("src", "MyUniformDiscret", "static int myUniformDiscret (int)");
//		md8.addMetric("NOM_class", 2);
//		md8.addMetric("LOC_class", 32);
//		md8.addMetric("WMC_class", 5);
//		md8.addMetric("LOC_method", 4);
//		md8.addMetric("CYCLO_method", 1);
//		compareTo.add(md8);
//		MethodData md9 = new MethodData("src", "MyUniformDiscret", "static void main (String[])");
//		md9.addMetric("NOM_class", 2);
//		md9.addMetric("LOC_class", 32);
//		md9.addMetric("WMC_class", 5);
//		md9.addMetric("LOC_method", 21);
//		md9.addMetric("CYCLO_method", 4);
//		compareTo.add(md9);
//		MethodData md10 = new MethodData("src", "Teste", "double uniform (double,double)");
//		md10.addMetric("NOM_class", 3);
//		md10.addMetric("LOC_class", 37);
//		md10.addMetric("WMC_class", 6);
//		md10.addMetric("LOC_method", 4);
//		md10.addMetric("CYCLO_method", 1);
//		compareTo.add(md10);
//		MethodData md11 = new MethodData("src", "Teste", "static int myUniformDiscret (List<Integer>)");
//		md11.addMetric("NOM_class", 3);
//		md11.addMetric("LOC_class", 37);
//		md11.addMetric("WMC_class", 6);
//		md11.addMetric("LOC_method", 3);
//		md11.addMetric("CYCLO_method", 1);
//		compareTo.add(md11);
//		MethodData md12 = new MethodData("src", "Teste", "static void main (String[])");
//		md12.addMetric("NOM_class", 3);
//		md12.addMetric("LOC_class", 37);
//		md12.addMetric("WMC_class", 6);
//		md12.addMetric("LOC_method", 19); 
//		md12.addMetric("CYCLO_method", 4); 
//		compareTo.add(md12);
//		MethodData md13 = new MethodData("src", "TriangularDistribution", "static double triangularDistribution (double,double,double)");
//		md13.addMetric("NOM_class", 2);
//		md13.addMetric("LOC_class", 39);
//		md13.addMetric("WMC_class", 6);
//		md13.addMetric("LOC_method", 10);
//		md13.addMetric("CYCLO_method", 2);
//		compareTo.add(md13);
//		MethodData md14 = new MethodData("src", "TriangularDistribution", "static void main (String[])");
//		md14.addMetric("NOM_class", 2);
//		md14.addMetric("LOC_class", 39);
//		md14.addMetric("WMC_class", 6);
//		md14.addMetric("LOC_method", 20);
//		md14.addMetric("CYCLO_method", 4);
//		compareTo.add(md14);
//		
//		assertEquals(compareTo.toString(), test.toString());

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
		
		MethodData test = new MethodData(parameter);
	}

	@Test
	final void testGetMetric() {
		/*
		 *  Não fiz teste para a string ser null ou vazia ou ñ existir no mapa
		 *  pk ñ está implementado (qualquer erro é do proprio map)
		 *  
		 */
		
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
		assertEquals(2, test.getMetric("LOC_class"));
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
