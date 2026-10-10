package ma.clinique.teleexpertise.dto;

import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.enums.Priorite;
import ma.clinique.teleexpertise.enums.StatutDemande;

public record DemandeResponse(
        Long id,
        Priorite priorite,
        StatutDemande statut,
        String question,
        Long specialiste,
        Long consultationId
) {}