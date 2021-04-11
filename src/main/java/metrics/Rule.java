package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Rule {
	private String nome;
	private String text;
	
	public Rule(String nome,String text) {
		this.nome = nome;
		this.text = text;
	}
	
	public boolean smellDetected(MethodData m) {
		Scanner scanner = new Scanner(text);
		String comMetricas = text.replaceFirst("\\bSE\\b\\s+","").replace("OU","||").replace("E","&&");
		
		while(scanner.hasNext()) {
			 String aux = scanner.next();
			 if (m.getMetrics().containsKey(aux))
				comMetricas = comMetricas.replace(aux,"" + m.getMetric(aux)); 	
		}
		scanner.close();
		
		try {

            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");
            
//            System.out.println(se.eval(comMetricas));
            return (boolean) se.eval(comMetricas);

        } catch (ScriptException e) {
            System.out.println("Invalid Expression");
            e.printStackTrace();
            return false;
        }
	}
	
	public ArrayList<Rule> allRules(File f){
		ArrayList<Rule> rulesList = new ArrayList<>();
		Scanner s;
		try {
			s = new Scanner(f);
			while(s.hasNext())
				rulesList.add(new Rule(s.next(),s.next()));	
			s.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return rulesList;
	}
	
	public String getNome() {
		return nome;
	}

	public static void main(String[] args) {

		String teste = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU ( LOC_class > 10 E WMC_class > 50 ) )";
		String teste2 = "SE ( ( NOM_class > 5 OU LOC_class > 20 ) OU WMC_class > 50 )";
		String teste3 = "SE ( NOM_class > 5 E LOC_class > 20 )";
		String teste4 = "SE ( ( NOM_class > 5 ) OU LOC_class > 20 )";
		String teste5 = "SE ( NOM_class > 5 )";
		String teste6 = "SE ( ( NOM_class > 5 ) OU ( LOC_class > 20 ) )";
		MethodData m = new MethodData();
		
		m.addMetric("NOM_class", 7);
		m.addMetric("LOC_class", 15);
		m.addMetric("WMC_class", 50);
		
		Rule r = new Rule(teste,"");
		Rule r2 = new Rule(teste2,"");
		Rule r3 = new Rule(teste3,"");
		Rule r4 = new Rule(teste4,"");
		Rule r5 = new Rule(teste5,"");
		Rule r6 = new Rule(teste6,"");
		
		System.out.println(r.smellDetected(m));
		System.out.println(r2.smellDetected(m));
		System.out.println(r3.smellDetected(m));
		System.out.println(r4.smellDetected(m));
		System.out.println(r5.smellDetected(m));
		System.out.println(r6.smellDetected(m));
	}
}
