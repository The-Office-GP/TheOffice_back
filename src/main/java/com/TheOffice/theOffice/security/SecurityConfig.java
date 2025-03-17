package com.TheOffice.theOffice.security;

import com.TheOffice.theOffice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    // Constructeur de la classe SecurityConfig pour injecter les dépendances nécessaires
    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    // Méthode principale de configuration de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF (pas nécessaire pour une API REST)
                .cors(Customizer.withDefaults()) // Active CORS avec la configuration par défaut
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Désactive la gestion de session (utilisation de JWT pour l'authentification stateless)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll() // Permet l'accès public aux endpoints /auth/** et /test/all
                                .requestMatchers("/user/**", "/companies/**", "/employee/**").hasRole("USER") // Permet l'accès aux utilisateurs avec le rôle USER pour /user/** et /companies/**
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Permet l'accès aux utilisateurs avec le rôle ADMIN pour /admin/**
                                .anyRequest().authenticated() // Requiert une authentification pour toute autre requête
                );
        // Ajoute un filtre JWT avant le filtre par défaut UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); // Retourne la configuration de sécurité
    }

    // Bean pour configurer l'AuthenticationProvider avec un DaoAuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Utilise le CustomUserDetailsService pour récupérer les détails de l'utilisateur
        provider.setPasswordEncoder(new BCryptPasswordEncoder()); // Utilise BCryptPasswordEncoder pour le codage des mots de passe
        return provider;
    }

    // Bean pour obtenir l'AuthenticationManager à partir de la configuration d'authentification
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Récupère le AuthenticationManager configuré
    }

    // Bean pour définir l'encodeur de mot de passe avec BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilise BCrypt pour le hachage des mots de passe
    }

    // Bean pour la configuration CORS (Cross-Origin Resource Sharing)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000")); // Permet l'origine localhost:3000 (utile pour les appels depuis un frontend local en développement)
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")); // Définit les méthodes HTTP autorisées
        configuration.setAllowCredentials(true); // Autorise l'envoi de cookies et d'en-têtes d'autorisation
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Définit les en-têtes autorisés
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique la configuration CORS à toutes les routes
        return source; // Retourne la source de configuration CORS
    }
}
