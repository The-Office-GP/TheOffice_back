package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.Event;

public class EventDto {
    private Long id;
    private Long recurrence;
    private String image;

    public EventDto() {
    }

    // Constructor
    public EventDto(Long id, Long recurrence, String image){
        this.id = id;
        this.recurrence = recurrence;
        this.image = image;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Long recurrence) {
        this.recurrence = recurrence;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static EventDto fromEntity(Event event) {
        return new EventDto(event.getId(), event.getRecurrence(), event.getImage());
    }
}
