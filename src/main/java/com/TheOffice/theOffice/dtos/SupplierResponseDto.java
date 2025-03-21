package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.staticModels.Suppliers.PriceSuppliers;
import com.TheOffice.theOffice.staticModels.Suppliers.SupplierName;

import java.util.List;

public class SupplierResponseDto {
    private List<SupplierName> supplierNames;
    private List<PriceSuppliers> priceSuppliers;

    public SupplierResponseDto(List<SupplierName> supplierNames, List<PriceSuppliers> priceSuppliers) {
        this.supplierNames = supplierNames;
        this.priceSuppliers = priceSuppliers;
    }

    public List<SupplierName> getSupplierNames() {
        return supplierNames;
    }

    public void setSupplierNames(List<SupplierName> supplierNames) {
        this.supplierNames = supplierNames;
    }

    public List<PriceSuppliers> getPriceSuppliers() {
        return priceSuppliers;
    }

    public void setPriceSuppliers(List<PriceSuppliers> priceSuppliers) {
        this.priceSuppliers = priceSuppliers;
    }

    public static SupplierResponseDto fromEntity(SupplierResponseDto supplierResponseDto){
        return new SupplierResponseDto(supplierResponseDto.getSupplierNames(), supplierResponseDto.getPriceSuppliers());
    }
}
