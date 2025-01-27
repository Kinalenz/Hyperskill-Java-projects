package carsharing.dao.impl;

import carsharing.dao.CustomerDao;
import carsharing.dao.DatabaseConnection;
import carsharing.models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;


public class CustomerDaoImpl implements CustomerDao {

    private static final System.Logger LOGGER = System.getLogger(CustomerDaoImpl.class.getName());

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS customer (
                    id              INT AUTO_INCREMENT PRIMARY KEY,
                    name            VARCHAR(255) UNIQUE NOT NULL,
                    rented_car_id   INT,
                    CONSTRAINT      fk_customer_car_id FOREIGN KEY (rented_car_id) REFERENCES car(id)
                );""";
    private static final String SQL_INSERT_CUSTOMER = """
            INSERT INTO customer (name)
            VALUES      (?);""";
    private static final String SQL_SELECT_ALL = """
            SELECT      *
            FROM        customer
            ORDER BY    id ASC;""";
    private static final String SQL_UPDATE_CAR = """
            UPDATE  customer
            SET     rented_car_id = ?
            WHERE   id = ?;""";
    private static final String SQL_DELETE_ALL = """
            DELETE
            FROM    customer;""";
    private static final String SQL_DROP_TABLE = """
            DROP TABLE IF EXISTS customer;""";

    private final DatabaseConnection dbConnection;

    public CustomerDaoImpl(String databaseFileName) {
        this.dbConnection = DatabaseConnection.getInstance(databaseFileName);
    }

    @Override
    public void createTable() {
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error creating CUSTOMER table.", e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving customers.", e);
        }
        return customers;
    }

    @Override
    public void createCustomer(String name) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CUSTOMER)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error adding customer.", e);
        }
    }

    @Override
    public void updateCustomerRentedCar(Customer customer) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CAR)) {
            if (customer.getCarId() == null) {
                preparedStatement.setNull(1, Types.INTEGER);
            } else {
                preparedStatement.setInt(1, customer.getCarId());
            }
            preparedStatement.setInt(2, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error updating customer.", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ALL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting all customers.", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting customer table.", e);
        }
    }

}
