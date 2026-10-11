package ma.clinique.teleexpertise.resource;

import java.util.List;
import java.util.Map;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.dto.CreerDemandeRequest;
import ma.clinique.teleexpertise.dto.DemandeResponse;
import ma.clinique.teleexpertise.entity.DemandeExpertise;
import ma.clinique.teleexpertise.repository.DemandeExpertiseRepository;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.security.UserSecurityContext;
import ma.clinique.teleexpertise.service.DemandeExpertiseService;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;
import ma.clinique.teleexpertise.dto.ReplyRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.BadRequestException;

@Path("/demandes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class DemandeExpertiseResource {

    @Context
    private SecurityContext securityContext;

    // private UserSecurityContext securityContext;

    @POST
    @RolesAllowed({ "MEDECIN" })
    public Response create(CreerDemandeRequest req) {
        if (req == null || req.priorite() == null
                || req.specialisteId() == null || req.specialisteId() <= 0
                || req.consultationId() == null || req.consultationId() <= 0
                || req.question() == null || req.question().isBlank()
                || req.question().length() > 255) {
            throw new BadRequestException("Champs obligatoires invalides (question: 1 a 255 caracteres)");
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (!securityContext.isUserInRole("MEDECIN")) {
                String email = securityContext.getUserPrincipal().getName();
                if (email == null || email.isBlank()) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("error", "Utilisateur non authentifie"))
                            .build();

                }
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("error", "doit etre un medecin pour creer une demande"))
                        .build();
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

    @PUT
    @Path("/{id}/reponse")
    public Response reply(
            @PathParam("id") Long id,
            @HeaderParam("X-Test-Email") String email,
            ReplyRequest request) {

        if (request == null || email == null || email.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of(
                            "error", "JSON et X-Test-Email obligatoires"))
                    .build();
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            DemandeExpertiseService service = new DemandeExpertiseService(
                    new DemandeExpertiseRepository(em),
                    new SpecialisteRepository(em));

            DemandeExpertise demande = service.Reply(
                    id,
                    email,
                    request.avis(),
                    request.recommandations());

            em.getTransaction().commit();

            return Response.ok(java.util.Map.of(
                    "id", demande.getId(),
                    "statut", demande.getStatut().name()))
                    .build();

        } catch (jakarta.persistence.EntityNotFoundException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(java.util.Map.of("error", e.getMessage()))
                    .build();

        } catch (SecurityException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            return Response.status(Response.Status.FORBIDDEN)
                    .entity(java.util.Map.of("error", e.getMessage()))
                    .build();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    @GET
    @RolesAllowed({ "SPECIALISTE", "MEDECIN" })
    public Response getMesDemandes(@QueryParam("statut") String statut,
            @QueryParam("consultationId") Long consultationId) {
        EntityManager em = JPAUtil.getEntityManager();
        DemandeExpertiseRepository repositoryDemande = new DemandeExpertiseRepository(em);
        SpecialisteRepository repositorySpecialiste = new SpecialisteRepository(em);
        DemandeExpertiseService service = new DemandeExpertiseService(repositoryDemande, repositorySpecialiste);

        String email = securityContext.getUserPrincipal().getName();

        try {
            em.getTransaction().begin();
            if (consultationId != null) {
                if (!securityContext.isUserInRole("MEDECIN")) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity(Map.of("error", "Acces reserve au medecin"))
                            .build();
                }

                if (consultationId <= 0) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("error", "id de consultation doit etre positif"))
                            .build();
                }
                List<DemandeResponse> demandes = service.findByConsultation(consultationId);

                return Response.ok(demandes).build();
            }
            if (securityContext.isUserInRole("SPECIALISTE")) {

                if (statut != null && (statut.isBlank() || !statut.equalsIgnoreCase("EN_ATTENTE"))) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("error", "Le statut doit etre EN_ATTENTE"))
                            .build();
                }

                List<DemandeResponse> demandes = service.findPendingForSpecialiste(email);

                return Response.ok(demandes).build();
            }

            // if (demandes.isEmpty()) {
            // return Response.status(Response.Status.NOT_FOUND)
            // .entity(Map.of(
            // "error",
            // "Aucune demande trouve"))
            // .build();
            // }

            em.getTransaction().commit();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                            "error",
                            "consultationId est obligatoire pour le médecin"))
                    .build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } finally {
            em.close();
        }

    }

}
