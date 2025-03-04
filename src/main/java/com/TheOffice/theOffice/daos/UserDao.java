package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.User;
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

//GET, POST, PUT, DELETE
@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, _) -> new User(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getBigDecimal("wallet")
    );

    public User findById(Long id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée"));
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvée"));
    }

    public boolean save(User user) {
        String sql = "INSERT INTO `user` (email, password, role) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole());
        return rowsAffected > 0;
    }

    public List<User> findAll(){
        String sql = "SELECT * FROM User";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public int save(String email, String username, String password, String role, BigDecimal wallet) {
        String sql = "INSERT INTO User (email, username, password, role, wallet) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setBigDecimal(5, wallet);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public User update(Long id, User user) {
        if (!userExists(id)) {
            throw new ResourceNotFoundException("Utilisateur avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE User SET email = ?, username = ?, password = ?, role = ?, wallet = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getUsername(), user.getPassword(), user.getRole(), user.getWallet(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de l'utilisateur avec l'ID : " + id);
        }
        return user;
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM User WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean userExists(Long id) {
        String sql = "SELECT COUNT(*) FROM User WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }
}
