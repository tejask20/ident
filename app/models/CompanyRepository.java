package models;

import java.util.*;

public class CompanyRepository {

    private static CompanyRepository companyRepository;
    Set<Company> companies = new HashSet<>();

    public static CompanyRepository getCompanyRepository()
    {
        if (companyRepository == null) {
            companyRepository = new CompanyRepository();
        }
        return companyRepository;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public Company getCompany(int id) {
        Company company = companies.stream()
                .filter(x -> id == x.getId())
                .findAny()
                .orElse(null);
        return company;
    }

    public List<Company> getAllCompanies() {
        return new ArrayList<Company>(companies);
    }
}
