package carsharing.dao.impl;

import carsharing.dao.CarDao;
import carsharing.dao.DatabaseConnection;
import carsharing.models.Car;
import carsharing.models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


public class CarDaoImpl implements CarDao {

    private static final System.Logger LOGGER = System.getLogger(CarDaoImpl.class.getName());

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS car (
                    id          INT AUTO_INCREMENT PRIMARY KEY,
                    name        VARCHAR(255) UNIQUE NOT NULL,
                    company_id  INT NOT NULL,
                    CONSTRAINT  fk_company FOREIGN KEY (company_id) REFERENCES company(id)
                );""";
    private static final String SQL_INSERT_CAR = """
            INSERT INTO car (name, company_id)
            VALUES (?, ?);""";
    private static final String SQL_SELECT_BY_COMPANY = """
            SELECT      *
            FROM        car
            WHERE       company_id = ?
            ORDER BY    id ASC;""";
    private static final String SQL_SELECT_BY_ID = """
            SELECT  *
            FROM    car
            WHERE   id = ?;""";
    private static final String SQL_SELECT_FREE_CARS = """
            SELECT  *
                FROM    car
                WHERE   company_id = ?
                    AND id NOT IN (
                        SELECT  rented_car_id
                        FROM    customer
                        WHERE   rented_car_id != 0
                    );""";
    private static final String SQL_DELETE_ALL = """
            DELETE
            FROM car;""";
    private static final String SQL_DROP_TABLE = """
            DROP TABLE IF EXISTS car;""";

    private final DatabaseConnection dbConnection;

    public CarDaoImpl(String databaseFileName) {
        this.dbConnection = DatabaseConnection.getInstance(databaseFileName);
    }

    @Override
    public void createTable() {
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error creating CAR table.", e);
        }
    }

    @Override
    public List<Car> getCarsByCompany(Company company) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_COMPANY)) {
            preparedStatement.setInt(1, company.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(new Car(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving cars.", e);
        }
        return cars;
    }

    @Override
    public List<Car> getFreeCarsByCompany(Company company) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_FREE_CARS)) {
            preparedStatement.setInt(1, company.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(new Car(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving free cars.", e);
        }
        return cars;
    }

    @Override
    public Optional<Car> getCar(int id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Car(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving car by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public void createCar(String name, Company company) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CAR)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error adding car.", e);
        }
    }

    @Override
    public void deleteAllCars() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ALL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting all cars.", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting car table.", e);
        }
    }

}
