package com.TheOffice.theOffice.controllers;

// Importation des classes nécessaires
import com.TheOffice.theOffice.daos.SupplierDao;
import com.TheOffice.theOffice.entities.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    // Déclaration de la dépendance pour l'accès aux données des fournisseurs
    private final SupplierDao supplierDao;

    // Constructeur pour initialiser le SupplierDao
    public SupplierController(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    // Méthode pour récupérer la liste de tous les fournisseurs
    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(supplierDao.findAll());  // Renvoie tous les fournisseurs en réponse
    }

    // Méthode pour récupérer un fournisseur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierDao.findById(id));  // Renvoie le fournisseur correspondant à l'ID fourni
    }

    // Méthode pour créer un nouveau fournisseur
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSupplier(@RequestBody Map<String, Object> request) {
        // Extraction des données depuis la requête JSON
        String name = (String) request.get("name");
        BigDecimal price = new BigDecimal(request.get("price").toString());  // Conversion du prix en BigDecimal
        String quality = (String) request.get("quality");
        Long companyId = ((Number) request.get("companyId")).longValue();  // Récupération de l'ID de la société associée

        // Sauvegarde du fournisseur dans la base de données via le SupplierDao
        int supplierId = supplierDao.save(name, price, quality, companyId);

        // Retourne une réponse HTTP 201 (création réussie) avec les détails du fournisseur créé
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "supplierId", supplierId,
                "name", name,
                "price", price,
                "quality", quality,
                "companyId", companyId
        ));
    }

    // Méthode pour mettre à jour un fournisseur existant
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        // Mise à jour du fournisseur via le SupplierDao et récupération du fournisseur mis à jour
        Supplier updatedSupplier = supplierDao.update(id, supplier);
        updatedSupplier.setId(id);  // Assure que l'ID reste inchangé
        return ResponseEntity.ok(updatedSupplier);  // Renvoie le fournisseur mis à jour
    }
}
