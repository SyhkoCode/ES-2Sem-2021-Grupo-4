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
	
	public boolean smellDetectedV0(ReadMethod m) {
		Stack<String> a = new Stack<>();
		Scanner scanner = new Scanner(text);
		
		while(scanner.hasNext()) {
			a.add(scanner.next());
			if(a.peek().equals(")")) {
				a.pop();
				String toAdd = "";
				
				if(a.peek().equals("true") || a.peek().equals("false")) {
					System.out.println("boop");
					String d = a.pop();
					System.out.println(d);
					if(a.peek().equals("(") || a.peek().equals("SE"))
						toAdd = d;
					else {
						String uniao = a.pop();
						System.out.println(uniao);
						String e = a.pop();
						System.out.println(e);
						toAdd = "" + checkEOU(e, uniao, d);
					}
				}
				else {
					
					int valor1 = Integer.parseInt(a.pop());
					String comparador1 = a.pop();
					String metrica1 = a.pop();
				
					boolean res1 = checkMetrica(m.getMetric(metrica1), comparador1, valor1);
					toAdd = "" + res1;
				
					if(a.peek().equals("("))
						a.pop();
				
					if(a.peek().equals("E") || a.peek().equals("OU")) {
						String uniao = a.pop();
						String e = a.pop();
						toAdd = "" + checkEOU(e, uniao, toAdd);
					}
				}
				
				if(a.peek().equals("("))
					a.pop();
				
				a.add(toAdd);
				
				
				
				/*if(a.peek().equals("OU") || a.peek().equals("E")) {
					String uniao = a.pop();
					int valor2 = Integer.parseInt(a.pop());
					String comparador2 = a.pop();
					String metrica2 = a.pop();
				}*/
			}
		}
		scanner.close();
		return Boolean.parseBoolean(a.pop());
	}
	
	public boolean smellDetected(ReadMethod m) {
		Stack<String> stack = new Stack<>();
		Scanner scanner = new Scanner(text);
		String comMetricas = "";
		
		while(scanner.hasNext()) {
			String aux = scanner.next();
			if(aux.equals("(")||aux.equals(")")||aux.equals("OU")||aux.equals("E")){
				comMetricas+=" "+ aux + " ";
			}
			else if (m.getMetrics().containsKey(aux)) {
				comMetricas += checkMetrica(m.getMetric(aux), scanner.next(), Integer.parseInt(scanner.next()));
				
			}
		}
		scanner.close();
		
		
		scanner = new Scanner(comMetricas);
		while(scanner.hasNext()) {
			String aux = scanner.next();
			stack.add(aux);
			//System.out.println(stack);
			if(stack.peek().equals(")"))
				stack.pop();
			
			String toAdd = "";
			
			if(stack.peek().equals("true") || stack.peek().equals("false")) {
				String d = stack.pop();
				toAdd = d;
				if(!stack.isEmpty()) {
					if(stack.peek().equals("(")) {
						stack.pop();
						toAdd = d;
					}
					else{
						String uniao = stack.pop();
						String e = stack.pop();
						toAdd = "" + checkEOU(e, uniao, d);
					}
				}
			}
			if(toAdd.length()>0)
				stack.add(toAdd);
		}
		scanner.close();
		
		return Boolean.parseBoolean(stack.pop());
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
			case "!=":
				return calculado > limite;
			default:
				System.out.println("the heck u got here???");
				return false;
		}
	}
	
	private boolean checkEOU(String e, String uniao, String d) {
		switch(uniao) {
		case "E":
			return Boolean.parseBoolean(e) && Boolean.parseBoolean(d);
		case "OU":
			return Boolean.parseBoolean(e) || Boolean.parseBoolean(d);
		default:
			System.out.println("the heck u got here???");
			return false;
		}
	}
	
	public static void main(String[] args) {

		String teste = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU ( LOC_class > 10 E WMC_class > 50 ) )";
		String teste2 = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU WMC_class > 50 )";
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
	
		Rule r = new Rule(teste);
		Rule r2 = new Rule(teste2);
		Rule r3 = new Rule(teste3);
		Rule r4 = new Rule(teste4);
		Rule r5 = new Rule(teste5);
		Rule r6 = new Rule(teste6);
		ReadMethod m = new ReadMethod();
		m.addMetric("NOM_class", 6);
		m.addMetric("LOC_class", 1);
		m.addMetric("WMC_class", 55);
		System.out.println(r.smellDetected(m));
		System.out.println(r2.smellDetected(m));
		System.out.println(r3.smellDetected(m));
		System.out.println(r4.smellDetected(m));
		System.out.println(r5.smellDetected(m));
		System.out.println(r6.smellDetected(m));
	}
}
