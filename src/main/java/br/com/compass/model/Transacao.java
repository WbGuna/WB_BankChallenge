package br.com.compass.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_transacao")
public class Transacao implements Serializable {
    
	private static final long serialVersionUID = -5378602972035183714L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transacao")
    private Long idTransacao;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    private Conta conta;

    @Column(name = "tipo_transacao", nullable = false)
    private String tipoTransacao;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

	public Transacao() {}
	
	public Transacao(Conta conta, String tipoTransacao, Double valor, LocalDateTime dataHora) {
		this.conta = conta;
		this.tipoTransacao = tipoTransacao;
		this.valor = valor;
		this.dataHora = dataHora;
	}

	public Long getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(Long idTransacao) {
		this.idTransacao = idTransacao;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTransacao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transacao other = (Transacao) obj;
		return Objects.equals(idTransacao, other.idTransacao);
	}

	@Override
	public String toString() {
		return "Transacao [idTransacao=" + idTransacao + ", conta=" + conta + ", tipoTransacao=" + tipoTransacao
				+ ", valor=" + valor + ", dataHora=" + dataHora + "]";
	}
}

