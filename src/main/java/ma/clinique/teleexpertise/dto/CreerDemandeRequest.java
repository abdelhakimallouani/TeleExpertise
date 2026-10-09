package ma.clinique.teleexpertise.dto;

import ma.clinique.teleexpertise.enums.Priorite;

public record CreerDemandeRequest(
        Long consultationId,
        Long specialisteId,
        Priorite priorite,
        String question) {
}