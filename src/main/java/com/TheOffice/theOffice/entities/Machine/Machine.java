package com.TheOffice.theOffice.entities.Machine;

import java.math.BigDecimal;

public class Machine {
    private Long id;
    private String name;
    private ProductionQuality production_quality;
    private BigDecimal price;
    private BigDecimal maintenance_cost;
    private String image;

    public Machine() {
    }
    // Constructor
    public Machine(Long id, String name, ProductionQuality production_quality, BigDecimal price, BigDecimal maintenance_cost, String image) {
        this.id = id;
        this.name = name;
        this.production_quality = production_quality;
        this.price = price;
        this.maintenance_cost = maintenance_cost;
        this.image = image;
    }

    //Getter and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProductionQuality getProduction_quality() { return production_quality; }
    public void setProduction_quality(ProductionQuality production_quality) { this.production_quality = production_quality; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getMaintenance_cost() { return maintenance_cost; }
    public void setMaintenance_cost(BigDecimal maintenance_cost) { this.maintenance_cost = maintenance_cost; }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
}