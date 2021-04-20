package metrics;

public enum Indicator {
	
	VP("Verdadeiro Positivo"), VN("Verdadeiro Negativo"), FP("Falso Positivo"), FN("Falso Negativo");

	private String name;

	Indicator(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
