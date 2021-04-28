package metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

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
		String[] auxCodeSmells = new String[map.keySet().size()];
		int counterHeader = 0;
		for (String s : map.keySet()) {
			auxCodeSmells[counterHeader] = s;
			counterHeader++;
		}
		String[] auxHeader = Stream
				.concat(Arrays.stream(new String[] { "MethodID","package", "class", "method" }), Arrays.stream(auxCodeSmells))
				.toArray(String[]::new);
		headerList.add(auxHeader);
		result.add(headerList.get(0));
		for (int i = 0; i < getMethods().size(); i++) {
			String[] aux = new String[3 + map.size()];
			aux[0] = getMethods().get(i).getPackageName();
			aux[1] = getMethods().get(i).getClassName();
			aux[2] = getMethods().get(i).getMethodName();
			int counter = 3;
			for (String nome : getMap().keySet()) {
				aux[counter] = getMap().get(nome).get(i).toString();
				counter++;
			}
			result.add(aux);
		}

		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < result.get(i).length; j++) {
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
					}
				}
			}

			aux_methods[0] = Integer.toString(i + 1);
			aux_methods[1] = getMethods().get(i).getMethodName();
			for (int j = 2; j < aux_methods.length; j++) {
				aux_methods[j] = map.get(getMethodSmells().get(j - 2)).get(i).toString();
				if (j == aux_methods.length - 1) {
					result.get(1).add(aux_methods);
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

	private String[] classHeader() {
		String[] header = new String[1 + getClassSmells().size()];
		header[0] = "Class";
		for (int i = 1; i < header.length; i++) {
			header[i] = getClassSmells().get(i - 1);
		}
		return header;
	}

	private String[] methodHeader() {
		String[] header = new String[2 + getMethodSmells().size()];
		header[0] = "ID";
		header[1] = "Method";
		for (int i = 2; i < header.length; i++) {
			header[i] = getMethodSmells().get(i - 2);
		}
		return header;
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
