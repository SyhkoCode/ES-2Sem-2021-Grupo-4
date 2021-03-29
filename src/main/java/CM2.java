import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CM2 {
	static private Pattern pattern = Pattern.compile("(\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;))", Pattern.MULTILINE);
	static private Pattern exceptions = Pattern.compile("(\\/\\/.*)((\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;)))");
	
	
	static public int nOfClyclo(String method) {
		int n = 1;
		
		Matcher matcher = pattern.matcher(method);
		Matcher remove = exceptions.matcher(method); 
		while(matcher.find()) {
			n++;
		}
		
		while(remove.find()) {
			n--;
		}
		
		return n;
		
	}
	
	private ArrayList<Integer> cyclos = new ArrayList<>();
	private int woc;
	
	public static void main(String[] args) {
		File file = new File("teste.txt");
		try {
			Scanner scanner = new Scanner(file);
			String s = scanner.useDelimiter("\\A").next();
			String noEnter = s.replaceAll(System.getProperty("line.separator"), "");
			String cleanText = noEnter.replaceAll("(\\\"(.*)\\\"|\\/\\/(.*)|\\/\\*(.*)|\\*\\/(.*))", "");
			System.out.println(nOfClyclo(cleanText));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
