package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.StockMaterialDao;
import com.TheOffice.theOffice.entities.StockMaterial;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stockMaterials")
public class StockMaterialController {
    private final StockMaterialDao stockMaterialDao;

    public StockMaterialController(StockMaterialDao stockMaterialDao) {
        this.stockMaterialDao = stockMaterialDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockMaterial>> getAllStockMaterials() {
        return ResponseEntity.ok(stockMaterialDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMaterial> getStockMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMaterialDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createStockMaterial(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        Integer quantity = (Integer) request.get("quantity");
        Long id_company = ((Number) request.get("id_company")).longValue();

        int id_stockMaterial = stockMaterialDao.save(name, quantity, id_company);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_stockMaterial", id_stockMaterial,
                "name", name,
                "quantity", quantity,
                "id_company", id_company
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockMaterial> updateStockMaterial(@PathVariable Long id, @RequestBody StockMaterial stockMaterial) {
        StockMaterial updatedStockMaterial = stockMaterialDao.update(id, stockMaterial);
        return ResponseEntity.ok(updatedStockMaterial);
    }
}
