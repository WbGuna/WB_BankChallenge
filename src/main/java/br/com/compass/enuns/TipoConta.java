package br.com.compass.enuns;

public enum TipoConta {

	CONTA_CORRENTE("Conta corrente"),
	CONTA_POUPANCA("Conta poupança"),
	CONTA_SALARIO("Conta salário");

	private String tipo = "";

	private TipoConta(String tipo) {
		this.tipo = tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return getTipo();
	}
}
