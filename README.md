# Sécurisation des Micro services avec Keycloak

**_Plan_ :**
<ul>
    <li>Téléchargement de Keycloak et installation - lien : <a href="https://www.keycloak.org/downloads.html">https://www.keycloak.org/downloads.html</a></li>
    <li>Démarrage Keycloak</li>
    <li>Création d'un compte Admin</li>
    <li>Création d'une Realm</li>
    <li>Création d'un client à sécuriser</li>
    <li>Création des utilisateurs</li>
    <li>Création des rôles</li>
    <li>Affectation des rôles aux utilisateurs</li>
    <li>Dans Postman :
        <ul>
            <li>Test l'authentification via password et username</li>
            <li>Analyse les contenus de JWT Access Token</li>
            <li>Teste l'authentification avec Refresh Token</li>
            <li>Teste l'authentification avec client ID et client secret</li>
        </ul>
    </li>
    <li>Sécuriser les Micro services wallet-service, ebank-service</li>
    <li>Teste les endpoints de micro-services ebank-service par HTTP Client</li>
    <li>Teste les endpoints de micro-services wallet-service par GraphQL</li>
</ul>

<p align="center">
  <img src="captures/img_35.png" width="700">
</p>

## Démarrage de Keycloak
<p align="center">
  <img src="captures/img.png" width="600">
</p>

<p align="center">
  <img src="captures/img_1.png" width="600">
</p>

## Connecter à Keycloak
<p align="center">
  <img src="captures/img_2.png" width="600">
</p>

## Créer un nouveau realm
<p align="center">
  <img src="captures/img_3.png" width="600">
</p>

<p align="center">
  <img src="captures/img_4.png" width="600">
</p>

## Créer un nouveau client
<p align="center">
  <img src="captures/img_5.png" width="600">
</p>

<p align="center">
  <img src="captures/img_6.png" width="600">
</p>

<p align="center">
  <img src="captures/img_7.png" width="600">
</p>

## Créer un nouveau Utilisateur
<p align="center">
  <img src="captures/img_8.png" width="600">
</p>

## Attribuer un mot de passe à l'utilisateur
<p align="center">
  <img src="captures/img_9.png" width="600">
</p>

<p align="center">
  <img src="captures/img_10.png" width="600">
</p>

## Créer un nouveau rôle
<p align="center">
  <img src="captures/img_11.png" width="600">
</p>

<p align="center">
  <img src="captures/img_12.png" width="600">
</p>

## Affecter le rôle à l'utilisateur
<p align="center">
  <img src="captures/img_13.png" width="600">
</p>

## Entrer Realm Settings pour récupérer le token
<p align="center">
  <img src="captures/img_14.png" width="600">
</p>

<p align="center">
  <img src="captures/img_15.png" width="600">
</p>

## 1 ere solution : authentification via password et username
<p align="center">
  <img src="captures/img_16.png" width="600">
</p>

<p align="center">
  <img src="captures/img_17.png" width="600">
</p>

## 2 éme solution : authentification via refresh token
<p align="center">
  <img src="captures/img_18.png" width="600">
</p>

## activer client authentification =>l’application qui envoie la requête qui va authentifier et n’est pas l’utilisateur
<p align="center">
  <img src="captures/img_19.png" width="600">
</p>

<p align="center">
  <img src="captures/img_20.png" width="600">
</p>

# Sécuriser Les Micro services wallet-service, ebank-service 

## Ajouter des dépendances dans le pom.xml de chaque micro service

```bash
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
   <groupId>org.keycloak</groupId>
   <artifactId>keycloak-spring-boot-starter</artifactId>
   <version>23.0.0</version>
</dependency>
```
## Configuration de la sécurité de l'application

```java
@Configuration
// Cette classe permet de configurer la sécurité de l'application en utilisant Keycloak.
public class KeycloakAdapterConfig {
@Bean
// Ce bean indique à Keycloak de lire la configuration à partir du fichier application.properties.
public KeycloakSpringBootConfigResolver springBootConfigResolver(){
return new KeycloakSpringBootConfigResolver();    // Cela signifie que la configuration de Keycloak sera basée sur le fichier application.properties plutôt que keycloak.json.
}
}
```

```java
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
        // Cette configuration indique que la gestion des utilisateurs et des rôles est déléguée à Keycloak,
        // ce n'est pas à cette application de gérer directement les utilisateurs et les rôles.
    }

    // Cette méthode configure les autorisations, c'est-à-dire les droits d'accès.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http); // Configure les aspects de sécurité par défaut.
        http.csrf().disable(); // Désactive la protection CSRF car elle n'est pas utile pour une API REST.
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); // Autorise l'accès à la console H2.
        http.headers().frameOptions().disable(); // Désactive les options de frame pour la console H2 pour permettre l'affichage de la console.
        http.authorizeRequests().anyRequest().authenticated();  // Toutes les autres requêtes nécessitent une authentification.
    }
}
```
## Teste les endpoints de micro-services ebank-service par HTTP Client
<p align="center">
  <img src="captures/img_21.png" width="600">
</p>

<p align="center">
  <img src="captures/img_22.png" width="600">
</p>

<p align="center">
  <img src="captures/img_23.png" width="600">
</p>

<p align="center">
  <img src="captures/img_24.png" width="600">
</p>

## Teste les endpoints de micro-services wllet-service par GraphQL
**query => comme select**
**mutation => tout ce qui est post, put, delete…**

<p align="center">
  <img src="captures/img_25.png" width="600">
</p>

<p align="center">
  <img src="captures/img_26.png" width="600">
</p>

<p align="center">
  <img src="captures/img_27.png" width="600">
</p>

**car on envoie la requete vers ebank qui est sécurisé**
<p align="center">
  <img src="captures/img_28.png" width="600">
</p>

## Wallet Frontend Angular Secured
**Authentification via Keycloak**
<p align="center">
  <img src="captures/img_34.png" width="600">
</p>

```bash
npm install --save bootstrap bootstrap-icons
npm install --save keycloak-js keycloak-angular
```
<p align="center">
  <img src="captures/img_29.png" width="600">
</p>

<p align="center">
  <img src="captures/img_30.png" width="600">
</p>

<p align="center">
  <img src="captures/img_31.png" width="600">
</p>

<p align="center">
  <img src="captures/img_32.png" width="600">
</p>

<p align="center">
  <img src="captures/img_33.png" width="600">
</p>

