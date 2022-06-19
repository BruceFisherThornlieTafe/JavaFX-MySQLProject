package customermanagementgui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * Collection of MySQL Database Utilities that can be reused.
 *
 * © Copyright - Deimos Coding Projects -> Programmer Bruce Fisher
 *
 * @author Email: deimoscodingprojects@gmail.com
 *
 * Website: <a href="https://deimoscodingprojects.com/">Website</a>
 *
 * @author © Deimos Coding Projects -> Bruce Fisher
 *
 * Date 8-5-2022
 */
public class DBUtils {

    // Standard Xampp MySQL DBMS to use for testing
    private static final String URL_DB = "jdbc:mysql://localhost:3306/"; // 3306 is default port for MySQL
    private static final String USER = "root"; // superuser account 'root'@'localhost'
    private static final String PASSWORD = ""; // password set for MySQL - default nothing for testing
    private static Connection databaseConnection = null; // connection for DBMS MySQL

    private static Statement statement = null; // SQL statement object
    private static String query; // SQL query string
    private static ResultSet resultSet = null; // results after SQL execution

    /**
     * Connect to Port MySQL Databases
     *
     * @param databaseName - Database Name to Connect to
     * @return MySQL Connected true of false
     */
    public static boolean connectDatabaseMySQL(String databaseName) {
        boolean mySQLConnected = false;
        try {
            // DriverManager - Attempts to establish a connection to the given database URL. 
            // The DriverManager attempts to select an appropriate driver from the set of 
            // registered JDBC drivers.
            databaseConnection = DriverManager.getConnection(URL_DB + databaseName, USER, PASSWORD);

            // setAutoCommit - set to false means that SQL statements are grouped into
            // transactions that are terminated by a call to either the method commit 
            // or the method rollback. 
            // Please note set to false as by default new connections are to auto-commit mode.
            databaseConnection.setAutoCommit(false);
            mySQLConnected = true;
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "connectDatabaseMySQL", "SQLException", "SQL Exception on MySQL Database connection: " + ex.getMessage());
        }

        return mySQLConnected;
    }

    /**
     * Close Connection to Port MySQL Databases
     */
    private static void closeDatabaseMySQL() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "closeDatabaseMySQL", "SQLException", "SQL Exception on MySQL Database close: " + ex.getMessage());
        }
    }

    /**
     * Create a New Database and Drop if already exists
     *
     * @param databaseName - New Database Name to create
     */
    public static void createNewDatabase(String databaseName) {
        try {
            databaseConnection = DriverManager.getConnection(URL_DB, USER, PASSWORD);
            statement = databaseConnection.createStatement();

            query = "DROP DATABASE IF EXISTS " + databaseName + ";";
            statement.executeUpdate(query);

            query = "CREATE DATABASE " + databaseName + ";";
            statement.executeUpdate(query);

            useDatabase(databaseName); // Use newly created Database
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "createNewDatabase", "SQLException", "SQL Exception CREATE DATABASE caught: " + ex.getMessage());
        }
    }

    /**
     * Use an existing Database
     *
     * @param databaseName - Database Name to Use
     */
    public static void useDatabase(String databaseName) {
        try {
            query = "USE " + databaseName + ";";
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "useDatabase", "SQLException", "SQL Exception USE DATABASE caught: " + ex.getMessage());
        }
    }

    /**
     * Create a New Table in supplied Database Name
     *
     * @param databaseName - Name of Database to use
     * @param tableName - Name of Table to create
     * @param columns - List of Columns (fields or attributes) to create
     */
    public static void createTable(String databaseName, String tableName, String columns) {
        try {
            query = "DROP TABLE IF EXISTS " + tableName + ";"; // If exists delete Table
            executeMySQLUpdate(databaseName, query);

            query = "CREATE TABLE " + tableName + columns + ";"; // Create Table with Columns
            executeMySQLUpdate(databaseName, query);
        } catch (Exception ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "createTable", "SQLException", "Exception DROP and CREATE TABLE caught: " + ex.getMessage());
        }
    }

    /**
     * Execute a MySQL Database Query
     *
     * @param databaseName - Name of Database to use
     * @param queryStatement - Database query to process
     * @return - Result from MySQL query
     */
    public static ResultSet executeMySQLQuery(String databaseName, String queryStatement) {
        CachedRowSet crs = null;

        try {
            connectDatabaseMySQL(databaseName); // connect to MySQL Databases

            statement = databaseConnection.createStatement();
            resultSet = statement.executeQuery(queryStatement);

            // RowSetProvider - A factory API that enables applications to obtain a RowSetFactory 
            // implementation that can be used to create different types of RowSet implementations.
            // Basically it's used to keep the data returned from SQL query tabular making it easier
            // to use the information using ResultSet.
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLQuery", "SQLException", "SQL Exception on executeMySQLQueryQuery: " + ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                closeDatabaseMySQL(); // close Connection to MySQL Databases
            } catch (SQLException ex) {
                showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLQuery", "SQLException", "SQL Exception caught on MySQL Database closing: " + ex.getMessage());
            }
        }
        return crs;
    }

    /**
     * Executes an Update to Table
     *
     * @param databaseName - Name of Database to use
     * @param sqlStatement - MySQL statement to process
     * @return - Number of records Updated
     */
    public static int executeMySQLUpdate(String databaseName, String sqlStatement) {
        int recordCount;

        try {
            connectDatabaseMySQL(databaseName); // connect to MySQL Database

            statement = databaseConnection.createStatement();
            recordCount = statement.executeUpdate(sqlStatement);
            databaseConnection.commit();

            return recordCount;
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLUpdate", "SQLException", "SQL Exception on executeQuery: " + ex.getMessage());
            // Attempt to Roll Back any failed Commits
            if (databaseConnection != null) {
                try {
                    showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLUpdate", "SQLException", "Transction is being Rolled Back!");
                    databaseConnection.rollback(); // Rolled Back changes to keep Database Integrity
                } catch (SQLException ex2) {
                    showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLUpdate", "SQLException", "SQL Exception on executeMySQLUpdate RollBack: " + ex2.getMessage());
                }
            }
            return 0;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                closeDatabaseMySQL(); // close Connection to MySQL Databases
            } catch (SQLException ex) {
                showAlertMessageBox(Alert.AlertType.ERROR, "executeMySQLUpdate", "SQLException", "SQL Exception caught on database closing: " + ex.getMessage());
            }
        }
    }

    /**
     * Show Message Box
     *
     * @param alertType - Type of Alert (CONFIRMATION, ERROR, INFORMATION, NONE,
     * WARNING)
     * @param title - Title of Box
     * @param message - Message in Box
     * @param header
     */
    public static void showAlertMessageBox(Alert.AlertType alertType, String title, String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
