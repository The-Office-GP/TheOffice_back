package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.*;
import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.*;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Local.Local;
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
    private final LocalDao localDao;
    private final JwtUtil jwtUtil;

    // Injection des dépendances via le constructeur
    public CompanyController(CompanyDao companyDao, UserDao userDao, CycleDao cycleDao, MachineDao machineDao, EmployeeDao employeeDao, SupplierDao supplierDao, EventDao eventDao, StockMaterialDao stockMaterialDao, StockFinalMaterialDao stockFinalMaterialDao, LocalDao localDao, JwtUtil jwtUtil) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.cycleDao = cycleDao;
        this.machineDao = machineDao;
        this.employeeDao = employeeDao;
        this.supplierDao = supplierDao;
        this.eventDao = eventDao;
        this.stockMaterialDao = stockMaterialDao;
        this.stockFinalMaterialDao = stockFinalMaterialDao;
        this.localDao = localDao;
        this.jwtUtil = jwtUtil;
    }

    // Récupère toutes les entreprises
    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    // Récupère une entreprise par son ID avec toutes ses relations (machines, employés, etc.)
    @GetMapping("/{id}")
    public ResponseEntity<CompanyRequestDto> getCompanyById(@PathVariable Long id) {
        Company company = companyDao.findById(id);
        Double wallet = userDao.findWalletByUserId(company.getId_user());

        // Conversion des entités en DTOs
        List<CycleDto> cycles = cycleDao.findByIdCompany(id).stream().map(CycleDto::fromEntity).collect(Collectors.toList());
        List<MachineDto> machines = machineDao.findByIdCompany(id).stream().map(MachineDto::fromEntity).collect(Collectors.toList());
        List<EmployeeDto> employees = employeeDao.findByIdCompany(id).stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
        List<SupplierDto> suppliers = supplierDao.findByIdCompany(id).stream().map(SupplierDto::fromEntity).collect(Collectors.toList());
        List<EventDto> events = eventDao.findByIdCompany(id).stream().map(EventDto::fromEntity).collect(Collectors.toList());
        List<StockMaterialDto> stockMaterials = stockMaterialDao.findByIdCompany(id).stream().map(StockMaterialDto::fromEntity).collect(Collectors.toList());
        List<StockFinalMaterialDto> stockFinalMaterials = stockFinalMaterialDao.findByIdCompany(id).stream().map(StockFinalMaterialDto::fromEntity).collect(Collectors.toList());
        Local local = localDao.findByIdCompany(id); // Récupère l'entité Local sans conversion

        // Création d'un DTO global pour l'entreprise
        CompanyDto companyDto = CompanyDto.fromEntity(company, wallet, cycles, machines, employees, suppliers, events, stockMaterials, stockFinalMaterials, local);

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
    public ResponseEntity<Map<String, Object>> createCompany(@Valid @RequestBody Map<String, Object> request) {
        // Extraction des paramètres de la requête
        String sector = (String) request.get("sector");
        String name = (String) request.get("name");
        Date creation_date = new Date(); // Définition de la date de création à la date actuelle
        Long id_user = ((Number) request.get("id_user")).longValue(); // Conversion en Long

        // Sauvegarde de l'entreprise et récupération de son ID généré
        int id_company = companyDao.save(sector, name, creation_date, id_user);

        // Création automatique d'un local par défaut pour cette entreprise
        localDao.saveDefaultLocal((long) id_company, sector);

        // Réponse HTTP 201 (Created) avec les détails de l'entreprise créée
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id_company", id_company,
                "sector", sector,
                "name", name,
                "creation_date", creation_date,
                "id_user", id_user,
                "message", "Entreprise et Local créés avec succès !"
        ));
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