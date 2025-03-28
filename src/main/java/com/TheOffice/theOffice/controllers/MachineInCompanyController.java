package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.MachineInCompanyDao;
import com.TheOffice.theOffice.entities.MachineInCompany;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machinesInCompany")
public class MachineInCompanyController {
    private final MachineInCompanyDao machineInCompanyDao;

    public MachineInCompanyController(MachineInCompanyDao machineInCompanyDao) {
        this.machineInCompanyDao = machineInCompanyDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MachineInCompany>> getAllMachinesInCompany() {
        return ResponseEntity.ok(machineInCompanyDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineInCompany> getMachineInCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(machineInCompanyDao.findById(id));
    }

    /*@PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMachineInCompany(@RequestBody Map<String, Object> request) {
        // Extraction des données depuis la requête JSON
        Long machineId = ((Number) request.get("machineId")).longValue();
        Long companyId = ((Number) request.get("companyId")).longValue();

        int machineInCompanyId = machineInCompanyDao.save(machineId, companyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "machineInCompanyId", machineInCompanyId,
                "machineId", machineId,
                "companyId", companyId
        ));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachineInCompany(@PathVariable Long id) {
        if (machineInCompanyDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

