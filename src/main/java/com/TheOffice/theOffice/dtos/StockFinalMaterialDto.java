package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.StockFinalMaterial;

public class StockFinalMaterialDto { private Long id;
    private String name;
    private Integer quality;
    private Integer quantity;
    private Long id_company;

    public StockFinalMaterialDto() {
    }

    public StockFinalMaterialDto(Long id, String name, Integer quality, Integer quantity, Long id_company) {
        this.id = id;
        this.name = name;
        this.quality = quality;
        this.quantity = quantity;
        this.id_company = id_company;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQuality() { return quality; }
    public void setQuality(Integer quality) { this.quality = quality; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getId_company() { return id_company; }
    public void setId_company(Long id_company) { this.id_company = id_company; }

    public static StockFinalMaterialDto fromEntity(StockFinalMaterial stockFinalMaterial) {
        return new StockFinalMaterialDto(stockFinalMaterial.getId(), stockFinalMaterial.getName(), stockFinalMaterial.getQuality(), stockFinalMaterial.getQuantity(), stockFinalMaterial.getId_company());
    }
}
