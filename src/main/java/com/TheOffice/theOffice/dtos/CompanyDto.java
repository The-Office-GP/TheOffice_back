package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.staticModels.Local;
import com.TheOffice.theOffice.staticModels.LocalList;
import com.TheOffice.theOffice.entities.Company;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyDto {
    private String sector;
    private String name;
    private Long idUser;
    private Local local;
    private Double wallet;

    // DTOs associés
    private List<CycleDto> cycles;
    private List<MachineDto> machines;
    private List<EmployeeDto> employees;
    private List<SupplierDto> suppliers;
    private List<EventDto> events;
    private List<StockMaterialDto> stockMaterials;
    private List<StockFinalMaterialDto> stockFinalMaterials;

    public CompanyDto() {}

    public static CompanyDto fromEntity(Company company, Double wallet,
                                        List<CycleDto> cycleDtos, List<MachineDto> machineDtos,
                                        List<EmployeeDto> employeeDtos, List<SupplierDto> supplierDtos,
                                        List<EventDto> eventDtos, List<StockMaterialDto> stockMaterialDtos,
                                        List<StockFinalMaterialDto> stockFinalMaterialDtos) {
        Local companyLocal = company.getLocal();
        try{
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/local.json");
            ObjectMapper objectMapper = new ObjectMapper();
            LocalList localList = objectMapper.readValue(jsonFile, LocalList.class);
            Map<Long, Local> localMap = new HashMap<>();
            localMap.put(1L, localList.getLocalList().get(0));
            localMap.put(2L, localList.getLocalList().get(1));
            localMap.put(3L, localList.getLocalList().get(2));
            localMap.put(4L, localList.getLocalList().get(3));
            localMap.put(5L, localList.getLocalList().get(4));
            localMap.put(6L, localList.getLocalList().get(5));
            localMap.put(7L, localList.getLocalList().get(6));
            localMap.put(8L, localList.getLocalList().get(7));
            localMap.put(9L, localList.getLocalList().get(8));

            companyLocal = localMap.get(company.getId_local());

        } catch (IOException e) {
            e.printStackTrace();
        }

        CompanyDto dto = new CompanyDto();
        dto.setSector(company.getSector());
        dto.setName(company.getName());
        dto.setIdUser(company.getId_user());
        dto.setLocal(company.getLocal());
        dto.setWallet(wallet);
        dto.setLocal(companyLocal);
        dto.setCycles(cycleDtos);
        dto.setMachines(machineDtos);
        dto.setEmployees(employeeDtos);
        dto.setSuppliers(supplierDtos);
        dto.setEvents(eventDtos);
        dto.setStockMaterials(stockMaterialDtos);
        dto.setStockFinalMaterials(stockFinalMaterialDtos);

        return dto;
    }

    // Getters & Setters
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public Local getLocal() {return local;}
    public void setLocal(Local local) {this.local = local;}

    public Double getWallet() { return wallet; }
    public void setWallet(Double wallet) { this.wallet = wallet; }

    public List<CycleDto> getCycles() { return cycles; }
    public void setCycles(List<CycleDto> cycles) { this.cycles = cycles; }

    public List<MachineDto> getMachines() { return machines; }
    public void setMachines(List<MachineDto> machines) { this.machines = machines; }

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
}
