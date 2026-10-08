package ma.clinique.teleexpertise.repository;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import ma.clinique.teleexpertise.entity.Utilisateur;

public class UtilisateurRepository {
    private final EntityManager em;

    public UtilisateurRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Utilisateur> findByEmail(String email) {

        return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public void save(Utilisateur utilisateur) {
        em.persist(utilisateur);
    }
}
