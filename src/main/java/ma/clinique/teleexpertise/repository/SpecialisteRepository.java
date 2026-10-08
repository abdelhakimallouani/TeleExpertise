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
                "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur ORDER BY s.id",
                Specialiste.class).getResultList();
    }

    public List<Specialiste> findBySpecialite(Specialite specialite) {
        return entityManager.createQuery(
                "SELECT s FROM Specialiste s JOIN FETCH s.utilisateur "
                        + "WHERE s.specialite = :specialite ORDER BY s.id",
                Specialiste.class)
                .setParameter("specialite", specialite)
                .getResultList();
    }

    public void save(Specialiste specialiste) {
        entityManager.persist(specialiste);
    }
}
