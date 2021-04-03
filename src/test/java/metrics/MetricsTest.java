package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetricsTest {

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
	final void testGetLines() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCountMethods() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetLinesOfMethods() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testNOfClyclo() {
		fail("Not yet implemented"); // TODO
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
