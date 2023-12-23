package org.sid.ebankservice.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

// Cette classe combine les annotations @Configuration, @EnableWebSecurity et @EnableGlobalMethodSecurity(prePostEnabled = true).
@KeycloakConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    // Cette méthode configure la stratégie d'authentification.
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        // Elle permet de définir la stratégie d'authentification utilisée par l'application.
    }

    // Cette méthode configure le gestionnaire d'authentification.
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(keycloakAuthenticationProvider());
        // Cette configuration indique que la gestion des utilisateurs et des rôles est déléguée à Keycloak, et apres l'adaptateur qui fait son travail
        // ce n'est pas à cette application de gérer directement les utilisateurs et les rôles.
    }

    // Cette méthode configure les autorisations, c'est-à-dire les droits d'accès.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http); // Configure les aspects de sécurité par défaut.
        http.csrf().disable(); // Puisqu'on utilise une authentification basé sur JWT(une authentification stateless) il faut le desactiver Désactive la protection CSRF car elle n'est pas utile pour une API REST.
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); // Autorise l'accès à la console H2.
        http.headers().frameOptions().disable(); // Désactive les options de frame pour la console H2 pour permettre l'affichage de la console.
        http.authorizeRequests().anyRequest().authenticated();  // Toutes les autres requêtes nécessitent une authentification.
    }
}
