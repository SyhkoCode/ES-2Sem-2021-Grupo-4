package metrics;

import java.util.Scanner;
import java.util.Stack;

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
		Rule r = new Rule(teste);
		ReadMethod m = new ReadMethod();
		m.addMetric("NOM_class", 4);
		m.addMetric("LOC_class", 4);
		m.addMetric("WMC_class", 4);
		System.out.println(r.smellDetected(m));
	}
}
