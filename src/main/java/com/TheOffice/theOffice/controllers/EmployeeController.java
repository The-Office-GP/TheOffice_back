package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.classes.EmployeeNameList;
import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Annotation pour indiquer qu'il s'agit d'un contrôleur REST et que les réponses seront en JSON
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    // Déclaration de la dépendance vers le DAO qui interagira avec la base de données
    private final EmployeeDao employeeDao;
    private final EmployeeService employeeService;

    // Injection de la dépendance via le constructeur
    public EmployeeController(EmployeeDao employeeDao, EmployeeService employeeService) {
        this.employeeDao = employeeDao;
        this.employeeService = employeeService;
    }

    // Méthode pour récupérer tous les employés de l'entreprise
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        // Retourne la liste des employés sous forme de réponse HTTP avec le statut 200 OK
        return ResponseEntity.ok(employeeDao.findAll());
    }

    // Méthode pour récupérer un employé spécifique via son ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        // Retourne un employé spécifique sous forme de réponse HTTP avec le statut 200 OK
        return ResponseEntity.ok(employeeDao.findById(id));
    }

    @GetMapping("/generate")
    public ResponseEntity<List<Employee>> employeeList() {
        List<Employee> employeeList = employeeService.generateEmployee();
        return ResponseEntity.ok(employeeList);
    }

    // Méthode pour créer un nouvel employé. Cette méthode reçoit des paramètres via un formulaire multipart (incluant une image).
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createEmployee(
            @RequestParam("name") String name,  // Nom de l'employé
            @RequestParam("gender") String gender,  // Sexe de l'employé
            @RequestParam("seniority") Integer seniority,  // Ancienneté de l'employé
            @RequestParam("salary") BigDecimal salary,  // Salaire de l'employé
            @RequestParam("level") Integer level,  // Niveau de l'employé
            @RequestParam("mood") String mood,  // Humeur de l'employé
            @RequestParam("status") String status,  // Statut de l'employé
            @RequestParam("job") String job,  // Poste de l'employé
            @RequestParam("health") Integer health,  // État de santé de l'employé
            @RequestParam("image") MultipartFile image,  // Image de l'employé (profil)
            @RequestParam("id_company") Long id_company) {  // ID de l'entreprise à laquelle l'employé est associé

        try {
            // Récupération du fichier image en tant que tableau de bytes
            byte[] imageBytes = image.getBytes();

            // Enregistrement de l'employé dans la base de données via le DAO et récupération de son ID
            int id_employee = employeeDao.save(name, gender, seniority, salary, level, mood, status, job, health, imageBytes);

            // Création d'un map pour structurer la réponse
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
            response.put("image", "Uploaded Successfully");  // Message indiquant que l'image a bien été uploadée
            response.put("id_company", id_company);

            // Lier l'employé à une entreprise spécifique
            employeeDao.linkEmployeeToCompany((long) id_employee, id_company);

            // Retourne une réponse HTTP 201 Created avec les informations de l'employé créé
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            // Si une erreur survient lors de l'upload de l'image, une réponse 500 Internal Server Error est renvoyée
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    // Méthode pour mettre à jour un employé via son ID
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        // Mise à jour de l'employé dans la base de données via le DAO
        Employee updatedEmployee = employeeDao.update(id, employee);
        // Retourne l'employé mis à jour sous forme de réponse HTTP 200 OK
        return ResponseEntity.ok(updatedEmployee);
    }

    // Méthode pour supprimer un employé via son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        // Si la suppression est réussie, on retourne un statut 204 No Content
        if (employeeDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            // Si l'employé n'est pas trouvé, on retourne un statut 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}
