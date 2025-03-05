package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CycleDao;
import com.TheOffice.theOffice.entities.Cycle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cycles")
public class CycleController {
    private final CycleDao cycleDao;

    public CycleController(CycleDao cycleDao) {
        this.cycleDao = cycleDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cycle>> getAllCycles() {
        return ResponseEntity.ok(cycleDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cycle> getCycleById(@PathVariable Long id) {
        return ResponseEntity.ok(cycleDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCycle(@RequestBody Map<String, Object> request) {
        Double cost = ((Number) request.get("cost")).doubleValue();
        Long employees = ((Number) request.get("employees")).longValue();
        Long productivity = ((Number) request.get("productivity")).longValue();
        Long popularity = ((Number) request.get("popularity")).longValue();
        Long step = ((Number) request.get("step")).longValue();
        Long id_company = ((Number) request.get("id_company")).longValue();

        int id_cycle = cycleDao.save(cost, employees, productivity, popularity, step, id_company);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_cycle", id_cycle,
                "cost", cost,
                "employees", employees,
                "productivity", productivity,
                "popularity", popularity,
                "step", step,
                "id_company", id_company
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable Long id, @RequestBody Cycle cycle) {
        Cycle updatedCycle = cycleDao.update(id, cycle);
        return ResponseEntity.ok(updatedCycle);
    }
}