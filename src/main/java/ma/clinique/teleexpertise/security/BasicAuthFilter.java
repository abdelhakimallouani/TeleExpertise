package ma.clinique.teleexpertise.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import jakarta.annotation.Priority;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import ma.clinique.teleexpertise.config.JPAUtil;
import ma.clinique.teleexpertise.entity.Utilisateur;
import ma.clinique.teleexpertise.repository.SpecialisteRepository;
import ma.clinique.teleexpertise.repository.UtilisateurRepository;
import ma.clinique.teleexpertise.service.UtilisateurService;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        String authorization = requestContext.getHeaderString(
                HttpHeaders.AUTHORIZATION);

        if (authorization == null
                || !authorization.startsWith("Basic ")) {
            abortUnauthorized(requestContext);
            return;
        }

        try {
            String encoded = authorization.substring(6).trim();

            String credentials = new String(
                    Base64.getDecoder().decode(encoded),
                    StandardCharsets.UTF_8);

            String[] parts = credentials.split(":", 2);

            if (parts.length != 2) {
                abortUnauthorized(requestContext);
                return;
            }

            String email = parts[0];
            String password = parts[1];

            EntityManager em = JPAUtil.getEntityManager();

            try {
                UtilisateurRepository repository = new UtilisateurRepository(em);
                SpecialisteRepository specialisteRepository = new SpecialisteRepository(em);

                UtilisateurService service = new UtilisateurService(repository, specialisteRepository);

                Utilisateur utilisateur = service
                        .authentief(email, password)
                        .orElse(null);

                if (utilisateur == null) {
                    abortUnauthorized(requestContext);
                    return;
                }

                UserSecurityContext userContext = new UserSecurityContext(
                        utilisateur,
                        requestContext.getSecurityContext());

                requestContext.setSecurityContext(userContext);

            } finally {
                em.close();
            }

        } catch (IllegalArgumentException e) {
            abortUnauthorized(requestContext);
        }
    }

    private void abortUnauthorized(
            ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(
                                HttpHeaders.WWW_AUTHENTICATE,
                                "Basic realm=\"TeleExpertise\"")
                        .entity(Map.of("error", "Identifiants invalides ou authentification requise"))
                        .build());
    }
}
