package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Loan;
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
public class LoanDao {

    private final JdbcTemplate jdbcTemplate;

    public LoanDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Loan> loanRowMapper = (rs, _) -> new Loan (
            rs.getLong("id"),
            rs.getBigDecimal("loan_amount"),
            rs.getBigDecimal("interest_rate"),
            rs.getInt("duration"),
            rs.getBigDecimal("rest"),
            rs.getLong("id_user")
    );

    //GET de tous les emprunts
    public List<Loan> findAll(){
        String sql = "SELECT * FROM Loan";
        return jdbcTemplate.query(sql, loanRowMapper);
    }

    //GET par id
    public Loan findById(Long id) {
        String sql = "SELECT * FROM Loan WHERE id = ?";
        return jdbcTemplate.query(sql, loanRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé"));
    }

    //POST
    public int save(BigDecimal loanAmount, BigDecimal interestRate, Integer duration, BigDecimal rest, Long userId) {
        String sql = "INSERT INTO Loan (loan_amount, interest_rate, duration, rest, id_user) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, loanAmount);
            ps.setBigDecimal(2, interestRate);
            ps.setInt(3, duration);
            ps.setBigDecimal(4, rest);
            ps.setLong(5, userId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
    public Loan update(Long id, Loan loan) {
        if (!loanExists(id)) {
            throw new RuntimeException("Emprunt avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Loan SET loan_amount = ?, interest_rate = ?, duration = ?, rest = ?, id_user = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, loan.getLoanAmount(), loan.getInterestRate(), loan.getDuration(), loan.getRest(), loan.getUserId(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de l'emprunt avec l'ID : " + id);
        }
        return loan;
    }

    //Vérifier si l'emprunt existe
    public boolean loanExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Loan WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
