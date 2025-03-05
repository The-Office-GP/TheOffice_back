package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeDao employeeDao;

    public EmployeeController(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeDao.findById(id));
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createEmployee(
            @RequestParam("name") String name,
            @RequestParam("gender") String gender,
            @RequestParam("seniority") Integer seniority,
            @RequestParam("salary") BigDecimal salary,
            @RequestParam("level") Integer level,
            @RequestParam("mood") String mood,
            @RequestParam("status") String status,
            @RequestParam("job") String job,
            @RequestParam("health") Integer health,
            @RequestParam("image") MultipartFile image,
            @RequestParam("id_company") Long id_company) {

        try {
            byte[] imageBytes = image.getBytes();

            int id_employee = employeeDao.save(name, gender, seniority, salary, level, mood, status, job, health, imageBytes);

            Map<String, Object> response = new HashMap<>();
            response.put("id_employee", id_employee);
            response.put("name", name);
            response.put("gender", gender);
            response.put("seniority", seniority);
            response.put("salary", salary);
            response.put("level", level);
            response.put("mood", mood);
            response.put("status", status);
            response.put("job", job);
            response.put("health", health);
            response.put("image", "Uploaded Successfully");
            response.put("id_company", id_company);

            employeeDao.linkEmployeeToCompany((long) id_employee, id_company);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeDao.update(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
