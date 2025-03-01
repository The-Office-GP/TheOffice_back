package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.LocalDao;
import com.TheOffice.theOffice.entities.Local.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
    public ResponseEntity<List<Local>> getAllLocals() {
        return ResponseEntity.ok(localDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Local> getLocalById(@PathVariable Long id) {
        return ResponseEntity.ok(localDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createLocal(@RequestBody Map<String, Object> request) {
        String level = String.valueOf(request.get("level"));
        Integer size = (Integer) request.get("size");
        BigDecimal rent = new BigDecimal(request.get("rent").toString());
        Integer maxEmployees = (Integer) request.get("maxEmployees");
        Integer maxMachines = (Integer) request.get("maxMachines");
        Long id_company = ((Number) request.get("id_company")).longValue();

        int id_local = localDao.save(level, size, rent, maxEmployees, maxMachines, id_company);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_local", id_local,
                "level", level,
                "size", size,
                "rent", rent,
                "maxEmployees", maxEmployees,
                "maxMachines", maxMachines,
                "id_company", id_company
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(@PathVariable Long id, @RequestBody Local local) {
        Local updatedLocal = localDao.update(id, local);
        return ResponseEntity.ok(updatedLocal);
    }
}
