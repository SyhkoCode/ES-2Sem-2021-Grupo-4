
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metrics {

	public static int getLines(File f) throws IOException {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		int i = 0;
		try {
			while (true) {
				if (br.readLine().indexOf("{") != -1) {
					while (br.readLine() != null)
						i++;
					break;
				}
			}
		} catch (NullPointerException e) {
		}
		br.close();
		fr.close();
		return i;
	}

	public static LinkedHashSet<String> countMethods(File filepath) {
		String regex = "(public|protected|private|static)+\\n*\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n*\\s*(\\w+)\\n*\\s*\\([^\\)]*\\) *(\\{?|[^;])";
		String regex2 = "^(?!\\s*(public|private|protected))\\s*(abstract)?\\n*\\s*[\\w\\<\\>\\[\\]\\.]+\\n* \\s*(\\w+) *\\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		//String regex3 = "(if|else|for|while|switch|catch)\\n* \\s*(\\w+) \\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])";
		String regex3 = "(if|else|for|while|switch|catch)\\n* \\s*(\\w+) \\n*\\s*\\([^\\)]*\\)* *(\\{?|[^;])|((^|\\s*)return )";
		
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
		Pattern pattern3 = Pattern.compile(regex3, Pattern.MULTILINE);

		LinkedHashSet<String> nomMethod = new LinkedHashSet<String>();

		try {
			Scanner scanner = new Scanner(filepath);
			String text = scanner.useDelimiter("\\A").next();
			String cleanText = text.replaceAll("\\/\\/(.*)|\\/\\*([\\s\\S]*?)\\*\\/", "");
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

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return nomMethod;
	}

	
	static public LinkedHashMap<String, String> getLinesOfMethods(File file, LinkedHashSet<String> methods) throws FileNotFoundException {
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
                  //  System.out.println(method);
                }
            } 

        }
        scanner.close();
        return map;
    }
	
	
	
	
	static public int nOfClyclo(String method) {
		int n = 1;
		Pattern pattern = Pattern.compile("(\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;))", Pattern.MULTILINE);
		String cleanText = method.replaceAll("\\/\\/(.*)|\\/\\*([\\s\\S]*?)\\*\\/", "");
	//	System.out.println(method);
		Matcher matcher = pattern.matcher(cleanText);
		while(matcher.find()) {
			n++;
		}
		
		return n;
		
	}
	
	static public int wmc(ArrayList<Integer> cyclos) {
		int i = 0;
		for(int f: cyclos)
			i+=f;
		return i;
	}
	
	
	static public ArrayList<Integer> countLinesOfMethods (LinkedHashMap<String, String> map) throws FileNotFoundException {
		ArrayList<Integer> getLines = new ArrayList<Integer> ();
		for(String key : map.keySet()) {
			getLines.add(map.get(key).split("\r?\n").length);
		}
		return getLines;
	}

	public static ArrayList<Integer> allCyclos(LinkedHashMap<String, String> linesOfMethods) {
		
		ArrayList<Integer> cycloOfAllMethods = new ArrayList<Integer> ();
		
		for(String key : linesOfMethods.keySet())
			cycloOfAllMethods.add(nOfClyclo(linesOfMethods.get(key)));
	
		return cycloOfAllMethods;
	
	}
	
	
}
