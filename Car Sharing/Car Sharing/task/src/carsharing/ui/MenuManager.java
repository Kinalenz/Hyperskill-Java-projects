package carsharing.ui;

import carsharing.service.CustomerService;
import carsharing.service.CompanyService;

public class MenuManager {

    private final UserInput userInput;
    private final CompanyService companyService;
    private final CustomerService customerService;

    public MenuManager(UserInput userInput, CompanyService companyService, CustomerService customerService) {
        this.userInput = userInput;
        this.companyService = companyService;
        this.customerService = customerService;
    }

    public void start() {
        showMainMenu();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit""");
            int userChoice = userInput.readInt();
            switch (userChoice) {
                case 1 -> showManagerMenu();
                case 2 -> customerService.showCustomerList();
                case 3 -> customerService.createCustomer();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void showManagerMenu() {
        while (true) {
            System.out.println();
            System.out.println("""
                    1. Company list
                    2. Create a company
                    0. Back""");
            int userChoice = userInput.readInt();
            switch (userChoice) {
                case 1 -> companyService.showCompanyList();
                case 2 -> companyService.createCompany();
                case 0 -> {
                    System.out.println();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
