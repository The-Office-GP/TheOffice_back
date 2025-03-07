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

    public EventController(EventDao eventDao, CompanyDao companyDao) {
        this.eventDao = eventDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventDao.findById(id));
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createEvent(
            @RequestParam("recurrence") Integer recurrence,
            @RequestParam("image") MultipartFile image,
            @RequestParam("id_company") Long id_company) {
        try {

            byte[] imageBytes = image.getBytes();

            int id_event = eventDao.save(recurrence, imageBytes);

            eventDao.linkEventToCompany((long) id_event, id_company);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "recurrence", recurrence,
                    "image", "Uploaded Successfully",
                    "id_company", id_company
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors du traitement de l'image"
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event updatedEvent = eventDao.update(id, event);
        return ResponseEntity.ok(updatedEvent);
    }
}
