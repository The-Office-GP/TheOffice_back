package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.services.EmployeeService;
import com.TheOffice.theOffice.entities.Employee.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Map<String, Object> request) {
        // Extraction des informations envoyées dans la requête
        String name = (request.get("name").toString());
        Gender gender = Gender.valueOf(request.get("gender").toString());
        Integer price = Integer.valueOf((request.get("seniority").toString()));
        BigDecimal salary = new BigDecimal(request.get("salary").toString());
        Integer level = Integer.valueOf((request.get("level").toString()));
        Mood mood = Mood.valueOf(request.get("mood").toString());
        Status status = Status.valueOf(request.get("status").toString());
        Job job = Job.valueOf(request.get("job").toString());
        Integer health = Integer.valueOf((request.get("health").toString()));
        String image = (request.get("image").toString());

        // Sauvegarde du prêt et récupération de son ID
        int id_employee = employeeDao.save(name, gender.name(), price, salary, level, mood.name(), status.name(), job.name(), health, image);

        // Construction de la réponse HTTP avec statut 201 (créé) et données du prêt
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_employee", id_employee,
                "name", name,
                "price", price,
                "salary", salary,
                "level", level,
                "mood", mood,
                "status", status,
                "job", job,
                "health", health,
                "image", image
        ));
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
