package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
import com.TheOffice.theOffice.daos.MachineDao;
import com.TheOffice.theOffice.entities.Machine.Machine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machines")
public class MachineController {

    // Déclaration de la dépendance pour l'accès aux données des machines
    private final MachineDao machineDao;

    // Constructeur pour initialiser le MachineDao
    public MachineController(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    // Méthode pour récupérer toutes les machines
    @GetMapping("/all")
    public ResponseEntity<List<Machine>> getAllMachines() {
        return ResponseEntity.ok(machineDao.findAll());  // Renvoie toutes les machines en réponse
    }

    // Méthode pour récupérer une machine par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(@PathVariable Long id) {
        return ResponseEntity.ok(machineDao.findById(id));  // Renvoie la machine correspondant à l'ID fourni
    }

    // Méthode pour créer une machine avec plusieurs paramètres, dont une image
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createMachine(
            @RequestParam("name") String name,
            @RequestParam("production_quality") String production_quality,
            @RequestParam("price") BigDecimal price,
            @RequestParam("maintenance_cost") BigDecimal maintenance_cost,
            @RequestParam("image") MultipartFile image,  // Paramètre pour l'image de la machine
            @RequestParam("id_company") Long id_company) {  // Paramètre pour l'ID de l'entreprise associée
        try {
            byte[] imageBytes = image.getBytes();  // Transformation de l'image en tableau de bytes

            // Sauvegarde de la machine dans la base de données via le MachineDao
            int id_machine = machineDao.save(name, production_quality, price, maintenance_cost, imageBytes);

            // Lien entre la machine et l'entreprise
            machineDao.linkMachineToCompany((long) id_machine, id_company);

            // Réponse HTTP avec les détails de la machine créée
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id_machine", id_machine,
                    "name", name,
                    "production_quality", production_quality,
                    "price", price,
                    "maintenance_cost", maintenance_cost,
                    "image", "Uploaded Successfully",  // Indique que l'image a bien été téléchargée
                    "id_company", id_company  // Renvoie également l'ID de l'entreprise associée
            ));
        } catch (IOException e) {
            // En cas d'erreur lors du traitement de l'image
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    // Méthode pour mettre à jour une machine existante
    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody Machine machine) {
        // Mise à jour de la machine avec l'ID donné
        Machine updatedMachine = machineDao.update(id, machine);
        return ResponseEntity.ok(updatedMachine);  // Renvoie la machine mise à jour en réponse
    }
}
