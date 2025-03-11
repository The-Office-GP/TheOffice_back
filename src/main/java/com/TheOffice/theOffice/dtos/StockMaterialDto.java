package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.StockMaterial;

public class StockMaterialDto {
    private Long id;
    private String name;
    private Integer quantity;
    private Long id_company;

    public StockMaterialDto() {
    }

    // Constructor
    public StockMaterialDto(Long id, String name, Integer quantity, Long id_company) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.id_company = id_company;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getId_company() { return id_company; }
    public void setId_company(Long id_company) { this.id_company = id_company; }

    public static StockMaterialDto fromEntity(StockMaterial stockMaterial) {
        return new StockMaterialDto(stockMaterial.getId(), stockMaterial.getName(), stockMaterial.getQuantity(), stockMaterial.getId_company());
    }
}
