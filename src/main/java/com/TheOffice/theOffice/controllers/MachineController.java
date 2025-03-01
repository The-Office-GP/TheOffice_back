package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.MachineDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Machine.Machine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMachine(@RequestBody Map<String, Object> request) {
        String name = String.valueOf(request.get("name"));
        String production_quality = String.valueOf(request.get("production_quality"));
        BigDecimal price = new BigDecimal(request.get("price").toString());
        BigDecimal maintenance_cost = new BigDecimal(request.get("maintenance_cost").toString());
        String image = String.valueOf(request.get("image"));

        int id_machine = machineDao.save(name, production_quality, price, maintenance_cost, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_machine", id_machine,
                "name", name,
                "production_quality", production_quality,
                "price", price,
                "maintenance_cost", maintenance_cost,
                "image", image
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Machine> updateMachine(@PathVariable Long id, @RequestBody Machine machine) {
        Machine updatedMachine = machineDao.update(id, machine);
        return ResponseEntity.ok(updatedMachine);
    }
}
