package org.sid.ebankservice.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Cette classe permet de configurer la sécurité de l'application en utilisant Keycloak.
public class KeycloakAdapterConfig {
    @Bean
    // Ce bean indique à Keycloak de lire la configuration à partir du fichier application.properties.
    public KeycloakSpringBootConfigResolver springBootConfigResolver(){
        return new KeycloakSpringBootConfigResolver();    // Cela signifie que la configuration de Keycloak sera basée sur le fichier application.properties plutôt que keycloak.json.
    }
}
