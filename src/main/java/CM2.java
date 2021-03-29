import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CM2 {
	static private Pattern pattern = Pattern.compile("(\\&\\&|\\|\\|)|((^| +|\\}|\\;|\t)((if|for|while|catch)( +|\\()))|(\\?.*\\:)|((\t|^|\\;|\\{\\})(case +|continue;))", Pattern.MULTILINE);
	
	private ArrayList<Integer> cyclos = new ArrayList<>();
	private int woc;
	
	static public int nOfClyclo(String method) {
		int n = 1;
		
		String cleanText = method.replaceAll("\\/\\/(.*)|\\/\\*([\\s\\S]*?)\\*\\/", "");
		System.out.println(method);
		
		Matcher matcher = pattern.matcher(cleanText);
		while(matcher.find()) {
			n++;
		}
		
		return n;
		
	}
	
	static public int woc(LinkedHashMap<String,Integer> cyclos) {
		int i = 0;
		for(String s: cyclos.keySet())
			i+=cyclos.get(s);
		return i;
	}
	
	/*Dos outros*/
	
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
                    System.out.println(method);
                }
            } 

        }
        scanner.close();
        return map;
    }
	
	public static void main(String[] args) {
		File file = new File("teste.txt");
		
		try {
			Scanner scanner = new Scanner(file);
			String s = scanner.useDelimiter("\\A").next();
			System.out.println(nOfClyclo(s));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String file = "SourceCodeParser.java";
		
		ArrayList<String> assinaturasMetodos =  CycloMethod.nameOfMethods(new File(file));
		
		try {
			LinkedHashMap<String, String> mapaDosMetodos = getLinesOfMethods(new File(file), assinaturasMetodos); //É o q dá a assinatura dos metodos
			LinkedHashMap<String, Integer> cycloDosMetodos = new LinkedHashMap<>(); //Usa a assinatura para apanhar as linhas de cada metodo
			for(String key : mapaDosMetodos.keySet())
				cycloDosMetodos.put(key, nOfClyclo(mapaDosMetodos.get(key))); //Mapa ordenado com a metrica de cada metodo
			int metricaDaClasseWOC = woc(cycloDosMetodos); //Condensa o mapa num unico int q dá o woc
				
			System.out.println("woc: " + metricaDaClasseWOC);
			
			for(String key : cycloDosMetodos.keySet())
				System.out.println(key + " " + cycloDosMetodos.get(key));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
