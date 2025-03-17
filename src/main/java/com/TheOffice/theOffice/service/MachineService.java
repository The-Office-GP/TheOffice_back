package com.TheOffice.theOffice.service;

import com.TheOffice.theOffice.classes.Machine;
import com.TheOffice.theOffice.classes.MachineList;
import com.TheOffice.theOffice.entities.Company;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class MachineService {
    public MachineList collectMachine(Company company){
        System.out.println("coucou");
        ObjectMapper objectMapper = new ObjectMapper();
        MachineList machineList = new MachineList();
        System.out.println("coucou2" + machineList);

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/machine.json");
            System.out.println("coucou3" + jsonFile);
            // Désérialiser le fichier JSON dans l'objet EmployeeList
            MachineList machineListGlobal = objectMapper.readValue(jsonFile, MachineList.class);
            for (int i = 0; i < machineListGlobal.getMachineList().size(); i++) {
                if(machineListGlobal.getMachineList().get(i).getId() <= 3 && company.getSector()=="carpentry"){
                    machineList.getMachineList().add(machineListGlobal.getMachineList().get(i));
                } else if (machineListGlobal.getMachineList().get(i).getId() > 3 && machineListGlobal.getMachineList().get(i).getId() <= 6 && company.getSector()=="creamery") {
                    machineList.getMachineList().add(machineListGlobal.getMachineList().get(i));
                } else {
                    machineList.getMachineList().add(machineListGlobal.getMachineList().get(i));
                }
            }
            machineList = machineListGlobal;
            System.out.println("coucou4" + machineList.getMachineList());

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println("machineList");
        System.out.println(machineList.getMachineList());
        return machineList;
    }
}
