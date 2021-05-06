package classes;

/**
 * Represents all the possible indicators to evaluate the quality of code smell detection 
 * @author Sofia Chaves
 * @version 1.0
 * @since   2021-04-15 
 */
public enum Indicator {
	
	VP("Verdadeiro Positivo"), VN("Verdadeiro Negativo"), FP("Falso Positivo"), FN("Falso Negativo");

	private String name;

	/**
	 * Constructor for enum Indicator that associates name to constant
	 * @param name
	 */
	Indicator(String name) {
		this.name = name;
	}

	/**
	 * Gets the corresponding name to the Indicator.
	 * @return the Indicator's name
	 */
	public String getName() {
		return name;
	}

}
