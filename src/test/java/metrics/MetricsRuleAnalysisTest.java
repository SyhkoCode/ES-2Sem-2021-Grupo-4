package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricsRuleAnalysisTest {

	File excelMetrics = new File(getClass().getResource("/jasml_0.10_metrics.xlsx").getFile());
	String csFileMetrics = excelMetrics.getAbsolutePath();
	
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	MethodData test = new MethodData("metrics", "ExcelDealer", "main");
	
	
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
	final void testMetricsRuleAnalysis() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(MetricsRuleAnalysis.class);
		assertNotNull(mra);
	}

	@Test
	final void testGetCodeSmellDetectedMap() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getCodeSmellDetectedMap());
		assertTrue(mra.getCodeSmellDetectedMap().containsKey("is_God_Class"));
		assertTrue(mra.getCodeSmellDetectedMap().containsKey("is_Long_Method"));
	}

	@Test
	final void testGetMethodsData() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getMethodsData());
		
		Object[] parameter = new Object[9];
		parameter[1] = "package default";
		parameter[2] = "jasml";
		parameter[3] = "main(String[])";
		parameter[4] = 8; //NOM_class
		parameter[5] = 204; //LOC_class
		parameter[6] = 49; //WMC_class
		parameter[7] = 8; //LOC_method
		parameter[8] = 2; //CYCLO_method
		
		MethodData test = new MethodData(parameter, new String[]{"NOM_class","LOC_class","WMC_class","LOC_method","CYCLO_method"});
		assertEquals(test.toString(),mra.getMethodsData().get(0).toString());
	}

	@Test
	final void testGetResults() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getResults());
		
		Object[] parameter = new Object[6];
		parameter[0] = "MethodID";
		parameter[1] = "package";
		parameter[2] = "class";
		parameter[3] = "method";
		parameter[4] = "is_God_Class";
		parameter[5] = "is_Long_Method";
		
		assertEquals(parameter[0],mra.getResults().get(0)[0]);
		assertEquals(parameter[4],mra.getResults().get(0)[4]);
		assertEquals(parameter[5],mra.getResults().get(0)[5]);
		
		Object[] data = new Object[5]; //row 1
		data[0] = "package default";
		data[1] = "jasml";
		data[2] = "main(String[])";
		data[3] = "true";
		data[4] = "false";
		
		assertEquals(data[3],mra.getResults().get(1)[3]); //row 1
		assertEquals(data[4],mra.getResults().get(1)[4]); //row 1
	}

	@Test
	final void testGetCodeSmellResults() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getCodeSmellResults());
		
		List<String[]> isgodclass = new ArrayList<>();
		isgodclass.add(new String[]{"Class", "is_God_Class"});
		
		List<String[]> isgodclassJasml = new ArrayList<>();
		isgodclassJasml.add(new String[]{"jasml", "true"});

		assertArrayEquals(isgodclass.get(0),mra.getCodeSmellResults().get(0).get(0));
		assertArrayEquals(isgodclassJasml.get(0),mra.getCodeSmellResults().get(0).get(1));
	}

}
