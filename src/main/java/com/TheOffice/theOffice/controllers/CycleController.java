package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CycleDao;
import com.TheOffice.theOffice.entities.Cycle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

// Annotation pour indiquer que cette classe est un contrôleur REST pour gérer les cycles
@RestController
@RequestMapping("/cycles")
public class CycleController {
    // Déclaration de la dépendance vers le DAO (Data Access Object) qui interagira avec la base de données pour les cycles
    private final CycleDao cycleDao;

    // Constructeur pour injecter le DAO Cycle
    public CycleController(CycleDao cycleDao) {
        this.cycleDao = cycleDao;
    }

    // Méthode pour récupérer tous les cycles dans la base de données
    @GetMapping("/all")
    public ResponseEntity<List<Cycle>> getAllCycles() {
        // Retourne tous les cycles sous forme de réponse HTTP avec un statut 200 OK
        return ResponseEntity.ok(cycleDao.findAll());
    }

    // Méthode pour récupérer un cycle spécifique via son ID
    @GetMapping("/{id}")
    public ResponseEntity<Cycle> getCycleById(@PathVariable Long id) {
        // Retourne un cycle spécifique sous forme de réponse HTTP avec un statut 200 OK
        return ResponseEntity.ok(cycleDao.findById(id));
    }

    // Méthode pour créer un nouveau cycle
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCycle(@RequestBody Map<String, Object> request) {
        // Extraction des informations envoyées dans le corps de la requête (format JSON)
        Double cost = ((Number) request.get("cost")).doubleValue();  // Coût du cycle
        Long employees = ((Number) request.get("employees")).longValue();  // Nombre d'employés
        Long productivity = ((Number) request.get("productivity")).longValue();  // Productivité
        Long popularity = ((Number) request.get("popularity")).longValue();  // Popularité
        Long step = ((Number) request.get("step")).longValue();  // Étape du cycle
        Long companyId = ((Number) request.get("companyId")).longValue();  // ID de l'entreprise

        // Enregistrement du cycle dans la base de données via le DAO et récupération de l'ID du cycle créé
        int cycleId = cycleDao.save(cost, employees, productivity, popularity, step, companyId);

        // Construction de la réponse avec les informations du cycle créé
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "cycleId", cycleId,  // ID du cycle
                "cost", cost,  // Coût du cycle
                "employees", employees,  // Nombre d'employés
                "productivity", productivity,  // Productivité
                "popularity", popularity,  // Popularité
                "step", step,  // Étape du cycle
                "companyId", companyId  // ID de l'entreprise
        ));
    }

    // Méthode pour mettre à jour un cycle existant via son ID
    @PutMapping("/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable Long id, @RequestBody Cycle cycle) {
        // Mise à jour du cycle dans la base de données via le DAO
        Cycle updatedCycle = cycleDao.update(id, cycle);
        // Retourne le cycle mis à jour sous forme de réponse HTTP 200 OK
        return ResponseEntity.ok(updatedCycle);
    }
}
