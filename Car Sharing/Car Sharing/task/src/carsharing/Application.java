package carsharing;

import carsharing.dao.CarDao;
import carsharing.dao.CompanyDao;
import carsharing.dao.CustomerDao;
import carsharing.dao.impl.CarDaoImpl;
import carsharing.dao.impl.CompanyDaoImpl;
import carsharing.dao.impl.CustomerDaoImpl;
import carsharing.service.CarService;
import carsharing.service.CompanyService;
import carsharing.service.CustomerService;
import carsharing.ui.MenuManager;
import carsharing.ui.UserInput;

public class Application {

    private final String databaseFileName;

    public Application(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public void run() {
        CompanyDao companyDao = new CompanyDaoImpl(databaseFileName);
        CarDao carDao = new CarDaoImpl(databaseFileName);
        CustomerDao customerDao = new CustomerDaoImpl(databaseFileName);

        UserInput userInput = new UserInput();

        CarService carService = new CarService(carDao, userInput);
        CompanyService companyService = new CompanyService(companyDao, carService, userInput);
        CustomerService customerService = new CustomerService(customerDao, carDao, companyDao, userInput);

        setupDatabase(customerDao, carDao, companyDao);

        MenuManager menuManager = new MenuManager(userInput, companyService, customerService);
        menuManager.start();
    }

    private void setupDatabase(CustomerDao customerDao, CarDao carDao, CompanyDao companyDao) {
        companyDao.createTable();
        carDao.createTable();
        customerDao.createTable();
    }

}
