package metrics;

import java.util.HashMap;

/**
 * Represents Quality test result to measure the Quality of the code smell detection.
 * @author Sofia Chaves
 * @version 1.0
 * @since   2021-04-21 
 */
public class Quality {
	
	private HashMap<String,Indicator> indicatorsPerMethod = new HashMap<>();
	private HashMap<String,Indicator> indicatorsPerClass = new HashMap<>();
	
	/**
	 * Constructor with two HashMap<String, Indicator> as parameters.
	 * @param indicatorsPerMethod This is the HashMap<String, Indicator>, in which String is the Method Name and corresponding Indicator.
	 * @param indicatorsPerClass This is the HashMap<String, Indicator>, in which String is the Class Name and corresponding Indicator. 
	 */
	public Quality(HashMap<String, Indicator> indicatorsPerMethod, HashMap<String, Indicator> indicatorsPerClass) {
		this.indicatorsPerMethod = indicatorsPerMethod;
		this.indicatorsPerClass = indicatorsPerClass;
	}

	/**
	 * Allows to get the indicatorsPerMethod parameter asked for, in which String is the Method Name and corresponding Indicator.
	 * Useful for statistics.
	 * @return indicatorsPerMethod This is the HashMap<String, Indicator> asked for, in which String is the Method Name and corresponding Indicator.
	 */
	public HashMap<String, Indicator> getIndicatorsPerMethod() {
		return indicatorsPerMethod;
	}

	/**
	 * Allows to get the indicatorsPerClass parameter asked for, in which String is the Class Name and corresponding Indicator.
	 * Useful for statistics.
	 * @return indicatorsPerClass This is the HashMap<String, Indicator> asked for, in which String is the Class Name and corresponding Indicator.
	 */
	public HashMap<String, Indicator> getIndicatorsPerClass() {
		return indicatorsPerClass;
	}
	
	/**
	 * Counts given Indicator in the classes.
	 * Useful for statistics.
	 * @param Indicator This is the Indicator required to count.
	 * @return Number of given Indicator in the classes.
	 */
	public long countIndicatorInClasses(Indicator indicator) {
		return indicatorsPerClass.values().stream().filter(v -> v.equals(indicator)).count();		
	}
	
	/**
	 * Counts given Indicator in the methods.
	 * Useful for statistics.
	 * @param Indicator This is the Indicator required to count.
	 * @return Number of given Indicator in the methods.
	 */
	public long countIndicatorInMethods(Indicator indicator) {
		return indicatorsPerMethod.values().stream().filter(v -> v.equals(indicator)).count();		
	}

}
