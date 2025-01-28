package br.com.compass.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
import br.com.compass.model.Transacao;
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

    public void update(Conta conta) {
        contaRepository.update(conta);
    }
   
    public Conta buscaContaPorCliente(Cliente cliente) {
        return contaRepository.buscaContaPorCliente(cliente);
    }
    
    public Conta buscaPorUsuarioESenha(String usuario, String senha) {
        return contaRepository.buscaPorUsuarioESenha(usuario, senha);
    }
    
    public void depositar(Long idConta, Double valor) {
        Conta conta = buscaPorId(idConta);
        if (conta != null) {
            conta.setSaldo(conta.getSaldo() + valor);
            update(conta);

            Transacao transacao = new Transacao(conta, "Deposit", valor, LocalDateTime.now());
            contaRepository.registrarTransacao(transacao);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Account not found.");
        }
    }

    public void sacar(Long idConta, Double valor) {
        Conta conta = buscaPorId(idConta);
        if (conta != null) {
            if (conta.getSaldo() >= valor) {
                conta.setSaldo(conta.getSaldo() - valor);
                update(conta);

                Transacao transacao = new Transacao(conta, "Withdraw", valor, LocalDateTime.now());
                contaRepository.registrarTransacao(transacao);
                System.out.println("Withdraw successful!");
            } else {
                System.out.println("Account not found.");
            }
        } else {
            System.out.println("Non-existent account.");
        }
    }

    public void transferir(Long idContaOrigem, Long idContaDestino, Double valor) {
        Conta contaOrigem = buscaPorId(idContaOrigem);
        Conta contaDestino = buscaPorId(idContaDestino);
        if (contaOrigem != null && contaDestino != null) {
            if (contaOrigem.getSaldo() >= valor) {
                contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
                contaDestino.setSaldo(contaDestino.getSaldo() + valor);
                update(contaOrigem);
                update(contaDestino);

                Transacao transacaoOrigem = new Transacao(contaOrigem, "Transfer Sent to Account: " + idContaDestino.toString(), valor, LocalDateTime.now());
                Transacao transacaoDestino = new Transacao(contaDestino, "Transfer Received from Account: " + idContaOrigem.toString(), valor, LocalDateTime.now());
                contaRepository.registrarTransacao(transacaoOrigem);
                contaRepository.registrarTransacao(transacaoDestino);
                System.out.println("Transfer successful!");
            } else {
                System.out.println("Account not found.");
            }
        } else {
            System.out.println("One or both accounts were not found.");
        }
    }

    public Double consultarSaldo(Long idConta) {
        Conta conta = buscaPorId(idConta);
        return (conta != null) ? conta.getSaldo() : null;
    }
    
    public List<Transacao> extratoBancario(Long idConta) {
        return contaRepository.buscaTransacoesPorConta(idConta);
    }  
    
    public boolean usuarioExists(String usuario) {
        return contaRepository.usuarioExists(usuario);
    }
}
