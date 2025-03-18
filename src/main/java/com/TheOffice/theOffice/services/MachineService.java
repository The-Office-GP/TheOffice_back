package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.staticModels.Machine.Machine;
import com.TheOffice.theOffice.entities.Company;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {
    public List<Machine> collectMachine(Company company){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Machine> machineList = new ArrayList<Machine>();

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
            // Désérialiser le fichier JSON dans l'objet EmployeeList
            List<Machine> machineListGlobal = objectMapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});
            switch (company.getSector()){
                case ("carpentry"):
                    machineListGlobal.removeIf(machine -> machine.getId() > 4 );
                    break;
                case ("creamery"):
                    machineListGlobal.removeIf(machine -> machine.getId() <= 4 || machine.getId() > 8);
                    break;
                case ("quarry"):
                    machineListGlobal.removeIf(machine -> machine.getId() <= 8);
                    break;
                default:
                    break;
            }
            machineList = machineListGlobal;
            System.out.println(machineList);

        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return machineList;
    }
}
