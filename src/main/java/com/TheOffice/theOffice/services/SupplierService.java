package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.TheOffice.theOffice.entities.Employee.Job;
import com.TheOffice.theOffice.staticModels.Suppliers.PriceSuppliers;
import com.TheOffice.theOffice.staticModels.Suppliers.SupplierName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SupplierService {
    public List<SupplierName> generateSupplier() {
        List<SupplierName> supplierList = new ArrayList<SupplierName>();

        for (int i = 0; i < 15; i++) {
            if(i < 5){
                supplierList.add(collectSupplier(, 1));
            } else if (i > 9) {
                supplierList.add(collectSupplier());
            }else {
                supplierList.add(collectSupplier());
            }
        }
        return supplierList;
    }

    public List<SupplierName> collectSupplier(Company company, int levelCompany){
        ObjectMapper objectMapper = new ObjectMapper();
        List<SupplierName> supplierNameList = new ArrayList<>();
        List<PriceSuppliers> priceSuppliersList = new ArrayList<>();

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/supplierName.json");
            File jsonFile2 = new File("src/main/java/com/TheOffice/theOffice/json/priceSuppliers.json");

            List<SupplierName> supplierNameListGlobal = objectMapper.readValue(jsonFile, new TypeReference<List<SupplierName>>() {});

            // Filtrage en fonction du secteur
            switch (company.getSector()) {
                case "carpentry":
                    supplierNameListGlobal.removeIf(supplierName -> supplierName.getId() > 31);
                    break;
                case "creamery":
                    supplierNameListGlobal.removeIf(supplierName -> supplierName.getId() <= 31 || supplierName.getId() > 61);
                    break;
                case "quarry":
                    supplierNameListGlobal.removeIf(supplierName -> supplierName.getId() <= 61);
                    break;
                default:
                    break;
            }

            supplierNameList = supplierNameListGlobal;
            priceSuppliersList = objectMapper.readValue(jsonFile2, new TypeReference<List<PriceSuppliers>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Génération du niveau aléatoire pour chaque fournisseur
        Random random = new Random();
        Map<Integer, Integer[]> levelMap = new HashMap<>();
        levelMap.put(1, new Integer[]{50, 20, 10, 10, 5});
        levelMap.put(2, new Integer[]{25, 25, 20, 10, 10});
        levelMap.put(3, new Integer[]{15, 17, 18, 20, 20});

        Integer[] ranges = levelMap.getOrDefault(levelCompany, new Integer[]{20, 20, 20, 20, 20});

        // Associer chaque fournisseur à un niveau
        Map<SupplierName, Integer> suppliersWithLevels = new HashMap<>();
        for (SupplierName supplier : supplierNameList) {
            int randomNumber = random.nextInt(100);
            int levelSupplier = choiceLevelSupplier(randomNumber, ranges[0], ranges[1], ranges[2], ranges[3], ranges[4]);
            suppliersWithLevels.put(supplier, levelSupplier);
        }

        // Retourner une liste avec les niveaux associés
        return new ArrayList<>(suppliersWithLevels.keySet());
    }

    public int choiceLevelSupplier(int rng, int range1, int range2, int range3, int range4, int range5){
        if (rng > 0 && rng <= range1){
            return 0;
        } else if (rng > range1 && rng <= range1+range2) {
            return 1;
        } else if (rng > range1+range2 && rng <= range1+range2+range3) {
            return 2;
        } else if (rng > range1+range2+range3 && rng <= range1+range2+range3+range4) {
            return 3;
        } else if (rng > range1+range2+range3+range4 && rng <= range1+range2+range3+range4+range5) {
            return 4;
        } else {
            return 5;
        }
    }
}
