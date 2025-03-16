package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.*;
import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.*;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Machine.Machine;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController // Indique que cette classe est un contrôleur REST
@RequestMapping("/companies") // Définit le préfixe d'URL pour toutes les routes de ce contrôleur
public class CompanyController {
    private final CompanyDao companyDao;
    private final UserDao userDao;
    private final CycleDao cycleDao;
    private final MachineDao machineDao;
    private final EmployeeDao employeeDao;
    private final SupplierDao supplierDao;
    private final EventDao eventDao;
    private final StockMaterialDao stockMaterialDao;
    private final StockFinalMaterialDao stockFinalMaterialDao;
    private final JwtUtil jwtUtil;

    // Injection des dépendances via le constructeur
    public CompanyController(CompanyDao companyDao, UserDao userDao, CycleDao cycleDao, MachineDao machineDao, EmployeeDao employeeDao, SupplierDao supplierDao, EventDao eventDao, StockMaterialDao stockMaterialDao, StockFinalMaterialDao stockFinalMaterialDao, JwtUtil jwtUtil) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.cycleDao = cycleDao;
        this.machineDao = machineDao;
        this.employeeDao = employeeDao;
        this.supplierDao = supplierDao;
        this.eventDao = eventDao;
        this.stockMaterialDao = stockMaterialDao;
        this.stockFinalMaterialDao = stockFinalMaterialDao;
        this.jwtUtil = jwtUtil;
    }

    // Récupère toutes les entreprises
    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    // Récupère une entreprise par son ID avec toutes ses relations (machines, employés, etc.)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Company company = companyDao.findById(id);

        // 🔥 Vérifier si l'utilisateur est bien le propriétaire de l'entreprise
        if (!company.getId_user().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Accès interdit : cette entreprise ne vous appartient pas."));
        }

        Double wallet = userDao.findWalletByUserId(company.getId_user());

        List<CycleDto> cycles = cycleDao.findByIdCompany(id).stream().map(CycleDto::fromEntity).collect(Collectors.toList());
        List<MachineDto> machines = machineDao.findByIdCompany(id).stream().map(MachineDto::fromEntity).collect(Collectors.toList());
        List<EmployeeDto> employees = employeeDao.findByIdCompany(id).stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
        List<SupplierDto> suppliers = supplierDao.findByIdCompany(id).stream().map(SupplierDto::fromEntity).collect(Collectors.toList());
        List<EventDto> events = eventDao.findByIdCompany(id).stream().map(EventDto::fromEntity).collect(Collectors.toList());
        List<StockMaterialDto> stockMaterials = stockMaterialDao.findByIdCompany(id).stream().map(StockMaterialDto::fromEntity).collect(Collectors.toList());
        List<StockFinalMaterialDto> stockFinalMaterials = stockFinalMaterialDao.findByIdCompany(id).stream().map(StockFinalMaterialDto::fromEntity).collect(Collectors.toList());

        CompanyDto companyDto = CompanyDto.fromEntity(company, wallet, cycles, machines, employees, suppliers, events, stockMaterials, stockFinalMaterials);

        return ResponseEntity.ok(CompanyRequestDto.fromDto(companyDto));
    }

    // Récupère les machines associées à une entreprise spécifique
    @GetMapping("/{id}/machines")
    public ResponseEntity<List<Machine>> getMachinesByCompanyId(@PathVariable Long id) {
        List<Machine> machines = companyDao.findMachinesByCompanyId(id);
        return ResponseEntity.ok(machines);
    }

    // Récupère les employés associés à une entreprise spécifique
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByCompanyId(@PathVariable Long id) {
        List<Employee> employees = companyDao.findEmployeesByCompanyId(id);
        return ResponseEntity.ok(employees);
    }

    // Récupère les événements associés à une entreprise spécifique
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Event>> getEventsByCompanyId(@PathVariable Long id){
        List<Event> events = companyDao.findEventsByCompanyId(id);
        return ResponseEntity.ok(events);
    }

    // Récupère toutes les entreprises associées à l'utilisateur connecté
    @GetMapping("/user")
    public ResponseEntity<List<Company>> getUserCompanies(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7); // Extraction du token JWT
        String email = jwtUtil.getEmailFromToken(token); // Extraction de l'email depuis le token
        User user = userDao.findByEmail(email); // Récupération de l'utilisateur par email
        List<Company> companies = companyDao.findByUserId(user.getId()); // Récupération des entreprises de l'utilisateur
        return ResponseEntity.ok(companies);
    }

    // Création d'une nouvelle entreprise
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCompany(
            @Valid @RequestBody Company company,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            Long id_user = userDetails.getId();
            company.setId_user(id_user);

            Date creation_date = new Date();
            company.setCreation_date(creation_date);

            // 🔥 Vérifier et assigner un id_local si null
            if (company.getId_local() == null) {

                // 🔹 Mapping des sectors vers id_local
                Map<String, Long> sectorToLocalMap = Map.of(
                        "carpentry", 1L,
                        "creamery", 4L,
                        "quarry", 7L
                );

                Long assignedLocal = sectorToLocalMap.get(company.getSector().toLowerCase());

                if (assignedLocal == null) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "error", "Secteur inconnu : " + company.getSector(),
                            "details", "Les secteurs valides sont : carpentry, creamery, quarry."
                    ));
                }

                company.setId_local(assignedLocal);
            }

            // Enregistrement en base de données
            int id_company = companyDao.save(
                    company.getSector(),
                    company.getName(),
                    creation_date,
                    company.getId_local(),
                    id_user
            );

            // Réponse
            Map<String, Object> response = new HashMap<>();
            response.put("id_company", id_company);
            response.put("sector", company.getSector());
            response.put("name", company.getName());
            response.put("creation_date", creation_date);
            response.put("id_local", company.getId_local());
            response.put("id_user", id_user);
            response.put("message", "Entreprise créée avec succès !");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors de la création de l'entreprise",
                    "details", e.getMessage()
            ));
        }
    }

    // Mise à jour d'une entreprise existante
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updatedCompany = companyDao.update(id, company);
        return ResponseEntity.ok(updatedCompany);
    }

    // Suppression d'une entreprise par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        if (companyDao.delete(id)) {
            return ResponseEntity.noContent().build(); // Réponse HTTP 204 si suppression réussie
        } else {
            return ResponseEntity.notFound().build(); // Réponse HTTP 404 si l'entreprise n'existe pas
        }
    }
}