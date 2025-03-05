package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Machine.Machine;
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
import java.util.Date;
import java.util.List;

@Repository
public class CompanyDao {

    private final JdbcTemplate jdbcTemplate;

    public CompanyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Company> companyRowMapper = (rs, _) -> new Company(
            rs.getLong("id"),
            rs.getString("sector"),
            rs.getString("name"),
            rs.getDate("creation_date"),
            rs.getLong("id_user"),
            null
    );

    private final RowMapper<Machine> machineRowMapper = (rs, _) -> new Machine(
            rs.getLong("id"),
            rs.getString("name"),
            ProductionQuality.valueOf(rs.getString("production_quality")),
            rs.getBigDecimal("price"),
            rs.getBigDecimal("maintenance_cost"),
            rs.getBytes("image")
    );

    public Company findById(Long id) {
        String sql = "SELECT * FROM Company WHERE id = ?";
        Company company = jdbcTemplate.query(sql, companyRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise non trouvée"));

        // Ajouter les machines associées
        company.setMachines(findMachinesByCompanyId(id));

        return company;
    }


    public List<Company> findAll() {
        String sql = "SELECT * FROM Company";
        return jdbcTemplate.query(sql, companyRowMapper);
    }

    public int save(String sector, String name, Date creation_date, Long id_user) {
        String sql = "INSERT INTO Company (sector, name, creation_date, id_user) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sector);
            ps.setString(2, name);
            ps.setDate(3, new java.sql.Date(creation_date.getTime()));
            ps.setLong(4, id_user);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Company update(Long id, Company company) {
        if (!companyExists(id)) {
            throw new ResourceNotFoundException("Entreprise avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Company SET sector = ?, name = ?, creation_date = ?, id_user = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, company.getSector(), company.getName(), company.getCreation_date(), company.getId_user(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de l'entreprise avec l'ID : " + id);
        }
        return company;
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM Company WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean companyExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Company WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    // 🔹 Récupérer toutes les machines d'une entreprise spécifique
    public List<Machine> findMachinesByCompanyId(Long companyId) {
        String sql = " SELECT Machine.* FROM Machine INNER JOIN MachineInCompany mic ON Machine.id = mic.id_machine WHERE mic.id_company = ? ";
        return jdbcTemplate.query(sql, machineRowMapper, companyId);
    }

    // 🔹 Associer une machine à une entreprise
    public void addMachineToCompany(Long companyId, Long machineId) {
        String sql = "INSERT INTO MachineInCompany (id_machine, id_company) VALUES (?, ?)";
        jdbcTemplate.update(sql, machineId, companyId);
    }
}
