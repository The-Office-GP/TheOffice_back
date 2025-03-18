package com.TheOffice.theOffice.entities;

public class StockMaterial {
    private Long id;
    private String name;
    private Integer quantity;
    private Long companyId;

    public StockMaterial() {
    }

    // Constructor
    public StockMaterial(Long id, String name, Integer quantity, Long companyId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.companyId = companyId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}