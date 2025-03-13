package com.TheOffice.theOffice.service;

import com.TheOffice.theOffice.classes.EmployeeNameList;
import com.TheOffice.theOffice.classes.EmployeeName;
import com.TheOffice.theOffice.daos.EmployeeDao;
import com.TheOffice.theOffice.entities.Employee.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public List<Employee> generateEmployee(){
        ObjectMapper objectMapper = new ObjectMapper();

        EmployeeNameList employeeNameList = new EmployeeNameList();
        List<Employee> employeeList;

        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/employeeNameList.json");

            // Désérialiser le fichier JSON dans l'objet EmployeeList
            EmployeeNameList nameList = objectMapper.readValue(jsonFile, EmployeeNameList.class);

            for (EmployeeName employeeName : nameList.getNameList()) {
                System.out.println(employeeName);

                employeeList.add(new Employee(employeeName.getId(), empl))
                employeeNameList = nameList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public Employee createEmployee(int rangeMin, int rangeMax, )
}
