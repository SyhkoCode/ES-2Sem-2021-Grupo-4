package classes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.Indicator;
import classes.Quality;


class QualityTest {
	
	static HashMap<String,Indicator> indicatorsPerMethod = new HashMap<>();
	static HashMap<String,Indicator> indicatorsPerClass = new HashMap<>();
	static Quality quality;
	static Indicator indicatorFP;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		quality = new Quality(indicatorsPerMethod,indicatorsPerClass);
		indicatorFP = Indicator.FP;
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
	final void testQuality() {	
		assertNotNull(quality);
		
		assertEquals(indicatorsPerMethod,quality.getIndicatorsPerMethod());
		assertEquals(indicatorsPerClass,quality.getIndicatorsPerClass());
		
		assertEquals(indicatorsPerClass.values().stream().filter(v -> v.equals(indicatorFP)).count(),quality.countIndicatorInClasses(indicatorFP));
		assertEquals(indicatorsPerMethod.values().stream().filter(v -> v.equals(indicatorFP)).count(),quality.countIndicatorInMethods(indicatorFP));
	}

	@Test
	final void testGetIndicatorsPerMethod() {
		assertEquals(indicatorsPerMethod,quality.getIndicatorsPerMethod());	
		
		indicatorsPerMethod.put("toString(Method)", indicatorFP);
		assertEquals(indicatorsPerMethod,quality.getIndicatorsPerMethod());
	}

	@Test
	final void testGetIndicatorsPerClass() {
		assertEquals(indicatorsPerClass,quality.getIndicatorsPerClass());	
		
		indicatorsPerClass.put("Scanner", indicatorFP);
		assertEquals(indicatorsPerClass,quality.getIndicatorsPerClass());	
	}

	@Test
	final void testCountIndicatorInClasses() {
		indicatorsPerClass.put("Scanner", indicatorFP);
		assertEquals(1,quality.countIndicatorInClasses(indicatorFP));
		
		indicatorsPerClass.put("JavaClassParser", indicatorFP);
		assertEquals(2,quality.countIndicatorInClasses(indicatorFP));
		
		indicatorsPerClass.clear();
		assertEquals(0,quality.countIndicatorInClasses(indicatorFP));
	}

	@Test
	final void testCountIndicatorInMethods() {
		indicatorsPerMethod.put("read()", indicatorFP);
		assertEquals(1,quality.countIndicatorInMethods(indicatorFP));	
		
		indicatorsPerMethod.put("toString(Method)", indicatorFP);
		assertEquals(2,quality.countIndicatorInMethods(indicatorFP));
		
		indicatorsPerMethod.clear();;
		assertEquals(0,quality.countIndicatorInMethods(indicatorFP));
	}

}
