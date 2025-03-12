package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.UserDao;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

// Annotation pour désigner cette classe comme un contrôleur REST pour l'authentification des utilisateurs
@RestController
@RequestMapping("/auth")
public class AuthController {
    // Dépendances nécessaires : gestion de l'authentification, accès aux utilisateurs, codage des mots de passe, génération de JWT
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    // Constructeur pour injecter les dépendances nécessaires
    public AuthController(AuthenticationManager authenticationManager, UserDao userDao, PasswordEncoder encoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    // Méthode pour enregistrer un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Vérifie si un utilisateur avec le même email existe déjà dans la base de données
        boolean alreadyExists = userDao.existsByEmail(user.getEmail());
        if (alreadyExists) {
            // Si l'email est déjà utilisé, retourne une réponse avec une erreur
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Création d'un nouvel utilisateur avec l'email, le nom d'utilisateur et le mot de passe encodé
        User newUser = new User(
                null,  // ID généré automatiquement par la base de données
                user.getEmail(),  // Email de l'utilisateur
                user.getUsername(),  // Nom d'utilisateur
                encoder.encode(user.getPassword()),  // Mot de passe encodé avec PasswordEncoder
                user.getRole() != null ? user.getRole() : "USER",  // Rôle de l'utilisateur (par défaut "USER" si aucun rôle n'est fourni)
                BigDecimal.ZERO  // Solde initial de l'utilisateur (par défaut 0)
        );

        // Enregistrement de l'utilisateur dans la base de données et vérification si l'enregistrement a réussi
        boolean isUserSaved = userDao.save(newUser);
        // Retourne une réponse en fonction du succès de l'enregistrement
        return isUserSaved ? ResponseEntity.ok("User registered successfully!") : ResponseEntity.badRequest().body("Error: User registration failed!");
    }

    // Méthode pour authentifier un utilisateur et générer un token JWT
    @PostMapping("/login")
    public String authenticateUser(@RequestBody User user) {
        // Authentification de l'utilisateur avec l'email et le mot de passe fournis
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),  // Email de l'utilisateur
                        user.getPassword()  // Mot de passe de l'utilisateur
                )
        );
        // Récupère les détails de l'utilisateur authentifié (principal)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Génère et retourne un token JWT pour l'utilisateur
        return jwtUtils.generateToken(userDetails.getUsername());
    }
}
