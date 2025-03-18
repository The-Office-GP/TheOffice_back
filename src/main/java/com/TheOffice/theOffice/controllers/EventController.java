package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CompanyDao;
import com.TheOffice.theOffice.daos.EventDao;
import com.TheOffice.theOffice.entities.Event;
import com.TheOffice.theOffice.entities.Machine.ProductionQuality;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventDao eventDao;

    // Constructeur avec injection de dépendance pour EventDao et CompanyDao
    public EventController(EventDao eventDao, CompanyDao companyDao) {
        this.eventDao = eventDao;
    }

    // Récupérer tous les événements
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventDao.findAll());  // Renvoie tous les événements
    }

    // Récupérer un événement par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventDao.findById(id));  // Renvoie l'événement correspondant à l'ID
    }

    // Créer un événement
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody Map<String, Object> request) {
        // Extraction des informations envoyées dans la requête
        long recurrence = Long.parseLong((request.get("recurrence").toString()));
        String image = (request.get("image").toString());

        // Sauvegarde du prêt et récupération de son ID
        int eventId = eventDao.save(Math.toIntExact(recurrence), image);

        // Construction de la réponse HTTP avec statut 201 (créé) et données du prêt
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "eventId", eventId,
                "recurrence", recurrence,
                "image", image
        ));
    }

    // Mettre à jour un événement
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        // Mise à jour de l'événement
        Event updatedEvent = eventDao.update(id, event);
        return ResponseEntity.ok(updatedEvent);  // Renvoie l'événement mis à jour
    }
}
