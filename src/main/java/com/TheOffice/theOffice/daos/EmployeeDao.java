package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Employee.Gender;
import com.TheOffice.theOffice.entities.Employee.Status;
import com.TheOffice.theOffice.entities.Employee.Job;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class EmployeeDao {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    //GET de tous les salariés
    public List<Employee> findAll() {
        String sql = "SELECT * FROM Employee";
        return jdbcTemplate.query(sql, employeeRowMapper);
    }

    //GET par id
    public Employee findById(Long id) {
        String sql = "SELECT * FROM Employee WHERE id = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Salarié non trouvé"));
    }

    //GET par id de l'entreprise
    public List<Employee> findByIdCompany(Long companyId) {
        String sql = "SELECT e.* FROM Employee e " +
                "JOIN EmployeeInCompany eic ON e.id = eic.id_employee " +
                "WHERE eic.id_company = ?";

        return jdbcTemplate.query(sql, employeeRowMapper, companyId);
    }

    //POST
    /*public Employee save(Employee employee) {
        String sql = "INSERT INTO Employee (name, gender, seniority, salary, level, mood, status, job, health, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println(employee.getGender());
        System.out.println(employee.getMood());
        System.out.println(employee.getJob());

        jdbcTemplate.update(sql, employee.getName(), "HOMME", employee.getSeniority(), employee.getSalary(), employee.getLevel(), "NEUTRE", "ACTIF", "PRODUCTION", employee.getHealth(), employee.getImage());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        long id = jdbcTemplate.queryForObject(sqlGetId, long.class);

        employee.setId(id);
        return employee;

    }*/

    public long save(Employee employee) {
        String sql = "INSERT INTO Employee (name, gender, seniority, salary, level, mood, status, job, health, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Utilisation de KeyHolder pour récupérer l'ID généré
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Préparer la requête d'insertion
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(java.sql.Connection connection) throws java.sql.SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, employee.getName());
                        ps.setString(2, employee.getGender().toString());
                        ps.setInt(3, employee.getSeniority());
                        ps.setBigDecimal(4, employee.getSalary());
                        ps.setInt(5, employee.getLevel());
                        ps.setString(6, employee.getMood().toString());  // Exemple de valeur pour mood
                        ps.setString(7, employee.getStatus().toString());   // Exemple de valeur pour status
                        ps.setString(8, employee.getJob().toString());  // Exemple de valeur pour job
                        ps.setLong(9, employee.getHealth());
                        ps.setString(10, employee.getImage()); // Supposons que `image` soit un tableau de bytes
                        return ps;
                    }
                },
                keyHolder);

        // Obtenir l'ID généré
        long id = keyHolder.getKey().longValue();

        // Mettre à jour l'ID de l'employé
        employee.setId(id);

        return id;
    }


    //PUT
    public Employee update(Long id, Employee employee) {
        if (!employeeExists(id)) {
            throw new ResourceNotFoundException("Salarié avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Employee SET name = ?, gender = ?, seniority = ?, salary = ?, level = ?, mood = ?, status = ?, job = ?, health = ?, image = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                employee.getName(),
                employee.getGender().name(),
                employee.getSeniority(),
                employee.getSalary(),
                employee.getLevel(),
                employee.getMood().name(),
                employee.getStatus().name(),
                employee.getJob().name(),
                employee.getHealth(),
                employee.getImage(),
                id
        );

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du salarié avec l'ID : " + id);
        }
        return employee;
    }

    //DELETE
    public boolean delete(Long id) {
        String sql = "DELETE FROM Employee WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    //Vérifie si le salarié existe
    public boolean employeeExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Employee WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    //Lier un salarié à une entreprise
    public void linkEmployeeToCompany(Long employeeId, Long companyId) {
        String sql = "INSERT INTO EmployeeInCompany (id_employee, id_company) VALUES (?, ?)";
        jdbcTemplate.update(sql, employeeId, companyId);
    }
}
