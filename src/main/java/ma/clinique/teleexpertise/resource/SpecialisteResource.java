package ma.clinique.teleexpertise.resource;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.service.SpecialisteService;

@Path("/specialiste")
public class SpecialisteResource {
    

    EntityManager em = JPAUtil.getEntityManager();
     SpecialisteRepository specialisteRepository = new SpecialisteRepository(em);
     SpecialisteService specialisteService = new SpecialisteService(specialisteRepository);
  
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public List<Specialiste> allSpecialists() {
         try{
         return specialisteService.listSpecialistes();
         }
         finally{
            em.close();
         }
        
    }
}