package ma.clinique.teleexpertise.config;

import jakarta.persistence.EntityManager;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.entity.Utilisateur;
import ma.clinique.teleexpertise.enums.Role;
import ma.clinique.teleexpertise.enums.Specialite;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.repository.UtilisateurRepository;
import ma.clinique.teleexpertise.service.UtilisateurService;

import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;

public class DataInitializer {

    public static void initialize() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            UtilisateurRepository utilisateurRepository = new UtilisateurRepository(em);
            SpecialisteRepository specialisteRepository = new SpecialisteRepository(em);

            UtilisateurService utilisateurService = new UtilisateurService(utilisateurRepository,
                    specialisteRepository);

            utilisateurService.initializeDefaultUsers();

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {
            em.close();
        }
    }
}