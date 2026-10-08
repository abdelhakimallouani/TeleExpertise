package ma.clinique.teleexpertise.service;

import java.util.List;

import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.enums.Specialite;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;

public class SpecialisteService {
    private final SpecialisteRepository specialisteRepository;

    public SpecialisteService(SpecialisteRepository specialisteRepository) {
        this.specialisteRepository = specialisteRepository;
    }

    // public List<Specialiste> listSpecialistes() {
    //     return specialisteRepository.findAll();
    // }

    public List<Specialiste> findBySpecialite(Specialite specialite) {
        return specialisteRepository.findAll().stream().filter(s -> s.getSpecialite().equals(specialite))
                .sorted((s1, s2) -> s1.getTarif().compareTo(s2.getTarif())).toList();
    }
}
