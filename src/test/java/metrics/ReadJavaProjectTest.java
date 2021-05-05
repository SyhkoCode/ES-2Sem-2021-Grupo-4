package metrics;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadJavaProjectTest {

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
	final void testReadJavaProject() throws Exception {
		assertThrows(NullPointerException.class, ()->{ReadJavaProject.readJavaProject("naoexiste");});
		
		List<String[]> result = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
		assertNotNull(result);
	}

}