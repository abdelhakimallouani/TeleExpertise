package ma.clinique.teleexpertise.security;

import java.security.Principal;

import jakarta.ws.rs.core.SecurityContext;
import ma.clinique.teleexpertise.entity.Utilisateur;

public class UserSecurityContext implements SecurityContext {

    private final Utilisateur utilisateur;
    private final SecurityContext originalContext;

    public UserSecurityContext(
            Utilisateur utilisateur,
            SecurityContext originalContext
    ) {
        this.utilisateur = utilisateur;
        this.originalContext = originalContext;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> utilisateur.getEmail();
    }

    @Override
    public boolean isUserInRole(String role) {
        return utilisateur.getRole() != null
                && utilisateur.getRole().name().equals(role);
    }

    @Override
    public boolean isSecure() {
        return originalContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
