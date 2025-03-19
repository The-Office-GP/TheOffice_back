package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.staticModels.EmployeeName;
import com.TheOffice.theOffice.staticModels.Salary;
import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public List<Employee> generateEmployee() {
        List<Employee> employeeList = new ArrayList<Employee>();

        for (int i = 0; i < 15; i++) {
            if(i < 5){
                employeeList.add(createEmployee(1, Job.PRODUCTION));
            } else if (i > 9) {
                employeeList.add(createEmployee(1, Job.MARKETING));
            }else {
                employeeList.add(createEmployee(1, Job.VENTE));
            }
        }
        return employeeList;
    }


    public Employee createEmployee(int levelCompany, Job job){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Salary> salaryList = new ArrayList<Salary>();
        List<EmployeeName> nameList = new ArrayList<EmployeeName>();

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/salary.json");
            File jsonFile2 = new File("src/main/java/com/TheOffice/theOffice/json/employeeNameList.json");

            // Désérialiser le fichier JSON dans l'objet EmployeeList
            salaryList = objectMapper.readValue(jsonFile, new TypeReference<List<Salary>>() {});
            nameList = objectMapper.readValue(jsonFile2, new TypeReference<List<EmployeeName>>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int randomNumber = random.nextInt(100 - 0) + 0;
        int choiceName = random.nextInt(nameList.size() - 1) +1;

        Map<Integer, Integer[]> levelMap = new HashMap<>();
        levelMap.put(1, new Integer[]{50, 20, 10, 10, 5});
        levelMap.put(2, new Integer[]{25, 25, 20, 10, 10});
        levelMap.put(3, new Integer[]{15, 17, 18, 20, 20});

        Integer[] ranges = levelMap.get(levelCompany);
        int levelEmployee = choiceLevelEmployee(randomNumber, ranges[0], ranges[1], ranges[2], ranges[3], ranges[4]);

        return new Employee(
                nameList.get(choiceName).getId(),
                nameList.get(choiceName).getName(),
                Gender.HOMME,
                0,
                salaryList.get(levelEmployee).getSalary(),
                salaryList.get(levelEmployee).getLevelSkill(),
                Mood.NEUTRE,
                Status.ACTIF,
                job,
                100,
                nameList.get(choiceName).getPath()
        );
    }

    public int choiceLevelEmployee(int rng, int range1, int range2, int range3, int range4, int range5){
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
