package ma.clinique.teleexpertise.service;

import java.util.List;

import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;

public class SpecialisteService {
    private final SpecialisteRepository specialisteRepository ;



    public SpecialisteService(SpecialisteRepository specialisteRepository) {
        this.specialisteRepository = specialisteRepository;
    }



    public List<Specialiste> listSpecialistes (){
        return  specialisteRepository.findAll();
    }    
}
