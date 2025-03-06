package com.TheOffice.theOffice.entities;

public class Event {
    private Long id;
    private Boolean renewable;
    private Long recurrence;
    private byte[] image;

    public Event() {
    }

    public Event(Long id, Boolean renewable, Long recurrence, byte[] image){
        this.id = id;
        this.renewable = renewable;
        this.recurrence = recurrence;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getRenewable() {
        return renewable;
    }

    public void setRenewable(Boolean renewable) {
        this.renewable = renewable;
    }

    public Long getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Long recurrence) {
        this.recurrence = recurrence;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
