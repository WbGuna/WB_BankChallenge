package br.com.compass.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {

    private static final String PERSISTENCE_UNIT_NAME = "db_BankChallenge";
    private static EntityManagerFactory factory;
    private static EntityManager entityManager;

    private EntityManagerUtil() {}
   
    private static void initialize() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            initialize();
        }
        return entityManager;
    }

    public static void close() {
        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}

