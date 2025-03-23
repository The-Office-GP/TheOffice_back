package com.TheOffice.theOffice.entities;

import java.math.BigDecimal;

public class Statistic {
    private Long id;
    private int year;
    private int month;
    private int product1LowQty;
    private int product1MidQty;
    private int product1HighQty;
    private int product2LowQty;
    private int product2MidQty;
    private int product2HighQty;
    private int product3LowQty;
    private int product3MidQty;
    private int product3HighQty;
    private BigDecimal totalIncomes;
    private BigDecimal totalExpenses;
    private Long idCompany;

    public Statistic() {
    }

    public Statistic(Long id, int year, int month, int product1LowQty, int product1MidQty, int product1HighQty, int product2LowQty, int product2MidQty, int product2HighQty, int product3LowQty, int product3MidQty, int product3HighQty, BigDecimal totalIncomes, BigDecimal totalExpenses, Long idCompany) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.product1LowQty = product1LowQty;
        this.product1MidQty = product1MidQty;
        this.product1HighQty = product1HighQty;
        this.product2LowQty = product2LowQty;
        this.product2MidQty = product2MidQty;
        this.product2HighQty = product2HighQty;
        this.product3LowQty = product3LowQty;
        this.product3MidQty = product3MidQty;
        this.product3HighQty = product3HighQty;
        this.totalIncomes = totalIncomes;
        this.totalExpenses = totalExpenses;
        this.idCompany = idCompany;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getProduct1LowQty() {
        return product1LowQty;
    }

    public void setProduct1LowQty(int product1LowQty) {
        this.product1LowQty = product1LowQty;
    }

    public int getProduct1MidQty() {
        return product1MidQty;
    }

    public void setProduct1MidQty(int product1MidQty) {
        this.product1MidQty = product1MidQty;
    }

    public int getProduct1HighQty() {
        return product1HighQty;
    }

    public void setProduct1HighQty(int product1HighQty) {
        this.product1HighQty = product1HighQty;
    }

    public int getProduct2LowQty() {
        return product2LowQty;
    }

    public void setProduct2LowQty(int product2LowQty) {
        this.product2LowQty = product2LowQty;
    }

    public int getProduct2MidQty() {
        return product2MidQty;
    }

    public void setProduct2MidQty(int product2MidQty) {
        this.product2MidQty = product2MidQty;
    }

    public int getProduct2HighQty() {
        return product2HighQty;
    }

    public void setProduct2HighQty(int product2HighQty) {
        this.product2HighQty = product2HighQty;
    }

    public int getProduct3LowQty() {
        return product3LowQty;
    }

    public void setProduct3LowQty(int product3LowQty) {
        this.product3LowQty = product3LowQty;
    }

    public int getProduct3MidQty() {
        return product3MidQty;
    }

    public void setProduct3MidQty(int product3MidQty) {
        this.product3MidQty = product3MidQty;
    }

    public int getProduct3HighQty() {
        return product3HighQty;
    }

    public void setProduct3HighQty(int product3HighQty) {
        this.product3HighQty = product3HighQty;
    }

    public BigDecimal getTotalIncomes() {
        return totalIncomes;
    }

    public void setTotalIncomes(BigDecimal totalIncomes) {
        this.totalIncomes = totalIncomes;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }
}


