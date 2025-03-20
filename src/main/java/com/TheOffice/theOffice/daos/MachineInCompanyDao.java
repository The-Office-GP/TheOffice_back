package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.MachineInCompany;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

//GET, POST? DELETE
@Repository
public class MachineInCompanyDao {
    private final JdbcTemplate jdbcTemplate;

    public MachineInCompanyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MachineInCompany> machineInCompanyRowMapper = (rs, _) -> new MachineInCompany (
            rs.getLong("id"),
            rs.getLong("machineId"),
            rs.getLong("companyId")
    );

    //GET par id des machines de l'entreprise
    public MachineInCompany findById(Long id) {
        String sql = "SELECT * FROM MachineInCompany WHERE id = ?";
        return jdbcTemplate.query(sql, machineInCompanyRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Machine de l'entreprise non trouvée"));
    }

    //GET all
    public List<MachineInCompany> findAll() {
        String sql = "SELECT * FROM MachineInCompany";
        return jdbcTemplate.query(sql, machineInCompanyRowMapper);
    }

    //GET par id de l'entreprise
    public List<MachineInCompany> findByIdCompany(Long companyId) {
        String sql = "SELECT * FROM MachineInCompany WHERE id_company = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new MachineInCompany(rs.getLong("id"),
                                rs.getLong("id_machine"),
                                rs.getLong("id_company")),
                companyId
        );
    }

    //POST
    public long save (String machineId, Long companyId) {
        String sql = "INSERT INTO MachineInCompany (id_machine, id_company) VALUES (?, ?)";

        jdbcTemplate.update(sql, machineId, companyId);

        return companyId;
    }

    //DELETE
    public boolean delete(Long id) {
        String sql = "DELETE FROM MachineInCompany WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean deleteAllByCompanyId(Long companyId) {
        String sql = "DELETE FROM MachineInCompany WHERE id_company = ?";
        int rowsAffected = jdbcTemplate.update(sql, companyId);
        return rowsAffected > 0;
    }
}
