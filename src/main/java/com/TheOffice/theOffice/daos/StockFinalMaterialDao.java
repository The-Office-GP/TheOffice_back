package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.StockFinalMaterial;
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
public class StockFinalMaterialDao {

    private final JdbcTemplate jdbcTemplate;

    public StockFinalMaterialDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<StockFinalMaterial> stockFinalMaterialRowMapper = (rs, rowNum) -> new StockFinalMaterial(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("quality"),
            rs.getInt("quantity"),
            rs.getLong("companyId")
    );

    //GET de tous les stocks des produits finaux
    public List<StockFinalMaterial> findAll() {
        String sql = "SELECT * FROM StockFinalMaterial";
        return jdbcTemplate.query(sql, stockFinalMaterialRowMapper);
    }

    //GET par id
    public StockFinalMaterial findById(Long id){
        String sql ="SELECT * FROM StockFinalMaterial WHERE id = ?";
        return jdbcTemplate.query(sql, stockFinalMaterialRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Stock des produits finaux non trouvé"));
    }

    //GET par id de l'entreprise
    public List<StockFinalMaterial> findByIdCompany(Long companyId) {
        String sql = "SELECT * FROM StockFinalMaterial WHERE id_company = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new StockFinalMaterial(rs.getLong("id"),
                                rs.getString("name"),
                                rs.getInt("quality"),
                                rs.getInt("quantity"),
                                rs.getLong("id_company")),
                companyId
        );
    }

    //POST
    public int save(String name, Integer quality, Integer quantity, Long companyId) {
        String sql = "INSERT INTO StockFinalMaterial (name, quality, quantity, id_company) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, quality);
            ps.setInt(3, quantity);
            ps.setLong(4, companyId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
    public StockFinalMaterial update(Long id, StockFinalMaterial stockFinalMaterial) {
        if (!stockFinalMaterialExists(id)) {
            throw new ResourceNotFoundException("Stock des produits finaux avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE StockFinalMaterial SET name = ?, quality = ?, quantity = ?, id_company = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, stockFinalMaterial.getName(), stockFinalMaterial.getQuality(), stockFinalMaterial.getQuantity(), stockFinalMaterial.getCompanyId(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour du stock des produits finaux avec l'ID : " + id);
        }
        return stockFinalMaterial;
    }

    //Vérifier si le stock des produits finaux existe
    public boolean stockFinalMaterialExists(Long id) {
        String sql = "SELECT COUNT(*) FROM StockFinalMaterial WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
