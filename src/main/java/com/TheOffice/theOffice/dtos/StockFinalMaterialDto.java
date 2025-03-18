package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.StockFinalMaterial;

public class StockFinalMaterialDto { private Long id;
    private String name;
    private Integer quality;
    private Integer quantity;
    private Long companyId;

    public StockFinalMaterialDto() {
    }

    // Constructor
    public StockFinalMaterialDto(Long id, String name, Integer quality, Integer quantity, Long companyId) {
        this.id = id;
        this.name = name;
        this.quality = quality;
        this.quantity = quantity;
        this.companyId = companyId;
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

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public static StockFinalMaterialDto fromEntity(StockFinalMaterial stockFinalMaterial) {
        return new StockFinalMaterialDto(stockFinalMaterial.getId(), stockFinalMaterial.getName(), stockFinalMaterial.getQuality(), stockFinalMaterial.getQuantity(), stockFinalMaterial.getCompanyId());
    }
}
