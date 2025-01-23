package br.com.compass.service;

import java.sql.SQLException;

import br.com.compass.model.Cliente;
import br.com.compass.repository.ClienteRepository;



public class ClienteService {

	private static ClienteService instance;
    private ClienteRepository clienteRepository;

    private ClienteService() throws SQLException {
        this.clienteRepository = ClienteRepository.getInstance();
    }

    public static synchronized ClienteService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ClienteService();
        }
        return instance;
    }
    
    public void novoCliente(Cliente cliente) {
        clienteRepository.novoCliente(cliente);
    }

    public Cliente buscaPorId(Long id) {
        return clienteRepository.buscaPorId(id);
    }
    
    public boolean emailExists(String email) {
        return clienteRepository.emailExists(email);
    }

    public boolean cpfExists(String cpf) {
        return clienteRepository.cpfExists(cpf);
    }
}
