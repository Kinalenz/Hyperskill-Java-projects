package carsharing.dao;

import carsharing.models.Customer;

import java.util.List;

public interface CustomerDao {

    void createTable();
    List<Customer> getAllCustomers();
    void createCustomer(String name);
    void updateCustomerRentedCar(Customer customer);
    void deleteAll();
    void dropTable();

}
