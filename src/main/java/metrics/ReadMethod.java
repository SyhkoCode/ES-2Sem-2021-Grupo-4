package metrics;

import java.util.HashMap;

public class ReadMethod {
	private HashMap<String, Integer> map = new HashMap<>();
		
	public HashMap<String, Integer> getMetrics(){
		return map;
	}
	
	public int getMetric(String metric) {
		return map.get(metric);
	}
		
	public void addMetric(String key, int value) {
		map.put(key, value);
	}
}
