package ma.clinique.teleexpertise.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ma.clinique.teleexpertise.enums.Priorite;
import ma.clinique.teleexpertise.enums.StatutDemande;

@Entity
@Table(name = "demande_expertise")
public class DemandeExpertise {
    public DemandeExpertise() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorite priorite;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;
    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private LocalDateTime dateCreation;
    @Column
    private String avis;
    @Column
    private String recommandations;

    @ManyToOne(optional = false)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private Specialiste specialiste;

    @Column(name = "consultation_id", nullable = false)
    private Long consultationId;

    public Long getId() {
        return id;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public String getQuestion() {
        return question;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public String getAvis() {
        return avis;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public Specialiste getSpecialiste() {
        return specialiste;
    }
    public Long getConsultationId() {
    return consultationId;
}

    public void setId(Long id) {
        this.id = id;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public void setSpecialiste(Specialiste specialiste) {
        this.specialiste = specialiste;
    }
    public void setConsultationId(Long consultationId) {
    this.consultationId = consultationId;
}
}
