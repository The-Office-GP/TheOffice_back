package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Employee.Gender;
import com.TheOffice.theOffice.entities.Employee.Status;
import com.TheOffice.theOffice.entities.Employee.Job;
import com.TheOffice.theOffice.entities.Machine.Machine;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public List<Employee> findByIdCompany(Long id_company) {
        String sql = "SELECT e.* FROM Employee e " +
                "JOIN EmployeeInCompany eic ON e.id = eic.id_employee " +
                "WHERE eic.id_company = ?";

        return jdbcTemplate.query(sql, employeeRowMapper, id_company);
    }

    //POST
    public int save(String name, String gender, Integer seniority, BigDecimal salary, Integer level, String mood, String status, String job, Integer health, String image) {
        String sql = "INSERT INTO Employee (name, gender, seniority, salary, level, mood, status, job, health, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setInt(3, seniority);
            ps.setBigDecimal(4, salary);
            ps.setInt(5, level);
            ps.setString(6, mood);
            ps.setString(7, status);
            ps.setString(8, job);
            ps.setInt(9, health);
            ps.setString(10, image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
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
