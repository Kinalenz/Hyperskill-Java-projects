package carsharing.service;

import carsharing.dao.CarDao;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.ui.UserInput;

import java.util.List;

public class CarService {

    private final CarDao carDao;
    private final UserInput userInput;

    public CarService(CarDao carDao, UserInput userInput) {
        this.carDao = carDao;
        this.userInput = userInput;
    }

    void showCarList(Company company) {
        List<Car> cars = carDao.getCarsByCompany(company);
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
            return;
        }
        System.out.println("\nChoose a car:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cars.get(i).getName());
        }
    }

    void createCar(Company company) {
        System.out.println("\nEnter the car name:");
        String carName = this.userInput.readLine();
        carDao.createCar(carName, company);
        System.out.println("The car was added!");
    }

}
