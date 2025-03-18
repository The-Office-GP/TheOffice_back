package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.staticModels.Machine;
import com.TheOffice.theOffice.entities.Machine.ProductionQuality;
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

//GET, POST, PULL
@Repository
public class MachineDao {

    private final JdbcTemplate jdbcTemplate;

    public MachineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Machine> machineRowMapper = (rs, _) -> new Machine (
            rs.getLong("id"),
            rs.getString("name"),
            ProductionQuality.valueOf(rs.getString("production_quality")),
            rs.getBigDecimal("price"),
            rs.getBigDecimal("maintenance_cost"),
            rs.getString("image")
    );

    //GET par id de l'entreprise
    public List<Machine> findByIdCompany(Long id_company) {
        String sql = "SELECT m.* FROM Machine m " +
                "JOIN MachineInCompany mic ON m.id = mic.id_machine " +
                "WHERE mic.id_company = ?";

        return jdbcTemplate.query(sql, machineRowMapper, id_company);
    }

    //GET de toutes les machines
    public List<Machine> findAll(){
        String sql = "SELECT * FROM Machine";
        return jdbcTemplate.query(sql, machineRowMapper);
    }

    //GET par id
    public Machine findById(Long id){
        String sql = "SELECT * FROM Machine WHERE id = ?";
        return jdbcTemplate.query(sql, machineRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Machine non trouvée"));
    }

    //POST
    public int save(String name, String production_quality, BigDecimal price, BigDecimal maintenance_cost, String image) {
        String sql ="INSERT INTO Machine (name, production_quality, price, maintenance_cost, image) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, production_quality);
            ps.setBigDecimal(3, price);
            ps.setBigDecimal(4, maintenance_cost);
            ps.setString(5, image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
    public Machine update(Long id, Machine machine) {
        if (!machineExists(id)) {
            throw new ResourceNotFoundException("Machine avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Machine SET name = ?, production_quality = ?, price = ?, maintenance_cost = ?, image = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, machine.getName(), machine.getProduction_quality().name(), machine.getPrice(), machine.getMaintenance_cost(), machine.getImage(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de la machine avec l'ID : " + id);
        }
        return machine;
    }

    //DELETE
    public boolean delete(Long id) {
        String sql = "DELETE FROM Machine WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    //Vérifie si la machine existe
    public boolean machineExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Machine WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    //Lier une machine à une entreprise
    public void linkMachineToCompany(Long machineId, Long companyId) {
        String sql = "INSERT INTO MachineInCompany (id_machine, id_company) VALUES (?, ?)";
        jdbcTemplate.update(sql, machineId, companyId);
    }
}