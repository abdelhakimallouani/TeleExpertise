package ma.clinique.teleexpertise.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestDatabase {

    public static void main(String[] args) {

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            System.out.println("Connexion à la base...");

            emf = Persistence.createEntityManagerFactory("TeleExpertisePU");

            em = emf.createEntityManager();

            System.out.println("Connexion reussie");
            System.out.println("Base de donnees : clinique");

        } catch (Exception e) {

            System.out.println("Erreur de connexion");
            e.printStackTrace();

        } finally {

            if (em != null) {
                em.close();
            }

            if (emf != null) {
                emf.close();
            }
        }
    }
}