package br.com.compass;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.compass.util.EntityManagerUtil;

public class H2ConnectionTest {

    public static void main(String[] args) {
        org.h2.tools.Server webServer = null;
        try {
            // Iniciar o servidor web embutido do H2
            webServer = org.h2.tools.Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
            System.out.println("Servidor web H2 iniciado em: " + webServer.getURL());

            // Obter o EntityManager
            EntityManager em = EntityManagerUtil.getEntityManager();

            // Iniciar transação
            em.getTransaction().begin();

            // Executar uma consulta simples para verificar a conexão
            Query query = em.createNativeQuery("SELECT 1");
            List result = query.getResultList();

            // Verificar o resultado da consulta
            if (!result.isEmpty() && result.get(0).equals(1)) {
                System.out.println("Conexão com o banco de dados H2 foi bem-sucedida!");
            } else {
                System.out.println("Erro ao conectar com o banco de dados H2.");
            }

            // Commit da transação
            em.getTransaction().commit();

            // Fechar o EntityManager e o EntityManagerFactory
            EntityManagerUtil.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (webServer != null) {
                webServer.stop();
                System.out.println("Servidor web H2 encerrado.");
            }
        }
    }
}





