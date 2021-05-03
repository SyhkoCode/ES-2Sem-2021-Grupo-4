package metrics;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodData {
	private final static String[] METRICS = new String[] { "NOM_class", "LOC_class", "WMC_class", "LOC_method",
			"CYCLO_method" };
	private HashMap<String, Integer> map = new HashMap<>();
	private String packageName, className, methodName;
	
	/**
	 * MethodData constructor
	 * <p>
	 * Creates a MethodData object given the methods' package, class and name.
	 * 
	 * @param packageName the name of the package the method belongs to.
	 * @param className the name of the class the method belongs to.
	 * @param methodName the methods' name.
	 */
	public MethodData(String packageName, String className, String methodName) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
	}

	
	/**
	 * MethodData constructor
	 * <p>
	 * Creates a map of the MethodData object metrics and respective value
	 * 
	 * @param array entire row of a metrics Excel file
	 * @param metrics array with the name of all existent metrics 
	 */
	public MethodData(Object[] array, String[] metrics) {
		this(array[1].toString(), array[2].toString(), array[3].toString());
		for (int i = 4, j = 0; i < array.length; i++, j++) {
			map.put(metrics[j], Integer.parseInt(array[i].toString()));
		}
	}

	
	/**
	 * MethodData constructor
	 * <p>
	 *  Creates a MethodData object given the methods' package, class and name.
	 * 
	 * @param array array containing the methods' package, class and name
	 */
	public MethodData(Object[] array) {
		this(array[1].toString(), array[2].toString(), array[3].toString());
	}

	/**
	 * Gets the map of metrics and respective values of the given MethodData object
	 * 
	 * @return the map of metrics and respective values of the given MethodData object
	 */
	public HashMap<String, Integer> getMetrics() {
		return map;
	}

	
	/**
	 * 
	 * Gets the requested metric value 
	 * 
	 * @param metric name of the desired metric
	 * @return the requested metric value 
	 */
	public int getMetric(String metric) {
		return map.get(metric);
	}

	/**
	 * Gets the name of the methods' package
	 * 
	 * @return the name of the methods' package
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Gets the name of the methods' class
	 * 
	 * @return the name of the methods' class
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the name of the method 
	 * 
	 * @return the name of the method
	 */
	public String getMethodName() {
		return methodName;
	}

	@Override
	public String toString() {
		return "MethodData [map=" + map + ", packageName=" + packageName + ", className=" + className + ", methodName="
				+ methodName + "]";
	}

	/**
	 * Creates an ArrayList of MethodData objects based on the given Excel file
	 * 
	 * @param path of the Excel file
	 * @return an ArrayList of MethodData objects based on the given Excel file path
	 * @throws Exception
	 */
	public static ArrayList<MethodData> excelToMetricsMap(String path) throws Exception {
		ArrayList<MethodData> methods = new ArrayList<>();
		for (Object[] o : ExcelDealer.getAllRows(path, 0))
			methods.add(new MethodData(o, METRICS));

		return methods;
	}

}
