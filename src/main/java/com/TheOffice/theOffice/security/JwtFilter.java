package com.TheOffice.theOffice.security;

import com.TheOffice.theOffice.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Dépendances injectées pour manipuler les JWT et obtenir les détails des utilisateurs
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // Constructeur pour initialiser le filtre avec les services nécessaires
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Méthode principale pour filtrer les requêtes HTTP avant qu'elles n'atteignent le contrôleur
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // Extraction et validation du token JWT à partir de la requête
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                // Si le token est valide, récupérer le nom d'utilisateur (email ici)
                String username = jwtUtil.getEmailFromToken(jwt);
                // Charger les détails de l'utilisateur à partir du service personnalisé
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // Créer un objet d'authentification avec les détails de l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, // Détails de l'utilisateur
                                null,        // Le mot de passe est null ici car il n'est pas nécessaire pour l'authentification via JWT
                                userDetails.getAuthorities() // Les autorités/permissions de l'utilisateur
                        );
                // Ajouter des détails supplémentaires sur la requête (comme l'adresse IP, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Ajouter l'authentification au contexte de sécurité de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // En cas d'erreur, on affiche un message dans la console (c'est une bonne pratique de gérer les erreurs proprement)
            System.out.println("Cannot set user authentication: " + e);
        }
        // Passer la requête et la réponse au prochain filtre ou au contrôleur
        chain.doFilter(request, response);
    }

    // Méthode pour extraire le JWT depuis l'en-tête de la requête HTTP
    private String parseJwt(HttpServletRequest request) {
        // Récupérer l'en-tête "Authorization" de la requête
        String headerAuth = request.getHeader("Authorization");
        // Vérifier si l'en-tête commence par "Bearer ", auquel cas le token JWT suit
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Extraire le token en supprimant "Bearer "
        }
        return null; // Si le token n'est pas présent, retourner null
    }
}
