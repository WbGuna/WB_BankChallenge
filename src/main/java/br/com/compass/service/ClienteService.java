package br.com.compass.service;

import java.sql.SQLException;
import java.util.List;

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

    public List<Cliente> buscaTodos() {
        return clienteRepository.buscaTodos();
    }

    public void update(Cliente cliente) {
        clienteRepository.update(cliente);
    }

    public void delete(Long id) {
        clienteRepository.delete(id);
    }
}
