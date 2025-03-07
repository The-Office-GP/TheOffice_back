package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Event;
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
public class EventDao {

    private final JdbcTemplate jdbcTemplate;

    public EventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Event> eventRowMapper = (rs,_) -> new Event (
            rs.getLong("id"),
            rs.getLong("recurrence"),
            rs.getBytes("image")
    );

    public List<Event> findAll(){
        String sql = "SELECT * FROM Event";
        return jdbcTemplate.query(sql, eventRowMapper);
    }

    public Event findById(Long id){
        String sql = "SELECT * FROM Event WHERE id=?";
        return jdbcTemplate.query(sql, eventRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Event non trouvé"));
    };

    public int save (Integer recurrence, byte[] image){
        String sql = "INSERT INTO Event (recurrence, image) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, recurrence);
            ps.setBytes(2, image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Event update(Long id, Event event) {
        if (!eventExists(id)) {
            throw new ResourceNotFoundException("Event avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE Event SET recurrence = ?, image = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, event.getRecurrence(), event.getImage(), id);

        if (rowsAffected <= 0) {
            throw new ResourceNotFoundException("Échec de la mise à jour de l'event avec l'ID : " + id);
        }
        return event;
    }

    public boolean eventExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Event WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    public void linkEventToCompany(Long eventId, Long companyId){
        String sql = "INSERT INTO CompanyEvent (id_event, id_company) VALUES (?,?)";
        jdbcTemplate.update(sql, eventId, companyId);
    }
}
