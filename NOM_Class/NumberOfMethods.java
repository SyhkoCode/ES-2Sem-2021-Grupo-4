import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumberOfMethods {

	private static Scanner scanner;
	private static HashSet<String> nomMethod = new HashSet<String>();

	public static void countMethods(String filepath) {
		File file = new File(filepath);
		String regex = "(public|protected|private|static)+\\n*\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+)\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex2 = "^\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		String regex3 = "(if|else|for|while|switch|catch)\\n* \\s*(\\w+) \\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
		Pattern pattern3 = Pattern.compile(regex3, Pattern.MULTILINE);

		try {
			scanner = new Scanner(file);
			String text = scanner.useDelimiter("\\A").next();
			String cleanText = text.replaceAll("(\\\"(.*)\\\"|\\/\\/(.*)|\\/\\*(.*)|\\*\\/(.*))", "");
			Matcher matcher = pattern.matcher(cleanText);
			Matcher matcher2 = pattern2.matcher(cleanText);
			while (matcher.find()) {
				nomMethod.add(matcher.group().trim().replace("{", ""));
			}

			while (matcher2.find()) {
				Matcher matcher3 = pattern3.matcher(matcher2.group());
				if (!matcher3.find()) {
					nomMethod.add(matcher2.group().trim().replace("{", ""));
				}

			}

			System.out.println(Arrays.toString(nomMethod.toArray()));
			System.out.println(nomMethod.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException {
		String str = "C:\\Users\\Pedro Pinheiro\\Downloads\\jasml_0.10\\src\\com\\jasml\\compiler\\SourceCodeParser.java";
		String str2 = "C:\\Users\\Pedro Pinheiro\\Desktop\\PCD - 2º Ano LEI (ISCTE)\\Eclipse PCD Trabalhos\\Projeto Graficos 88657.zip_expanded\\ProjetoIP_Graficos\\src\\Projeto\\Grafico.java";
		countMethods(str2);
	}
}
