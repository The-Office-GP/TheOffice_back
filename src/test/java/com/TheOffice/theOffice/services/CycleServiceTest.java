package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.daos.StatisticDao;
import com.TheOffice.theOffice.dtos.CompanyDto;
import com.TheOffice.theOffice.dtos.EmployeeDto;
import com.TheOffice.theOffice.entities.Employee.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CycleServiceTest {

    @Test
    void testOfCapacityEmployee() {
        int marketingEmployee = 15;

        System.out.println((long)(0.5+((double)100 * marketingEmployee * 100 / 15)/30));

        System.out.println(100 * marketingEmployee );
    }
}