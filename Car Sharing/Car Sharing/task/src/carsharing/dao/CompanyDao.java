package carsharing.dao;

import carsharing.models.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {

    void createTable();
    List<Company> getAllCompanies();
    Optional<Company> getCompany(int id);
    void createCompany(String name);
    void deleteAllCompanies();
    void dropTable();

}
