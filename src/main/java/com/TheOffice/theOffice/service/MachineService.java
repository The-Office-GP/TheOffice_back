package com.TheOffice.theOffice.service;

import com.TheOffice.theOffice.classes.Machine;
import com.TheOffice.theOffice.classes.MachineList;
import com.TheOffice.theOffice.entities.Company;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MachineService {
    public MachineList collectMachine(Company company){
        System.out.println("coucou");
        ObjectMapper objectMapper = new ObjectMapper();
        MachineList machineList = new MachineList();
        System.out.println(machineList.getMachineList());
        System.out.println("coucou2" + machineList);

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
            // Désérialiser le fichier JSON dans l'objet EmployeeList
            MachineList machineListGlobal = objectMapper.readValue(jsonFile, MachineList.class);
            System.out.println("sector: " + machineList.getMachineList());
            switch (company.getSector()){
                case ("carpentry"):
                    machineListGlobal.getMachineList().removeIf(machine -> machine.getId() > 4 );
                    break;
                case ("creamery"):
                    machineListGlobal.getMachineList().removeIf(machine -> machine.getId() <= 4 || machine.getId() > 8);
                    break;
                case ("quarry"):
                    machineListGlobal.getMachineList().removeIf(machine -> machine.getId() <= 8);
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
