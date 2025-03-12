package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
import com.TheOffice.theOffice.daos.UserDao;
import com.TheOffice.theOffice.dtos.UserDto;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    // Déclaration des dépendances nécessaires pour le contrôle des utilisateurs
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    // Constructeur pour initialiser les dépendances
    public UserController(UserDao userDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    // Méthode pour récupérer la liste de tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userDao.findAll());  // Renvoie tous les utilisateurs en réponse
    }

    // Méthode pour récupérer un utilisateur en fonction de son identifiant
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userDao.findById(id));  // Renvoie l'utilisateur correspondant à l'ID fourni
    }

    // Méthode pour récupérer un utilisateur connecté en fonction du token JWT
    @GetMapping("/connected")
    public ResponseEntity<UserDto> getUserByToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);  // Extrait le token à partir de l'en-tête d'autorisation
        String email = jwtUtil.getEmailFromToken(token);  // Récupère l'email à partir du token JWT
        return ResponseEntity.ok(UserDto.fromEntity(userDao.findByEmail(email)));  // Renvoie l'utilisateur sous forme de DTO
    }

    // Méthode pour créer un nouvel utilisateur
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody Map<String, Object> request) {
        // Extraction des données depuis la requête JSON
        String email = (String) request.get("email");
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String role = (String) request.get("role");
        BigDecimal wallet = new BigDecimal(request.get("wallet").toString());

        // Sauvegarde de l'utilisateur dans la base de données via le DAO
        int userId = userDao.save(email, username, password, role, wallet);

        // Retourne une réponse HTTP 201 (création réussie) avec les détails de l'utilisateur créé
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", userId,
                "email", email,
                "username", username,
                "role", role,
                "wallet", wallet
        ));
    }

    // Méthode pour mettre à jour un utilisateur existant
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        // Mise à jour de l'utilisateur via le DAO et récupération de l'utilisateur mis à jour
        User updatedUser = userDao.update(id, user);
        return ResponseEntity.ok(updatedUser);  // Renvoie l'utilisateur mis à jour
    }

    // Méthode pour supprimer un utilisateur en fonction de son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        // Si l'utilisateur a été supprimé avec succès, renvoie une réponse sans contenu (204)
        if (userDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            // Si l'utilisateur n'existe pas ou n'a pas pu être supprimé, renvoie une erreur 404
            return ResponseEntity.notFound().build();
        }
    }
}
