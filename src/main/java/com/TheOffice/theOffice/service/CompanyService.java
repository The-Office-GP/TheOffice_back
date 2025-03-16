package com.TheOffice.theOffice.service;

import com.TheOffice.theOffice.daos.CompanyDao;
import com.TheOffice.theOffice.entities.Company;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyDao companyDao;

    public CompanyService(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public Company createCompany(Company company) {
        // Insérer en base de données
        int generatedId = companyDao.save(
                company.getSector(),
                company.getName(),
                company.getCreation_date(),
                company.getId_local(), // 👈 Utilisation directe
                company.getId_user()
        );

        return new Company(
                (long) generatedId,
                company.getSector(),
                company.getName(),
                company.getCreation_date(),
                company.getId_local(),
                null, // Local sera chargé plus tard si nécessaire
                company.getId_user(),
                company.getMachines(),
                company.getEmployees(),
                company.getEvents()
        );
    }
}
