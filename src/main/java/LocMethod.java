import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocMethod {
	static private Pattern pattern = Pattern.compile("^if |^if\\(|^case |^catch |^catch\\(|\\} catch \\(|^do |^do\\{|^while |^while\\(|^for |^for\\(|^continue;|\\?.*\\:|\\|\\||\\&\\&|.*else if");
	static private Pattern ignoreComments = Pattern.compile("\\/\\/|\\/\\*|^\\*");
	
	static public int nOfCyclo(ArrayList<String> method) {
		int n = 1;
		
		for(String line: method) {
			Matcher matcher = pattern.matcher(line.trim());
			while(matcher.find()) {
				Matcher comments = ignoreComments.matcher(line.trim());
				if(comments.find()) {
					if(line.indexOf(matcher.group()) < line.indexOf(comments.group()))
						n++;
				}
				else {
					n++;
				}
				
			}
		}
		return n;
	}
	
	static public LinkedHashMap<String, String> getLinesOfMethods(File file, ArrayList<String> methods) throws FileNotFoundException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		Scanner scanner = new Scanner(file);
		for(String m : methods) {
			String method = new String ("");
			boolean foundMethod = false;
			while(scanner.hasNext() && !foundMethod) {
				String checkIfStart = scanner.nextLine();
				if(checkIfStart.contains(m)) {
					foundMethod = true;
					boolean endedMethod = false;
					method = method + checkIfStart;
					int openCurly = (checkIfStart.contains("{")) ? 1 : 0;
					
					while(openCurly == 0) {						
						String got1stCurly = scanner.nextLine().trim();
						openCurly += got1stCurly.contains("{") ? 1:0;
						if(got1stCurly.contains("}")) {
							endedMethod = true;
							break;
						}
					}
					while(scanner.hasNext() && !endedMethod) {
						String check4Curlies = scanner.nextLine();
						method = method + "\n" + check4Curlies;
						openCurly += (check4Curlies.contains("{") && !check4Curlies.contains("'{'")) ? 1:0;
						openCurly -= (check4Curlies.contains("}") && !check4Curlies.contains("'}'"))  ? 1:0;
						if(openCurly == 0)
							endedMethod = true;
					}
					map.put(m, method);
				}
			} 
			
		}			
		scanner.close();
		
		return map;
	}
	
	public static ArrayList<String> nameOfMethods(File file){
		ArrayList<String> result = new ArrayList<>();
		
		String[] classname = file.getName().split("[.]");
		String regexa = "(public|protected|private|static)+\\n* \\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regexb = "(public|protected|private|static)+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regexc = "(public|protected|private|static)+\\n* \\s*" + "(" + classname[0] + ")" + " *\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		Pattern patterna = Pattern.compile(regexa, Pattern.MULTILINE);
		Pattern patternb = Pattern.compile(regexb, Pattern.MULTILINE);
		Pattern patternc = Pattern.compile(regexc, Pattern.MULTILINE);

		try {
			Scanner scanner = new Scanner(file);
			String text = scanner.useDelimiter("\\A").next();
			Matcher matchera = patterna.matcher(text);

			while (matchera.find()) {
				Matcher matcherb = patternb.matcher(matchera.group());
				if (matcherb.find()) {
					Matcher matcher3 = patternc.matcher(matcherb.group());
					if (matcher3.find()) {
						result.add(matcherb.group());
					}
				} else {
					result.add(matchera.group());
				}

			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	static public ArrayList<Integer> countLinesOfMethods (LinkedHashMap<String, String> map) throws FileNotFoundException {
		ArrayList<Integer> getLines = new ArrayList<Integer> ();
		for(String key : map.keySet()) {
			getLines.add(map.get(key).split("\r?\n").length);
		}
		return getLines;
	}
	
	public static void main(String[] args) {
		ArrayList<String> teste =  nameOfMethods(new File("GrammerException.java"));
		
		
		try {
			LinkedHashMap<String, String> map = getLinesOfMethods(new File("GrammerException.java"), teste);
			for (String key: map.keySet()) {
				System.out.println(map.get(key));
			}
			
			ArrayList<Integer> lines = countLinesOfMethods(map); 
			for (int i : lines) {
				System.out.println(i);
			 }
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}