package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.StockFinalMaterial;
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
            rs.getInt("quantity_low"),
            rs.getInt("quantity_mid"),
            rs.getInt("quantity_high"),
            rs.getInt("proportion_product"),
            rs.getInt("quantity_to_product"),
            rs.getInt("month_production"),
            rs.getInt("sell"),
            rs.getInt("price"),
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
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new StockFinalMaterial(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("quantity_low"),
                        rs.getInt("quantity_mid"),
                        rs.getInt("quantity_high"),
                        rs.getInt("proportion_product"),
                        rs.getInt("quantity_to_product"),
                        rs.getInt("month_production"),
                        rs.getInt("sell"),
                        rs.getInt("price"),
                        companyId
                )
        );
    }

    //POST
    public int save(String name, Integer quantityLow, Integer quantityMid, Integer quantityHigh, Integer proportionProduct, Integer quantityToProduct, Integer monthProduction, Integer sell, Integer price, Long companyId) {
        // Requête SQL corrigée pour inclure les trois quantités distinctes
        String sql = "INSERT INTO StockFinalMaterial (name, quantity_low, quantity_mid, quantity_high, proportion_product, quantity_to_product, month_production, sell, price, id_company) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, quantityLow);  // Insertion dans quantity_low
            ps.setInt(3, quantityMid);  // Insertion dans quantity_mid
            ps.setInt(4, quantityHigh); // Insertion dans quantity_high
            ps.setInt(5, proportionProduct);
            ps.setInt(6, quantityToProduct);
            ps.setInt(7, monthProduction);
            ps.setInt(8, sell);
            ps.setInt(9, price);
            ps.setLong(10, companyId);   // Insertion dans id_company
            return ps;
        }, keyHolder);

        // Retourner l'ID généré
        return keyHolder.getKey().intValue();
    }



    //PUT
    public StockFinalMaterial update(Long id, StockFinalMaterial stockFinalMaterial) {
        if (!stockFinalMaterialExists(id)) {
            throw new ResourceNotFoundException("Stock des produits finaux avec l'ID : " + id + " n'existe pas");
        }

        // Requête SQL mise à jour pour inclure toutes les colonnes pertinentes
        String sql = "UPDATE StockFinalMaterial SET name = ?, quantity_low = ?, quantity_mid = ?, quantity_high = ?, proportion_product = ?, quantity_to_product = ?, month_production = ?, sell = ?, month_sell = ?, id_company = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                stockFinalMaterial.getName(),
                stockFinalMaterial.getQuantityLow(),
                stockFinalMaterial.getQuantityMid(),
                stockFinalMaterial.getQuantityHigh(),
                stockFinalMaterial.getProportionProduct(),
                stockFinalMaterial.getQuantityToProduct(),
                stockFinalMaterial.getMonthProduction(),
                stockFinalMaterial.getSell(),
                stockFinalMaterial.getPrice(),
                stockFinalMaterial.getCompanyId(),
                id
        );

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
