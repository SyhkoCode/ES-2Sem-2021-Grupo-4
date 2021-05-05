package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricsRuleAnalysisTest {

	File excelMetrics = new File(getClass().getResource("/jasml_0.10_metrics_created.xlsx").getFile());
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
	}

	@Test
	final void testGetMethodsData() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getMethodsData());
	}

	@Test
	final void testGetResults() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getResults());
	}

	@Test
	final void testGetCodeSmellResults() throws Exception {
		ArrayList<Rule> rules = Rule.allRules(testeregras.getAbsolutePath());
		ArrayList<MethodData> methods = MethodData.excelToMetricsMap(csFileMetrics);
		MetricsRuleAnalysis mra= new MetricsRuleAnalysis(methods, rules);
		assertNotNull(mra.getCodeSmellResults());
	}

}
