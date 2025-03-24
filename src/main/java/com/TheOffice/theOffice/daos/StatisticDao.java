package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Statistic;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticDao {

    private final JdbcTemplate jdbcTemplate;

    public StatisticDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Mise à jour du RowMapper pour correspondre aux nouvelles colonnes
    private final RowMapper<Statistic> statisticRowMapper = (rs, _) -> new Statistic(
            rs.getLong("id"),
            rs.getInt("year"),
            rs.getInt("month"),
            rs.getInt("product1_low_qty_sell"),
            rs.getInt("product1_mid_qty_sell"),
            rs.getInt("product1_high_qty_sell"),
            rs.getInt("product2_low_qty_sell"),
            rs.getInt("product2_mid_qty_sell"),
            rs.getInt("product2_high_qty_sell"),
            rs.getInt("product3_low_qty_sell"),
            rs.getInt("product3_mid_qty_sell"),
            rs.getInt("product3_high_qty_sell"),
            rs.getInt("product1_low_qty_prod"),
            rs.getInt("product1_mid_qty_prod"),
            rs.getInt("product1_high_qty_prod"),
            rs.getInt("product2_low_qty_prod"),
            rs.getInt("product2_mid_qty_prod"),
            rs.getInt("product2_high_qty_prod"),
            rs.getInt("product3_low_qty_prod"),
            rs.getInt("product3_mid_qty_prod"),
            rs.getInt("product3_high_qty_prod"),
            rs.getInt("product4_low_qty_prod"),
            rs.getInt("product4_mid_qty_prod"),
            rs.getInt("product4_high_qty_prod"),
            rs.getInt("material_low_qty"),
            rs.getInt("material_mid_qty"),
            rs.getInt("material_high_qty"),
            rs.getBigDecimal("total_incomes"),
            rs.getBigDecimal("total_expenses"),
            rs.getLong("popularity"),
            rs.getLong("id_company")
    );

    // Méthode pour trouver un statistique par son id
    public Statistic findById(Long id) {
        String sql = "SELECT * FROM Statistic WHERE id = ?";
        return jdbcTemplate.query(sql, statisticRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Statistic with ID " + id + " not found"));
    }

    // Méthode pour récupérer toutes les statistiques d'une entreprise
    public List<Statistic> findAllCompanyStatistic(Long idCompany) {
        String sql = "SELECT * FROM Statistic WHERE id_company = ?";
        return jdbcTemplate.query(sql, statisticRowMapper, idCompany);
    }

    // Méthode pour sauvegarder une nouvelle statistique
    public void save(Statistic statistic) {
        String sql = "INSERT INTO Statistic (year, month, product1_low_qty_sell, product1_mid_qty_sell, product1_high_qty_sell, " +
                "product2_low_qty_sell, product2_mid_qty_sell, product2_high_qty_sell, product3_low_qty_sell, product3_mid_qty_sell, " +
                "product3_high_qty_sell, product1_low_qty_prod, product1_mid_qty_prod, product1_high_qty_prod, " +
                "product2_low_qty_prod, product2_mid_qty_prod, product2_high_qty_prod, product3_low_qty_prod, " +
                "product3_mid_qty_prod, product3_high_qty_prod, product4_low_qty_prod, product4_mid_qty_prod, product4_high_qty_prod, material_low_qty, material_mid_qty, material_high_qty, " +
                "total_incomes, total_expenses, popularity, id_company) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                statistic.getYear(),
                statistic.getMonth(),
                statistic.getProduct1LowQtySell(),
                statistic.getProduct1MidQtySell(),
                statistic.getProduct1HighQtySell(),
                statistic.getProduct2LowQtySell(),
                statistic.getProduct2MidQtySell(),
                statistic.getProduct2HighQtySell(),
                statistic.getProduct3LowQtySell(),
                statistic.getProduct3MidQtySell(),
                statistic.getProduct3HighQtySell(),
                statistic.getProduct1LowQtyProd(),
                statistic.getProduct1MidQtyProd(),
                statistic.getProduct1HighQtyProd(),
                statistic.getProduct2LowQtyProd(),
                statistic.getProduct2MidQtyProd(),
                statistic.getProduct2HighQtyProd(),
                statistic.getProduct3LowQtyProd(),
                statistic.getProduct3MidQtyProd(),
                statistic.getProduct3HighQtyProd(),
                statistic.getProduct4LowQtyProd(),
                statistic.getProduct4MidQtyProd(),
                statistic.getProduct4HighQtyProd(),
                statistic.getMaterialLowQty(),
                statistic.getMaterialMidQty(),
                statistic.getMaterialHighQty(),
                statistic.getTotalIncomes(),
                statistic.getTotalExpenses(),
                statistic.getPopularity(),
                statistic.getIdCompany()); // Assurez-vous qu'il y a bien 24 paramètres ici
    }


    // Méthode pour supprimer une statistique par son id
    public boolean delete(Long id) {
        String sql = "DELETE FROM Statistic WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // Vérifier si une statistique existe
    public boolean statisticExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Statistic WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
