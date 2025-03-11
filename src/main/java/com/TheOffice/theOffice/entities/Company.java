package com.TheOffice.theOffice.entities;

import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Machine.Machine;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Company {
    private Long id;
    @NotBlank (message = "Le secteur de l'entreprise ne peut pas être vide")
    private String sector;
    @NotBlank (message = "Le nom de l'entreprise ne peut pas être vide")
    private String name;
    private Date creation_date;
    private Long id_user;
    private List<Machine> machines = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public Company (){
    }

    // Constructor
    public Company(Long id, String sector, String name, Date creation_date, Long id_user, List<Machine> machines, List<Employee> employees,  List<Event> events) {
        this.id = id;
        this.sector = sector;
        this.name = name;
        this.creation_date = creation_date;
        this.id_user = id_user;
        this.machines = machines;
        this.employees = employees;
        this.events = events;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public List<Machine> getMachines() {return machines;}

    public void setMachines(List<Machine> machines) {
        this.machines = (machines != null) ? machines : new ArrayList<>();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = (employees != null) ? employees : new ArrayList<>();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = (events != null) ? events : new ArrayList<>();
    }
}
