package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Cycle;
import com.TheOffice.theOffice.entities.StockMaterial;
import com.TheOffice.theOffice.entities.Supplier;
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

//GET, POST, PUT
@Repository
public class SupplierDao {

    private final JdbcTemplate jdbcTemplate;

    public SupplierDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Supplier> supplierRowMapper = (rs, rowNum) -> new Supplier(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            rs.getString("quality"),
            rs.getLong("companyId")
    );

    //GET de tous les fournisseurs
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM Supplier";
        return jdbcTemplate.query(sql, supplierRowMapper);
    }

    //GET par id de l'entreprise
    public List<Supplier> findByIdCompany(Long companyId) {
        String sql = "SELECT * FROM Supplier WHERE id_company = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Supplier(rs.getLong("id"),
                                rs.getString("name"),
                                rs.getBigDecimal("price"),
                                rs.getString("quality"),
                                rs.getLong("id_company")),
                companyId
        );
    }

    //GET par id
    public Supplier findById(Long id) {
        String sql = "SELECT * FROM Supplier WHERE id = ?";
        return jdbcTemplate.query(sql,supplierRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Fournisseur non trouvé"));
    }

    //POST
    public int save(String name, BigDecimal price, String quality, Long companyId) {
        String sql = "INSERT INTO Supplier (name, price, quality, id_company) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setBigDecimal(2, price);
            ps.setString(3, quality);
            ps.setLong(4, companyId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
    //PUT
    public Supplier update(Long id, Supplier supplier) {
        if (!supplierExists(id)) {
            throw new ResourceNotFoundException("Fournisseur avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Supplier SET name = ?, price = ?, quality = ?, id_company = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, supplier.getName(), supplier.getPrice(), supplier.getQuality(), supplier.getCompanyId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du fournisseur avec l'ID : " + id);
        }
        return supplier;
    }

    public boolean supplierExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Supplier WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
