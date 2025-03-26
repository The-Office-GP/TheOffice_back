package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
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

    // Déclaration de la dépendance pour l'accès aux données des matériaux de stock
    private final StockMaterialDao stockMaterialDao;

    // Constructeur pour initialiser le StockMaterialDao
    public StockMaterialController(StockMaterialDao stockMaterialDao) {
        this.stockMaterialDao = stockMaterialDao;
    }

    // Méthode pour récupérer la liste de tous les matériaux de stock
    @GetMapping("/all")
    public ResponseEntity<List<StockMaterial>> getAllStockMaterials() {
        return ResponseEntity.ok(stockMaterialDao.findAll());  // Renvoie tous les matériaux de stock en réponse
    }

    // Méthode pour récupérer un matériau de stock par son ID
    @GetMapping("/{id}")
    public ResponseEntity<StockMaterial> getStockMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMaterialDao.findById(id));  // Renvoie le matériau de stock correspondant à l'ID fourni
    }

    // Méthode pour créer un nouveau matériau de stock
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createStockMaterial(@RequestBody Map<String, Object> request) {
        // Extraction des données depuis la requête JSON
        String name = (String) request.get("name");
        Integer quantityLow = (Integer) request.get("quantityLow");  // Quantité basse
        Integer quantityMid = (Integer) request.get("quantityMid");  // Quantité moyenne
        Integer quantityHigh = (Integer) request.get("quantityHigh"); // Quantité haute
        Long companyId = ((Number) request.get("companyId")).longValue();  // Récupération de l'ID de la société associée

        int stockMaterialId = stockMaterialDao.save(name, quantityLow, quantityMid, quantityHigh, companyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "stockMaterialId", stockMaterialId,
                "name", name,
                "quantityLow", quantityLow,
                "quantityMid", quantityMid,
                "quantityHigh", quantityHigh,
                "companyId", companyId
        ));
    }


    // Méthode pour mettre à jour un matériau de stock existant
    /*@PutMapping("/{id}")
    public ResponseEntity<StockMaterial> updateStockMaterial(@PathVariable Long id, @RequestBody StockMaterial stockMaterial) {
        // Mise à jour du matériau de stock via le StockMaterialDao et récupération du matériau mis à jour
        StockMaterial updatedStockMaterial = stockMaterialDao.update(id, stockMaterial);
        return ResponseEntity.ok(updatedStockMaterial);  // Renvoie le matériau de stock mis à jour
    }*/
}
