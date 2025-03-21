package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.entities.Company;
import com.TheOffice.theOffice.staticModels.Suppliers.PriceSuppliers;
import com.TheOffice.theOffice.staticModels.Suppliers.Supplier;
import com.TheOffice.theOffice.staticModels.Suppliers.SupplierName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Supplier> collectSupplier(Company company, int levelCompany) {

        List<SupplierName> supplierNameList = new ArrayList<>();
        List<PriceSuppliers> priceSuppliersList = new ArrayList<>();

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/supplierName.json");
            File jsonFile2 = new File("src/main/java/com/TheOffice/theOffice/json/priceSuppliers.json");

            supplierNameList = objectMapper.readValue(jsonFile, new TypeReference<List<SupplierName>>() {});
            priceSuppliersList = objectMapper.readValue(jsonFile2, new TypeReference<List<PriceSuppliers>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int randomNumber = random.nextInt(100 - 0) + 0;
        int choiceName = random.nextInt(supplierNameList.size() - 1) +1;
        Map<Integer, Integer[]> levelMap = new HashMap<>();
        levelMap.put(1, new Integer[]{50, 20, 10, 10, 5});
        levelMap.put(2, new Integer[]{25, 25, 20, 10, 10});
        levelMap.put(3, new Integer[]{15, 17, 18, 20, 20});

        Integer[] ranges = levelMap.get(levelCompany);
        int levelSupplier = choiceLevelSupplier(randomNumber, ranges[0], ranges[1], ranges[2], ranges[3], ranges[4]);

        return new Supplier(
                supplierNameList.get(choiceName).getId(),
                supplierNameList.get(choiceName).getName(),
                priceSuppliersList.get(choiceName).getPrice(),
                priceSuppliersList.get(choiceName).getLevelQuality()
        )
//            if (supplierFile.exists()) {
//                List<SupplierName> supplierNameListGlobal = objectMapper.readValue(supplierFile, new TypeReference<List<SupplierName>>() {});
//
//                // Filtrage selon le secteur
//                supplierNameList = supplierNameListGlobal.stream()
//                        .filter(supplierName -> {
//                            switch (company.getSector()) {
//                                case "carpentry": return supplierName.getId() <= 30;
//                                case "creamery": return supplierName.getId() > 30 && supplierName.getId() <= 60;
//                                case "quarry": return supplierName.getId() > 60;
//                                default: return false;
//                            }
//                        })
//                        .collect(Collectors.toList());
//            } else {
//                throw new IOException("Fichier supplierName.json non trouvé");
//            }
//
//            if (priceFile.exists()) {
//                priceSuppliersList = objectMapper.readValue(priceFile, new TypeReference<List<PriceSuppliers>>() {});
//            } else {
//                throw new IOException("Fichier priceSuppliers.json non trouvé");
//            }



        // Génération du niveau aléatoire pour chaque fournisseur
        return assignSupplierLevels(supplierNameList, levelCompany);
    }

    private List<SupplierName> assignSupplierLevels(List<SupplierName> supplierNameList, int levelCompany) {

        Integer[] ranges = levelMap.getOrDefault(levelCompany, new Integer[]{20, 20, 20, 20, 20});
        List<SupplierName> resultList = new ArrayList<>();

        for (SupplierName supplier : supplierNameList) {
            int rng = random.nextInt(100);
            int level = choiceLevelSupplier(rng, ranges);
            resultList.add(supplier);
        }
        return resultList;
    }

    private int choiceLevelSupplier(int rng, Integer[] ranges) {
        int sum = 0;
        for (int i = 0; i < ranges.length; i++) {
            sum += ranges[i];
            if (rng <= sum) return i;
        }
        return ranges.length; // Niveau max par défaut
    }
}
