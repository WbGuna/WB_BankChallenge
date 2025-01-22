package br.com.compass.controller;

import java.sql.SQLException;
import java.util.List;

import br.com.compass.model.Cliente;
import br.com.compass.service.ClienteService;



public class ClienteController {

	private static ClienteController instance;
    private ClienteService clienteService;

    private ClienteController() throws SQLException {
        this.clienteService = ClienteService.getInstance();
    }

    public static synchronized ClienteController getInstance() throws SQLException {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }
    
    public void novoCliente(Cliente cliente) {
        clienteService.novoCliente(cliente);
    }

    public Cliente buscaPorId(Long id) {
        return clienteService.buscaPorId(id);
    }

    public List<Cliente> buscaTodos() {
        return clienteService.buscaTodos();
    }

    public void update(Cliente cliente) {
        clienteService.update(cliente);
    }

    public void delete(Long id) {
        clienteService.delete(id);
    }
}
