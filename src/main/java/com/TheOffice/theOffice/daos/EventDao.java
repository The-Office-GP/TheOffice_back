package com.TheOffice.theOffice.daos;

import com.TheOffice.theOffice.entities.Employee.Employee;
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
            rs.getString("image")
    );

    //GET de tous les événements
    public List<Event> findAll(){
        String sql = "SELECT * FROM Event";
        return jdbcTemplate.query(sql, eventRowMapper);
    }

    //GET par id
    public Event findById(Long id){
        String sql = "SELECT * FROM Event WHERE id=?";
        return jdbcTemplate.query(sql, eventRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Event non trouvé"));
    };

    //GET par id de l'entreprise
    public List<Event> findByIdCompany(Long id_company) {
        String sql = "SELECT e.* FROM Event e " +
                "JOIN CompanyEvent ce ON e.id = ce.id_event " +
                "WHERE ce.id_company = ?";

        return jdbcTemplate.query(sql, eventRowMapper, id_company);
    }

    //POST
    public int save (Integer recurrence, String image){
        String sql = "INSERT INTO Event (recurrence, image) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, recurrence);
            ps.setString(2, image);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //PUT
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

    //Vérifie si l'événement existe
    public boolean eventExists(Long id) {
        String sql = "SELECT COUNT(*) FROM Event WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    //Lier un événement à une entreprise
    public void linkEventToCompany(Long eventId, Long companyId){
        String sql = "INSERT INTO CompanyEvent (id_event, id_company) VALUES (?,?)";
        jdbcTemplate.update(sql, eventId, companyId);
    }
}
