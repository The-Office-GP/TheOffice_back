package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
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

    // Déclaration de la dépendance pour l'accès aux données des matériaux de stock final
    private final StockFinalMaterialDao stockFinalMaterialDao;

    // Constructeur pour initialiser le StockFinalMaterialDao
    public StockFinalMaterialController(StockFinalMaterialDao stockFinalMaterialDao) {
        this.stockFinalMaterialDao = stockFinalMaterialDao;
    }

    // Méthode pour récupérer la liste de tous les matériaux de stock final
    @GetMapping("/all")
    public ResponseEntity<List<StockFinalMaterial>> getAllStockFinalMaterials() {
        return ResponseEntity.ok(stockFinalMaterialDao.findAll());  // Renvoie tous les matériaux de stock final en réponse
    }

    // Méthode pour récupérer un matériau de stock final par son ID
    @GetMapping("/{id}")
    public ResponseEntity<StockFinalMaterial> getStockFinalMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(stockFinalMaterialDao.findById(id));  // Renvoie le matériau de stock final correspondant à l'ID fourni
    }

    // Méthode pour créer un nouveau matériau de stock final
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createStockFinalMaterial(@RequestBody Map<String, Object> request) {
        // Extraction des données depuis la requête JSON
        String name = (String) request.get("name");
        Integer quality = (Integer) request.get("quality");  // Qualité du matériau
        Integer quantity = (Integer) request.get("quantity");  // Quantité du matériau
        Long id_company = ((Number) request.get("id_company")).longValue();  // Récupération de l'ID de la société associée

        // Sauvegarde du matériau de stock final dans la base de données via le StockFinalMaterialDao
        int id_stockFinalMaterial = stockFinalMaterialDao.save(name, quality, quantity, id_company);

        // Retourne une réponse HTTP 201 (création réussie) avec les détails du matériau de stock final créé
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_stockFinalMaterial", id_stockFinalMaterial,
                "name", name,
                "quality", quality,
                "quantity", quantity,
                "id_company", id_company
        ));
    }

    // Méthode pour mettre à jour un matériau de stock final existant
    @PutMapping("/{id}")
    public ResponseEntity<StockFinalMaterial> updateStockFinalMaterial(@PathVariable Long id, @RequestBody StockFinalMaterial stockFinalMaterial) {
        // Mise à jour du matériau de stock final via le StockFinalMaterialDao et récupération du matériau mis à jour
        StockFinalMaterial updatedStockFinalMaterial = stockFinalMaterialDao.update(id, stockFinalMaterial);
        return ResponseEntity.ok(updatedStockFinalMaterial);  // Renvoie le matériau de stock final mis à jour
    }
}
