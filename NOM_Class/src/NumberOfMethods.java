import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberOfMethods {

	private static Scanner scanner;

	public static int countMethods(String filepath) {
		File file = new File(filepath);
		String[] classname = file.getName().split("[.]");
		String regex = "(public|protected|private|static)+\\n* \\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex2 = "(public|protected|private|static)+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex3 = "(public|protected|private|static)+\\n* \\s*" + "(" + classname[0] + ")" + " *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
		Pattern pattern3 = Pattern.compile(regex3, Pattern.MULTILINE);
		int count = 0;

		try {
			scanner = new Scanner(file);
			String text = scanner.useDelimiter("\\A").next();
			Matcher matcher = pattern.matcher(text);

			while (matcher.find()) {
				Matcher matcher2 = pattern2.matcher(matcher.group());
				if (matcher2.find()) {
					Matcher matcher3 = pattern3.matcher(matcher2.group());
					if (matcher3.find()) {
						System.out.println(matcher2.group());
						count++;
					}
				} else {
					System.out.println(matcher.group());
					count++;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return count;
	}

	public static void main(String[] args) throws FileNotFoundException {
		String str = "colocar aqui o path do ficheiro .java";
		System.out.println(countMethods(str));

	}
}
