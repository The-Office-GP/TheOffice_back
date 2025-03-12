package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.LocalDao;
import com.TheOffice.theOffice.entities.Local.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locals")
public class LocalController {
    private final LocalDao localDao;

    public LocalController(LocalDao localDao) {
        this.localDao = localDao;
    }

    // Récupérer tous les locaux
    @GetMapping("/all")
    public ResponseEntity<List<Local>> getAllLocals() {
        return ResponseEntity.ok(localDao.findAll());  // Renvoie tous les locaux dans une réponse HTTP OK
    }

    // Récupérer un local par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Local> getLocalById(@PathVariable Long id) {
        return ResponseEntity.ok(localDao.findById(id));  // Renvoie le local correspondant à l'ID donné
    }

    // Créer un local
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createLocal(
            @RequestParam("level") String level,
            @RequestParam("size") Integer size,
            @RequestParam("rent") BigDecimal rent,
            @RequestParam("maxEmployees") Integer maxEmployees,
            @RequestParam("maxMachines") Integer maxMachines,
            @RequestParam("id_company") Long idCompany,
            @RequestParam("background_image") MultipartFile background_image) {
        try {
            // Convertir l'image en tableau de bytes
            byte[] imageBytes = background_image.getBytes();

            // Sauvegarder le local et obtenir son ID
            int id_local = localDao.save(level, size, rent, maxEmployees, maxMachines, imageBytes, idCompany);

            // Retourner une réponse HTTP avec les détails du local créé
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id_local", id_local,
                    "level", level,
                    "size", size,
                    "rent", rent,
                    "maxEmployees", maxEmployees,
                    "maxMachines", maxMachines,
                    "background_image", "Uploaded Successfully",  // Confirmation que l'image a été téléchargée
                    "id_company", idCompany
            ));
        } catch (IOException e) {
            // En cas d'erreur lors du traitement de l'image, retourner une erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    // Mettre à jour un local
    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(@PathVariable Long id, @RequestBody Local local) {
        // Mettre à jour le local avec les nouvelles informations
        Local updatedLocal = localDao.update(id, local);
        return ResponseEntity.ok(updatedLocal);  // Retourner le local mis à jour
    }
}
