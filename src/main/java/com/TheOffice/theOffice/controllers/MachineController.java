package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
import com.TheOffice.theOffice.daos.MachineDao;
import com.TheOffice.theOffice.classes.Machine;
import com.TheOffice.theOffice.entities.Machine.ProductionQuality;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMachine(@RequestBody Map<String, Object> request) {
        // Extraction des informations envoyées dans la requête
        String name = (request.get("name").toString());
        ProductionQuality productionQuality = ProductionQuality.valueOf(request.get("production_quality").toString());
        BigDecimal price = new BigDecimal(request.get("price").toString());
        BigDecimal maintenance_cost = new BigDecimal(request.get("maintenance_cost").toString());
        String image = (request.get("image").toString());

        // Sauvegarde du prêt et récupération de son ID
        int id_machine = machineDao.save(name, productionQuality.name(), price, maintenance_cost, image);

        // Construction de la réponse HTTP avec statut 201 (créé) et données du prêt
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_machine", id_machine,
                "name", name,
                "production_quality", productionQuality,
                "price", price,
                "maintenance_cost", maintenance_cost,
                "image", image
        ));
    }
}
