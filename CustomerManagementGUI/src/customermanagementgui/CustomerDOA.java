package customermanagementgui;

import static customermanagementgui.DBUtils.showAlertMessageBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

/**
 * Customer Data Access Object
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
public class CustomerDOA {

    private static final String DATABASE = "smtbiz";
    private static final String TABLE = "customer";
    private static final String CREATE_COLUMNS = "(ID INTEGER (6) NOT NULL, Name VARCHAR(30), Email VARCHAR(40), Mobile VARCHAR(12), PRIMARY KEY(ID));";

    /**
     * Create the Database
     */
    public static void createDatabase() {
        DBUtils.createNewDatabase(DATABASE);
        DBUtils.createTable(DATABASE, TABLE, CREATE_COLUMNS);
        showAlertMessageBox(Alert.AlertType.INFORMATION, "SUCCESS", "New DabaBase \"" + DATABASE + "\" and Table \"" + TABLE + "\" Created ", "");
    }

    /**
     * Insert new Customer into Database
     *
     * @param id - Customer ID
     * @param name - Customer Name
     * @param email - Customer Email
     * @param mobile - Customer Mobile
     * @return User Message
     */
    public static String insertCustomer(int id, String name, String email, String mobile) {
        String insertSQL = String.format("INSERT INTO " + TABLE + " (ID, Name, Email, Mobile) VALUES ('%s', '%s', '%s', '%s');", id, name, email, mobile);
        int recordCount = DBUtils.executeMySQLUpdate(DATABASE, insertSQL);

        if (recordCount == 0) {
            showAlertMessageBox(Alert.AlertType.ERROR, "ERROR", "insertCustomer", "Failed to add a new Customer!");
        } else {
            return "Customer ID \"" + id + "\" Successfully Added";
        }
        return "";
    }

    /**
     * Update Customer Details for Database
     *
     * @param id - Customer ID
     * @param name - Customer Name
     * @param email - Customer Email
     * @param mobile - Customer Mobile
     * @return User Message
     */
    public static String updateCustomer(int id, String name, String email, String mobile) {
        String updateSQLCustomer = String.format("UPDATE " + TABLE + " SET Name = ('%s'), Email = ('%s'), Mobile = ('%s') WHERE ID = ('%s');", name, email, mobile, id);
        int recordCount = DBUtils.executeMySQLUpdate(DATABASE, updateSQLCustomer);

        if (recordCount == 0) {
            showAlertMessageBox(Alert.AlertType.ERROR, "ERROR", "updateCustomer", "Failed to update Customer ID " + id);
        } else {
            return "Customer ID \"" + id + "\" Successfully Updated";
        }
        return "";
    }

    /**
     * Delete Customer from Table customer
     *
     * @param id - Customer ID
     * @return User Message
     */
    public static String deleteCustomerByID(int id) {
        String deleteSQL = "DELETE FROM " + TABLE + " WHERE ID='" + id + "';";
        int count = DBUtils.executeMySQLUpdate(DATABASE, deleteSQL);

        if (count == 0) {
            return "Customer ID \"" + id + "\" NOT Found!";
        } else {
            return "Customer ID \"" + id + "\" Successfully Deleted";
        }
    }

    /**
     * Search for Customer ID in Table customer
     *
     * @param id - Customer ID
     * @return - Customer
     */
    public static Customer searchCustomerByID(int id) {
        String query = "SELECT * FROM " + TABLE + " WHERE ID=" + id + ";";
        Customer customer = null;

        try {
            ResultSet rs = DBUtils.executeMySQLQuery(DATABASE, query);

            if (rs.next()) {
                customer = new Customer();
                customer.setId(rs.getInt("ID"));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobile(rs.getString("Mobile"));
            }

        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "SQLException", "searchCustomerByID", "ERROR: SQL Exception on executeQuery: " + ex.getMessage());
        }

        return customer;
    }

    /**
     * Get All Customers Details from Database
     *
     * @return customers - Contents of Database to Display
     */
    public static List<Customer> getAllCustomerRecords() {
        String query = "SELECT * FROM " + TABLE + ";";
        List<Customer> customers = new ArrayList<>(); // Stores Contents of Database to Display

        try {
            ResultSet rs = DBUtils.executeMySQLQuery(DATABASE, query);

            while (rs.next()) {
                Customer customer = new Customer(); // Create a new instance of StudentResults
                customer.setId(rs.getInt("ID"));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobile(rs.getString("Mobile"));
                customers.add(customer);
            }
        } catch (SQLException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "getAllCustomerRecords", "SQLException", "ERROR: SQL Exception on executeQuery: " + ex.getMessage());
        }

        return customers;
    }
}
