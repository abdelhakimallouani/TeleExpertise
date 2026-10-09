package ma.clinique.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.client.Entity;
import ma.clinique.teleexpertise.entity.DemandeExpertise;

public class DemandeExpertiseRepository {
    private final EntityManager em;

    public DemandeExpertiseRepository(EntityManager entityManager) {
        this.em = entityManager;
    }
    public void  save(DemandeExpertise demmande){
        
        em.persist(demmande);

    }


}
