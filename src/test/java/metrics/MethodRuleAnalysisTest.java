package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodRuleAnalysisTest {
	File testeregras = new File(getClass().getResource("/testeregras.txt").getFile());
	MethodData test = new MethodData("metrics", "ExcelDealer", "main");
	ArrayList<MethodData> methods = MethodData.excelToMetricsMap("src/test/resources/Snake_metrics.xlsx");
	ArrayList<Rule> rules = Rule.allRules(testeregras);
	MethodRuleAnalysis mra= new MethodRuleAnalysis(methods, rules);

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
	final void testMethodRuleAnalysis() {
		assertNotNull(MethodRuleAnalysis.class);
		assertNotNull(mra);
	}

	@Test
	final void testGetMap() {
		assertNotNull(mra.getMap());
	}

	@Test
	final void testGetMethods() {
		assertNotNull(mra.getMethods());
	}

	@Test
	final void testGetResults() {
		assertNotNull(mra.getResults());
	}

	@Test
	final void testGetCodeSmellResults() {
		assertNotNull(mra.getCodeSmellResults());
	}

}
