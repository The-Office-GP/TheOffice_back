package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.services.MachineService;
import com.TheOffice.theOffice.staticModels.Local;
import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyDto {
    private String sector;
    private String name;
    private Long popularity;
    private Long userId;
    private Local local;
    private List<Machine> machines;
    private Double wallet;

    // DTOs associés
    private List<CycleDto> cycles;
    private List<EmployeeDto> employees;
    private List<SupplierDto> suppliers;
    private List<EventDto> events;
    private List<StockMaterialDto> stockMaterials;
    private List<StockFinalMaterialDto> stockFinalMaterials;
    private List<MachineInCompanyDto> machinesInCompany;
    private MachineService machineService;

    public CompanyDto() {}

    public static CompanyDto fromEntity(Company company, Double wallet,
                                        List<CycleDto> cycleDtos, List<EmployeeDto> employeeDtos, List<SupplierDto> supplierDtos,
                                        List<EventDto> eventDtos, List<StockMaterialDto> stockMaterialDtos,
                                        List<StockFinalMaterialDto> stockFinalMaterialDtos,List<MachineInCompanyDto> machinesInCompany,
                                        MachineService machineService) {
        Local companyLocal = new Local();
        try{
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/local.json");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Local> localList = objectMapper.readValue(jsonFile, new TypeReference<List<Local>>() {});
            Map<Long, Local> localMap = new HashMap<>();
            localMap.put(1L, localList.get(0));
            localMap.put(2L, localList.get(1));
            localMap.put(3L, localList.get(2));
            localMap.put(4L, localList.get(3));
            localMap.put(5L, localList.get(4));
            localMap.put(6L, localList.get(5));
            localMap.put(7L, localList.get(6));
            localMap.put(8L, localList.get(7));
            localMap.put(9L, localList.get(8));

            companyLocal = localMap.get(company.getLocalId());

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Machine> machineList = machineService.collectMachine(company);

        CompanyDto dto = new CompanyDto();
        dto.setSector(company.getSector());
        dto.setName(company.getName());
        dto.setPopularity(company.getPopularity());
        dto.setUserId(company.getUserId());
        dto.setWallet(wallet);
        dto.setLocal(companyLocal);
        dto.setMachines(machineList);
        dto.setCycles(cycleDtos);
        dto.setEmployees(employeeDtos);
        dto.setSuppliers(supplierDtos);
        dto.setEvents(eventDtos);
        dto.setStockMaterials(stockMaterialDtos);
        dto.setStockFinalMaterials(stockFinalMaterialDtos);
        dto.setMachinesInCompany(machinesInCompany);

        return dto;
    }

    // Getters & Setters
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Local getLocal() {return local;}
    public void setLocal(Local local) {this.local = local;}

    public List<Machine> getMachines() {
        return machines;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

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
