package ma.clinique.teleexpertise.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.client.Entity;
import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.enums.StatutDemande;

public class DemandeExpertiseRepository {
    private final EntityManager em;

    public DemandeExpertiseRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    public void save(DemandeExpertise demmande) {

        em.persist(demmande);

    }

    public List<DemandeExpertise> findByStatut(StatutDemande statut) {
        return em.createQuery("SELECT d FROM DemandeExpertise d WHERE d.statut = :statut", DemandeExpertise.class)
                .setParameter("statut", statut)
                .getResultList();
    }

    public List<DemandeExpertise> findByConsultation(Long consultationId) {
        return em
                .createQuery("SELECT d FROM DemandeExpertise d" + " WHERE d.consultationId= :consultationId",
                        DemandeExpertise.class)
                .setParameter("consultationId", consultationId)
                .getResultList();
    }

}
