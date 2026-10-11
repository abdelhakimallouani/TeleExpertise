package ma.clinique.teleexpertise.dto;

import java.math.BigDecimal;

import ma.clinique.teleexpertise.entity.Utilisateur;

public record FindBySpecialite(
        Long specialiteId,
        Long userId,
        String specialiteName,
        BigDecimal tarif) {
}
