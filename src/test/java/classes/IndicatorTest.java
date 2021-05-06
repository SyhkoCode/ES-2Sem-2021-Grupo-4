package classes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.Indicator;

class IndicatorTest {

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
	final void testIndicator() {
		assertEquals(Indicator.VP,Indicator.valueOf("VP"));
		assertEquals(Indicator.VN,Indicator.valueOf("VN"));
		assertEquals(Indicator.FP,Indicator.valueOf("FP"));
		assertEquals(Indicator.FN,Indicator.valueOf("FN"));
		
		ArrayList<Indicator> values = new ArrayList<Indicator>();
		for(Indicator i : Indicator.values() ) {
			values.add(i);
		}
		
		ArrayList<Indicator> indicators = new ArrayList<Indicator>();
		indicators.add(Indicator.VP);
		indicators.add(Indicator.VN);
		indicators.add(Indicator.FP);
		indicators.add(Indicator.FN);
		
		assertEquals(indicators,values);
	}

	@Test
	final void testGetName() {
		assertEquals("Verdadeiro Positivo", Indicator.VP.getName());
		assertEquals("Verdadeiro Negativo", Indicator.VN.getName());
		assertEquals("Falso Positivo", Indicator.FP.getName());
		assertEquals("Falso Negativo", Indicator.FN.getName());
	}

}
