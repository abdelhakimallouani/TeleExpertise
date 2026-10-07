package ma.clinique.teleexpertise.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("/api")
public class JaxRsApplication extends ResourceConfig {
    public JaxRsApplication() {
        packages("ma.clinique.teleexpertise.resource", "ma.clinique.teleexpertise.security",
                "ma.clinique.teleexpertise.exception");
        register(RolesAllowedDynamicFeature.class);
    }
}
