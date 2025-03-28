package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.*;
import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.*;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.services.CycleService;
import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.security.JwtUtil;
import com.TheOffice.theOffice.services.MachineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final MachineInCompanyDao machineInCompanyDao;
    private final JwtUtil jwtUtil;
    private final MachineService machineService;
    private final CycleService cycleService;
    private final StatisticDao statisticDao;

    // Injection des dépendances via le constructeur
    public CompanyController(CompanyDao companyDao, UserDao userDao, CycleDao cycleDao, EmployeeDao employeeDao, SupplierDao supplierDao, EventDao eventDao, StockMaterialDao stockMaterialDao, StockFinalMaterialDao stockFinalMaterialDao, MachineInCompanyDao machineInCompanyDao, JwtUtil jwtUtil, MachineService machineService, CycleService cycleService, StatisticDao statisticDao) {
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
        this.machineInCompanyDao = machineInCompanyDao;
        this.cycleService = cycleService;
        this.statisticDao = statisticDao;
    }

    // Récupère toutes les entreprises
    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyDao.findAll());
    }

    // Récupère une entreprise par son ID avec toutes ses relations (machines, employés, etc.)
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        Company company = companyDao.findById(id);
        String token = authorizationHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        User user = userDao.findByEmail(email);
        List<Statistic> statisticListForDto = statisticDao.findAllCompanyStatistic(id);

        if (!company.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Double wallet = userDao.findWalletByUserId(company.getUserId());
        CycleDto cycle = CycleDto.fromEntity(cycleDao.findByIdCompany(id));
        StockMaterialDto stockMaterials = StockMaterialDto.fromEntity(stockMaterialDao.findByIdCompany(id));

        List<EmployeeDto> employees = employeeDao.findByIdCompany(id).stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
        List<SupplierDto> suppliers = supplierDao.findByIdCompany(id).stream().map(SupplierDto::fromEntity).collect(Collectors.toList());
        List<EventDto> events = eventDao.findByIdCompany(id).stream().map(EventDto::fromEntity).collect(Collectors.toList());
        List<StockFinalMaterialDto> stockFinalMaterials = stockFinalMaterialDao.findByIdCompany(id).stream().map(StockFinalMaterialDto::fromEntity).collect(Collectors.toList());
        List<MachineInCompanyDto> machinesInCompany = machineInCompanyDao.findByIdCompany(id).stream().map(MachineInCompanyDto::fromEntity).collect(Collectors.toList());

        CompanyDto companyDto = CompanyDto.fromEntity(company, wallet, cycle, employees, suppliers, events, stockMaterials, stockFinalMaterials,machinesInCompany ,machineService, statisticListForDto);

        return ResponseEntity.ok(companyDto);
    }

    public CompanyDto getCompanyById2(Long id) {
        Company company = companyDao.findById(id);

        Double wallet = userDao.findWalletByUserId(company.getUserId());
        CycleDto cycle = CycleDto.fromEntity(cycleDao.findByIdCompany(id));
        StockMaterialDto stockMaterials = StockMaterialDto.fromEntity(stockMaterialDao.findByIdCompany(id));
        List<Statistic> statisticListForDto = statisticDao.findAllCompanyStatistic(id);

        List<EmployeeDto> employees = employeeDao.findByIdCompany(id).stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
        List<SupplierDto> suppliers = supplierDao.findByIdCompany(id).stream().map(SupplierDto::fromEntity).collect(Collectors.toList());
        List<EventDto> events = eventDao.findByIdCompany(id).stream().map(EventDto::fromEntity).collect(Collectors.toList());
        List<StockFinalMaterialDto> stockFinalMaterials = stockFinalMaterialDao.findByIdCompany(id).stream().map(StockFinalMaterialDto::fromEntity).collect(Collectors.toList());
        List<MachineInCompanyDto> machinesInCompany = machineInCompanyDao.findByIdCompany(id).stream().map(MachineInCompanyDto::fromEntity).collect(Collectors.toList());

        CompanyDto companyDto = CompanyDto.fromEntity(company, wallet, cycle, employees, suppliers, events, stockMaterials, stockFinalMaterials,machinesInCompany ,machineService, statisticListForDto);

        return companyDto;
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

            LocalDate creationDate = LocalDate.now();
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
            companyDao.save(company);
            Company companyResponse = companyDao.findLastCompanyByUserId(company.getUserId());
            cycleDao.save(1, 100, 50, 50, 0, 0,"None", companyResponse.getId());
            stockMaterialDao.save("Product",0, 0, 0, companyResponse.getId());
            for (int i = 0; i < 4; i++) {
                stockFinalMaterialDao.save("Product"+(i+1), 0, 0, 0, 0, 0, 0, 0, 0, companyResponse.getId());
            }

            // Réponse
            Map<String, Object> response = new HashMap<>();
            response.put("companyId", companyResponse.getId());
            response.put("sector", companyResponse.getSector());
            response.put("name", companyResponse.getName());
            response.put("creationDate", companyResponse.getCreationDate());
            response.put("popularity", companyResponse.getPopularity());
            response.put("localId", companyResponse.getLocalId());
            response.put("machineId", companyResponse.getMachineId());
            response.put("userId", companyResponse.getId());
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
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDtoFromBody) {
        Company companyFromBody = companyDao.findById(id);
        Company companyForUpdate = CompanyDto.companyFromDto(companyFromBody, companyDtoFromBody);
        companyDao.update(id, companyForUpdate);


        machineInCompanyDao.deleteAllByCompanyId(id);
        for (int i = 0; i < companyDtoFromBody.getMachinesInCompany().size(); i++) {
            machineInCompanyDao.save(companyDtoFromBody.getMachinesInCompany().get(i).getMachineId().toString(), id);
        }

        for (int i = 0; i < companyDtoFromBody.getEmployees().size(); i++) {
            System.out.println(companyDtoFromBody.getEmployees().get(i).getId());
            if(employeeDao.employeeExistsInCompany(companyDtoFromBody.getEmployees().get(i).getId(), companyDtoFromBody.getEmployees().get(i).getName())){
                employeeDao.update(companyDtoFromBody.getEmployees().get(i).getId(), EmployeeDto.convertInEntity(companyDtoFromBody.getEmployees().get(i)));
            }
            else{
                long idCompany = employeeDao.save(EmployeeDto.convertInEntity(companyDtoFromBody.getEmployees().get(i)));
                employeeDao.linkEmployeeToCompany(idCompany,id);
            }
        }
        stockMaterialDao.update(id, StockMaterialDto.dtoToEntity(companyDtoFromBody.getStockMaterial()));
        User newInfos = userDao.findById(companyDao.findById(id).getId());

        userDao.updateInfoGame(companyDao.findById(id).getId(), new UserDto(newInfos.getId(), newInfos.getEmail(), newInfos.getUsername(), newInfos.getRole(), new BigDecimal(companyDtoFromBody.getWallet())));

        CompanyDto newCompanyDto = getCompanyById2(id);

        return ResponseEntity.ok(newCompanyDto);
    }

    @PutMapping("/cycle/{id}")
    public ResponseEntity<CompanyDto> runCycleCompany(@PathVariable Long id, @RequestBody CompanyDto companyDtoFromBody) {
        Company companyFromBody = companyDao.findById(id);
        User userForUpdateWallet = userDao.findById(companyDtoFromBody.getUserId());


        Statistic statistic = new Statistic(0L, 1, companyDtoFromBody.getCycle().getStep(),0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 , new BigDecimal(0), new BigDecimal(0), 0, id);
        companyDtoFromBody.getStatistic().add(statistic);

        cycleService.runCycle(companyDtoFromBody);


        Company companyForUpdate = CompanyDto.companyFromDto(companyFromBody, companyDtoFromBody);
        companyDao.update(id, companyForUpdate);
        User newInfos = userDao.findById(companyDao.findById(id).getId());
        userDao.updateInfoGame(companyDao.findById(id).getId(), new UserDto(newInfos.getId(), newInfos.getEmail(), newInfos.getUsername(), newInfos.getRole(), new BigDecimal(companyDtoFromBody.getWallet())));
        cycleDao.update(companyDtoFromBody.getCycle().getId(), CycleDto.dtoToEntity(companyDtoFromBody.getCycle()));
        for (int i = 0; i < companyDtoFromBody.getEmployees().size(); i++) {
            employeeDao.update(companyDtoFromBody.getEmployees().get(i).getId(), EmployeeDto.convertInEntity(companyDtoFromBody.getEmployees().get(i)));
        }
        for (int i = 0; i < companyDtoFromBody.getStockFinalMaterials().size(); i++) {
            stockFinalMaterialDao.update(companyDtoFromBody.getStockFinalMaterials().get(i).getId(), StockFinalMaterialDto.dtoToEntity(companyDtoFromBody.getStockFinalMaterials().get(i)));
        }
        stockMaterialDao.update(companyDtoFromBody.getStockMaterial().getId(), StockMaterialDto.dtoToEntity(companyDtoFromBody.getStockMaterial()));
        statisticDao.save(companyDtoFromBody.getStatistic().getLast());


        CompanyDto newCompanyDto = companyDtoFromBody;

        return ResponseEntity.ok(newCompanyDto);
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