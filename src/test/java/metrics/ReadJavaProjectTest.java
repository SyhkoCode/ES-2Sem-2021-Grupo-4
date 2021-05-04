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
		List<String[]> result = ReadJavaProject.readJavaProject("src\\test\\resources\\jasml_0.10");
		assertNotNull(result);
		// só com este cobre 95,1%
		
//		assertNotNull(ReadJavaProject.readJavaProject("C:\\Users\\sophi\\Desktop\\Projeto")); // mais este fica 97,7%
//		assertNotNull(ReadJavaProject.readJavaProject("C:\\Users\\sophi\\Desktop\\Snake")); // ainda mais este fica 99,0%
	}

}