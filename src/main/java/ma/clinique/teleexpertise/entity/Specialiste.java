package ma.clinique.teleexpertise.entity;

import jakarta.persistence.*;
import ma.clinique.teleexpertise.enums.Specialite;

@Entity
@Table(name = "specialistes")
public class Specialiste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Specialite specialite;

    public Specialiste() {
    }

    public Specialiste(Utilisateur utilisateur, Specialite specialite) {
        this.utilisateur = utilisateur;
        this.specialite = specialite;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public Specialite getSpecialite() { return specialite; }
    public void setSpecialite(Specialite specialite) { this.specialite = specialite; }
}

