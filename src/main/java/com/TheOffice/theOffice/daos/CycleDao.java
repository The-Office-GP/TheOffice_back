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
            rs.getInt("step"),
            rs.getInt("production_speed"),
            rs.getInt("priority_production"),
            rs.getInt("priority_marketing"),
            rs.getInt("count_good_sell"),
            rs.getInt("count_bad_sell"),
            rs.getString("trend"),
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
    public int save(Integer step, Integer productionSpeed, Integer priorityProduction, Integer priorityMarketing, Integer countGoodSell, Integer countBadSell, String trend, Long companyId) {
        String sql = "INSERT INTO Cycle (step, production_speed, priority_production, priority_marketing, count_good_sell, count_bad_sell, trend, id_company) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, step);
            ps.setInt(2, productionSpeed);
            ps.setInt(3, priorityProduction);
            ps.setInt(4, priorityMarketing);
            ps.setInt(5, countGoodSell);
            ps.setInt(6, countBadSell);
            ps.setString(7, trend);
            ps.setLong(8, companyId);

            return ps;
        }, keyHolder);


        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue();
        } else {
            throw new RuntimeException("Erreur lors de l'insertion dans la table Cycle, clé générée non trouvée.");
        }
    }


    // PUT
    public Cycle update(Long id, Cycle cycle) {
        if (!cycleExists(id)) {
            throw new ResourceNotFoundException("Cycle avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Cycle SET step = ?, production_speed = ?, priority_production = ?, priority_marketing = ?, count_good_sell = ?, id_company = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, cycle.getStep(), cycle.getProductionSpeed(), cycle.getPriorityProduction(), cycle.getPriorityMarketing(), cycle.getCountGoodSell(), cycle.getCompanyId(), id);

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
