package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RuleTest {

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
	final void testRule() {
		Exception exception = assertThrows(NullPointerException.class, ()->{new Rule(null, null);});
		String expectedMessage = "Os argumentos nao podem ser nulos.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		exception = assertThrows(NullPointerException.class, ()->{new Rule("is_God_Class", null);});
		expectedMessage = "Os argumentos nao podem ser nulos.";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		exception = assertThrows(NullPointerException.class, ()->{new Rule (null, "SE ( NOM_class > 5 )");});
		expectedMessage = "Os argumentos nao podem ser nulos.";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		exception = assertThrows(IllegalArgumentException.class, ()->{new Rule ("", "");});
		expectedMessage = "Os argumentos não podem ser vazios.";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		exception = assertThrows(IllegalArgumentException.class, ()->{new Rule ("is_God_Class", "");});
		expectedMessage = "Os argumentos não podem ser vazios.";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		exception = assertThrows(IllegalArgumentException.class, ()->{new Rule ("", "SE ( NOM_class > 5 )");});
		expectedMessage = "Os argumentos não podem ser vazios.";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		Rule test = new Rule ("is_God_Class", "SE ( NOM_class > 5 )");
		assertEquals("is_God_Class", test.getNome());
		assertEquals("SE ( NOM_class > 5 )", test.getText());
		
	}

	@Test
	final void testSmellDetected() {
		String teste = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) E ( LOC_class > 10 E WMC_class > 50 ) )";
		String teste2 = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU WMC_class > 50 )";
		String teste3 = "SE ( NOM_class > 5 E LOC_class > 20 )";
		String teste4 = "SE ( ( NOM_class > 5 ) OU LOC_class > 20 )";
		String teste5 = "SE ( NOM_class > 5 )";
		String teste6 = "SE ( ( NOM_class > 5 ) OU ( LOC_class > 20 ) )";
		
		Rule r = new Rule("nome", teste);
		Rule r2 = new Rule("nome", teste2);
		Rule r3 = new Rule("nome", teste3);
		Rule r4 = new Rule("nome", teste4);
		Rule r5 = new Rule("nome", teste5);
		Rule r6 = new Rule("nome", teste6);
		
		Exception exception = assertThrows(NullPointerException.class, ()->{r.smellDetected(null);});
		String expectedMessage = "O argumento nao pode ser nulo.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		MethodData m = new MethodData("package", "class", "metodo");
		m.addMetric("NOM_class", 7);
		m.addMetric("LOC_class", 15);
		m.addMetric("WMC_class", 50);
		
		assertEquals(false, r.smellDetected(m));
		assertEquals(true, r2.smellDetected(m));
		assertEquals(false, r3.smellDetected(m));
		assertEquals(true, r4.smellDetected(m));
		assertEquals(true, r5.smellDetected(m));
		assertEquals(true, r6.smellDetected(m));
		
		Rule wrong = new Rule("fail_pls", "shdjsdfhjdfhdf");
		exception = assertThrows(IllegalStateException.class, ()->{wrong.smellDetected(m);});
		expectedMessage = "Regra invalida, verifique o ficheiro";
		actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	@Test
	final void testAllRules() {
		Exception exception = assertThrows(NullPointerException.class, ()->{Rule.allRules(null);});
		String expectedMessage = "O ficheiro nao pode ser nulo.";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
		
		ArrayList<Rule> teorical = new ArrayList<>();
		teorical.add(new Rule("is_God_Class", "SE ( NOM_class > 5 OU LOC_class > 100 )"));
		teorical.add(new Rule("is_Long_Method", "SE ( ( LOC_method > 15 E CYCLO_method > 4 ) )"));
		
		File rules = new File(getClass().getResource("/testeregras.txt/").getFile());
		ArrayList<Rule> test = Rule.allRules(rules);

		assertEquals(teorical.size(), test.size());
		
		for(int i = 0; i < teorical.size(); i++) {
			assertEquals(teorical.get(i).getNome(), test.get(i).getNome());
			assertEquals(teorical.get(i).getText(), test.get(i).getText());
		}
			
		
		File givesNull = new File(getClass().getResource("/wrongRules.txt/").getFile());
		assertEquals(null, Rule.allRules(givesNull));
		File givesNull2 = new File(getClass().getResource("/wrongRules2.txt/").getFile());
		assertEquals(null, Rule.allRules(givesNull2));

	}

	@Test
	final void testGetNome() {
		Rule test = new Rule ("is_God_Class", "SE ( NOM_class > 5 )");
		assertEquals("is_God_Class", test.getNome());
		
		test = new Rule ("is_Long_Method", "SE ( NOM_class > 5 )");
		assertEquals("is_Long_Method", test.getNome());
		
		test = new Rule ("is_Long_Class", "SE ( NOM_class > 5 )");
		assertEquals("is_Long_Class", test.getNome());
	}
	
	@Test
	final void testGetText() {
		Rule test = new Rule ("is_God_Class", "SE ( NOM_class > 5 )");
		assertEquals("SE ( NOM_class > 5 )", test.getText());
		
		test = new Rule ("is_Long_Method", "SE ( ( NOM_class > 5 OU LOC_class > 20 ) E ( LOC_class > 10 E WMC_class > 50 ) )");
		assertEquals("SE ( ( NOM_class > 5 OU LOC_class > 20 ) E ( LOC_class > 10 E WMC_class > 50 ) )", test.getText());
		
		test = new Rule ("is_Long_Method", "SE ( ( LOC_class > 10 E WMC_class > 50 ) )");
		assertEquals("SE ( ( LOC_class > 10 E WMC_class > 50 ) )", test.getText());
	}

}
