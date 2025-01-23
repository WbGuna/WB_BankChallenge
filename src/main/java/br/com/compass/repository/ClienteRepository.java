package br.com.compass.repository;

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
    
    public boolean emailExists(String email) {
        Long count = entityManager.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class)
                                  .setParameter("email", email)
                                  .getSingleResult();
        return count > 0;
    }

    public boolean cpfExists(String cpf) {
        Long count = entityManager.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.cpf = :cpf", Long.class)
                                  .setParameter("cpf", cpf)
                                  .getSingleResult();
        return count > 0;
    }
}
