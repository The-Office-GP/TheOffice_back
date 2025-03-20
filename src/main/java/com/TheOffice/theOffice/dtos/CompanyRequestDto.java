package com.TheOffice.theOffice.dtos;

import java.util.List;

public class CompanyRequestDto {
    private String sector;
    private String name;
    private Long userId;
    private Double wallet;

    // DTOs associés
    private List<CycleDto> cycles;
    private List<EmployeeDto> employees;
    private List<SupplierDto> suppliers;
    private List<EventDto> events;
    private List<StockMaterialDto> stockMaterials;
    private List<StockFinalMaterialDto> stockFinalMaterials;
    private List<MachineInCompanyDto> machinesInCompany;

    public CompanyRequestDto() {}

    // Méthode `fromDto()` qui convertit un `Company` en `CompanyRequestDto`
    public static CompanyRequestDto fromDto(CompanyDto companyDto) {
        CompanyRequestDto dto = new CompanyRequestDto();

        dto.setSector(companyDto.getSector());
        dto.setName(companyDto.getName());
        dto.setUserId(companyDto.getUserId());
        dto.setWallet(companyDto.getWallet());

        // Récupération des DTOs associés
        dto.setCycles(companyDto.getCycles());
        dto.setEmployees(companyDto.getEmployees());
        dto.setSuppliers(companyDto.getSuppliers());
        dto.setEvents(companyDto.getEvents());
        dto.setStockMaterials(companyDto.getStockMaterials());
        dto.setStockFinalMaterials(companyDto.getStockFinalMaterials());
        dto.setMachinesInCompany(companyDto.getMachinesInCompany());

        return dto;
    }

    // Getters & Setters
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getWallet() { return wallet; }
    public void setWallet(Double wallet) { this.wallet = wallet; }

    public List<CycleDto> getCycles() { return cycles; }
    public void setCycles(List<CycleDto> cycles) { this.cycles = cycles; }

    public List<EmployeeDto> getEmployees() { return employees; }
    public void setEmployees(List<EmployeeDto> employees) { this.employees = employees; }

    public List<SupplierDto> getSuppliers() { return suppliers; }
    public void setSuppliers(List<SupplierDto> suppliers) { this.suppliers = suppliers; }

    public List<EventDto> getEvents() { return events; }
    public void setEvents(List<EventDto> events) { this.events = events; }

    public List<StockMaterialDto> getStockMaterials() { return stockMaterials; }
    public void setStockMaterials(List<StockMaterialDto> stockMaterials) { this.stockMaterials = stockMaterials; }

    public List<StockFinalMaterialDto> getStockFinalMaterials() { return stockFinalMaterials; }
    public void setStockFinalMaterials(List<StockFinalMaterialDto> stockFinalMaterials) { this.stockFinalMaterials = stockFinalMaterials; }

    public List<MachineInCompanyDto> getMachinesInCompany() { return machinesInCompany; }
    public void setMachinesInCompany(List<MachineInCompanyDto> machinesInCompany) { this.machinesInCompany = machinesInCompany; }
}
