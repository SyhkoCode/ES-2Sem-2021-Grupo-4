import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CycloMethod {
	static private Pattern pattern = Pattern.compile("^if |^if\\(|^case |^catch |^catch\\(|\\} catch \\(|^do |^do\\{|^while |^while\\(|^for |^for\\(|^continue;|\\?.*\\:|\\|\\||\\&\\&|.*else if");
	static private Pattern ignoreComments = Pattern.compile("\\/\\/|\\/\\*|^\\*");
	
	/*
	 * Para conseguir o cyclo e o woc vê o main
	 * 
	 * Fiz assim pk facilita depois substituir os 2 metodos q são utilizados primeiro pelos q dão as assinaturas da classe
	 * e as linhas dos metodos das classes de outras classes, par ñ termos codigo q faz praticamente o mesmo repetido
	 * 
	 */
	
	public static void main(String[] args) {
		//mudar para receber o nome do ficheiro java
		String file = "SourceCodeParser.java";
		
		ArrayList<String> assinaturasMetodos =  nameOfMethods(new File(file));
		try {
			LinkedHashMap<String, ArrayList<String>> mapaDosMetodos = getLinesOfMethods(new File(file), assinaturasMetodos); //É o q dá a assinatura dos metodos
			LinkedHashMap<String, Integer> cycloDosMetodos = new LinkedHashMap<>(); //Usa a assinatura para apanhar as linhas de cada metodo
			for(String key : mapaDosMetodos.keySet())
				cycloDosMetodos.put(key, cyclo(mapaDosMetodos.get(key))); //Mapa ordenado com a metrica de cada metodo
			int metricaDaClasseWOC = woc(cycloDosMetodos); //Condensa o mapa num unico int q dá o woc
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public int woc(LinkedHashMap<String,Integer> cyclos) {
		int i = 0;
		for(String s: cyclos.keySet())
			i+=cyclos.get(s);
		return i;
	}
	
	static public int cyclo(ArrayList<String> method) {
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
						openCurly += (check4Curlies.contains("{") && !check4Curlies.contains("'{'")) ? 1:0;
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
	
	
	
	
	// podem ignorar apartir daqui, foi para testes e/ou é wip
	
	static private Pattern v2 = Pattern.compile("(\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;))", Pattern.MULTILINE);
	static private Pattern exception1 = Pattern.compile("(\\/\\/.*)((\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;)))");
	
	
	static public void nOfClyclo(String method) {
		int n = 1;
		
	}
	
	private ArrayList<Integer> cyclos = new ArrayList<>();
	private int woc;
	
	
	
	
	
	/*public static void main(String[] args) {
		ArrayList<String> teste =  nameOfMethods(new File("SourceCodeParser.java"));
		
		
		
		try {
			LinkedHashMap<String, ArrayList<String>> map = getLinesOfMethods(new File("SourceCodeParser.java"), teste);
			/*for(String key : map.keySet()) {
				for(String s: map.get(key))
					System.out.println(s);
				System.out.println("------------------------------");
			}
			
			LinkedHashMap<String, Integer> cyclo = new LinkedHashMap<>();
			for(String key : map.keySet())
				cyclo.put(key, cyclo(map.get(key)));

			int i = 0;
			for(String key : cyclo.keySet()) {
				System.out.println(key + " -> " + cyclo.get(key));
				/*for(String s: map.get(key))
					System.out.println(s);
				System.out.println("------------------------------");
				i += cyclo.get(key);
			}
			System.out.println("total " + i);
			
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	void teste() {
		
	}
}
