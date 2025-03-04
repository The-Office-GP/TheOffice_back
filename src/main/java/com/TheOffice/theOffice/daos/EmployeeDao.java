package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Employee.Gender;
import com.TheOffice.theOffice.entities.Employee.Status;
import com.TheOffice.theOffice.entities.Employee.Job;
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
            rs.getBytes("image")
    );

    public List<Employee> findAll() {
        String sql = "SELECT * FROM Employee";
        return jdbcTemplate.query(sql, employeeRowMapper);
    }

    public Employee findById(Long id) {
        String sql = "SELECT * FROM Employee WHERE id = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Salarié non trouvé"));
    }

    public int save(String name, String gender, Integer seniority, BigDecimal salary, Integer level, String mood, String status, String job, Integer health, byte[] image) {
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
            ps.setBytes(10, image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

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

    public boolean delete(Long id) {
        String sql = "DELETE FROM Employee WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean employeeExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Employee WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
