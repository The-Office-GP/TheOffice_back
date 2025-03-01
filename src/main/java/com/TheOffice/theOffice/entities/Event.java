package com.TheOffice.theOffice.entities;

public class Event {
    Long id;
    String result;

    public Event() {
    }

    public Event(Long id, String result){
        this.id = id;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
