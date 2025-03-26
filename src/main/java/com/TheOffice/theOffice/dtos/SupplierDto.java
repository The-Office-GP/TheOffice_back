package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.Supplier;

import java.math.BigDecimal;

public class SupplierDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String quality;

    // Constructor
    public SupplierDto(Long id, String name, BigDecimal price, String quality) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quality = quality;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public static SupplierDto fromEntity(Supplier supplier) {
        return new SupplierDto(supplier.getId(), supplier.getName(), supplier.getPrice(), supplier.getQuality());
    }
}
