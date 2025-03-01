package com.TheOffice.theOffice.controllers;

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
    private final SupplierDao supplierDao;

    public SupplierController(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(supplierDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSupplier(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        BigDecimal price = new BigDecimal(request.get("price").toString());
        String quality = (String) request.get("quality");
        Long id_company = ((Number) request.get("id_company")).longValue();

        int id_supplier = supplierDao.save(name, price, quality, id_company);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_supplier", id_supplier,
                "name", name,
                "price", price,
                "quality", quality,
                "id_company", id_company
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        Supplier updatedSupplier = supplierDao.update(id, supplier);
        updatedSupplier.setId(id);
        return ResponseEntity.ok(updatedSupplier);
    }

}
