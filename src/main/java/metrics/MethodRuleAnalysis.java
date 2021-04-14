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
//		
//		for(int i=0;i<getMethods().size();i++) {
//			System.out.println(getMethods().get(i).getMethodName());
//			for(String nome : getMap().keySet()) {
//				System.out.println(getMap().get(nome).get(i));			
//			}
//		}
	}

	public HashMap<String, ArrayList<Boolean>> getMap() {
		return map;
	}

	public ArrayList<MethodData> getMethods() {
		return methods;
	}
	
	public ArrayList<String[]> getResults(){
		ArrayList<String[]> result = new ArrayList<>();
		for(int i=0;i<getMethods().size();i++) {
			String[] aux = new String[4];
			aux[0] = getMethods().get(i).getClassName();
			aux[1] = getMethods().get(i).getMethodName();

//			System.out.println(aux[0] + " AQUI");
			int counter = 2;
			for(String nome : getMap().keySet()) {
				aux[counter] = getMap().get(nome).get(i).toString();
				counter++;
//				System.out.println(getMap().get(nome).get(i));			
			}
			result.add(aux);
		}
		
		for(int i=0;i<result.size();i++) {
			for(int j=0;j<result.get(i).length;j++) {
				System.out.println(result.get(i)[j]);
			}
		}
		
		return result;
		
	}
	
	
	public static void main(String[] args) {
		String path = "C:\\Users\\tiago\\OneDrive\\Ambiente de Trabalho\\CoordenacaoIII_metrics.xlsx";
		
		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(path),Rule.allRules(new File("testeregras")));
		for(int i=0;i<mra.getMethods().size();i++) {
			System.out.println(mra.getMethods().get(i).getMethodName());
			for(String nome : mra.getMap().keySet()) {
				System.out.println(mra.getMap().get(nome).get(i));			
			}
		}
	}

}
