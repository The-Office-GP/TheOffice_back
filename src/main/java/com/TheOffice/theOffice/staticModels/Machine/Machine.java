package com.TheOffice.theOffice.staticModels.Machine;

import java.math.BigDecimal;

public class Machine {
    private Long id;
    private String name;
    private ProductionQuality productionQuality;
    private BigDecimal price;
    private BigDecimal maintenanceCost;
    private String image;

    public Machine() {
    }
    // Constructor
    public Machine(Long id, String name, ProductionQuality productionQuality, BigDecimal price, BigDecimal maintenanceCost, String image) {
        this.id = id;
        this.name = name;
        this.productionQuality = productionQuality;
        this.price = price;
        this.maintenanceCost = maintenanceCost;
        this.image = image;
    }

    //Getter and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProductionQuality getProductionQuality() { return productionQuality; }
    public void setProductionQuality(ProductionQuality productionQuality) { this.productionQuality = productionQuality; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(BigDecimal maintenanceCost) { this.maintenanceCost = maintenanceCost; }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
}