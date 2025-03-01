package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Long id = employee.getId();
        String name = employee.getName();
        String gender = String.valueOf(employee.getGender());
        Integer seniority = employee.getSeniority();
        BigDecimal salary = employee.getSalary();
        Integer level = employee.getLevel();
        String mood = String.valueOf(employee.getMood());
        String status = String.valueOf(employee.getStatus());
        String job = String.valueOf(employee.getJob());
        Integer health = employee.getHealth();
        String image = employee.getImage();

        int id_employee = employeeDao.save(name, gender, seniority, salary, level, mood, status, job, health, image);
        employee.setId((long) id_employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
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
