package ma.clinique.teleexpertise.resource;

import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.dto.CreerDemandeRequest;
import ma.clinique.teleexpertise.dto.DemandeResponse;
import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.repository.DemandeExpertiseRepository;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.service.DemandeExpertiseService;
import jakarta.ws.rs.NotFoundException;

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
                throw new NotFoundException("Consultation introuvable");
            }
            DemandeExpertiseService demandeExpertiseService = new DemandeExpertiseService(
                    new DemandeExpertiseRepository(em), new SpecialisteRepository(em));
            DemandeExpertise demande = demandeExpertiseService.CreateDemande(req.priorite(), req.specialisteId(),
                    req.consultationId(), req.question());
            em.getTransaction().commit();
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("id", demande.getId(), "statut", demande.getStatut().name()))
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

    @GET
    public Response getMesDemandes(@QueryParam("statut") String statut,
            @QueryParam("consultationId") Long consultationId) {
        EntityManager em = JPAUtil.getEntityManager();
        DemandeExpertiseRepository repositoryDemande = new DemandeExpertiseRepository(em);
        SpecialisteRepository repositorySpecialiste = new SpecialisteRepository(em);
        DemandeExpertiseService service = new DemandeExpertiseService(repositoryDemande, repositorySpecialiste);

        if (statut != null && (statut.isBlank() || !statut.equalsIgnoreCase("EN_ATTENTE"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Le statut doit etre EN_ATTENTE"))
                    .build();
        }

        if (consultationId != null && consultationId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "consultationId doit etre un entier positif"))
                    .build();

        }

        try {
            em.getTransaction().begin();
            List<DemandeResponse> demades = service.findPendingForSpecialiste();
            if (consultationId != null) {
                demades = service.findByConsultation(consultationId);
            } else {
                demades = service.findPendingForSpecialiste();
            }

            if (demades.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of(
                                "error",
                                "Aucune demande trouvée"))
                        .build();
            }

            em.getTransaction().commit();
            return Response.ok(demades).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } finally {
            em.close();
        }

    }

}
