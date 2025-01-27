package carsharing.service;

import carsharing.dao.CompanyDao;
import carsharing.models.Company;
import carsharing.ui.UserInput;

import java.util.List;

public class CompanyService {

    private final CompanyDao companyDao;
    private final CarService carService;
    private final UserInput userInput;

    public CompanyService(CompanyDao companyDao, CarService carService, UserInput userInput) {
        this.companyDao = companyDao;
        this.carService = carService;
        this.userInput = userInput;
    }

    public void showCompanyMenu(Company company) {
        while (true) {
            System.out.println();
            System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
            int userChoice = userInput.readInt();
            switch (userChoice) {
                case 1 -> carService.showCarList(company);
                case 2 -> carService.createCar(company);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void showCompanyList() {
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
            System.out.printf("\n'%s' company", companies.get(userChoice - 1).getName());
            showCompanyMenu(companies.get(userChoice - 1));
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    public void createCompany() {
        System.out.println("\nEnter the company name:");
        String companyName = this.userInput.readLine();
        companyDao.createCompany(companyName);
        System.out.println("The company was created!");
    }

}
