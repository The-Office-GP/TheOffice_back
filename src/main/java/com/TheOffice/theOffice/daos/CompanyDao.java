package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.classes.Local;
import com.TheOffice.theOffice.dataLoader.LocalDataLoader;
import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Employee.*;
import com.TheOffice.theOffice.entities.Event;
import com.TheOffice.theOffice.classes.Machine;
import com.TheOffice.theOffice.entities.Machine.ProductionQuality;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CompanyDao {

    private final JdbcTemplate jdbcTemplate;
    private LocalDataLoader localDataLoader;

    public CompanyDao(JdbcTemplate jdbcTemplate, LocalDataLoader localDataLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.localDataLoader = localDataLoader;
    }

    //RowMapper pour les entreprises
    private final RowMapper<Company> companyRowMapper = (rs, rowNum) -> {
        try {
            Long id_local = rs.getLong("id_local");

            // Charger l'objet Local depuis localDataLoader
            Local local = LocalDataLoader.getInstance().getLocalById(id_local)
                    .orElseThrow(() -> new ResourceNotFoundException("Local non trouvé pour id " + id_local));

            return new Company(
                    rs.getLong("id"),
                    rs.getString("sector"),
                    rs.getString("name"),
                    rs.getDate("creation_date"),
                    id_local,
                    local, // Remplace id_local par l'objet complet
                    rs.getLong("id_user"),
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };

    //RowMapper pour les machines
    private final RowMapper<Machine> machineRowMapper = (rs, _) -> new Machine(
            rs.getLong("id"),
            rs.getString("name"),
            ProductionQuality.valueOf(rs.getString("production_quality")),
            rs.getBigDecimal("price"),
            rs.getBigDecimal("maintenance_cost"),
            rs.getString("image")
    );

    //RowMapper pour les employés
    private final RowMapper<Employee> employeeRowMapper = (rs, _) -> new Employee(
            rs.getLong("id"),
            rs.getString("name"),
            Gender.valueOf(rs.getString("gender")),
            rs.getInt("seniority"),
            rs.getBigDecimal("salary"),
            rs.getInt("level"),
            Mood.valueOf(rs.getString("mood")),
            Status.valueOf(rs.getString("status")),
            Job.valueOf(rs.getString("job")),
            rs.getInt("health"),
            rs.getString("image")
    );

    //RowMapper pour les events
    private final RowMapper<Event> eventRowMapper = (rs, _) -> new Event(
            rs.getLong("id"),
            rs.getLong("recurrence"),
            rs.getString("image")
    );

    //GET par id
    public Company findById(Long id) {
        // 1. Récupérer l'entreprise
        String sqlCompany = "SELECT * FROM Company WHERE id = ?";
        Company company = jdbcTemplate.query(sqlCompany, companyRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise non trouvée"));

        // 2. Récupérer les machines de l'entreprise
        String sqlMachines = "SELECT m.* FROM Machine m JOIN MachineInCompany mic ON m.id = mic.id_machine WHERE mic.id_company = ?";
        List<Machine> machines = jdbcTemplate.query(sqlMachines, machineRowMapper, id);
        company.setMachines(machines);

        // 3. Récupérer les employés de l'entreprise
        String sqlEmployees = "SELECT e.* FROM Employee e JOIN EmployeeInCompany eic ON e.id = eic.id_employee WHERE eic.id_company = ?";
        List<Employee> employees = jdbcTemplate.query(sqlEmployees, employeeRowMapper, id);
        company.setEmployees(employees);

        // 4. Récupérer les events de l'entreprise
        String sqlEvents = "SELECT e.* FROM Event e JOIN CompanyEvent ce ON e.id = ce.id_event WHERE ce.id_company = ?";
        List<Event> events = jdbcTemplate.query(sqlEvents, eventRowMapper, id);
        company.setEvents(events);

        return company;
    }

    public Company findByIdAndUser(Long companyId, Long userId) {
        String sql = "SELECT * FROM Company WHERE id = ? AND id_user = ?";
        return jdbcTemplate.query(sql, companyRowMapper, companyId, userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Vous n'avez pas accès à cette entreprise"));
    }

    //GET par nom
    public Optional<Company> findByName(String name) {
        String sql = "SELECT * FROM Company WHERE name = ?";
        return Optional.ofNullable(jdbcTemplate.query(sql, companyRowMapper, name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise non trouvée")));
    }

    //GET de toutes les entreprises
    public List<Company> findAll() {
        String sql = "SELECT * FROM Company";
        return jdbcTemplate.query(sql, companyRowMapper);
    }

    //GET par id de l'utilisateur
    public List<Company> findByUserId(Long userId) {
        String sql = "SELECT * FROM Company WHERE id_user = ?"; // 🔥 Filtrer uniquement les entreprises du user connecté
        System.out.println("Requête SQL exécutée: " + sql + " avec id_user = " + userId); // 🔥 Debug log
        List<Company> companies = jdbcTemplate.query(sql, companyRowMapper, userId);
        System.out.println("Entreprises trouvées: " + companies.size()); // 🔥 Vérifier si la requête retourne bien des résultats
        return companies;
    }

    //POST
    public int save(String sector, String name, Date creation_date, Long id_local, Long id_user) {
        String sql = "INSERT INTO Company (sector, name, creation_date, id_local, id_user) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sector);
            ps.setString(2, name);
            ps.setDate(3, new java.sql.Date(creation_date.getTime()));
            ps.setObject(4, id_local);  // ✅ Index correct
            ps.setLong(5, id_user);     // ✅ Correction ici (était 4)
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
    public Company update(Long id, Company company) {
        if (!companyExists(id)) {
            throw new ResourceNotFoundException("Entreprise avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Company SET sector = ?, name = ?, creation_date = ?,id_local = ?, id_user = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, company.getSector(), company.getName(), company.getCreation_date(),company.getId_local(), company.getId_user(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de l'entreprise avec l'ID : " + id);
        }
        return company;
    }

    //DELETE
    public boolean delete(Long id) {
        String sql = "DELETE FROM Company WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // Vérifier si une entreprise existe
    public boolean companyExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Company WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    // Récupérer toutes les machines d'une entreprise spécifique
    public List<Machine> findMachinesByCompanyId(Long companyId) {
        String sql = " SELECT Machine.* FROM Machine INNER JOIN MachineInCompany mic ON Machine.id = mic.id_machine WHERE mic.id_company = ? ";
        return jdbcTemplate.query(sql, machineRowMapper, companyId);
    }

    // Associer une machine à une entreprise
    public void addMachineToCompany(Long companyId, Long machineId) {
        String sql = "INSERT INTO MachineInCompany (id_machine, id_company) VALUES (?, ?)";
        jdbcTemplate.update(sql, machineId, companyId);
    }

    // Récupérer tous les employés d'une entreprise spécifique
    public List<Employee> findEmployeesByCompanyId(Long companyId){
        String sql = "SELECT Employee.* FROM Employee INNER JOIN EmployeeInCompany eic ON Employee.id = eic.id_employee WHERE eic.id_company = ?";
        return jdbcTemplate.query(sql,employeeRowMapper, companyId);
    }

    // Associer un employé à une entreprise
    public void addEmployeeToCompany(Long companyId, Long employeeId){
        String sql = "INSERT INTO EmployeeInCompany (id_employee, id_company) VALUES (?,?)";
        jdbcTemplate.update(sql, employeeId, companyId);
    }

    // Récupérer tous les events d'une entreprise spécifique
    public List<Event> findEventsByCompanyId(Long companyId){
        String sql = "SELECT Event.* FROM Event INNER JOIN CompanyEvent ce ON Event.id = ce.id_event WHERE ce.id_company = ?";
        return jdbcTemplate.query(sql,eventRowMapper, companyId);
    }

    // Associer un event à une entreprise
    public void addEventToCompany(Long companyId, Long eventId){
        String sql = "INSERT INTO CompanyEvent (id_event, id_company) VALUES (?,?)";
        jdbcTemplate.update(sql, eventId, companyId);
    }
}