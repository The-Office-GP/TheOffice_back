package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Statistic;
import com.TheOffice.theOffice.entities.User;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StatiticDao {
    private final JdbcTemplate jdbcTemplate;

    public StatiticDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Statistic> statisticRowMapper = (rs, _) -> new Statistic(
            rs.getLong("id"),
            rs.getInt("year"),
            rs.getInt("month"),
            rs.getInt("product1_low_qty"),
            rs.getInt("product1_mid_qty"),
            rs.getInt("product1_high_qty"),
            rs.getInt("product2_low_qty"),
            rs.getInt("product2_mid_qty"),
            rs.getInt("product2_high_qty"),
            rs.getInt("product3_low_qty"),
            rs.getInt("product3_mid_qty"),
            rs.getInt("product3_high_qty"),
            rs.getBigDecimal("total_incomes"),
            rs.getBigDecimal("total_expenses"),
            rs.getLong("id_company")
    );

    public Statistic findById(Long id) {
        String sqlCompany = "SELECT * FROM Statistic WHERE id = ?";
        Statistic statistic = jdbcTemplate.query(sqlCompany, statisticRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise non trouvée"));

        return statistic;
    }

    public List<Statistic> findAll(Long idCompany) {
        String sql = "SELECT * FROM Statistic WHERE id_company = ?";
        return jdbcTemplate.query(sql, statisticRowMapper, idCompany);
    }

    public void save(Statistic statistic) {
        String sql = "INSERT INTO Statistic (year, month, product1_low_qty, product1_mid_qty, product1_high_qty, " +
                "product2_low_qty, product2_mid_qty, product2_high_qty, product3_low_qty, product3_mid_qty, product3_high_qty, " +
                "total_incomes, total_expenses, id_company) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                statistic.getYear(),
                statistic.getMonth(),
                statistic.getProduct1LowQty(),
                statistic.getProduct1MidQty(),
                statistic.getProduct1HighQty(),
                statistic.getProduct2LowQty(),
                statistic.getProduct2MidQty(),
                statistic.getProduct2HighQty(),
                statistic.getProduct3LowQty(),
                statistic.getProduct3MidQty(),
                statistic.getProduct3HighQty(),
                statistic.getTotalIncomes(),
                statistic.getTotalExpenses(),
                statistic.getIdCompany());

    }

    public Statistic update(Long id, Statistic statistic) {
        if (!statisticExists(id)) {
            throw new ResourceNotFoundException("Statistique avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Statistic SET " +
                "year = ?, " +
                "month = ?, " +
                "product1_low_qty = ?, " +
                "product1_mid_qty = ?, " +
                "product1_high_qty = ?, " +
                "product2_low_qty = ?, " +
                "product2_mid_qty = ?, " +
                "product2_high_qty = ?, " +
                "product3_low_qty = ?, " +
                "product3_mid_qty = ?, " +
                "product3_high_qty = ?, " +
                "total_incomes = ?, " +
                "total_expenses = ? " +
                "WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                statistic.getYear(),
                statistic.getMonth(),
                statistic.getProduct1LowQty(),
                statistic.getProduct1MidQty(),
                statistic.getProduct1HighQty(),
                statistic.getProduct2LowQty(),
                statistic.getProduct2MidQty(),
                statistic.getProduct2HighQty(),
                statistic.getProduct3LowQty(),
                statistic.getProduct3MidQty(),
                statistic.getProduct3HighQty(),
                statistic.getTotalIncomes(),
                statistic.getTotalExpenses(),
                id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de la statistique avec l'ID : " + id);
        }

        statistic.setId(id);
        return statistic;
    }


    public boolean delete(Long id) {
        String sql = "DELETE FROM Statistic WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // Vérifier si une entreprise existe
    public boolean statisticExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Statistic WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}

