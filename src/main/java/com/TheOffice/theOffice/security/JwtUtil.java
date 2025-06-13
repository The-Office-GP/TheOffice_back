package com.TheOffice.theOffice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    // Lecture des valeurs de configuration depuis application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;

    // Méthode appelée après la construction de l'objet pour initialiser la clé secrète
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Méthode pour générer un token JWT pour un utilisateur donné, basé sur son email
    public String generateToken(String email) {
        // Construction du JWT avec le sujet (email), la date d'émission, la date d'expiration et la signature
        return Jwts.builder()
                .setSubject(email) // Le sujet du token est l'email de l'utilisateur
                .setIssuedAt(new Date()) // Date de création du token
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Définition de l'expiration à partir de la date actuelle
                .signWith(key, SignatureAlgorithm.HS256) // Signature du token avec la clé
                .compact(); // Génération du token compacté
    }

    // Méthode pour extraire l'email (sujet) à partir du token JWT
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build() // Configuration du parser avec la clé secrète
                .parseClaimsJws(token) // Parsing du token JWT
                .getBody() // Accès au corps du JWT
                .getSubject(); // Retourne l'email du sujet du token
    }

    // Méthode pour valider le token JWT
    public boolean validateJwtToken(String token) {
        try {
            // Tente de parser et valider le JWT
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // Si le parsing réussit, le token est valide
        } catch (SecurityException e) {
            // Si la signature est invalide
            throw new SecurityException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            // Si le token est malformé
            throw new MalformedJwtException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            // Si le token a expiré
            throw new ExpiredJwtException(null, null, "JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Si le type de token n'est pas supporté
            throw new UnsupportedJwtException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Si le contenu du token est vide ou invalide
            throw new IllegalArgumentException("JWT claims string is empty: " + e.getMessage());
        }
    }
}
