package metrics;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodData {
	private final static String[] METRICS = new String[] { "NOM_class", "LOC_class", "WMC_class", "LOC_method",
			"CYCLO_method" };
	private HashMap<String, Integer> map = new HashMap<>();
	private String packageName, className, methodName;
	
	public MethodData(String packageName, String className, String methodName) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
	}

	public MethodData(Object[] array, String[] metrics) {
		this(array[1].toString(), array[2].toString(), array[3].toString());
		for (int i = 4, j = 0; i < array.length; i++, j++) {
			map.put(metrics[j], Integer.parseInt(array[i].toString()));
		}
	}

	public MethodData(Object[] array) {
		this(array[1].toString(), array[2].toString(), array[3].toString());
	}

	public void addMetric(String key, int value) {
		map.put(key, value);
	}

	public HashMap<String, Integer> getMetrics() {
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

	public static ArrayList<MethodData> excelToMetricsMap(String path) throws Exception {
		ArrayList<MethodData> methods = new ArrayList<>();
		for (Object[] o : ExcelDealer.getAllRows(path, 0))
			methods.add(new MethodData(o, METRICS));

		return methods;
	}

}
