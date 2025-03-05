package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.StockFinalMaterialDao;
import com.TheOffice.theOffice.entities.StockFinalMaterial;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stockFinalMaterials")
public class StockFinalMaterialController {
    private final StockFinalMaterialDao stockFinalMaterialDao;

    public StockFinalMaterialController(StockFinalMaterialDao stockFinalMaterialDao) {
        this.stockFinalMaterialDao = stockFinalMaterialDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockFinalMaterial>> getAllStockFinalMaterials() {
        return ResponseEntity.ok(stockFinalMaterialDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockFinalMaterial> getStockFinalMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(stockFinalMaterialDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createStockFinalMaterial(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        Integer quality = (Integer) request.get("quality");
        Integer quantity = (Integer) request.get("quantity");
        Long id_company = ((Number) request.get("id_company")).longValue();

        int id_stockFinalMaterial = stockFinalMaterialDao.save(name, quality, quantity, id_company);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_stockFinalMaterial", id_stockFinalMaterial,
                "name", name,
                "quality", quality,
                "quantity", quantity,
                "id_company", id_company
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockFinalMaterial> updateStockFinalMaterial(@PathVariable Long id, @RequestBody StockFinalMaterial stockFinalMaterial) {
        StockFinalMaterial updatedStockFinalMaterial = stockFinalMaterialDao.update(id, stockFinalMaterial);
        return ResponseEntity.ok(updatedStockFinalMaterial);
    }
}
