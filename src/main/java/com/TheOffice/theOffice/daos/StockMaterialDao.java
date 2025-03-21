package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Cycle;
import com.TheOffice.theOffice.entities.StockMaterial;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

//GET, POST, PUT
@Repository
public class StockMaterialDao {

    private final JdbcTemplate jdbcTemplate;

    public StockMaterialDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StockMaterial> stockMaterialRowMapper = (rs, rowNum) -> new StockMaterial(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("quantity_low"),
            rs.getInt("quantity_mid"),
            rs.getInt("quantity_high"),
            rs.getLong("id_company")
    );

    //GET de tous les stocks des produits
    public List<StockMaterial> findAll() {
        String sql = "SELECT * FROM StockMaterial";
        return jdbcTemplate.query(sql, stockMaterialRowMapper);
    }

    //GET par id
    public StockMaterial findById(Long id) {
        String sql = "SELECT * FROM StockMaterial WHERE id = ?";
        return jdbcTemplate.query(sql,stockMaterialRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Stock des produits non trouvé"));
    }

    public StockMaterial findByIdCompany(Long companyId) {
        String sql = "SELECT * FROM StockMaterial WHERE id_company = ?";
        return jdbcTemplate.query(sql,stockMaterialRowMapper, companyId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Stock des produits non trouvé"));
    }


    //POST
    public int save(String name, Integer quantityLow, Integer quantityMid, Integer quantityHigh, Long companyId) {
        // La requête SQL pour insérer dans les trois colonnes de quantité
        String sql = "INSERT INTO StockMaterial (name, quantity_low, quantity_mid, quantity_high, id_company) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Utilisation de jdbcTemplate pour exécuter la requête et récupérer l'ID généré
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, quantityLow);
            ps.setInt(3, quantityMid);
            ps.setInt(4, quantityHigh);
            ps.setLong(5, companyId);
            return ps;
        }, keyHolder);

        // Retourne l'ID généré
        return keyHolder.getKey().intValue();
    }


    //PUT
    /*public StockMaterial update(Long id, StockMaterial stockMaterial) {
        if (!stockMaterialExists(id)) {
            throw new ResourceNotFoundException("Stock des produits avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE StockMaterial SET name = ?, quantity = ?, id_company = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, stockMaterial.getName(), stockMaterial.getQuantity(), stockMaterial.getCompanyId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du stock des produits avec l'ID : " + id);
        }
        return stockMaterial;
    }*/

    public boolean stockMaterialExists(Long id) {
        String sql = "SELECT COUNT(*) FROM StockMaterial WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
