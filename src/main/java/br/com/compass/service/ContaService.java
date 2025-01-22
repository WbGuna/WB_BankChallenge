package br.com.compass.service;

import java.sql.SQLException;
import java.util.List;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
import br.com.compass.repository.ContaRepository;

public class ContaService {

	private static ContaService instance;
    private ContaRepository contaRepository;

    private ContaService() throws SQLException {
        this.contaRepository = ContaRepository.getInstance();
    }

    public static synchronized ContaService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContaService();
        }
        return instance;
    }
    
    public void novaConta(Conta conta) {
        contaRepository.novaConta(conta);
    }

    public Conta buscaPorId(Long id) {
        return contaRepository.buscaPorId(id);
    }

    public List<Conta> buscaTodos() {
        return contaRepository.buscaTodos();
    }

    public void update(Conta conta) {
        contaRepository.update(conta);
    }

    public void delete(Long id) {
        contaRepository.delete(id);
    }
    
    public Conta buscaContaPorCliente(Cliente cliente) {
        return contaRepository.buscaContaPorCliente(cliente);
    }
    
    public Conta buscaPorUsuarioESenha(String usuario, String senha) {
        return contaRepository.buscaPorUsuarioESenha(usuario, senha);
    }
}
