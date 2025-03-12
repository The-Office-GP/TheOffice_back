package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.CompanyDao;
import com.TheOffice.theOffice.daos.EventDao;
import com.TheOffice.theOffice.entities.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createEvent(
            @RequestParam("recurrence") Integer recurrence,
            @RequestParam("image") MultipartFile image,
            @RequestParam("id_company") Long id_company) {
        try {
            // Conversion de l'image en tableau d'octets
            byte[] imageBytes = image.getBytes();

            // Sauvegarde de l'événement et récupération de l'ID
            int id_event = eventDao.save(recurrence, imageBytes);

            // Lier l'événement à une entreprise
            eventDao.linkEventToCompany((long) id_event, id_company);

            // Réponse HTTP 201 avec les détails de l'événement créé
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "recurrence", recurrence,
                    "image", "Uploaded Successfully",
                    "id_company", id_company
            ));
        } catch (IOException e) {
            // Si une erreur survient lors du traitement de l'image
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    // Mettre à jour un événement
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        // Mise à jour de l'événement
        Event updatedEvent = eventDao.update(id, event);
        return ResponseEntity.ok(updatedEvent);  // Renvoie l'événement mis à jour
    }
}
