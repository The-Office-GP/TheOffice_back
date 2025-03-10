package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CompanyDao;
import com.TheOffice.theOffice.daos.UserDao;
import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Event;
import com.TheOffice.theOffice.entities.Machine.Machine;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
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
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    public CompanyController(CompanyDao companyDao, UserDao userDao, JwtUtil jwtUtil) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyDao.findById(id));
    }

    @GetMapping("/{id}/machines")
    public ResponseEntity<List<Machine>> getMachinesByCompanyId(@PathVariable Long id) {
        List<Machine> machines = companyDao.findMachinesByCompanyId(id);
        return ResponseEntity.ok(machines);

    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByCompanyId(@PathVariable Long id) {
        List<Employee> employees = companyDao.findEmployeesByCompanyId(id);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getEventsByCompanyId(@PathVariable Long id){
        List<Event> events = companyDao.findEventsByCompanyId(id);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Company>> getUserCompanies(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        User user = userDao.findByEmail(email);
        List<Company> companies = companyDao.findByUserId(user.getId());
        return ResponseEntity.ok(companies);
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