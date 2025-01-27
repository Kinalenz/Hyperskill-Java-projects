package carsharing.service;

import carsharing.dao.CarDao;
import carsharing.dao.CompanyDao;
import carsharing.dao.CustomerDao;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;
import carsharing.ui.UserInput;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private static final System.Logger LOGGER = System.getLogger(CustomerService.class.getName());

    private final CustomerDao customerDao;
    private final CarDao carDao;
    private final CompanyDao companyDao;
    private final UserInput userInput;

    public CustomerService(CustomerDao customerDao, CarDao carDao, CompanyDao companyDao, UserInput userInput) {
        this.customerDao = customerDao;
        this.carDao = carDao;
        this.companyDao = companyDao;
        this.userInput = userInput;
    }

    public void showCustomerMenu(Customer customer) {
        while (true) {
            System.out.println();
            System.out.println("""
                    1. Rent a car
                    2. Return a rented car
                    3. My rented car
                    0. Back""");
            int userChoice = userInput.readInt();
            switch (userChoice) {
                case 1 -> rent(customer);
                case 2 -> returnRentedCar(customer);
                case 3 -> showRentedCarDetails(customer);
                case 0 -> {
                    System.out.println();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void showCustomerList() {
        List<Customer> customers = customerDao.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!\n");
            return;
        }
        System.out.println("\nCustomer list:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, customers.get(i).getName());
        }
        System.out.println("0. Back");
        int userChoice = userInput.readInt();
        if (userChoice == 0) {
            return;
        }
        else if (userChoice > 0 && userChoice <= customers.size()) {
            showCustomerMenu(customers.get(userChoice - 1));
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    public void createCustomer() {
        System.out.println("\nEnter the customer name:");
        String customerName = this.userInput.readLine();
        customerDao.createCustomer(customerName);
        System.out.println("The customer was added!\n");
    }

    private void returnRentedCar(Customer customer) {
        if (customer.getCarId() == null) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }
        try {
            Optional<Car> optionalCar = carDao.getCar(customer.getCarId());
            if (optionalCar.isPresent()) {
                customer.setCarId(null);
                customerDao.updateCustomerRentedCar(customer);
                System.out.println("\nYou've returned a rented car!");
            } else {
                System.out.println("\nYou didn't rent a car!");
            }
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error returning rented car.", e);
        }
    }

    private void showRentedCarDetails(Customer customer) {
        Optional<Car> optionalCar = carDao.getCar(customer.getCarId());
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            System.out.println("\nYour rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            Optional<Company> optionalCompany = companyDao.getCompany(car.getCompanyId());
            if (optionalCompany.isPresent()) {
                System.out.println(optionalCompany.get().getName());
            } else {
                System.out.println("Company information is unavailable.");
            }
        } else {
            System.out.println("\nYou didn't rent a car!");
        }
    }

    private void rent(Customer customer) {
        if (customer.getCarId() > 0) {
            System.out.println("\nYou've already rented a car!");
            return;
        }
        showCompanyListForCustomer(customer);
    }

    private void showCompanyListForCustomer(Customer customer) {
        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return;
        }
        System.out.println("\nChoose the company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, companies.get(i).getName());
        }
        System.out.println("0. Back");
        int userChoice = userInput.readInt();
        if (userChoice == 0) {
            return;
        } else if (userChoice > 0 && userChoice <= companies.size()) {
            showAvailableCarsForCustomer(customer, companies.get(userChoice - 1));
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void showAvailableCarsForCustomer(Customer customer, Company company) {
        List<Car> cars = carDao.getFreeCarsByCompany(company);
        if (cars.isEmpty()) {
            System.out.printf("\nNo available cars in the '%s' company", company.getName());
            return;
        }
        System.out.println("\nChoose a car:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cars.get(i).getName());
        }
        System.out.println("0. Back");
        int userChoice = userInput.readInt();
        if (userChoice == 0) {
            return;
        } else if (userChoice > 0 && userChoice <= cars.size()) {
            Car selectedCar = cars.get(userChoice - 1);
            rentCar(customer, selectedCar);
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void rentCar(Customer customer, Car car) {
        customer.setCarId(car.getId());
        customerDao.updateCustomerRentedCar(customer);
        System.out.printf("\nYou rented '%s'%n", car.getName());
    }

}
