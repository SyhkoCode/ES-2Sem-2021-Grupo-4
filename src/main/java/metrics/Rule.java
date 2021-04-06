package metrics;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Rule {
	private String text;
	
	public Rule(String text) {
		this.text = text;
	}
	
	public boolean smellDetected(ReadMethod m) {
		Stack<String> a = new Stack<>();
		Scanner scanner = new Scanner(text);
		while(scanner.hasNext()) {
			a.add(scanner.next());
			if(a.peek().equals(")")) {
				a.pop();
				int valor1 = Integer.parseInt(a.pop());
				String comparador1 = a.pop();
				String metrica1 = a.pop();
				
				boolean res1 = checkMetrica(m.getMetric(metrica1), comparador1, valor1); 
				
				if(a.peek().equals("OU") || a.peek().equals("E")) {
					String uniao = a.pop();
					int valor2 = Integer.parseInt(a.pop());
					String comparador2 = a.pop();
					String metrica2 = a.pop();
				}
			}
		}
		scanner.close();
		return true;
	}
	
	private boolean checkMetrica(int calculado, String comparador, int limite) {
		switch(comparador) {
			case ">":
				return calculado > limite;
			case ">=":
				return calculado >= limite;
			case "<":
				return calculado < limite;
			case "<=":
				return calculado > limite;
			case "==":
				return calculado > limite;
			default:
				System.out.println("the heck u got here???");
				return false;
		}
	}
	
	public static void main(String[] args) {
		String teste = "SE ( ( NOM_class > 5 OU LOC_class > 20) OU WMC_class > 50 )";
		String teste2 = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU ( LOC_class > 10 E WMC_class > 50 ) )";
		
		String teste3 = "SE ( NOM_class > 5 OU LOC_class > 20 )";
		String teste4 = "SE ( ( NOM_class > 5 ) OU LOC_class > 20 )";
		String teste5 = "SE ( NOM_class > 5 )";
		String teste6 = "SE ( ( NOM_class > 5 ) OU ( LOC_class > 20 ) )";
		String testFormatted = teste6.replaceFirst("\\bSE\\b\\s+","").replace("OU","||").replace("E","&&");
	//	Rule r = new Rule(testeForma);
		ReadMethod m = new ReadMethod();
		m.addMetric("NOM_class", 8);
		m.addMetric("LOC_class", 15);
		m.addMetric("WMC_class", 4);
		
		
//		Pattern pattern = Pattern.compile("\\(.*\\)");
//		Matcher matcher = pattern.matcher(testFormatted);
		//String stryolo = null;
		//if (matcher.find()) {
			
			for(String s: m.getMetrics().keySet()){
				testFormatted = testFormatted.replace(s,"" + m.getMetric(s));
			}
			//}
			System.out.println(testFormatted);
			
			
			 try {

		            ScriptEngineManager sem = new ScriptEngineManager();
		            ScriptEngine se = sem.getEngineByName("JavaScript");
		            
		            System.out.println(se.eval(testFormatted));

		        } catch (ScriptException e) {

		            System.out.println("Invalid Expression");
		            e.printStackTrace();

		        }
			
			
				//System.out.println(Boolean.parseBoolean(sss));
				//System.out.println(Boolean.parseBoolean("25 > 20"));
		  // String str = matcher.group();
		   //str.replaceAll()
		}
		
		
	//	System.out.println(r.smellDetected(m));
	
}
