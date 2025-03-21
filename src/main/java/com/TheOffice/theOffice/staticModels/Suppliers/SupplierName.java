package com.TheOffice.theOffice.staticModels.Suppliers;

import java.util.List;

public class SupplierName {
    private Long id;
    private String name;

    public SupplierName() {
    }

    public SupplierName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
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
}
