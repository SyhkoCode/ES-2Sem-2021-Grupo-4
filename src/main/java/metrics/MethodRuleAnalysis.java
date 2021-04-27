package metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MethodRuleAnalysis {
	private HashMap<String, ArrayList<Boolean>> map = new HashMap<>();
	private ArrayList<MethodData> methods;
//	HashMap => <is_God_Class, [true,false,true]>

	public MethodRuleAnalysis(ArrayList<MethodData> methods, ArrayList<Rule> rules) {
		this.methods = methods;
		for (Rule r : rules) {
			map.put(r.getNome(), new ArrayList<Boolean>());
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

	public ArrayList<String[]> getResults() {
		ArrayList<String[]> result = new ArrayList<>();
		ArrayList<String[]> headerList = new ArrayList<>();
		String[] auxHeader = new String[map.keySet().size()];
		int counterHeader = 0;
		for(String s : map.keySet()) {
			auxHeader[counterHeader] = s;
			counterHeader++;
		}
		headerList.add(auxHeader);		
//		System.out.println(Arrays.toString(headerList.get(0)));
		result.add(headerList.get(0));
		for (int i = 0; i < getMethods().size(); i++) {
			String[] aux = new String[2 + map.size()];
			aux[0] = getMethods().get(i).getClassName();
			aux[1] = getMethods().get(i).getMethodName();
			int counter = 2;
			for (String nome : getMap().keySet()) {
				aux[counter] = getMap().get(nome).get(i).toString();
				counter++;
			}
			System.out.println(Arrays.toString(aux));
			result.add(aux);
		}

		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < result.get(i).length; j++) {
//				System.out.println(result.get(i)[j]);
			}
		}
		return result;

	}

	public ArrayList<ArrayList<String[]>> getCodeSmellResults() {
		ArrayList<String> classesAndCodeSmellsFound = new ArrayList<>();
		ArrayList<ArrayList<String[]>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		result.add(new ArrayList<>());
		result.get(0).add(classHeader());
		result.get(1).add(methodHeader());
		for (int i = 0; i < getMethods().size(); i++) {
			String[] aux_classes = new String[getClassSmells().size() + 1];
			String[] aux_methods = new String[getMethodSmells().size() + 2];

			if (!classesAndCodeSmellsFound.contains(getMethods().get(i).getClassName())) {
				aux_classes[0] = getMethods().get(i).getClassName();
				for (int j = 1; j < aux_classes.length; j++) {
					aux_classes[j] = map.get(getClassSmells().get(j - 1)).get(i).toString();

					if (j == aux_classes.length - 1) {
						classesAndCodeSmellsFound.add(getMethods().get(i).getClassName());
						result.get(0).add(aux_classes);
//						System.out.println(Arrays.toString(aux_classes));
					}
				}
			}

			aux_methods[0] = Integer.toString(i + 1);
			aux_methods[1] = getMethods().get(i).getMethodName();
			for (int j = 2; j < aux_methods.length; j++) {
				aux_methods[j] = map.get(getMethodSmells().get(j - 2)).get(i).toString();
				if (j == aux_methods.length - 1) {
					result.get(1).add(aux_methods);
//					System.out.println(Arrays.toString(aux_methods));
				}
			}
		}
		return result;
	}

	private ArrayList<String> getClassSmells() {
		ArrayList<String> aux = new ArrayList<String>();
		for (String s : map.keySet()) {
			if (s.contains("Class"))
				aux.add(s);
		}
		return aux;
	}

	private ArrayList<String> getMethodSmells() {
		ArrayList<String> aux = new ArrayList<String>();
		for (String s : map.keySet()) {
			if (s.contains("Method"))
				aux.add(s);
		}
		return aux;
	}

//	public static void main(String[] args) {
//		String path = "C:\\Users\\sophi\\Desktop\\CoordenacaoIII_metrics.xlsx";
//
//		MethodRuleAnalysis mra = new MethodRuleAnalysis(MethodData.excelToMetricsMap(path),Rule.allRules(new File("testeregras")));
//		mra.getCodeSmellResults();
////		for (int i = 0; i < mra.getMethods().size(); i++) {
////			System.out.println(mra.getMethods().get(i).getMethodName());
////			for (String nome : mra.getMap().keySet()) {
////				System.out.println(mra.getMap().get(nome).get(i));
////			}
////		}
//	}

}
