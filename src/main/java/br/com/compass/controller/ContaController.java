package br.com.compass.controller;

import java.sql.SQLException;
import java.util.List;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
import br.com.compass.model.Transacao;
import br.com.compass.service.ContaService;

public class ContaController {

	private static ContaController instance;
    private ContaService contaService;

    private ContaController() throws SQLException {
        this.contaService = ContaService.getInstance();
    }

    public static synchronized ContaController getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContaController();
        }
        return instance;
    }
    
    public void novaConta(Conta conta) {
        if (!contaService.usuarioExists(conta.getUsuario())) {
            contaService.novaConta(conta);
        } else {
            throw new IllegalArgumentException("Username already exists.");
        }
    }

    public Conta buscaPorId(Long id) {
        return contaService.buscaPorId(id);
    }

    public void update(Conta conta) {
        contaService.update(conta);
    }
  
    public Conta buscaContaPorCliente(Cliente cliente) {
        return contaService.buscaContaPorCliente(cliente);
    }
    
    public Conta buscaPorUsuarioESenha(String usuario, String senha) {
        return contaService.buscaPorUsuarioESenha(usuario, senha);
    }
    
    public void depositar(Long idConta, Double valor) {
        contaService.depositar(idConta, valor);
    }

    public void sacar(Long idConta, Double valor) {
        contaService.sacar(idConta, valor);
    }

    public Double consultarSaldo(Long idConta) {
        return contaService.consultarSaldo(idConta);
    }

    public void transferir(Long idContaOrigem, Long idContaDestino, Double valor) {
        contaService.transferir(idContaOrigem, idContaDestino, valor);
    }

    public List<Transacao> extratoBancario(Long idConta) {
        return contaService.extratoBancario(idConta);
    }
    
    public boolean usuarioExists(String usuario) {
        return contaService.usuarioExists(usuario);
    }
}
