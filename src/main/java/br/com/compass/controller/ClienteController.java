package br.com.compass.controller;

import java.sql.SQLException;

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
        if (!clienteService.emailExists(cliente.getEmail())) {
            if (!clienteService.cpfExists(cliente.getCpf())) {
                clienteService.novoCliente(cliente);
            } else {
                throw new IllegalArgumentException("CPF already exists.");
            }
        } else {
            throw new IllegalArgumentException("Email already exists.");
        }
    }

    public Cliente buscaPorId(Long id) {
        return clienteService.buscaPorId(id);
    }
    
    public boolean emailExists(String email) {
        return clienteService.emailExists(email);
    }

    public boolean cpfExists(String cpf) {
        return clienteService.cpfExists(cpf);
    }
}
