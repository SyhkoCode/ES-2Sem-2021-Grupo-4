package metrics;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodData {
	private HashMap<String, Integer> map = new HashMap<>();
	private String packageName;
	private String className;
	private String methodName;

	
	public MethodData(Object[] array) {
		/*this.packageName = array[1].toString();
		this.className = array[2].toString();
		this.methodName =  array[3].toString();
		String[] metrics = {"NOM_class","LOC_class","WMC_class","LOC_method","CYCLO_method"};
		for(int i=4,j=0;i<array.length;i++,j++) {
			map.put(metrics[j], Integer.parseInt(array[i].toString()));
		}*/
		
		// Alternativa q usa as 2 funções a seguir
		this(array[1].toString(), array[2].toString(), array[3].toString());
		String[] metrics = {"NOM_class","LOC_class","WMC_class","LOC_method","CYCLO_method"};
		for(int i=4,j=0;i<array.length;i++,j++) {
			addMetric(metrics[j], Integer.parseInt(array[i].toString()));
		}
	}
	
	// Só para facilitar criação de testes
	
	public MethodData(String packageName, String className, String methodName) {
		this.packageName = packageName;
		this.className = className;
		this.methodName =  methodName;
	}

	public void addMetric(String key, int value) {
		map.put(key, value);
	}
	
	//
	
	
	public static ArrayList<MethodData> excelToMetricsMap(String filename){
		ArrayList<MethodData> methods = new ArrayList<>();
		for( Object[] o : new ExcelDealer(filename, true).getAllRows() )
			methods.add(new MethodData(o));
		
		return methods;
	}

	public HashMap<String, Integer> getMetrics(){
		return map;
	}

	public int getMetric(String metric) {
		return map.get(metric);
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public String toString() {
		return "MethodData [map=" + map + ", packageName=" + packageName + ", className=" + className + ", methodName="
				+ methodName + "]";
	}

	public static void main(String[] args) {
		String path = "C:\\Users\\tiago\\OneDrive\\Ambiente de Trabalho\\CoordenacaoIII_metrics.xlsx";
//		System.out.println(MethodData.excelToMetricsMap(path));
	}


		
}
