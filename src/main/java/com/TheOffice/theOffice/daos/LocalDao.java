package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Cycle;
import com.TheOffice.theOffice.entities.Local.Local;
import com.TheOffice.theOffice.entities.Local.LocalLevel;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

//GET, POST, PUT
@Repository
public class LocalDao {

    private final JdbcTemplate jdbcTemplate;

    public LocalDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Local> localRowMapper = (rs, _) -> new Local(
            rs.getLong("id"),
            LocalLevel.valueOf(rs.getString("level")),
            rs.getInt("size"),
            rs.getBigDecimal("rent"),
            rs.getInt("maxEmployees"),
            rs.getInt("maxMachines"),
            rs.getBytes("background_image")
    );

    //GET de tous les locaux
    public List<Local> findAll() {
        String sql = "SELECT * FROM Local";
        return jdbcTemplate.query(sql, localRowMapper);
    }

    //GET par id
    public Local findById(Long id){
        String sql = "SELECT * FROM Local WHERE id = ?";
        return jdbcTemplate.query(sql, localRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Local non trouvé"));
    }

    //POST
    public int save(String level, Integer size, BigDecimal rent, Integer maxEmployees, Integer maxMachines, byte[] background_image) {
        String sql = "INSERT INTO Local (level, size, rent, maxEmployees, maxMachines, background_image) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, level);
            ps.setInt(2, size);
            ps.setBigDecimal(3, rent);
            ps.setInt(4, maxEmployees);
            ps.setInt(5, maxMachines);
            ps.setBytes(6, background_image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
    public Local update(Long id, Local local) {
        if (!localExists(id)) {
            throw new ResourceNotFoundException("Local avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Local SET level = ?, size = ?, rent = ?, maxEmployees = ?, maxMachines = ?, background_image = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, local.getLevel().name(), local.getSize(), local.getRent(), local.getMaxEmployees(), local.getMaxMachines(), local.getBackground_image(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du local avec l'ID : " + id);
        }
        return local;
    }

    //Vérifie si le local existe
    public boolean localExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Local WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
