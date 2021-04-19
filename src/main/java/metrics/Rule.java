package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Rule {
	private String nome;
	private String text;
	
	public Rule(String nome,String text) {
		if(nome == null || text == null)
			throw new NullPointerException("Os argumentos nao podem ser nulos.");
		if(nome.isEmpty() || text.isEmpty())
			throw new IllegalArgumentException("Os argumentos não podem ser vazios.");
		this.nome = nome;
		this.text = text;
	}
	
	public boolean smellDetected(MethodData m) {
		if(m == null)
			throw new NullPointerException("O argumento nao pode ser nulo."); 
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
            return (boolean) se.eval(comMetricas);

        } catch (ScriptException e) {
        	e.printStackTrace();
        	throw new IllegalStateException("Regra invalida, verifique o ficheiro");
        }
	}
	
	public static ArrayList<Rule> allRules(File f){
		if(f == null)
			throw new NullPointerException("O ficheiro nao pode ser nulo.");
		ArrayList<Rule> rulesList = new ArrayList<>();
		Scanner s;
		try {
			s = new Scanner(f);
			while(s.hasNext())
				rulesList.add(new Rule(s.nextLine(),s.nextLine()));	
			s.close();
			
		} catch (FileNotFoundException | NoSuchElementException e ) {
			e.printStackTrace();
			return null;
		}
		if(rulesList.size() == 0)
			return null;
		return rulesList;
	}

	public String getNome() {
		return nome;
	}
	
	public String getText() {
		return text;
	}

}
