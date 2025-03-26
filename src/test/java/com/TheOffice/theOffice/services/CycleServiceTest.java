package com.TheOffice.theOffice.services;

import com.TheOffice.theOffice.daos.StatisticDao;
import com.TheOffice.theOffice.dtos.EmployeeDto;
import com.TheOffice.theOffice.entities.Employee.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CycleServiceTest {
    StatisticDao statisticDao

    @Test
    void testOfCapacityEmployee() {
assertArrayEquals(2, statisticDao.findAllCompanyStatistic(1));
    }
}