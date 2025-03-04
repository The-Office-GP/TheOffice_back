package com.TheOffice.theOffice.entities.Employee;

import java.math.BigDecimal;

public class Employee {
    private Long id;
    private String name;
    private Gender gender;
    private Integer seniority;
    private BigDecimal salary;
    private Integer level;
    private Mood mood;
    private Status status;
    private Job job;
    private Integer health;
    private byte[] image;

    public Employee() {
    }

    public Employee(Long id, String name, Gender gender, Integer seniority, BigDecimal salary, Integer level, Mood mood, Status status, Job job, Integer health, byte [] image) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.seniority = seniority;
        this.salary = salary;
        this.level = level;
        this.mood = mood;
        this.status = status;
        this.job = job;
        this.health = health;
        this.image = image;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public Integer getSeniority() { return seniority; }
    public void setSeniority(Integer seniority) { this.seniority = seniority; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Mood getMood() { return mood; }
    public void setMood(Mood mood) { this.mood = mood; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public Integer getHealth() { return health; }
    public void setHealth(Integer health) { this.health = health; }

    public byte [] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}
}

