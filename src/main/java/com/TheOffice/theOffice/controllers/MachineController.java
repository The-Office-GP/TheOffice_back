package com.TheOffice.theOffice.controllers;

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
    private final MachineDao machineDao;

    public MachineController(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Machine>> getAllMachines() {
        return ResponseEntity.ok(machineDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(@PathVariable Long id) {
        return ResponseEntity.ok(machineDao.findById(id));
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createMachine(
            @RequestParam("name") String name,
            @RequestParam("production_quality") String production_quality,
            @RequestParam("price") BigDecimal price,
            @RequestParam("maintenance_cost") BigDecimal maintenance_cost,
            @RequestParam("image") MultipartFile image,
            @RequestParam("id_company") Long id_company) {  // Ajout du paramètre id_company
        try {
            byte[] imageBytes = image.getBytes();

            int id_machine = machineDao.save(name, production_quality, price, maintenance_cost, imageBytes);

            // Lier la machine à l'entreprise
            machineDao.linkMachineToCompany((long) id_machine, id_company);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id_machine", id_machine,
                    "name", name,
                    "production_quality", production_quality,
                    "price", price,
                    "maintenance_cost", maintenance_cost,
                    "image", "Uploaded Successfully",
                    "id_company", id_company  // Retourne aussi l'association
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody Machine machine) {
        Machine updatedMachine = machineDao.update(id, machine);
        return ResponseEntity.ok(updatedMachine);
    }
}