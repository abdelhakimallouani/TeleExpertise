package ma.clinique.teleexpertise.resource;

import java.util.List;

import org.hibernate.annotations.QueryCacheLayout;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ma.clinique.teleexpertise.enums.Specialite;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.entity.Specialiste;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.service.SpecialisteService;

@Path("/specialiste")
public class SpecialisteResource {

    EntityManager em = JPAUtil.getEntityManager();
    SpecialisteRepository specialisteRepository = new SpecialisteRepository(em);
    SpecialisteService specialisteService = new SpecialisteService(specialisteRepository);

    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public List<Specialiste> allSpecialists() {
    // try {
    // return specialisteService.listSpecialistes();
    // } finally {
    // em.close();
    // }

    // }

    @GET
    public Response triSpecialistes(@QueryParam("specialite") String specialite) {
        if (specialite == null || specialite.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("le specialite est obligatoire")
                    .build();
        }
        try {
            Specialite specialiteEnum = Specialite.valueOf(specialite.toUpperCase());
            List<Specialiste> specialistes = specialisteService.findBySpecialite(specialiteEnum);
            return Response.ok(specialistes).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("specialite inconnue  " + specialite)
                    .build();
        } finally {
            em.close();
        }

    }

}