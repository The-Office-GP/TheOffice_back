package com.TheOffice.theOffice.entities;

import java.math.BigDecimal;

public class Statistic {
    private Long id;
    private int year;
    private int month;
    private int product1LowQtySell;
    private int product1MidQtySell;
    private int product1HighQtySell;
    private int product2LowQtySell;
    private int product2MidQtySell;
    private int product2HighQtySell;
    private int product3LowQtySell;
    private int product3MidQtySell;
    private int product3HighQtySell;
    private int product4LowQtySell;
    private int product4MidQtySell;
    private int product4HighQtySell;
    private int product1LowQtyProd;
    private int product1MidQtyProd;
    private int product1HighQtyProd;
    private int product2LowQtyProd;
    private int product2MidQtyProd;
    private int product2HighQtyProd;
    private int product3LowQtyProd;
    private int product3MidQtyProd;
    private int product3HighQtyProd;
    private int product4LowQtyProd;
    private int product4MidQtyProd;
    private int product4HighQtyProd;
    private int materialLowQty;
    private int materialMidQty;
    private int materialHighQty;
    private BigDecimal totalIncomes;
    private BigDecimal totalExpenses;
    private long popularity;
    private Long idCompany;

    public Statistic() {
    }

    public Statistic(Long id, int year, int month, int product1LowQtySell, int product1MidQtySell, int product1HighQtySell, int product2LowQtySell, int product2MidQtySell, int product2HighQtySell, int product3LowQtySell, int product3MidQtySell, int product3HighQtySell, int product4LowQtySell, int product4MidQtySell, int product4HighQtySell, int product1LowQtyProd, int product1MidQtyProd, int product1HighQtyProd, int product2LowQtyProd, int product2MidQtyProd, int product2HighQtyProd, int product3LowQtyProd, int product3MidQtyProd, int product3HighQtyProd, int product4LowQtyProd, int product4MidQtyProd, int product4HighQtyProd, int materialLowQty, int materialMidQty, int materialHighQty, BigDecimal totalIncomes, BigDecimal totalExpenses, long popularity, Long idCompany) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.product1LowQtySell = product1LowQtySell;
        this.product1MidQtySell = product1MidQtySell;
        this.product1HighQtySell = product1HighQtySell;
        this.product2LowQtySell = product2LowQtySell;
        this.product2MidQtySell = product2MidQtySell;
        this.product2HighQtySell = product2HighQtySell;
        this.product3LowQtySell = product3LowQtySell;
        this.product3MidQtySell = product3MidQtySell;
        this.product3HighQtySell = product3HighQtySell;
        this.product4LowQtySell = product4LowQtySell;
        this.product4MidQtySell = product4MidQtySell;
        this.product4HighQtySell = product4HighQtySell;
        this.product1LowQtyProd = product1LowQtyProd;
        this.product1MidQtyProd = product1MidQtyProd;
        this.product1HighQtyProd = product1HighQtyProd;
        this.product2LowQtyProd = product2LowQtyProd;
        this.product2MidQtyProd = product2MidQtyProd;
        this.product2HighQtyProd = product2HighQtyProd;
        this.product3LowQtyProd = product3LowQtyProd;
        this.product3MidQtyProd = product3MidQtyProd;
        this.product3HighQtyProd = product3HighQtyProd;
        this.product4LowQtyProd = product4LowQtyProd;
        this.product4MidQtyProd = product4MidQtyProd;
        this.product4HighQtyProd = product4HighQtyProd;
        this.materialLowQty = materialLowQty;
        this.materialMidQty = materialMidQty;
        this.materialHighQty = materialHighQty;
        this.totalIncomes = totalIncomes;
        this.totalExpenses = totalExpenses;
        this.popularity = popularity;
        this.idCompany = idCompany;
    }

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

    public int getProduct1LowQtySell() {
        return product1LowQtySell;
    }

    public void setProduct1LowQtySell(int product1LowQtySell) {
        this.product1LowQtySell = product1LowQtySell;
    }

    public int getProduct1MidQtySell() {
        return product1MidQtySell;
    }

    public void setProduct1MidQtySell(int product1MidQtySell) {
        this.product1MidQtySell = product1MidQtySell;
    }

    public int getProduct1HighQtySell() {
        return product1HighQtySell;
    }

    public void setProduct1HighQtySell(int product1HighQtySell) {
        this.product1HighQtySell = product1HighQtySell;
    }

    public int getProduct2LowQtySell() {
        return product2LowQtySell;
    }

    public void setProduct2LowQtySell(int product2LowQtySell) {
        this.product2LowQtySell = product2LowQtySell;
    }

    public int getProduct2MidQtySell() {
        return product2MidQtySell;
    }

    public void setProduct2MidQtySell(int product2MidQtySell) {
        this.product2MidQtySell = product2MidQtySell;
    }

    public int getProduct2HighQtySell() {
        return product2HighQtySell;
    }

    public void setProduct2HighQtySell(int product2HighQtySell) {
        this.product2HighQtySell = product2HighQtySell;
    }

    public int getProduct3LowQtySell() {
        return product3LowQtySell;
    }

    public void setProduct3LowQtySell(int product3LowQtySell) {
        this.product3LowQtySell = product3LowQtySell;
    }

    public int getProduct3MidQtySell() {
        return product3MidQtySell;
    }

    public void setProduct3MidQtySell(int product3MidQtySell) {
        this.product3MidQtySell = product3MidQtySell;
    }

    public int getProduct3HighQtySell() {
        return product3HighQtySell;
    }

    public void setProduct3HighQtySell(int product3HighQtySell) {
        this.product3HighQtySell = product3HighQtySell;
    }

    public int getProduct4LowQtySell() {
        return product4LowQtySell;
    }

    public void setProduct4LowQtySell(int product4LowQtySell) {
        this.product4LowQtySell = product4LowQtySell;
    }

    public int getProduct4MidQtySell() {
        return product4MidQtySell;
    }

    public void setProduct4MidQtySell(int product4MidQtySell) {
        this.product4MidQtySell = product4MidQtySell;
    }

    public int getProduct4HighQtySell() {
        return product4HighQtySell;
    }

    public void setProduct4HighQtySell(int product4HighQtySell) {
        this.product4HighQtySell = product4HighQtySell;
    }

    public int getProduct1LowQtyProd() {
        return product1LowQtyProd;
    }

    public void setProduct1LowQtyProd(int product1LowQtyProd) {
        this.product1LowQtyProd = product1LowQtyProd;
    }

    public int getProduct1MidQtyProd() {
        return product1MidQtyProd;
    }

    public void setProduct1MidQtyProd(int product1MidQtyProd) {
        this.product1MidQtyProd = product1MidQtyProd;
    }

    public int getProduct1HighQtyProd() {
        return product1HighQtyProd;
    }

    public void setProduct1HighQtyProd(int product1HighQtyProd) {
        this.product1HighQtyProd = product1HighQtyProd;
    }

    public int getProduct2LowQtyProd() {
        return product2LowQtyProd;
    }

    public void setProduct2LowQtyProd(int product2LowQtyProd) {
        this.product2LowQtyProd = product2LowQtyProd;
    }

    public int getProduct2MidQtyProd() {
        return product2MidQtyProd;
    }

    public void setProduct2MidQtyProd(int product2MidQtyProd) {
        this.product2MidQtyProd = product2MidQtyProd;
    }

    public int getProduct2HighQtyProd() {
        return product2HighQtyProd;
    }

    public void setProduct2HighQtyProd(int product2HighQtyProd) {
        this.product2HighQtyProd = product2HighQtyProd;
    }

    public int getProduct3LowQtyProd() {
        return product3LowQtyProd;
    }

    public void setProduct3LowQtyProd(int product3LowQtyProd) {
        this.product3LowQtyProd = product3LowQtyProd;
    }

    public int getProduct3MidQtyProd() {
        return product3MidQtyProd;
    }

    public void setProduct3MidQtyProd(int product3MidQtyProd) {
        this.product3MidQtyProd = product3MidQtyProd;
    }

    public int getProduct3HighQtyProd() {
        return product3HighQtyProd;
    }

    public void setProduct3HighQtyProd(int product3HighQtyProd) {
        this.product3HighQtyProd = product3HighQtyProd;
    }

    public int getMaterialLowQty() {
        return materialLowQty;
    }

    public void setMaterialLowQty(int materialLowQty) {
        this.materialLowQty = materialLowQty;
    }

    public int getMaterialMidQty() {
        return materialMidQty;
    }

    public void setMaterialMidQty(int materialMidQty) {
        this.materialMidQty = materialMidQty;
    }

    public int getMaterialHighQty() {
        return materialHighQty;
    }

    public void setMaterialHighQty(int materialHighQty) {
        this.materialHighQty = materialHighQty;
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

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public int getProduct4LowQtyProd() {
        return product4LowQtyProd;
    }

    public void setProduct4LowQtyProd(int product4LowQtyProd) {
        this.product4LowQtyProd = product4LowQtyProd;
    }

    public int getProduct4MidQtyProd() {
        return product4MidQtyProd;
    }

    public void setProduct4MidQtyProd(int product4MidQtyProd) {
        this.product4MidQtyProd = product4MidQtyProd;
    }

    public int getProduct4HighQtyProd() {
        return product4HighQtyProd;
    }

    public void setProduct4HighQtyProd(int product4HighQtyProd) {
        this.product4HighQtyProd = product4HighQtyProd;
    }
}


