package br.com.compass.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.compass.model.Cliente;
import br.com.compass.model.Conta;
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

    public List<Conta> buscaTodos() {
        return entityManager.createQuery("FROM Conta", Conta.class).getResultList();
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

    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Conta conta = entityManager.find(Conta.class, id);
            if (conta != null) {
                entityManager.remove(conta);
            }
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
}
