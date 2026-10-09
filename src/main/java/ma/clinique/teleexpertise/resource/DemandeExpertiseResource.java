package ma.clinique.teleexpertise.resource;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.dto.CreerDemandeRequest;
import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.repository.DemandeExpertiseRepository;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.service.DemandeExpertiseService;

@Path("/demandes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class DemandeExpertiseResource {


    @POST
    public Response create(CreerDemandeRequest req) {
        if (req == null || req.priorite() == null
                || req.specialisteId() == null || req.specialisteId() <= 0
                || req.consultationId() == null || req.consultationId() <= 0
                || req.question() == null || req.question().isBlank()
                || req.question().length() > 255) {
            throw new jakarta.ws.rs.BadRequestException("Champs obligatoires invalides (question: 1 a 255 caracteres)");
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Number count = (Number) em.createNativeQuery(
                    "SELECT COUNT(*) FROM consultation WHERE id = :id")
                    .setParameter("id", req.consultationId()).getSingleResult();
            if (count.longValue() == 0) {
                throw new jakarta.ws.rs.NotFoundException("Consultation introuvable");
            }
            DemandeExpertiseService demandeExpertiseService = new DemandeExpertiseService(
                    new DemandeExpertiseRepository(em), new SpecialisteRepository(em));
            DemandeExpertise demande = demandeExpertiseService.CreateDemande(req.priorite(), req.specialisteId(),
                    req.consultationId(), req.question());
            em.getTransaction().commit();
            return Response.status(Response.Status.CREATED)
                    .entity(java.util.Map.of("id", demande.getId(), "statut", demande.getStatut().name()))
                    .build();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }

    }

}
