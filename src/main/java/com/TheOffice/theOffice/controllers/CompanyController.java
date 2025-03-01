package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CompanyDao;
import com.TheOffice.theOffice.entities.Company;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyDao companyDao;

    public CompanyController(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCompany(@Valid @RequestBody Map<String, Object> request) {
        String sector = (String) request.get("sector");
        String name = (String) request.get("name");
        Date creation_date = new Date();
        Long id_user = ((Number) request.get("id_user")).longValue();

        int id_company = companyDao.save(sector, name, creation_date, id_user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_company", id_company,
                "sector", sector,
                "name", name,
                "creation_date", creation_date,
                "id_user", id_user
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updatedCompany = companyDao.update(id, company);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        if (companyDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
