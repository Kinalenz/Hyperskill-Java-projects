package carsharing.dao.impl;

import carsharing.dao.CompanyDao;
import carsharing.dao.DatabaseConnection;
import carsharing.models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


public class CompanyDaoImpl implements CompanyDao {

    private static final System.Logger LOGGER = System.getLogger(CompanyDaoImpl.class.getName());

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS company (
                    id   INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) UNIQUE NOT NULL
                );""";
    private static final String SQL_INSERT_COMPANY = """
            INSERT INTO company (name)
            VALUES      (?);""";
    private static final String SQL_SELECT_ALL = """
            SELECT      *
            FROM        company
            ORDER BY    id ASC;""";
    private static final String SQL_SELECT_BY_ID = """
            SELECT  *
            FROM    company
            WHERE   id = ?;""";
    private static final String SQL_DELETE_ALL = """
            DELETE
            FROM    company;""";
    private static final String SQL_DROP_TABLE = """
            DROP TABLE IF EXISTS company;""";

    private final DatabaseConnection dbConnection;

    public CompanyDaoImpl(String databaseFileName) {
        this.dbConnection = DatabaseConnection.getInstance(databaseFileName);
    }

    @Override
    public void createTable() {
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error creating COMPANY table.", e);
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving companies.", e);
        }
        return companies;
    }

    @Override
    public Optional<Company> getCompany(int id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Company(resultSet.getInt(1),
                            resultSet.getString(2)));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error retrieving company by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public void createCompany(String name) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_COMPANY)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error adding company.", e);
        }
    }

    @Override
    public void deleteAllCompanies() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ALL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting all companies.", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Error deleting company table.", e);
        }
    }

}
