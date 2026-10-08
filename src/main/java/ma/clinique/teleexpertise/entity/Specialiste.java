package ma.clinique.teleexpertise.entity;

import java.math.BigDecimal;

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
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarif;

    public Specialiste() {
    }

    public Specialiste(Utilisateur utilisateur, Specialite specialite, BigDecimal tarif) {
        this.utilisateur = utilisateur;
        this.specialite = specialite;
        this.tarif = tarif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public BigDecimal getTarif() {
        return tarif;
    }

    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }
}
