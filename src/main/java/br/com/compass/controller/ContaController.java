package br.com.compass.controller;

import java.sql.SQLException;
import java.util.List;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
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
        contaService.novaConta(conta);
    }

    public Conta buscaPorId(Long id) {
        return contaService.buscaPorId(id);
    }

    public List<Conta> buscaTodos() {
        return contaService.buscaTodos();
    }

    public void update(Conta conta) {
        contaService.update(conta);
    }

    public void delete(Long id) {
        contaService.delete(id);
    }
    
    public Conta buscaContaPorCliente(Cliente cliente) {
        return contaService.buscaContaPorCliente(cliente);
    }
    
    public Conta buscaPorUsuarioESenha(String usuario, String senha) {
        return contaService.buscaPorUsuarioESenha(usuario, senha);
    }
}
