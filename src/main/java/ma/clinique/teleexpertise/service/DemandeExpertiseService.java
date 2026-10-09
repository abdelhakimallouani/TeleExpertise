package ma.clinique.teleexpertise.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.enums.Priorite;
import ma.clinique.teleexpertise.enums.Specialite;
import ma.clinique.teleexpertise.enums.StatutDemande;
import ma.clinique.teleexpertise.repository.DemandeExpertiseRepository;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;

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
            Long specialite_id, Long consultation_id , String question)  {

        Specialiste specialiste = specialisteRepository.findById(specialite_id);
        if (specialiste == null) {
            throw new jakarta.ws.rs.NotFoundException("Specialiste introuvable");
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

}
