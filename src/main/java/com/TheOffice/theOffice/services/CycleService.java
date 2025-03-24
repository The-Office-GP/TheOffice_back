package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Statistic;
import com.TheOffice.theOffice.exceptions.ResourceNotFoundException;
import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CycleService {
    private static Map<Long, Machine> machineMap = new HashMap<>();
    static {
        loadMachines();
    }

    private static void loadMachines() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
            List<Machine> machineList = objectMapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});
            for (Machine machine : machineList) {
                machineMap.put(machine.getId(), machine);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement des machines depuis le fichier JSON", e);
        }
    }

    public void runCycle1(CompanyDto company, CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial, Statistic statistic){
        int dayCycle = 0;

        while(dayCycle < 30) {
            companyProduct(cycle, machineInCompany, employeeList, stockProduct, stockMaterial, statistic);
            if (dayCycle >= 6) {
                companySell(employeeList, stockProduct, cycle, statistic, company);
            }

            companyMarket(cycle, company, employeeList, statistic);

            for (int i = 0; i < employeeList.size(); i++) {
                company.setWallet(company.getWallet() - employeeList.get(i).getSalary().doubleValue());
                statistic.setTotalExpenses(statistic.getTotalExpenses().add(employeeList.get(i).getSalary()));
            }
            dayCycle++;
        }
        cycle.setStep(cycle.getStep()+1);
    }


    public void lastRunCycle(CompanyDto company, CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial, Statistic statistic){
        int dayCycle = 0;

        while(dayCycle < 30) {
            companyProduct(cycle, machineInCompany, employeeList, stockProduct, stockMaterial, statistic);
            if (dayCycle >= 6) {
                companySell(employeeList, stockProduct, cycle, statistic, company);
            }
            companyMarket(cycle, company,employeeList, statistic);

            for (int i = 0; i < employeeList.size(); i++) {
                company.setWallet(company.getWallet() - employeeList.get(i).getSalary().doubleValue());
                statistic.setTotalExpenses(statistic.getTotalExpenses().add(employeeList.get(i).getSalary()));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Machine> machineList = new ArrayList<>();

            try{
                File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
                machineList = objectMapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});

            } catch (IOException e) {
                e.printStackTrace();
            }
            dayCycle++;
        }
        for (int i = 0; i < machineInCompany.size(); i++) {
            Machine machine = machineMap.get(machineInCompany.get(i).getMachineId());
            company.setWallet(company.getWallet() - machine.getMaintenanceCost().doubleValue());
            statistic.setTotalExpenses(statistic.getTotalExpenses().add(machine.getMaintenanceCost()));
        }
        cycle.setStep(cycle.getStep()+1);
    }

    public void companyProduct(CycleDto cycle, List<MachineInCompanyDto> machineInCompany, List<EmployeeDto> employeeList, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial, Statistic statistic){
        double coeffProductionSpeed = (double)cycle.getProductionSpeed() / 100;
        double coeffPriorityProduction = (double)cycle.getPriorityProduction() / 100;
        int totalProduction = 0;

        defineThePriorityProduction(machineInCompany, totalProduction, employeeList, stockProduct, coeffProductionSpeed, coeffPriorityProduction);

        startProduct(cycle, employeeList, machineInCompany, stockProduct, stockMaterial, coeffProductionSpeed, coeffPriorityProduction, statistic);
    }

    public Integer employeeProductionCapacity(EmployeeDto employee, MachineInCompanyDto machineInCompany, double coeffPriorityProduction, double coeffProductionSpeed) {
        Integer productionCapacity = levelEmployee(employee);

        Double machineContribution = fetchTheMachineContribution(machineInCompany);

        return (int)((productionCapacity * machineContribution * coeffProductionSpeed + 1) * coeffPriorityProduction);
    }

    public Integer levelEmployee(EmployeeDto employee) {
        Integer levelForAction = 0;

        int adjustmentFactor;
        switch (employee.getMood()) {
            case Mood.MAUVAISE:
                adjustmentFactor = -4;
                break;
            case Mood.BOF:
                adjustmentFactor = -2;
                break;
            case Mood.NEUTRE:
                adjustmentFactor = 0;
                break;
            case Mood.BONNE:
                adjustmentFactor = 1;
                break;
            case Mood.HEUREUSE:
                adjustmentFactor = 3;
                break;
            default:
                adjustmentFactor = 0;
                break;
        }

        double healthFactor;
        if (employee.getHealth() > 50) {
            healthFactor = 0.5;
        } else if (employee.getHealth() > 25) {
            healthFactor = 0.25;
        } else {
            healthFactor = 0;
        }

        levelForAction = (int) ((employee.getLevel() + adjustmentFactor) * healthFactor);

        return levelForAction;
    }

    public Double fetchTheMachineContribution(MachineInCompanyDto machineInCompany) {
        Machine machine = machineMap.get(machineInCompany.getMachineId());

        if (machine == null) {
            throw new ResourceNotFoundException("Machine avec l'ID : " + machineInCompany.getMachineId() + " non trouvée");
        }

        int apportMachine = 0;
        switch (machine.getProductionQuality()) {
            case MEDIOCRE:
                apportMachine = 50;
                break;
            case NORMAL:
                apportMachine = 100;
                break;
            case BONNE:
                apportMachine = 150;
                break;
            case PERFORMANTE:
                apportMachine = 200;
                break;
        }

        return apportMachine / 100.0;
    }

    public void defineThePriorityProduction(List<MachineInCompanyDto> machineInCompany, int totalProduction, List<EmployeeDto> employeeList, List<StockFinalMaterialDto> stockProduct, double coeffProductionSpeed, double coeffPriorityProduction){
        for (int i = 0; i < machineInCompany.size(); i++) {
            totalProduction += employeeProductionCapacity(employeeList.get(i), machineInCompany.get(i), coeffProductionSpeed, coeffPriorityProduction);
        }
        for (int i = 0; i < stockProduct.size(); i++) {
            stockProduct.get(i).setQuantityToProduct((int) Math.round((double)totalProduction * (double)stockProduct.get(i).getProportionProduct()/100));
        }
    }

    public void startProduct(CycleDto cycle, List<EmployeeDto> employeeList, List<MachineInCompanyDto> machineInCompany, List<StockFinalMaterialDto> stockProduct, StockMaterialDto stockMaterial, double coeffProductionSpeed, double coeffPriorityProduction, Statistic statistic){
        int index = 0;

        for (int i = 0; i < machineInCompany.size(); i++) {
            int productionCapacity = employeeProductionCapacity(employeeList.get(i), machineInCompany.get(i), coeffPriorityProduction, coeffProductionSpeed);

            for (int j = 0; j < productionCapacity; j++) {
                if (index < stockProduct.size()) {
                    if (stockProduct.get(index).getQuantityToProduct() > 0) {
                        if(stockMaterial.getQuantityHigh() > 0){
                            defineQualityOfProduction(employeeProductionCapacity(employeeList.get(i), machineInCompany.get(i), coeffPriorityProduction, coeffProductionSpeed), stockProduct.get(index), cycle.getProductionSpeed(), stockMaterial, 3, statistic);
                            stockMaterial.setQuantityHigh(stockMaterial.getQuantityHigh()-1);
                            statistic.setMaterialHighQty(statistic.getMaterialHighQty() + 1);
                        } else if (stockMaterial.getQuantityMid() > 0) {
                            defineQualityOfProduction(employeeProductionCapacity(employeeList.get(i), machineInCompany.get(i), coeffPriorityProduction, coeffProductionSpeed), stockProduct.get(index), cycle.getProductionSpeed(), stockMaterial, 0, statistic);
                            stockMaterial.setQuantityMid(stockMaterial.getQuantityMid()-1);
                            statistic.setMaterialMidQty(statistic.getMaterialMidQty() + 1);
                        } else if (stockMaterial.getQuantityLow() > 0) {
                            defineQualityOfProduction(employeeProductionCapacity(employeeList.get(i), machineInCompany.get(i), coeffPriorityProduction, coeffProductionSpeed), stockProduct.get(index), cycle.getProductionSpeed(), stockMaterial, -2, statistic);
                            stockMaterial.setQuantityLow(stockMaterial.getQuantityLow()-1);
                            statistic.setMaterialLowQty(statistic.getMaterialLowQty() + 1);
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

    public void defineQualityOfProduction(int productivityEmployee, StockFinalMaterialDto stockProduct, int productionSpeed, StockMaterialDto stockMaterial, int bonusMaterial, Statistic statistic){
        int tresholdProductivity = Math.round((productivityEmployee + bonusMaterial) * 100 / 30);
        int ecart = (int) (-Math.pow((double)(productionSpeed - 50)/ 10, 2)  + 50);

        double choice = Math.floor(Math.random() * 100);
        if (productionSpeed <= 50) {
            int lowTreshold = Math.round(tresholdProductivity + ecart / 2);
            int highTreshold = (int) Math.round(lowTreshold + ecart / 1.5);
            if (choice < lowTreshold) {
                stockProduct.setQuantityHigh(stockProduct.getQuantityHigh()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1HighQtyProd(statistic.getProduct1HighQtyProd() + 1);
                }else if(stockProduct.getName() == "Product2"){
                    statistic.setProduct2HighQtyProd(statistic.getProduct2HighQtyProd() + 1);
                }else {
                    statistic.setProduct3HighQtyProd(statistic.getProduct3HighQtyProd() + 1);
                }
            } else if (choice <= highTreshold) {
                stockProduct.setQuantityMid(stockProduct.getQuantityMid()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1MidQtyProd(statistic.getProduct1MidQtyProd() + 1);
                }else if(stockProduct.getName() == "Product2"){
                    statistic.setProduct2MidQtyProd(statistic.getProduct2MidQtyProd() + 1);
                }else {
                    statistic.setProduct3MidQtyProd(statistic.getProduct3MidQtyProd() + 1);
                }
            } else {
                stockProduct.setQuantityLow(stockProduct.getQuantityLow()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1LowQtyProd(statistic.getProduct1LowQtyProd() + 1);
                }else if(stockProduct.getName() == "Product1"){
                    statistic.setProduct2LowQtyProd(statistic.getProduct2LowQtyProd() + 1);
                }else {
                    statistic.setProduct3LowQtyProd(statistic.getProduct3LowQtyProd() + 1);

                }
            }
        } else {
            int lowTreshold = 100 - Math.round(tresholdProductivity + ecart / 2);
            int highTreshold = (int) Math.round(lowTreshold + ecart / 1.5);
            if (choice < lowTreshold) {
                stockProduct.setQuantityLow(stockProduct.getQuantityLow()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1LowQtyProd(statistic.getProduct1LowQtyProd() + 1);
                }else if(stockProduct.getName() == "Product1"){
                    statistic.setProduct2LowQtyProd(statistic.getProduct2LowQtyProd() + 1);
                }else {
                    statistic.setProduct3LowQtyProd(statistic.getProduct3LowQtyProd() + 1);
                }
            } else if (choice <= highTreshold) {
                stockProduct.setQuantityMid(stockProduct.getQuantityMid()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1MidQtyProd(statistic.getProduct1MidQtyProd() + 1);
                }else if(stockProduct.getName() == "Product2"){
                    statistic.setProduct2MidQtyProd(statistic.getProduct2MidQtyProd() + 1);
                }else {
                    statistic.setProduct3MidQtyProd(statistic.getProduct3MidQtyProd() + 1);
                }

            } else {
                stockProduct.setQuantityHigh(stockProduct.getQuantityHigh()+1);
                if(stockProduct.getName() == "Product1"){
                    statistic.setProduct1HighQtyProd(statistic.getProduct1HighQtyProd() + 1);
                }else if(stockProduct.getName() == "Product2"){
                    statistic.setProduct2HighQtyProd(statistic.getProduct2HighQtyProd() + 1);
                }else {
                    statistic.setProduct3HighQtyProd(statistic.getProduct3HighQtyProd() + 1);
                }
            }
        }
        stockProduct.setMonthProduction(stockProduct.getMonthProduction()+1);
    }




    public void companySell(List<EmployeeDto> employeeList, List<StockFinalMaterialDto> stockProduct, CycleDto cycle, Statistic statistic, CompanyDto company){
        for (int i = 0; i < employeeList.size(); i++) {
            employeeSell(employeeList.get(i), stockProduct, cycle, statistic, company);
        }
    }

    public void employeeSell(EmployeeDto employee, List<StockFinalMaterialDto> stockProduct, CycleDto cycle, Statistic statistic, CompanyDto company){
        int capacityToSell = sellCapacity(employee);
        double chanceToSell = (double)capacityToSell * 100 / 15;

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
                        stockProduct.get(index).setPrice(stockProduct.get(index).getPrice()+1);
                        if(stockProduct.get(index).getName() == "Product1"){
                            statistic.setProduct1HighQtySell(statistic.getProduct1HighQtySell() + 1);
                        }else if(stockProduct.get(index).getName() == "Product2"){
                                statistic.setProduct2HighQtySell(statistic.getProduct2HighQtySell() + 1);
                        }else {
                            statistic.setProduct3HighQtySell(statistic.getProduct3HighQtySell() + 1);
                        }
                        cycle.setCountGoodSell(cycle.getCountGoodSell()+1);
                        company.setWallet(company.getWallet() + stockProduct.get(index).getPrice());
                        statistic.setTotalIncomes(statistic.getTotalIncomes().add(new BigDecimal(20)));
                        i++;
                    } else if (stockProduct.get(index).getQuantityMid() > 0) {
                        stockProduct.get(index).setQuantityMid(stockProduct.get(index).getQuantityMid()-1);
                        stockProduct.get(index).setSell(stockProduct.get(index).getSell()+1);
                        stockProduct.get(index).setPrice(stockProduct.get(index).getPrice()+1);
                        if(stockProduct.get(index).getName() == "Product1"){
                            statistic.setProduct1MidQtySell(statistic.getProduct1MidQtySell() + 1);
                        }else if(stockProduct.get(index).getName() == "Product2"){
                            statistic.setProduct2MidQtySell(statistic.getProduct2MidQtySell() + 1);
                        }else {
                            statistic.setProduct3MidQtySell(statistic.getProduct3MidQtySell() + 1);
                        }
                        company.setWallet(company.getWallet() + 20);
                        statistic.setTotalIncomes(statistic.getTotalIncomes().add(new BigDecimal(20)));
                        i++;
                    } else if (stockProduct.get(index).getQuantityLow() > 0) {
                        stockProduct.get(index).setQuantityLow(stockProduct.get(index).getQuantityLow()-1);
                        stockProduct.get(index).setSell(stockProduct.get(index).getSell()+1);
                        stockProduct.get(index).setPrice(stockProduct.get(index).getPrice()+1);
                        if(stockProduct.get(index).getName() == "Product1"){
                            statistic.setProduct1LowQtySell(statistic.getProduct1LowQtySell() + 1);
                        }else if(stockProduct.get(index).getName() == "Product2"){
                            statistic.setProduct2LowQtySell(statistic.getProduct2LowQtySell() + 1);
                        }else {
                            statistic.setProduct3LowQtySell(statistic.getProduct3LowQtySell() + 1);
                        }
                        cycle.setCountBadSell(cycle.getCountBadSell()+1);
                        company.setWallet(company.getWallet() + 20);
                        statistic.setTotalIncomes(statistic.getTotalIncomes().add(new BigDecimal(20)));
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

    public void companyMarket(CycleDto cycle, CompanyDto company, List<EmployeeDto> employeeList, Statistic statistic){
        if (cycle.getCountGoodSell() > (100- (double)company.getPopularity()/100000)) {
            for (int i = 0; i < employeeList.size(); i++) {
                company.setPopularity(company.getPopularity() + marketEmployee(employeeList.get(i)));
                statistic.setPopularity(statistic.getPopularity() - marketEmployee(employeeList.get(i)));
                cycle.setCountGoodSell(0);
            }
        }

        if (cycle.getCountBadSell() > (100-(double)company.getPopularity()/100000)) {
            for (int i = 0; i < employeeList.size(); i++) {
                company.setPopularity(company.getPopularity() - 1);
                statistic.setPopularity(statistic.getPopularity() - 1);
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


