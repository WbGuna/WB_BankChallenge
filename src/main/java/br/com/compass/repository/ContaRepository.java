package br.com.compass.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
import br.com.compass.model.Transacao;
import br.com.compass.util.EntityManagerUtil;

public class ContaRepository {

	private static ContaRepository instance;
    private final EntityManager entityManager;

    private ContaRepository() {
        this.entityManager = EntityManagerUtil.getEntityManager();
    }

    public static synchronized ContaRepository getInstance() {
        if (instance == null) {
            instance = new ContaRepository();
        }
        return instance;
    }
    
    public void novaConta(Conta conta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(conta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Conta buscaPorId(Long id) {
        return entityManager.find(Conta.class, id);
    }

    public void update(Conta conta) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(conta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
  
    public Conta buscaContaPorCliente(Cliente cliente) {
        try {
            return entityManager.createQuery("SELECT c FROM Conta c WHERE c.cliente = :cliente", Conta.class)
                .setParameter("cliente", cliente)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Conta buscaPorUsuarioESenha(String usuario, String senha) {
        try {
            return entityManager.createQuery("SELECT c FROM Conta c WHERE c.usuario = :usuario AND c.senha = :senha", Conta.class)
                .setParameter("usuario", usuario)
                .setParameter("senha", senha)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void registrarTransacao(Transacao transacao) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(transacao);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Transacao> buscaTransacoesPorConta(Long idConta) {
        return entityManager.createQuery("SELECT t FROM Transacao t WHERE t.conta.idConta = :idConta ORDER BY t.dataHora DESC", Transacao.class)
            .setParameter("idConta", idConta)
            .getResultList();
    }
    
    public boolean usuarioExists(String usuario) {
        Long count = entityManager.createQuery("SELECT COUNT(c) FROM Conta c WHERE c.usuario = :usuario", Long.class)
                                  .setParameter("usuario", usuario)
                                  .getSingleResult();
        return count > 0;
    }
}
