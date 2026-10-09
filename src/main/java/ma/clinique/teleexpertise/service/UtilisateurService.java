package ma.clinique.teleexpertise.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.entity.Utilisateur;
import ma.clinique.teleexpertise.enums.Role;
import ma.clinique.teleexpertise.enums.Specialite;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.repository.UtilisateurRepository;

public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final SpecialisteRepository specialisteRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
            SpecialisteRepository specialisteRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.specialisteRepository = specialisteRepository;
    }

    public void initializeDefaultUsers() {
        String password = "123456";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        if (utilisateurRepository.findByEmail("infirmier@clinique.ma").isEmpty()) {
            Utilisateur infirmier = new Utilisateur();
            infirmier.setNom("Infirmier");
            infirmier.setEmail("infirmier@clinique.ma");
            infirmier.setMotDePasse(hash);
            infirmier.setRole(Role.INFIRMIER);
            utilisateurRepository.save(infirmier);
        }

        if (utilisateurRepository.findByEmail("medecin@clinique.ma").isEmpty()) {

            Utilisateur medecin = new Utilisateur();

            medecin.setNom("Médecin Généraliste");
            medecin.setEmail("medecin@clinique.ma");
            medecin.setMotDePasse(hash);
            medecin.setRole(Role.MEDECIN);

            utilisateurRepository.save(medecin);
        }

        if (utilisateurRepository.findByEmail("cardiologue@clinique.ma").isEmpty()) {

            Utilisateur utilisateur = new Utilisateur();

            utilisateur.setNom("Cardiologue");
            utilisateur.setEmail("cardiologue@clinique.ma");
            utilisateur.setMotDePasse(hash);
            utilisateur.setRole(Role.SPECIALISTE);

            utilisateurRepository.save(utilisateur);

            Specialiste specialiste = new Specialiste(utilisateur, Specialite.CARDIOLOGIE, new BigDecimal("500.00"));

            specialisteRepository.save(specialiste);
        }

        if (utilisateurRepository.findByEmail("dermatologue@clinique.ma").isEmpty()) {

            Utilisateur utilisateur = new Utilisateur();

            utilisateur.setNom("Dermatologue");
            utilisateur.setEmail("dermatologue@clinique.ma");
            utilisateur.setMotDePasse(hash);
            utilisateur.setRole(Role.SPECIALISTE);

            utilisateurRepository.save(utilisateur);

            Specialiste specialiste = new Specialiste(
                    utilisateur,
                    Specialite.DERMATOLOGIE,
                    new BigDecimal("400.00"));

            specialisteRepository.save(specialiste);
        }
    }

    public Optional<Utilisateur> authentief(String email, String password){
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);

        if (utilisateur.isEmpty()) {
            return Optional.empty();
        }
        Utilisateur user = utilisateur.get();

        boolean passwordValid = BCrypt.checkpw(password, user.getMotDePasse());
        return passwordValid ? Optional.of(user) : Optional.empty();
    }


}
