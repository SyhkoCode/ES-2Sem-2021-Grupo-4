package metrics;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

class FileDealerTest {

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
	final void testCreateFile() throws IOException {
        TemporaryFolder tempFolder = new TemporaryFolder();
        tempFolder.create();
        File output = tempFolder.newFile("output.txt");
        String path = output.getPath();
        
        ArrayList<String> write = new ArrayList<>();
        write.add("is_God_Class");
        write.add("SE ( ( NOM_class > 5 E LOC_method > 20 ) OU ( LOC_class > 10 E WMC_class > 50 ) )");     
        FileDealer.createFile(path, write);
        assertTrue(output.exists());
	}

	@Test
	final void testReadFile() throws IOException {        
        assertThrows(IllegalArgumentException.class, ()->{FileDealer.readFile("naoexiste");});
        assertNotNull(FileDealer.readFile("testeregras"));       
	}

}
