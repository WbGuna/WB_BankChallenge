package br.com.compass.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.compass.model.Cliente;
import br.com.compass.util.EntityManagerUtil;


public class ClienteRepository {

	private static ClienteRepository instance;
    private final EntityManager entityManager;

    private ClienteRepository() {
        this.entityManager = EntityManagerUtil.getEntityManager();
    }

    public static synchronized ClienteRepository getInstance() {
        if (instance == null) {
            instance = new ClienteRepository();
        }
        return instance;
    }
    
    public void novoCliente(Cliente cliente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Cliente buscaPorId(Long id) {
        return entityManager.find(Cliente.class, id);
    }

    public List<Cliente> buscaTodos() {
        return entityManager.createQuery("FROM Cliente", Cliente.class).getResultList();
    }

    public void update(Cliente cliente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(cliente);
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
            Cliente cliente = entityManager.find(Cliente.class, id);
            if (cliente != null) {
                entityManager.remove(cliente);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
