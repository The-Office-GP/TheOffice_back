package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.Cycle;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Employee.PriorityAction;
import com.TheOffice.theOffice.staticModels.EmployeeName;
import com.TheOffice.theOffice.staticModels.Local;
import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.TheOffice.theOffice.staticModels.Machine.ProductionQuality;
import com.TheOffice.theOffice.staticModels.Salary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CycleService {

    public void runCycle1(CompanyDto company, CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial){
        int dayCycle = 0;

        while(dayCycle < 30) {
            companyProduct(cycle, employeeList, machineInCompany, stockProduct, stockMaterial);
            dayCycle++;
            if (dayCycle >= 6) {
                companySell(employeeList, stockProduct, cycle);
            }
            companyMarket(cycle, company,employeeList);

            for (int i = 0; i < employeeList.size(); i++) {
                company.setWallet(company.getWallet() - employeeList.get(i).getSalary().doubleValue()*30);
            }
        }
    }

    public void lastRunCycle(CompanyDto company, CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial){
        int dayCycle = 0;

        while(dayCycle < 30) {
            companyProduct(cycle, employeeList, machineInCompany, stockProduct, stockMaterial);
            dayCycle++;
            if (dayCycle >= 6) {
                companySell(employeeList, stockProduct, cycle);
            }
            companyMarket(cycle, company,employeeList);

            for (int i = 0; i < employeeList.size(); i++) {
                company.setWallet(company.getWallet() - employeeList.get(i).getSalary().doubleValue()*30);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Machine> machineList = new ArrayList<>();

            try{
                File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
                machineList = objectMapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < machineList.size(); i++) {
                company.setWallet(company.getWallet() - machineList.get(i).getMaintenanceCost().doubleValue());
            }
        }
    }

    public void companyProduct(CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial){
        Integer companyProduction = 0;
        double coeffProductionSpeed = cycle.getProductionSpeed() / 100;
        double coeffPriorityProduction = cycle.getPriorityProduction() / 100;

        for (int i = 0; i < employeeList.size(); i++) {
            companyProduction += employeeProductionCapacity(employeeList.get(i), employeeList, machineInCompany, coeffPriorityProduction, coeffProductionSpeed);
        }

        for (int i = 0; i < stockProduct.size(); i++) {
            stockProduct.get(i).setQuantityToProduct(Math.round(companyProduction * stockProduct.get(i).getProportionProduct()/100));
        }

        int index = 0;
        for (int i = 0; i < employeeList.size(); i++) {
            int productionCapacity = employeeProductionCapacity(employeeList.get(i), employeeList, machineInCompany, coeffPriorityProduction, coeffProductionSpeed);
            for (int j = 0; j < productionCapacity; j++) {
                if (index < stockProduct.size()) {
                    if (stockProduct.get(index).getQuantityToProduct() > 0) {
                        if(stockMaterial.totalStock() > 0){
                            defineQualityOfProduction(productionEmployee(employeeList.get(i)), stockProduct.get(index), cycle.getProductionSpeed(), stockMaterial);
                        }
                        stockProduct.get(index).setQuantityToProduct(stockProduct.get(index).getQuantityToProduct()-1);
                    }
                    if (stockProduct.get(index).getQuantityToProduct() <= 0 && index < stockProduct.size()) {
                        index++;
                    }
                }
            }
        }

    }

    public Integer employeeProductionCapacity(EmployeeDto employee, List<EmployeeDto> employeeInCompany, List<MachineInCompanyDto> machineInCompany, double coeffPriorityProduction, double coeffProductionSpeed) {
        Integer productionCapacity = productionEmployee(employee);
        int idForMachine = employeeInCompany.indexOf(employee);
        if (idForMachine < machineInCompany.size()) {
            productionCapacity = (int)(fetchTheProductionOfMachine(machineInCompany.get(idForMachine)) * productionCapacity);
            return (int)((productionCapacity * coeffProductionSpeed + 1) * coeffPriorityProduction);
        } else {
            return 0;
        }
    }

    public Integer productionEmployee(EmployeeDto employee) {
        Integer productionEmployee = 0;
        switch (employee.getMood()) {
            case Mood.MAUVAISE: {
                if(employee.getHealth() > 50){
                    productionEmployee = (employee.getLevel()- 4) / 2;
                } else if (employee.getHealth() > 25) {
                    productionEmployee = (employee.getLevel() - 4) / 4;
                }else{
                    productionEmployee = 0;
                }
                break;
            }
            case Mood.BOF : {
                if(employee.getHealth() > 50){
                    productionEmployee = (employee.getLevel()- 2) / 2;
                } else if (employee.getHealth() > 25) {
                    productionEmployee = (employee.getLevel() - 2) / 4;
                }else{
                    productionEmployee = 0;
                }
                break;
            }
            case Mood.NEUTRE: {
                if(employee.getHealth() > 50){
                    productionEmployee = (employee.getLevel()) / 2;
                } else if (employee.getHealth() > 25) {
                    productionEmployee = (employee.getLevel()) / 4;
                }else{
                    productionEmployee = 0;
                }
                break;
            }
            case Mood.BONNE: {
                if(employee.getHealth() > 50){
                    productionEmployee = (employee.getLevel()+1) / 2;
                } else if (employee.getHealth() > 25) {
                    productionEmployee = (employee.getLevel()+1) / 4;
                }else{
                    productionEmployee = 0;
                }
                break;
            }
            case Mood.HEUREUSE: {
                if(employee.getHealth() > 50){
                    productionEmployee = (employee.getLevel()+ 3) / 2;
                } else if (employee.getHealth() > 25) {
                    productionEmployee = (employee.getLevel() + 3) / 4;
                }else{
                    productionEmployee = 0;
                }
                break;
            }
        }
        return productionEmployee;
    }

    public double fetchTheProductionOfMachine(MachineInCompanyDto machineInCompany) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Machine> machineList = new ArrayList<>();

        try{
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
            machineList = objectMapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }
        Machine machine = machineList.get(0);

        for (int i = 0; i < machineList.size(); i++) {
            if(machineList.get(i).getId() == machineInCompany.getMachineId()){
                machine = machineList.get(i);
            }
        }

        Integer apportMachine = 0;

        switch (machine.getProductionQuality()) {
            case ProductionQuality.MEDIOCRE:
                apportMachine += 50;
                break;
            case ProductionQuality.NORMAL:
                apportMachine += 100;
                break;
            case ProductionQuality.BONNE:
                apportMachine += 150;
                break;
            case ProductionQuality.PERFORMANTE:
                apportMachine += 200;
                break;
        }
        return apportMachine / 100;
    }

    public void defineQualityOfProduction(int productivityEmployee, StockFinalMaterialDto stockProduct, int productionSpeed, StockMaterialDto stockMaterial){
        int tresholdProductivity = Math.round(productivityEmployee * 100 / 30);
        int ecart = (int) (-Math.pow((productionSpeed - 50)/ 10, 2)  + 50);

        double choice = Math.floor(Math.random() * 100);
        if (productionSpeed <= 50) {
            int lowTreshold = Math.round(tresholdProductivity + ecart / 2);
            int highTreshold = (int) Math.round(lowTreshold + ecart / 1.5);
            if (choice < lowTreshold) {
                stockProduct.setQuantityHigh(stockProduct.getQuantityHigh()+1);
            } else if (choice <= highTreshold) {
                stockProduct.setQuantityMid(stockProduct.getQuantityMid()+1);
            } else {
                stockProduct.setQuantityLow(stockProduct.getQuantityLow()+1);
            }
        } else {
            int lowTreshold = 100 - Math.round(tresholdProductivity + ecart / 2);
            int highTreshold = (int) Math.round(lowTreshold + ecart / 1.5);
            if (choice < lowTreshold) {
                stockProduct.setQuantityLow(stockProduct.getQuantityLow()+1);
            } else if (choice <= highTreshold) {
                stockProduct.setQuantityMid(stockProduct.getQuantityMid()+1);
            } else {
                stockProduct.setQuantityHigh(stockProduct.getQuantityHigh()+1);
            }
        }
        stockProduct.setMonthProduction(stockProduct.getMonthProduction()+1);

        if(stockMaterial.getQuantityHigh() > 0){
            stockMaterial.setQuantityHigh(stockMaterial.getQuantityHigh()-1);
        } else if (stockMaterial.getQuantityMid() > 0) {
            stockMaterial.setQuantityMid(stockMaterial.getQuantityMid()-1);
        } else {
            stockMaterial.setQuantityLow(stockMaterial.getQuantityLow()-1);
        }
    }

    public void companySell(List<EmployeeDto> employeeList, List<StockFinalMaterialDto> stockProduct, CycleDto cycle){
        for (int i = 0; i < employeeList.size(); i++) {
            employeeSell(employeeList.get(i), stockProduct, cycle);
        }
    }

    public void employeeSell(EmployeeDto employee, List<StockFinalMaterialDto> stockProduct, CycleDto cycle){
        int capacityToSell = sellCapacity(employee);
        double chanceToSell = capacityToSell * 100 / 15;

        chooseTheProductForSell(employee, stockProduct);

        if (stockProduct.size() > 0) {
            int index = 0;
            int i = 0;
            while (i < capacityToSell/4) {
                int succesSell = (int) Math.floor(Math.random() * 100);
                if (succesSell <= chanceToSell && index < stockProduct.size()) {
                    if (stockProduct.get(index).getQuantityHigh() > 0) {
                        stockProduct.get(index).setQuantityHigh(stockProduct.get(index).getQuantityHigh()-1);
                        stockProduct.get(index).setSell(stockProduct.get(index).getSell()+1);
                        stockProduct.get(index).setMonthSell(stockProduct.get(index).getMonthSell()+1);
                        cycle.setCountGoodSell(cycle.getCountGoodSell()+1);
                        i++;
                    } else if (stockProduct.get(index).getQuantityMid() > 0) {
                        stockProduct.get(index).setQuantityMid(stockProduct.get(index).getQuantityMid()-1);
                        stockProduct.get(index).setSell(stockProduct.get(index).getSell()+1);
                        stockProduct.get(index).setMonthSell(stockProduct.get(index).getMonthSell()+1);
                        i++;
                    } else if (stockProduct.get(index).getQuantityLow() > 0) {
                        stockProduct.get(index).setQuantityLow(stockProduct.get(index).getQuantityLow()-1);
                        stockProduct.get(index).setSell(stockProduct.get(index).getSell()+1);
                        stockProduct.get(index).setMonthSell(stockProduct.get(index).getMonthSell()+1);
                        cycle.setCountBadSell(cycle.getCountBadSell()+1);
                        i++;
                    }else {
                        index++;
                        if (index >= stockProduct.size()) {
                            break;
                        }
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public int sellCapacity(EmployeeDto employee) {
        int capacityToSellEmployee = 0;
        switch (employee.getMood()) {
            case Mood.MAUVAISE: {
                if(employee.getHealth() > 50){
                    capacityToSellEmployee = (employee.getLevel()- 4);
                } else if (employee.getHealth() > 25) {
                    capacityToSellEmployee = (employee.getLevel() - 4) / 2;
                }else{
                    capacityToSellEmployee = 0;
                }
                break;
            }
            case Mood.BOF : {
                if(employee.getHealth() > 50){
                    capacityToSellEmployee = (employee.getLevel()- 2) ;
                } else if (employee.getHealth() > 25) {
                    capacityToSellEmployee = (employee.getLevel() - 2) / 2;
                }else{
                    capacityToSellEmployee = 0;
                }
                break;
            }
            case Mood.NEUTRE: {
                if(employee.getHealth() > 50){
                    capacityToSellEmployee = (employee.getLevel());
                } else if (employee.getHealth() > 25) {
                    capacityToSellEmployee = (employee.getLevel()) / 2;
                }else{
                    capacityToSellEmployee = 0;
                }
                break;
            }
            case Mood.BONNE: {
                if(employee.getHealth() > 50){
                    capacityToSellEmployee = (employee.getLevel()+1);
                } else if (employee.getHealth() > 25) {
                    capacityToSellEmployee = (employee.getLevel()+1) / 2;
                }else{
                    capacityToSellEmployee = 0;
                }
                break;
            }
            case Mood.HEUREUSE: {
                if(employee.getHealth() > 50){
                    capacityToSellEmployee = (employee.getLevel()+ 3);
                } else if (employee.getHealth() > 25) {
                    capacityToSellEmployee = (employee.getLevel() + 3) / 2;
                }else{
                    capacityToSellEmployee = 0;
                }
                break;
            }
        }
        return Math.round(capacityToSellEmployee);
    }

    public void chooseTheProductForSell(EmployeeDto employee, List<StockFinalMaterialDto> stockProduct){
        List<StockFinalMaterialDto> stock = new ArrayList<>();

        for (int i = 0; i < stockProduct.size(); i++) {
            if (employee.getPriorityAction().toString().equals(stockProduct.get(i).getName()) && stockProduct.get(i).totalStock() > 0) {
                stock.add(stockProduct.get(i));
            }
        }

        if (stock.size() == 0) {
            stockProduct.sort(this::compare);
        }
        else {
            StockFinalMaterialDto selectedProduct = stock.get(0);
            stockProduct.remove(selectedProduct);
            stockProduct.sort(this::compare);
            stockProduct.add(0, selectedProduct);
        }
    }

    // Méthode de comparaison pour trier les produits par stock
    public int compare(StockFinalMaterialDto p1, StockFinalMaterialDto p2) {
        return Integer.compare(p1.totalStock(), p2.totalStock());
    }

    public void companyMarket(CycleDto cycle, CompanyDto company, List<EmployeeDto> employeeList){
        if (cycle.getCountGoodSell() > (100-company.getPopularity()/100000)) {
            for (int i = 0; i < employeeList.size(); i++) {
                company.setPopularity(company.getPopularity() + marketEmployee(employeeList.get(i)));
                cycle.setCountGoodSell(0);
            }
        }

        if (cycle.getCountBadSell() > (100-company.getPopularity()/100000)) {
            for (int i = 0; i < employeeList.size(); i++) {
                company.setPopularity(company.getPopularity() - 20);
                cycle.setCountGoodSell(0);
            }
        }
    }

    public int marketEmployee(EmployeeDto employee) {
        int capacityToMarket = 0;
        switch (employee.getMood()) {
            case Mood.MAUVAISE: {
                if(employee.getHealth() > 50){
                    capacityToMarket = (employee.getLevel()- 4);
                } else if (employee.getHealth() > 25) {
                    capacityToMarket = (employee.getLevel() - 4) / 2;
                }else{
                    capacityToMarket = 0;
                }
                break;
            }
            case Mood.BOF : {
                if(employee.getHealth() > 50){
                    capacityToMarket = (employee.getLevel()- 2) ;
                } else if (employee.getHealth() > 25) {
                    capacityToMarket = (employee.getLevel() - 2) / 2;
                }else{
                    capacityToMarket = 0;
                }
                break;
            }
            case Mood.NEUTRE: {
                if(employee.getHealth() > 50){
                    capacityToMarket = (employee.getLevel());
                } else if (employee.getHealth() > 25) {
                    capacityToMarket = (employee.getLevel()) / 2;
                }else{
                    capacityToMarket = 0;
                }
                break;
            }
            case Mood.BONNE: {
                if(employee.getHealth() > 50){
                    capacityToMarket = (employee.getLevel()+1);
                } else if (employee.getHealth() > 25) {
                    capacityToMarket = (employee.getLevel()+1) / 2;
                }else{
                    capacityToMarket = 0;
                }
                break;
            }
            case Mood.HEUREUSE: {
                if(employee.getHealth() > 50){
                    capacityToMarket = (employee.getLevel()+ 3);
                } else if (employee.getHealth() > 25) {
                    capacityToMarket = (employee.getLevel() + 3) / 2;
                }else{
                    capacityToMarket = 0;
                }
                break;
            }
        }
        return Math.round(capacityToMarket);
    }
}


