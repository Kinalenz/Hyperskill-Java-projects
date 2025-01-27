package carsharing.dao;

import carsharing.models.Car;
import carsharing.models.Company;

import java.util.List;
import java.util.Optional;

public interface CarDao {

    void createTable();
    List<Car> getCarsByCompany(Company company);
    List<Car> getFreeCarsByCompany(Company company);
    Optional<Car> getCar(int id);
    void createCar(String name, Company company);
    void deleteAllCars();
    void dropTable();
}
