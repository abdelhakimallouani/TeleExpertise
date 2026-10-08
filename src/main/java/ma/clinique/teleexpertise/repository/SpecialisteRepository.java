package ma.clinique.teleexpertise.repository;

import jakarta.persistence.EntityManager;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.enums.Specialite;
import java.util.List;

public class SpecialisteRepository {
    private final EntityManager entityManager;

    public SpecialisteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Specialiste findById(Long id) {
        return entityManager.find(Specialiste.class, id);
    }

    public List<Specialiste> findAll() {
        return entityManager.createQuery(
                "SELECT s FROM Specialiste s"  ,
                Specialiste.class).getResultList();
    }
    // public List<Specialiste> findAll() {
    //     return entityManager.createQuery(
    //             "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur"  ,
    //             Specialiste.class).getResultList();
    // }

    public void save(Specialiste specialiste) {
        entityManager.persist(specialiste);
    }
}
