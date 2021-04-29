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

/**
 * Represents a Rule that can be used to detect a code smell
 * 
 * @author Susana Polido
 * @author Pedro Pinheiro
 * @author Tiago Mendes
 * 
 */
public class Rule {
	private String nome;
	private String expression;
	
	/**
	 * Rule constructor
	 * <p>
	 * Creates a Rule from 2 Strings.
	 * @param nome The name of the code smell that can be detected with this rule example: "is_God_Class"
	 * @param expression String that represents the actual rule example: "SE ( LOC_class > 5 OU LOC_class > 100 )"
	 * @throws NullPointerException if the arguments are null
	 * @throws IllegalArgumentException if the arguments are empty
	 */
	public Rule(String nome,String expression){
		if(nome == null || expression == null)
			throw new NullPointerException("Os argumentos nao podem ser nulos.");
		if(nome.isEmpty() || expression.isEmpty())
			throw new IllegalArgumentException("Os argumentos nao podem ser vazios.");
		this.nome = nome;
		this.expression = expression;
	}
	
	/**
	 * Sees if the code smell is present in a method.
	 * <p>
	 * Using the data extracted from the code of a method, it sees if the method tests 
	 * positive for the code smell represented by the rule.
	 * <p>
	 * Copies and changes the expression of the rule so it resembles a boolean expression.
	 * Then changes the names of the metrics the rule checks with the corresponding data from the methodData.
	 * The final boolean result is calculated by a JavaScript engine.
	 * @param methodData the metrics of the method that is to be tested
	 * @return true if the code smell was detected in the method, false otherwise
	 * @throws NullPointerException if the argument is null
	 * @throws IllegalStatementException if the final expression can't be turned into a boolean by the JavaScript engine
	 * @see MethodData
	 */
	public boolean smellDetected(MethodData methodData) {
		if(methodData == null)
			throw new NullPointerException("O argumento nao pode ser nulo."); 
		Scanner scanner = new Scanner(expression);
		String booleanExpression = expression.replaceFirst("\\bSE\\b\\s+","").replace("OU","||").replace("E","&&");
		
		while(scanner.hasNext()) {
			 String metricName = scanner.next();
			 if (methodData.getMetrics().containsKey(metricName))
				booleanExpression = booleanExpression.replace(metricName,"" + methodData.getMetric(metricName)); 	
		}
		scanner.close();
		
		try {

            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");
            return (boolean) se.eval(booleanExpression);

        } catch (ScriptException e) {
        	e.printStackTrace();
        	throw new IllegalStateException("Regra invalida, verifique o ficheiro");
        }
	}
	
	/**
	 * Extracts all the rules in a file
	 * <p>
	 * Using a scanner it reads a file containing the lines necessary to create rules.
	 * Each rule is 2 lines, the 1st is the code smell it detects, the 2nd the expression that represents the rule.
	 * @param file File with several rules
	 * @return ArrayList of all rules in a file, if there's an error reading the file or no rules then it returns null
	 * @throws NullPointerException if the file is null
	 */
	public static ArrayList<Rule> allRules(File file){
		if(file == null)
			throw new NullPointerException("O ficheiro nao pode ser nulo.");
		ArrayList<Rule> rulesList = new ArrayList<>();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNext())
				rulesList.add(new Rule(scanner.nextLine(),scanner.nextLine()));	
			scanner.close();
			
		} catch (FileNotFoundException | NoSuchElementException e ) {
			e.printStackTrace();
			return null;
		}
		if(rulesList.size() == 0)
			return null;
		return rulesList;
	}

	/**
	 * Gets the name of the code smell the rule detects
	 * @return the rule's name
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Gets the expression that represents the rule 
	 * @return the rule's expression
	 */
	public String getExpression() {
		return expression;
  }
}
