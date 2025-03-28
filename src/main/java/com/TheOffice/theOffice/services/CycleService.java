package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.dtos.*;
import com.TheOffice.theOffice.entities.Employee.Job;
import com.TheOffice.theOffice.entities.Employee.Mood;
import com.TheOffice.theOffice.entities.Employee.Status;
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

    public void runCycle(CompanyDto company){
        int dayForCycle = 0;
        company.getCycle().setStep(company.getCycle().getStep() + 1);
        long stockPopularity = Math.round((double)company.getPopularity()/10);
        List<EmployeeDto> productionEmployee = jobFilter(company.getEmployees(), Job.PRODUCTION);
        List<EmployeeDto> sellEmployee = jobFilter(company.getEmployees(), Job.VENTE);
        List<EmployeeDto> marketingEmployee = jobFilter(company.getEmployees(), Job.MARKETING);

        while (dayForCycle < 30 ){
            if(company.getMachinesInCompany().size() < productionEmployee.size()){
                for (int i = 0; i < company.getMachinesInCompany().size(); i++) {
                    employeeProduct(productionEmployee.get(i), company.getCycle(), company.getMachinesInCompany().get(i), company.getStockFinalMaterials(), company.getStockMaterial(), company.getStatistic().getLast() );
                }
            }else{
                for (int i = 0; i < productionEmployee.size(); i++) {
                    employeeProduct(productionEmployee.get(i), company.getCycle(), company.getMachinesInCompany().get(i), company.getStockFinalMaterials(), company.getStockMaterial(), company.getStatistic().getLast() );

                }
            }

            if(dayForCycle > 6){
                for (int i = 0; i < sellEmployee.size(); i++) {
                    employeesSell(company.getCycle(), sellEmployee.get(i), company.getStockFinalMaterials(), company, company.getStatistic().getLast(), stockPopularity);

                }
                for (int i = 0; i < marketingEmployee.size(); i++) {
                    employeeMakeMarketing(marketingEmployee.get(i), company);
                }
            }

            System.out.println("Envide de caner3");
            dayForCycle++;

        }

        for (int i = 0; i < company.getEmployees().size(); i++) {
            company.setWallet(company.getWallet() - company.getEmployees().get(i).getSalary().doubleValue());
            company.getStatistic().getLast().setTotalExpenses(company.getStatistic().getLast().getTotalExpenses().add(company.getEmployees().get(i).getSalary()));
            if (company.getEmployees().get(i).getStatus() == Status.ACTIF){
                company.getEmployees().get(i).setHealth(company.getEmployees().get(i).getHealth() - (int)(Math.random() * 5));

            }else{
                company.getEmployees().get(i).setHealth(100);
            }
        }

        company.getStatistic().getLast().setMonth(company.getCycle().getStep());
        company.getStatistic().getLast().setPopularity(company.getPopularity());
    }

    public List<EmployeeDto> jobFilter(List<EmployeeDto> listForFilter, Job job){
        List<EmployeeDto> newList = new ArrayList<>();
        for (int i = 0; i < listForFilter.size(); i++) {
            if(listForFilter.get(i).getJob() == job && listForFilter.get(i).getStatus() == Status.ACTIF){
                newList.add(listForFilter.get(i));
            }
        }
        return newList;
    }

    // Product part
    public void employeeProduct(EmployeeDto employee, CycleDto cycle, MachineInCompanyDto machineInCompanyDto, List<StockFinalMaterialDto> stockToProduct, StockMaterialDto stockMaterial, Statistic statistic){
        double coeffProductionSpeed = (150.0 - (double)cycle.getProductionSpeed()) / 100.0;
        double coeffPriorityProduction = (double)cycle.getPriorityProduction() / 100.0;
        long productionCapacity = capacityEmployee(employee);

        System.out.println((150.0 - (double)cycle.getProductionSpeed()));

        double machineContribution = fetchTheMachineContribution(machineInCompanyDto);

        long employeeCapacity = Math.round(((double)productionCapacity * machineContribution * coeffProductionSpeed + 0.5) * coeffPriorityProduction);
        for (int i = 0; i < stockToProduct.size(); i++) {
            System.out.println((double)employeeCapacity / 100);
            System.out.println((int)((double)stockToProduct.get(i).getProportionProduct() * (double)employeeCapacity / 100));
            long quantityToProduct = Math.round((double)stockToProduct.get(i).getProportionProduct() * (double)employeeCapacity / 100);
            for (int j = 0; j < quantityToProduct; j++) {
                defineQualityOfProduction(employeeCapacity, stockToProduct.get(i), cycle.getProductionSpeed(), stockMaterial, statistic);
            }
    }
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

    public void defineQualityOfProduction(long productivityEmployee, StockFinalMaterialDto stockProduct, int productionSpeed, StockMaterialDto stockMaterial, Statistic statistic) {
        if (stockMaterial.getQuantityHigh() > 0) {
            processProduction(productivityEmployee, stockProduct, productionSpeed, statistic, 15, 0, stockMaterial, "High");
        } else if (stockMaterial.getQuantityMid() > 0) {
            processProduction(productivityEmployee, stockProduct, productionSpeed, statistic, -5, 10, stockMaterial, "Mid");
        } else if (stockMaterial.getQuantityLow() > 0) {
            processProduction(productivityEmployee, stockProduct, productionSpeed, statistic, -8, -7, stockMaterial, "Low");
        }
    }

    private void processProduction(long productivityEmployee, StockFinalMaterialDto stockProduct, int productionSpeed, Statistic statistic, int bonusMaterialForLow, int bonusMaterialForHigh, StockMaterialDto stockMaterial, String materialQuality) {
        switch ((int)productivityEmployee){
            case 1:
                choseTheQuality(6, 4, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 2:
                choseTheQuality(12, 5, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 3:
                choseTheQuality(18, 10, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 4:
                choseTheQuality(24, 15, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 5:
                choseTheQuality(30, 20, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 6:
                choseTheQuality(36, 30, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 7:
                choseTheQuality(42, 25, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 8:
                choseTheQuality(48, 30, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 9:
                choseTheQuality(54, 25, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 10:
                choseTheQuality(60, 25, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 11:
                choseTheQuality(66, 15, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 12:
                choseTheQuality(72, 15, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 13:
                choseTheQuality(78, 15, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 14:
                choseTheQuality(84, 10, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            case 15:
                choseTheQuality(90, 10, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
            default:
                choseTheQuality(90, 10, stockProduct, productionSpeed, statistic, bonusMaterialForLow, bonusMaterialForHigh, stockMaterial, materialQuality);
                break;
        }
    }

    private void choseTheQuality(int lowTreshold, int highTreshold, StockFinalMaterialDto stockProduct, int productionSpeed, Statistic statistic, int bonusMaterialForLow, int bonusMaterialForHigh, StockMaterialDto stockMaterial, String materialQuality){
        double choice = Math.floor(Math.random() * 100);
        lowTreshold += lowTreshold + bonusMaterialForLow;
        highTreshold += lowTreshold + bonusMaterialForHigh;

        if(productionSpeed<= 50){
            lowTreshold += Math.round((50 - (double)productionSpeed) / 5.0);
        } else {
            lowTreshold -= Math.round(((double)productionSpeed - 50) / 10.0);
            highTreshold -= Math.round(((double)productionSpeed - 50) / 10.0);
        }

        if (choice < lowTreshold) {
            stockProduct.setQuantityHigh(stockProduct.getQuantityHigh() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "High");
        } else if (choice <= lowTreshold + highTreshold) {
            stockProduct.setQuantityMid(stockProduct.getQuantityMid() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "Mid");
        } else {
            stockProduct.setQuantityLow(stockProduct.getQuantityLow() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "Low");
        }

        updateStockAndProduction(stockMaterial, materialQuality, stockProduct);
    }


    /*private void processProduction(double choice, int productivityEmployee, StockFinalMaterialDto stockProduct, int productionSpeed, Statistic statistic, int bonusMaterial, StockMaterialDto stockMaterial, String materialQuality) {
        int tresholdProductivity = (int) Math.floor((productivityEmployee + bonusMaterial) * 100.0 / 30);
        int ecart = (int) (-Math.pow((double) (productionSpeed - 50) / 10, 2) + 50);

        int lowTreshold = (productionSpeed <= 50) ? Math.round(tresholdProductivity + ecart / 2) : 100 - Math.round(tresholdProductivity + ecart / 2);
        int highTreshold = (int) Math.round(lowTreshold + ecart / 1.5);

        if (choice < lowTreshold) {
            stockProduct.setQuantityHigh(stockProduct.getQuantityHigh() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "High");
        } else if (choice <= highTreshold) {
            stockProduct.setQuantityMid(stockProduct.getQuantityMid() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "Mid");
        } else {
            stockProduct.setQuantityLow(stockProduct.getQuantityLow() + 1);
            updateStatisticsForProduct(stockProduct.getName(), statistic, "Low");
        }

        updateStockAndProduction(stockMaterial, materialQuality, stockProduct);
    }*/

    private void updateStatisticsForProduct(String productName, Statistic statistic, String quality) {
        if ("Product1".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct1HighQtyProd(statistic.getProduct1HighQtyProd() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct1MidQtyProd(statistic.getProduct1MidQtyProd() + 1);
            } else {
                statistic.setProduct1LowQtyProd(statistic.getProduct1LowQtyProd() + 1);
            }
        } else if ("Product2".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct2HighQtyProd(statistic.getProduct2HighQtyProd() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct2MidQtyProd(statistic.getProduct2MidQtyProd() + 1);
            } else {
                statistic.setProduct2LowQtyProd(statistic.getProduct2LowQtyProd() + 1);
            }
        } else if ("Product3".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct3HighQtyProd(statistic.getProduct3HighQtyProd() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct3MidQtyProd(statistic.getProduct3MidQtyProd() + 1);
            } else {
                statistic.setProduct3LowQtyProd(statistic.getProduct3LowQtyProd() + 1);
            }
        } else if ("Product4".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct4HighQtyProd(statistic.getProduct4HighQtyProd() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct4MidQtyProd(statistic.getProduct4MidQtyProd() + 1);
            } else {
                statistic.setProduct4LowQtyProd(statistic.getProduct4LowQtyProd() + 1);
            }
        }
    }

        private void updateStockAndProduction(StockMaterialDto stockMaterial, String materialQuality, StockFinalMaterialDto stockProduct) {
            switch (materialQuality) {
                case "High":
                    stockMaterial.setQuantityHigh(stockMaterial.getQuantityHigh() - 1);
                    break;
                case "Mid":
                    stockMaterial.setQuantityMid(stockMaterial.getQuantityMid() - 1);
                    break;
                case "Low":
                    stockMaterial.setQuantityLow(stockMaterial.getQuantityLow() - 1);
                    break;
            }

            stockProduct.setMonthProduction(stockProduct.getMonthProduction() + 1);
        }

        //Sell part
    public void employeesSell(CycleDto cycle, EmployeeDto employee, List<StockFinalMaterialDto> stockProduct, CompanyDto company, Statistic statistic, long popularityStack){
        long sellCapacity = capacityEmployee(employee);

        for (int i = 0; i < sellCapacity; i++) {
            chooseTheProductForSell(employee, stockProduct);
            successSell(stockProduct, sellCapacity, company, statistic, popularityStack);
        }

    }

    public void chooseTheProductForSell(EmployeeDto employee, List<StockFinalMaterialDto> stockProduct) {
        List<StockFinalMaterialDto> stock = new ArrayList<>();

        for (StockFinalMaterialDto product : stockProduct) {
            if (employee.getPriorityAction().toString().equals(product.getName()) && product.totalStock() > 0) {
                stock.add(product);
            }
        }

        if (stock.isEmpty()) {
            stockProduct.sort(this::compare);
        } else {
            StockFinalMaterialDto selectedProduct = stock.get(0);
            stockProduct.remove(selectedProduct);
            stockProduct.sort(this::compare);
            stockProduct.add(0, selectedProduct);
        }
    }

    private int compare(StockFinalMaterialDto p1, StockFinalMaterialDto p2) {
        // Comparaison basée sur le prix, en supposant que StockFinalMaterialDto a une méthode getPrice()
        return Double.compare(p1.getQuantityHigh(), p2.getQuantityHigh());
    }

    public void successSell(List<StockFinalMaterialDto> stockProduct, long capacityToSell, CompanyDto company, Statistic statistic, long popularityStack) {
        long chanceToSell = Math.round((double)capacityToSell * 100 / 30);

        if (popularityStack > 0){
            chanceToSell = 100;
            popularityStack--;
        }

        if (stockProduct.size() > 0) {
            int index = 0;
            int i = 0;

            while (i < capacityToSell / 4) {
                long successSell =  Math.round(Math.floor(Math.random() * 100));
                if (successSell <= chanceToSell && index < stockProduct.size()) {
                    StockFinalMaterialDto product = stockProduct.get(index);
                    if (sellProduct(product, company, statistic)) {
                        i++;
                    } else {
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

    public Long capacityEmployee(EmployeeDto employee) {
        Long levelForAction = 0L;

        int moodFactor;
        switch (employee.getMood()) {
            case Mood.MAUVAISE:
                moodFactor = -4;
                break;
            case Mood.BOF:
                moodFactor = -2;
                break;
            case Mood.NEUTRE:
                moodFactor = 0;
                break;
            case Mood.BONNE:
                moodFactor = 1;
                break;
            case Mood.HEUREUSE:
                moodFactor = 3;
                break;
            default:
                moodFactor = 0;
                break;
        }

        double healthFactor;
        double bonus;
        if (employee.getHealth() > 70){
            healthFactor = 1.0;
            bonus = 0.5;
        } else if (employee.getHealth() > 40) {
            healthFactor = 0.5;
            bonus = 0.5;
        } else if (employee.getHealth() > 20) {
            healthFactor = 0.25;
            bonus = 0.5;
        }else {
            healthFactor = 0;
            bonus = 0;
        }
        levelForAction = Math.round((double)(employee.getLevel() + (double)moodFactor) * healthFactor + bonus);
        return levelForAction;
    }

    private boolean sellProduct(StockFinalMaterialDto product, CompanyDto company, Statistic statistic) {
        if (product.getQuantityHigh() > 0) {
            updateProductSale(product, "High", company, statistic);
            return true;
        } else if (product.getQuantityMid() > 0) {
            updateProductSale(product, "Mid",company, statistic);
            return true;
        } else if (product.getQuantityLow() > 0) {
            updateProductSale(product, "Low",company, statistic);
            return true;
        }
        return false;
    }

    private void updateProductSale(StockFinalMaterialDto product, String quality, CompanyDto company, Statistic statistic) {
        switch (quality) {
            case "High":
                product.setQuantityHigh(product.getQuantityHigh() - 1);
                updateStatisticsForSale(product.getName(), statistic, "High");
                double rng = Math.round(Math.random() * 100);
                company.setPopularity(company.getPopularity() + Math.round(Math.random()));
                company.setPopularity(company.getPopularity() + 1 );
                break;
            case "Mid":
                product.setQuantityMid(product.getQuantityMid() - 1);
                updateStatisticsForProduct(product.getName(), statistic, "Mid");
                company.setPopularity(company.getPopularity() - Math.round(Math.random()));
                company.setPopularity(company.getPopularity() + Math.round(Math.random()));
                break;
            case "Low":
                product.setQuantityLow(product.getQuantityLow() - 1);
                updateStatisticsForProduct(product.getName(), statistic, "Low");
                company.setPopularity(company.getPopularity() - Math.round(Math.random()*6.0+1));
                if(company.getPopularity() < 0){
                    company.setPopularity(0L);
                }
                break;
        }

        product.setSell(product.getSell() + 1);
        company.setWallet(company.getWallet() + 35);
        statistic.setTotalIncomes(statistic.getTotalIncomes().add(new BigDecimal(35)));
    }

    private void updateStatisticsForSale(String productName, Statistic statistic, String quality) {
        if ("Product1".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct1HighQtySell(statistic.getProduct1HighQtySell() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct1MidQtySell(statistic.getProduct1MidQtySell() + 1);
            } else {
                statistic.setProduct1LowQtySell(statistic.getProduct1LowQtySell() + 1);
            }
        } else if ("Product2".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct2HighQtySell(statistic.getProduct2HighQtySell() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct2MidQtySell(statistic.getProduct2MidQtySell() + 1);
            } else {
                statistic.setProduct2LowQtySell(statistic.getProduct2LowQtySell() + 1);
            }
        } else if ("Product3".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct3HighQtySell(statistic.getProduct3HighQtySell() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct3MidQtySell(statistic.getProduct3MidQtySell() + 1);
            } else {
                statistic.setProduct3LowQtySell(statistic.getProduct3LowQtySell() + 1);
            }
        } else if ("Product4".equals(productName)) {
            if ("High".equals(quality)) {
                statistic.setProduct4HighQtySell(statistic.getProduct4HighQtySell() + 1);
            } else if ("Mid".equals(quality)) {
                statistic.setProduct4MidQtySell(statistic.getProduct4MidQtySell() + 1);
            } else {
                statistic.setProduct4LowQtySell(statistic.getProduct4LowQtySell() + 1);
            }
        }
    }

    public void employeeMakeMarketing(EmployeeDto employee, CompanyDto companyDto){
        long marketingEmployee = capacityEmployee(employee);


        companyDto.setPopularity(companyDto.getPopularity() + (companyDto.getCycle().getPriorityMarketing() * marketingEmployee/50) );
    }
}


