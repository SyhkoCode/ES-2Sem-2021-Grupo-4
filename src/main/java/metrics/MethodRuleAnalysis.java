package metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodRuleAnalysis {
	private HashMap<String, ArrayList<Boolean>> map = new HashMap<>();
	private ArrayList<MethodData> methods;
//	HashMap => <is_God_Class, [true,false,true]>

	public MethodRuleAnalysis(ArrayList<MethodData> methods, ArrayList<Rule> rules) {
		this.methods = methods;
		for (Rule r : rules) {	
			map.put(r.getNome(),new ArrayList<Boolean>());
			for (MethodData md : methods) {
				map.get(r.getNome()).add(r.smellDetected(md));
			}
		}
	}

	public HashMap<String, ArrayList<Boolean>> getMap() {
		return map;
	}

	public ArrayList<MethodData> getMethods() {
		return methods;
	}
	
	
	public static void main(String[] args) {
		String path = "C:\\Users\\Pedro Pinheiro\\Music\\jasml_0.10_metrics.xlsx";
		
		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(path),Rule.allRules(new File("C:\\Users\\Pedro Pinheiro\\Desktop\\rules.txt")));
		for(int i=0;i<mra.getMethods().size();i++) {
			System.out.println(mra.getMethods().get(i).getMethodName());
			for(String nome : mra.getMap().keySet()) {
				System.out.println(mra.getMap().get(nome).get(i));			
			}
		}
	}

}
