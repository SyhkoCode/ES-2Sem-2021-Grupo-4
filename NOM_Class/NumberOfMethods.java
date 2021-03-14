import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NumberOfMethods {

	private static Scanner scanner;
	private static List<String> list = new ArrayList<String>();
	private static List<String> list2 = new ArrayList<>();

	public static void countMethods(String filepath) {
		File file = new File(filepath);
		String[] classname = file.getName().split("[.]");
		String regex = "(public|protected|private|static)+\\n* \\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex2 = "(public|protected|private|static)+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex3 = "(public|protected|private|static)+\\n* \\s*" + "(" + classname[0] + ")"
				+ " *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex4 = "^\\s*[\\w\\<\\>\\[\\]\\.]+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		String regex5 = "(if|else|for|while|switch|catch)\\n* \\s*(\\w+) \\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
		Pattern pattern3 = Pattern.compile(regex3, Pattern.MULTILINE);
		Pattern pattern4 = Pattern.compile(regex4, Pattern.MULTILINE);
		Pattern pattern5 = Pattern.compile(regex5, Pattern.MULTILINE);

		try {
			scanner = new Scanner(file);
			String text = scanner.useDelimiter("\\A").next();
			String text2 = text.replaceAll("(\\\"(.*)\\\"|\\/\\/(.*)|\\/\\*(.*)|\\*\\/(.*))", "");
			Matcher matcher = pattern.matcher(text2);
			Matcher matcher4 = pattern4.matcher(text2);
			while (matcher.find()) {

				list.add(matcher.group().trim());
			}

			

			while (matcher4.find()) {
				Matcher matcher5 = pattern5.matcher(matcher4.group());
				if (!matcher5.find()) {
					list.add(matcher4.group().trim());
				}
			}

			List<String> list3 = list.stream().distinct().collect(Collectors.toList());
			for (String str : list3) {
				Matcher matcher6 = pattern2.matcher(str);
				if (matcher6.find()) {
					Matcher matcher7 = pattern3.matcher(str);
					if (!matcher7.find()) {
						list2.add(str);
					}
				}
			}

			list3.addAll(list2);
			list3 = list3.stream()
					.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
					.entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey)
					.collect(Collectors.toList());

			System.out.println(Arrays.toString(list3.toArray()));
			System.out.println(list3.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		String str = "C:\\Users\\Pedro Pinheiro\\Downloads\\jasml_0.10\\src\\com\\jasml\\compiler\\SourceCodeParser.java";
		countMethods(str);
	}
}
