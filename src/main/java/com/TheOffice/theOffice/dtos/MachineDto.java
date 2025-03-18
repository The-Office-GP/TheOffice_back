package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.staticModels.Machine;
import com.TheOffice.theOffice.entities.Machine.ProductionQuality;

import java.math.BigDecimal;

public class MachineDto {
    private Long id;
    private String name;
    private ProductionQuality productionQuality;
    private BigDecimal price;
    private BigDecimal maintenanceCost;
    private String image;

    public MachineDto() {
    }

    // Constructor
    public MachineDto(Long id, String name, ProductionQuality productionQuality, BigDecimal price, BigDecimal maintenanceCost, String image) {
        this.id = id;
        this.name = name;
        this.productionQuality = productionQuality;
        this.price = price;
        this.maintenanceCost = maintenanceCost;
        this.image = image;
    }

    // Getters and Setters
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

    public static MachineDto fromEntity(Machine machine) {
        return new MachineDto(machine.getId(), machine.getName(), machine.getProductionQuality(), machine.getPrice(), machine.getMaintenanceCost(), machine.getImage());
    }
}
