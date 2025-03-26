package com.TheOffice.theOffice.entities;

public class Event {
    private Long id;
    private Long recurrence;
    private String image;

    public Event() {
    }

    //Constructor
    public Event(Long id, Long recurrence, String image){
        this.id = id;
        this.recurrence = recurrence;
        this.image = image;
    }

    //Getters and Setters
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
}
