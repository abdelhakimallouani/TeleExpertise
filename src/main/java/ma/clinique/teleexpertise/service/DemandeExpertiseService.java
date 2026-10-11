package ma.clinique.teleexpertise.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.enums.Priorite;
import ma.clinique.teleexpertise.enums.Specialite;
import ma.clinique.teleexpertise.enums.StatutDemande;
import ma.clinique.teleexpertise.repository.DemandeExpertiseRepository;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.dto.DemandeResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.persistence.EntityExistsException;

public class DemandeExpertiseService {
    private final DemandeExpertiseRepository demandeExpertiseRepository;
    private final SpecialisteRepository specialisteRepository;

    public DemandeExpertiseService(
            DemandeExpertiseRepository demandeExpertiseRepository,
            SpecialisteRepository specialisteRepository) {
        this.demandeExpertiseRepository = demandeExpertiseRepository;
        this.specialisteRepository = specialisteRepository;
    }

    public DemandeExpertise CreateDemande(Priorite priorite,
            Long specialite_id, Long consultation_id, String question) {

        Specialiste specialiste = specialisteRepository.findById(specialite_id);
        if (specialiste == null) {
            throw new NotFoundException("Specialiste introuvable");
        }
        DemandeExpertise demandeExpertise = new DemandeExpertise();
        demandeExpertise.setPriorite(priorite);
        demandeExpertise.setStatut(StatutDemande.EN_ATTENTE);
        demandeExpertise.setDateCreation(LocalDateTime.now());
        demandeExpertise.setSpecialiste(specialiste);
        demandeExpertise.setConsultationId(consultation_id);
        demandeExpertise.setQuestion(question);

        demandeExpertiseRepository.save(demandeExpertise);
        return demandeExpertise;
    }

    public DemandeExpertise Reply(Long id,
            String emailSpecialiste,
            String avis,
            String recommandations) {

        DemandeExpertise demande = demandeExpertiseRepository.findById(id);
        if (demande == null) {
            throw new EntityExistsException("demande  introuvable");

        }

        String emailCheck = demande.getSpecialiste().getUtilisateur().getEmail();
        if (emailCheck == null || !emailCheck.equalsIgnoreCase(emailSpecialiste)) {
            throw new SecurityException("vous ne peuvez pas repondre a cette demande ");
        }
        demande.setAvis(avis);
        demande.setRecommandations(recommandations);
        demande.setStatut(StatutDemande.TERMINEE);
        return demande;
    }

    public List<DemandeResponse> findPendingForSpecialiste(String email) {
        List<DemandeExpertise> demandes = demandeExpertiseRepository.findByStatut(email,
                StatutDemande.EN_ATTENTE);

        return demandes.stream()
                .sorted(Comparator.comparingInt(d -> {
                    if (d.getPriorite() == Priorite.URGENTE)
                        return 0;
                    if (d.getPriorite() == Priorite.NORMALE)
                        return 1;
                    return 2;
                }))
                .map(d -> new DemandeResponse(
                        d.getId(),
                        d.getPriorite(),
                        d.getStatut(),
                        d.getQuestion(),
                        d.getSpecialiste().getId(),
                        d.getConsultationId()))
                .toList();
    }

    public List<DemandeResponse> findByConsultation(Long consultationId) {
        if (consultationId == null) {
            throw new IllegalArgumentException("consultationId doit etre un entier positif");

        }
        List<DemandeExpertise> demandes = demandeExpertiseRepository.findByConsultation(consultationId);
        return demandes.stream().map(d -> new DemandeResponse(
                d.getId(),
                d.getPriorite(),
                d.getStatut(),
                d.getQuestion(),
                d.getSpecialiste().getId(),
                d.getConsultationId()))
                .toList();
    }

}
