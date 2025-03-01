package com.TheOffice.theOffice.entities;

import java.math.BigDecimal;

public class Loan {
    private Long id;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer duration;
    private BigDecimal rest;
    private Long id_user;

    public Loan() {
    }

    public Loan(Long id, BigDecimal loanAmount, BigDecimal interestRate, Integer duration, BigDecimal rest, Long id_user) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.duration = duration;
        this.rest = rest;
        this.id_user = id_user;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public BigDecimal getRest() { return rest; }
    public void setRest(BigDecimal rest) { this.rest = rest; }

    public Long getId_user() { return id_user; }
    public void setId_user(Long id_user) { this.id_user = id_user; }
}