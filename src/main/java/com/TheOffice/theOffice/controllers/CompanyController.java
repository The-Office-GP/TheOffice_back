package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.*;
import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.*;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
import com.TheOffice.theOffice.services.MachineService;
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
    private final EmployeeDao employeeDao;
    private final SupplierDao supplierDao;
    private final EventDao eventDao;
    private final StockMaterialDao stockMaterialDao;
    private final StockFinalMaterialDao stockFinalMaterialDao;
    private final JwtUtil jwtUtil;
    private final MachineService machineService;

    // Injection des dépendances via le constructeur
    public CompanyController(CompanyDao companyDao, UserDao userDao, CycleDao cycleDao, EmployeeDao employeeDao, SupplierDao supplierDao, EventDao eventDao, StockMaterialDao stockMaterialDao, StockFinalMaterialDao stockFinalMaterialDao, JwtUtil jwtUtil, MachineService machineService) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.cycleDao = cycleDao;
        this.employeeDao = employeeDao;
        this.supplierDao = supplierDao;
        this.eventDao = eventDao;
        this.stockMaterialDao = stockMaterialDao;
        this.stockFinalMaterialDao = stockFinalMaterialDao;
        this.jwtUtil = jwtUtil;
        this.machineService = machineService;
    }

    // Récupère toutes les entreprises
    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    // Récupère une entreprise par son ID avec toutes ses relations (machines, employés, etc.)
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Company company = companyDao.findById(id);

        Double wallet = userDao.findWalletByUserId(company.getUserId());

        List<CycleDto> cycles = cycleDao.findByIdCompany(id).stream().map(CycleDto::fromEntity).collect(Collectors.toList());
        List<EmployeeDto> employees = employeeDao.findByIdCompany(id).stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
        List<SupplierDto> suppliers = supplierDao.findByIdCompany(id).stream().map(SupplierDto::fromEntity).collect(Collectors.toList());
        List<EventDto> events = eventDao.findByIdCompany(id).stream().map(EventDto::fromEntity).collect(Collectors.toList());
        List<StockMaterialDto> stockMaterials = stockMaterialDao.findByIdCompany(id).stream().map(StockMaterialDto::fromEntity).collect(Collectors.toList());
        List<StockFinalMaterialDto> stockFinalMaterials = stockFinalMaterialDao.findByIdCompany(id).stream().map(StockFinalMaterialDto::fromEntity).collect(Collectors.toList());

        CompanyDto companyDto = CompanyDto.fromEntity(company, wallet, cycles, employees, suppliers, events, stockMaterials, stockFinalMaterials, machineService);

        return ResponseEntity.ok(companyDto);
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

    @PostMapping("/buyMachine")
    public ResponseEntity<List<Machine>> getMachineForBuy(@RequestBody Company company){
        List<Machine> machineList = machineService.collectMachine(company);
        return ResponseEntity.ok(machineList);
    }

    // Création d'une nouvelle entreprise
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCompany(
            @Valid @RequestBody Company company,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            Long userId = userDetails.getId();
            company.setUserId(userId);

            Date creationDate = new Date();
            company.setCreationDate(creationDate);

            // Vérifier et assigner un localId si null
            if (company.getLocalId() == null) {

                // Mapping des sectors vers localId
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

                company.setLocalId(assignedLocal);
            }

            // Enregistrement en base de données
            int companyId = companyDao.save(
                    company.getSector(),
                    company.getName(),
                    creationDate,
                    company.getPopularity(),
                    company.getLocalId(),
                    company.getMachineId(),
                    userId
            );

            // Réponse
            Map<String, Object> response = new HashMap<>();
            response.put("companyId", companyId);
            response.put("sector", company.getSector());
            response.put("name", company.getName());
            response.put("creationDate", creationDate);
            response.put("popularity", company.getPopularity());
            response.put("localId", company.getLocalId());
            response.put("machineId", company.getMachineId());
            response.put("userId", userId);
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
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}