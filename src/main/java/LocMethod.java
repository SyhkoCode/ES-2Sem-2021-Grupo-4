import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocMethod {
	//static private Pattern pattern = Pattern.compile("^case |^if |^if\\(|^else |^else\\{|^for |^for\\(|^while |^while\\(");
	static private Pattern pattern = Pattern.compile("^if |^if\\(|^case |^catch |^catch\\(|\\} catch \\(|^throw |^do |^do\\{|^while |^while\\(|^for |^for\\(|^continue;|\\?.*\\:|\\|\\||\\&\\&|.*else if"); 
		
	private ArrayList<Integer> cyclos = new ArrayList<>();
	private int woc;
	
	static public int nOfCyclo(ArrayList<String> method) {
		int n = 1;
		
		for(String line: method) {
			Matcher matcher = pattern.matcher(line.trim());
			while(matcher.find())
				n++;

		}
		return n;
	}
	
	static public LinkedHashMap<String, ArrayList<String>> getLinesOfMethods(File file, ArrayList<String> methods) throws FileNotFoundException {
		LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<>();
		Scanner scanner = new Scanner(file);
		for(String m : methods) {
			map.put(m, new ArrayList<String>());
			boolean foundMethod = false;
			while(scanner.hasNext() && !foundMethod) {
				String checkIfStart = scanner.nextLine();
				if(checkIfStart.contains(m)) {
					foundMethod = true;
					boolean endedMethod = false;
					map.get(m).add(checkIfStart);
					
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
						map.get(m).add(check4Curlies);
						openCurly += check4Curlies.contains("{") ? 1:0;
						openCurly -= (check4Curlies.contains("}") && !check4Curlies.contains("'}'"))  ? 1:0;
						if(openCurly == 0)
							endedMethod = true;
					}
					
				}
			}
		}			
		scanner.close();
		
		return map;
	}
	
	static public ArrayList<Integer> countLinesOfMethods (LinkedHashMap<String, ArrayList<String>> map) throws FileNotFoundException {
		ArrayList<Integer> getLines = new ArrayList<Integer> ();
		for(String key : map.keySet()) {
			getLines.add(map.get(key).size());
		}
		return getLines;
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
	
	public static void main(String[] args) {
		ArrayList<String> teste =  nameOfMethods(new File("SourceCodeParser.java"));
		
		
		try {
			LinkedHashMap<String, ArrayList<String>> map = getLinesOfMethods(new File("SourceCodeParser.java"), teste);
			ArrayList<Integer> lines = countLinesOfMethods(map); 
				for (int i : lines) {
					System.out.println(i);
				
			}
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}