package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Cycle;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

// GET, POST, PUT
@Repository
public class CycleDao {

    private final JdbcTemplate jdbcTemplate;

    public CycleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cycle> cycleRowMapper = (rs, _) -> new Cycle(
            rs.getLong("id"),
            rs.getInt("production_speed"),
            rs.getInt("priority_production"),
            rs.getInt("count_good_sell"),
            rs.getInt("count_bad_sell"),
            rs.getLong("id_company")
    );

    // GET par id
    public Cycle findById(Long id) {
        String sql = "SELECT * FROM Cycle WHERE id = ?";
        return jdbcTemplate.query(sql, cycleRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cycle non trouvée"));
    }

    public Cycle findByIdCompany(Long id) {
        String sql = "SELECT * FROM Cycle WHERE id_company = ?";
        return jdbcTemplate.query(sql, cycleRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cycle non trouvée"));
    }

    // GET de tous les cycles
    public List<Cycle> findAll(){
        String sql = "SELECT * FROM Cycle";
        return jdbcTemplate.query(sql, cycleRowMapper);
    }

    // POST
    public int save(Integer productionSpeed, Integer prioritySpeed, Integer countGoodSell, Integer countBadSell, Long companyId) {
        String sql = "INSERT INTO Cycle (production_speed, priority_production, count_good_sell, count_bad_sell, id_company) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, productionSpeed);
            ps.setLong(2, prioritySpeed);
            ps.setLong(3, countGoodSell);
            ps.setLong(3, countBadSell);
            ps.setLong(4, companyId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // PUT
    public Cycle update(Long id, Cycle cycle) {
        if (!cycleExists(id)) {
            throw new ResourceNotFoundException("Cycle avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Cycle SET production_speed = ?, priority_production = ?, count_good_sell = ?, id_company = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, cycle.getProductionSpeed(), cycle.getPriorityProduction(), cycle.getCountGoodSell(), cycle.getCompanyId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du cycle avec l'ID : " + id);
        }
        return cycle;
    }

    //Vérifie si le cycle existe
    public boolean cycleExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Cycle WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
