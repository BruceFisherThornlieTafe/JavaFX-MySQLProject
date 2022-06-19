package customermanagementgui;

/**
 * FXMLController Controller Code
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
import static customermanagementgui.DBUtils.showAlertMessageBox;
import java.awt.Toolkit;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

public class FXMLController {

    // DataBase to Use - used to test mySLQ connection upon initialize of application
    static final String DATABASE = "smtbiz";

    /**
     * Validation Setup
     */
    // Max character limit for Text Field inputs
    static final int CUSTOMERID_TEXT_FIELD_MAX_INPUT = 6;
    static final int CUSTOMERFULLNAME_TEXT_FIELD_MAX_INPUT = 30;
    static final int EMAIL_TEXT_FIELD_MAX_INPUT = 40;
    static final int MOBILE_TEXT_FIELD_MAX_INPUT = 12;

    // Instances of each Text Field Validation call
    ValidateTextField validateTextFieldSearchCustomerId = new ValidateTextField();
    ValidateTextField validateTextFieldCustomerId = new ValidateTextField();
    ValidateTextField validateTextFieldFullName = new ValidateTextField();
    ValidateTextField validateTextFieldEmail = new ValidateTextField();
    ValidateTextField validateTextFieldMobile = new ValidateTextField();

    // Returned object containing boolean error and errorMessage from Validation
    ErrorMessage errorMessage = new ErrorMessage();

    // Validation Regular Expression Check for Final Input in Customer Form for email
    static final String REGEX_EMAIL = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // Boolean to LOCK Customer ID input and record contents to reset any input
    static boolean lockCustomerID = false;
    static int contentsLockedCustomerID;

    /**
     * Initialize
     */
    @FXML
    public void initialize() {
        // set ALL Error Images to invisible at start
        clearAllErrorImages();
        labelErrorMessage.setText("");

        // Setup TableView and Columns
        tableViewCustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableViewCustomerFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewCustomerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableViewCustomerMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        // Prevent User from changing properties on TableView
        tableViewCustomers.setEditable(false);
        tableViewCustomerID.setResizable(false);
        tableViewCustomerFullName.setResizable(false);
        tableViewCustomerEmail.setResizable(false);
        tableViewCustomerMobile.setResizable(false);

        // If database exists
        if (DBUtils.connectDatabaseMySQL(DATABASE)) {
            displayCustomerTableView();
        } else {
            showAlertMessageBox(Alert.AlertType.ERROR, "mySQL Server", "No mySQL Connection to DataBase \"" + DATABASE + "\"", "If First Run of Application Try \"Create DataBase Button\" at Top\n\nOR See Administrator to Start mySQL Server");
        }
    }

    @FXML
    private AnchorPane anchorPaneCustomers;

    /**
     * Buttons Id's
     */
    @FXML
    private Button buttonCreateDataBase;

    @FXML
    private Button buttonClearCustomerInput;

    @FXML
    private Button buttonAddCustomer;

    @FXML
    private Button buttonDeleteCustomer;

    @FXML
    private Button buttonUpdateCustomer;

    @FXML
    private Button buttonSearchCustomerId;

    /**
     * Error Icon Images
     */
    @FXML
    private ImageView searchCustomerIdErrorImage;

    @FXML
    private ImageView customerIdErrorImage;

    @FXML
    private ImageView customerFullNameErrorImage;

    @FXML
    private ImageView customerEmailErrorImage;

    @FXML
    private ImageView customerMobileErrorImage;

    @FXML
    private Label labelErrorMessage;

    /**
     * Customer TextFields
     */
    @FXML
    private TextField textFieldSearchCustomerId;

    @FXML
    private TextField textFieldCustomerId;

    @FXML
    private TextField textFieldCustomerFullName;

    @FXML
    private TextField textFieldCustomerEmail;

    @FXML
    private TextField textFieldCustomerMobile;

    /**
     * Table View Setup Columns
     */
    @FXML
    private TableView<Customer> tableViewCustomers;

    @FXML
    private TableColumn<Customer, String> tableViewCustomerID;

    @FXML
    private TableColumn<Customer, String> tableViewCustomerFullName;

    @FXML
    private TableColumn<Customer, String> tableViewCustomerEmail;

    @FXML
    private TableColumn<Customer, String> tableViewCustomerMobile;

    /**
     * TableView Mouse Clicked Action - Set Customer TextFields to Selected
     * Attributes
     *
     * @param event - event handler
     */
    @FXML
    void tableViewCustomersMouseClicked(MouseEvent event) {
        clearAllErrorImages();
        labelErrorMessage.setText("");
        try {
            ObservableList<Customer> selectedRowCustomer = tableViewCustomers.getSelectionModel().getSelectedItems();
            setCustomerTextFields(selectedRowCustomer.get(0));
            labelErrorMessage.setText("");
            contentsLockedCustomerID = selectedRowCustomer.get(0).getId();
            lockCustomerId(); // prevent user from changing Customer ID
            textFieldSearchCustomerId.clear();
        } catch (Exception ex) {
            // do nothing - stop exeception error when use selected Column Headings
        }
    }

    /**
     * Buttons Actions
     *
     * @param event - event handler
     */
    @FXML
    void createDatabase(ActionEvent event) {
        if (confirmCreateNewDatabase()) {
            CustomerDOA.createDatabase();

            // Insert Customers and their Details into Database
            CustomerDOA.insertCustomer(197681, "Bruce Fisher", "brucefisher@tafe.wa.edu.au", "0400 000 000");
            CustomerDOA.insertCustomer(83274, "Nolan Bushnell", "nolanbushnell@atari.com", "0400 110 340");
            CustomerDOA.insertCustomer(83642, "Steve Wozniak", "stevewozniak@apple.com", "0400 111 111");
            CustomerDOA.insertCustomer(99363, "Bill Gates", "billgates@tafe.wa.edu.au", "0499 995 555");
            CustomerDOA.insertCustomer(1046, "Steve Jobs", "stevejobs@apple.com", "0444 555 555");

            displayCustomerTableView();
            displayUserMsg(""); // clear error message
        } else {
            displayUserMsg("User Cancelled Creation of New DataBase \"" + DATABASE + "\"");
        }
    }

    @FXML
    void clearCustomerTextFields(ActionEvent event) {
        unLockCustomerId(); // allow user to change Customer ID
        clearAllTextFields();
        clearAllErrorImages();
        labelErrorMessage.setText("");
    }

    @FXML
    void addCustomer(ActionEvent event) {
        // Create duplicateCustomer to check
        Customer duplicateCustomer = null;
        try {
            labelErrorMessage.setText("");
            clearAllErrorImages();
            if (!lockCustomerID) {
                if (validCustomerInput() && validEmailInput() && ((duplicateCustomer = CustomerDOA.searchCustomerByID(Integer.parseInt(textFieldCustomerId.getText()))) == null)) {
                    int customerId = Integer.parseInt(textFieldCustomerId.getText());
                    displayUserMsg(CustomerDOA.insertCustomer(customerId, textFieldCustomerFullName.getText(), textFieldCustomerEmail.getText(), textFieldCustomerMobile.getText()));
                    clearAllTextFields();
                    displayCustomerTableView();
                } else {
                    if (duplicateCustomer != null) {
                        Toolkit.getDefaultToolkit().beep();
                        displayUserMsg("ERROR: Customer with ID \"" + duplicateCustomer.getId() + "\" Already Exists!");
                    } else if (!validEmailInput() && validCustomerInput()) {
                        Toolkit.getDefaultToolkit().beep();
                        displayUserMsg("ERROR: Invalid Email Address Entered!");
                        customerEmailErrorImage.setVisible(true);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        displayUserMsg("ERROR: Enter all Customer Details!");
                    }
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("Can not Add and Existing Customer - Press \"Clear Inputs\" - to Add New Customer Details!");
            }
        } catch (NumberFormatException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "addCustomer", "NumberFormatException", " Exception Caught " + ex.getMessage());
        }
    }

    @FXML
    void deleteCustomer(ActionEvent event) {
        try {
            labelErrorMessage.setText("");
            clearAllErrorImages();
            if (validCustomerInput() & validEmailInput()) {
                int customerId = Integer.parseInt(textFieldCustomerId.getText());
                if (confirmAndDelete(customerId)) {
                    displayUserMsg(CustomerDOA.deleteCustomerByID(customerId));
                    clearAllTextFields();
                    unLockCustomerId();
                    displayCustomerTableView();
                } else {
                    displayUserMsg("User Cancelled Customer ID \"" + customerId + "\" Deletion!");
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Search for Customer or Select Customer from Table View below to Delete!");
            }
        } catch (NumberFormatException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "deleteCustomer", "NumberFormatException", " Exception Caught " + ex.getMessage());
        }
    }

    @FXML
    void updateCustomer(ActionEvent event) {
        // Create duplicateCustomer to check
        Customer existsCustomer = null;
        try {
            labelErrorMessage.setText("");
            clearAllErrorImages();
            if (validCustomerInput() && validEmailInput() && !((existsCustomer = CustomerDOA.searchCustomerByID(Integer.parseInt(textFieldCustomerId.getText()))) == null)) {
                int customerId = Integer.parseInt(textFieldCustomerId.getText());
                displayUserMsg(CustomerDOA.updateCustomer(customerId, textFieldCustomerFullName.getText(), textFieldCustomerEmail.getText(), textFieldCustomerMobile.getText()));
                clearAllTextFields();
                unLockCustomerId();
                displayCustomerTableView();
                return;
            }
            if (textFieldCustomerId.getText().isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Search for Customer or Select Customer from Table View below to Edit!");
                customerEmailErrorImage.setVisible(true);
            } else if (!validCustomerInput()) {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Enter all Customer Details!");
            } else if (!validEmailInput()) {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Invalid Email Address Entered!");
                customerEmailErrorImage.setVisible(true);
            } else if (existsCustomer == null) {
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Customer with ID \"" + textFieldCustomerId.getText() + "\" Does Not Exist!");
            }

        } catch (NumberFormatException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "updateCustomer", "NumberFormatException", " Exception Caught " + ex.getMessage());
        }
    }

    @FXML
    void searchCustomerId(ActionEvent event) {
        // Create foundCustomer to display
        Customer foundCustomer;

        try {
            labelErrorMessage.setText("");
            clearAllErrorImages();
            if (!textFieldSearchCustomerId.getText().isEmpty()) {
                int customerId = Integer.parseInt(textFieldSearchCustomerId.getText());
                if ((foundCustomer = CustomerDOA.searchCustomerByID(customerId)) != null) {
                    displayUserMsg("Found Customer ID \"" + customerId + "\"");
                    // Set TextFields to Found Data
                    setCustomerTextFields(foundCustomer);
                    lockCustomerId(); // prevent user chsnging Customer ID
                    contentsLockedCustomerID = foundCustomer.getId();
                    textFieldSearchCustomerId.clear();
                } else {
                    displayUserMsg("Customer ID \"" + customerId + "\" NOT Found!");
                    textFieldSearchCustomerId.clear();
                }
            } else {
                searchCustomerIdErrorImage.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
                displayUserMsg("ERROR: Enter Customer ID to Search!");
            }
        } catch (NumberFormatException ex) {
            showAlertMessageBox(Alert.AlertType.ERROR, "searchCustomerId", "NumberFormatException", " Exception Caught " + ex.getMessage());
        }
    }

    @FXML
    void textFieldSearchCustomerIdMouseClicked(MouseEvent event) {
        displayUserMsg(""); // clear error message once user mouse clicks textfield

        // Clear all other text fields
        textFieldCustomerId.clear();
        textFieldCustomerFullName.clear();
        textFieldCustomerEmail.clear();
        textFieldCustomerMobile.clear();
        unLockCustomerId();

        // Store cursor postion where user has clicked into Text Field
        validateTextFieldSearchCustomerId.setOldCursorCaretPostion(textFieldSearchCustomerId.getCaretPosition());
    }

    @FXML
    void textFieldCustomerIdMouseClicked(MouseEvent event) {
        displayUserMsg(""); // clear error message once user mouse clicks textfield

        if (!lockCustomerID) {
            // Store cursor postion where user has clicked into Text Field
            validateTextFieldCustomerId.setOldCursorCaretPostion(textFieldCustomerId.getCaretPosition());
        } else {
            Toolkit.getDefaultToolkit().beep();
            displayUserMsg("Can not Edit existing Customer ID - Press \"Clear Inputs\" - to Add New Customer Details");
        }
    }

    @FXML
    void textFieldCustomerFullNameMouseClicked(MouseEvent event) {
        displayUserMsg(""); // clear error message once user mouse clicks textfield

        // Store cursor postion where user has clicked into Text Field
        validateTextFieldFullName.setOldCursorCaretPostion(textFieldCustomerFullName.getCaretPosition());
    }

    @FXML
    void textFieldCustomerEmailMouseClicked(MouseEvent event) {
        displayUserMsg(""); // clear error message once user mouse clicks textfield

        // Store cursor postion where user has clicked into Text Field
        validateTextFieldEmail.setOldCursorCaretPostion(textFieldCustomerEmail.getCaretPosition());
    }

    @FXML
    void textFieldCustomerMobileMouseClicked(MouseEvent event) {
        displayUserMsg(""); // clear error message once user mouse clicks textfield

        // Store cursor postion where user has clicked into Text Field
        validateTextFieldMobile.setOldCursorCaretPostion(textFieldCustomerMobile.getCaretPosition());
    }

    @FXML
    void validateSearchCustomerIdTextFieldKeyTyped(KeyEvent event) {
        displayUserMsg(""); // clear error message

        // Validate input
        errorMessage = validateTextFieldSearchCustomerId.validateDigit(textFieldSearchCustomerId, event, CUSTOMERID_TEXT_FIELD_MAX_INPUT);
        if (ErrorMessage.isError()) {
            displayUserMsg(ErrorMessage.getErrMsg());
            Toolkit.getDefaultToolkit().beep();
        }

        searchCustomerIdErrorImage.setVisible(ErrorMessage.isError());
    }

    @FXML
    void validateCustomerIdTextFieldKeyTyped(KeyEvent event) {
        displayUserMsg(""); // clear error message

        if (!lockCustomerID) {
            // Validate input
            errorMessage = validateTextFieldCustomerId.validateDigit(textFieldCustomerId, event, CUSTOMERID_TEXT_FIELD_MAX_INPUT);
            if (ErrorMessage.isError()) {
                displayUserMsg(ErrorMessage.getErrMsg());
                Toolkit.getDefaultToolkit().beep();
            }

            customerIdErrorImage.setVisible(ErrorMessage.isError());
        } else {
            try {
                textFieldCustomerId.setText(Integer.toString(contentsLockedCustomerID));
            } catch (Exception ex) {
                showAlertMessageBox(Alert.AlertType.ERROR, "validateCustomerIdTextFieldKeyTyped", "Integer.toString Exception", " Exception Caught " + ex.getMessage());
            }
            Toolkit.getDefaultToolkit().beep();
            displayUserMsg("Can not Edit existing Customer ID - Press \"Clear Inputs\" - to Add New Customer Details");
        }

    }

    @FXML
    void validateCustomerFullNameTextFieldKeyTyped(KeyEvent event) {
        displayUserMsg(""); // clear error message

        // Validate input
        errorMessage = validateTextFieldFullName.validateLetter(textFieldCustomerFullName, event, CUSTOMERFULLNAME_TEXT_FIELD_MAX_INPUT);
        if (ErrorMessage.isError()) {
            displayUserMsg(ErrorMessage.getErrMsg());
            Toolkit.getDefaultToolkit().beep();
        }

        customerFullNameErrorImage.setVisible(ErrorMessage.isError());
    }

    @FXML
    void validateCustomerEmailTextFieldKeyTyped(KeyEvent event) {
        displayUserMsg(""); // clear error message

        // Validate input
        errorMessage = validateTextFieldEmail.validateEmail(textFieldCustomerEmail, event, EMAIL_TEXT_FIELD_MAX_INPUT);
        if (ErrorMessage.isError()) {
            displayUserMsg(ErrorMessage.getErrMsg());
            Toolkit.getDefaultToolkit().beep();
        }

        customerEmailErrorImage.setVisible(ErrorMessage.isError());
    }

    @FXML
    void validateCustomerMobileTextFieldKeyTyped(KeyEvent event) {
        displayUserMsg(""); // clear error message

        // Validate input
        errorMessage = validateTextFieldMobile.validateMobile(textFieldCustomerMobile, event, MOBILE_TEXT_FIELD_MAX_INPUT);
        if (ErrorMessage.isError()) {
            displayUserMsg(ErrorMessage.getErrMsg());
            Toolkit.getDefaultToolkit().beep();
        }

        customerMobileErrorImage.setVisible(ErrorMessage.isError());
    }

    /**
     * Check Customer Input Form is Valid
     *
     * @return Valid true InValid False
     */
    private boolean validCustomerInput() {
        if (textFieldCustomerId.getText().isEmpty()) {
            customerIdErrorImage.setVisible(true);
        }
        if (textFieldCustomerFullName.getText().isEmpty()) {
            customerFullNameErrorImage.setVisible(true);
        }
        if (textFieldCustomerMobile.getText().isEmpty()) {
            customerMobileErrorImage.setVisible(true);
        }

        return !textFieldCustomerId.getText().isEmpty() && !textFieldCustomerFullName.getText().isEmpty() && !textFieldCustomerMobile.getText().isEmpty();
    }

    /**
     * Check Customer Input Email Valid
     *
     * @return Valid true InValid False
     */
    private boolean validEmailInput() {
        if (textFieldCustomerEmail.getText().isEmpty() || !textFieldCustomerEmail.getText().matches(REGEX_EMAIL)) {
            customerEmailErrorImage.setVisible(true);
        }
        return !textFieldCustomerEmail.getText().isEmpty() || textFieldCustomerEmail.getText().matches(REGEX_EMAIL);
    }

    /**
     * Set Customer ID input to lock state and set effect to GaussianBlur
     * TextField input
     */
    private void lockCustomerId() {
        lockCustomerID = true;
        textFieldCustomerId.setEffect(new DropShadow());
    }

    /**
     * ReSet Customer ID input to unlock state and set effect to InnerShadow
     * TextField input
     */
    private void unLockCustomerId() {
        lockCustomerID = false;
        textFieldCustomerId.setEffect(new InnerShadow());
    }

    /**
     * Confirmation of Creating New Customer DataBase Message Box
     *
     * @return true - yes, false - no
     */
    private boolean confirmCreateNewDatabase() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Create New DataBase \"" + DATABASE + "\"");
        alert.setHeaderText("Any Current Records will be Erased!");
        alert.setContentText("Are you sure you want to Create a New DataBase?");

        Optional<ButtonType> result = alert.showAndWait();
        return !(!result.isPresent() || result.get() != ButtonType.OK);
    }

    /**
     * Confirmation of Deleting Customer Message Box
     *
     * @param customerId - Customer to be deleted
     * @return true - yes, false - no
     */
    private boolean confirmAndDelete(int customerId) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("To Delete Please Confirm");
        alert.setHeaderText("Delete Customer ID \"" + customerId + "\"?");
        alert.setContentText("Are you sure you want to delete this Customer?");

        Optional<ButtonType> result = alert.showAndWait();
        return !(!result.isPresent() || result.get() != ButtonType.OK);
    }

    /**
     * Display Customers in TableView
     */
    private void displayCustomerTableView() {

        if (ObservableListOfCustomers() != null) {
            tableViewCustomers.setItems(ObservableListOfCustomers());
        }
    }

    /**
     * Create FXCollections ObservableList from customers List to display to
     * TableView
     *
     * @return ObservableList customers
     */
    private ObservableList<Customer> ObservableListOfCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(CustomerDOA.getAllCustomerRecords());
        return customers;
    }

    /**
     * Set Customer TextFields to passed customer Object
     *
     * @param customer
     */
    private void setCustomerTextFields(Customer customer) {
        textFieldCustomerId.setText(Integer.toString(customer.getId()));
        textFieldCustomerFullName.setText(customer.getName());
        textFieldCustomerEmail.setText(customer.getEmail());
        textFieldCustomerMobile.setText(customer.getMobile());
    }

    /**
     * Display in Label labelErrorMsg passed Error Message
     *
     * @param msg - Error Message to be displayed
     */
    private void displayUserMsg(String msg) {
        labelErrorMessage.setText(msg);

        if (msg.isEmpty()) {
            clearAllErrorImages();
        }
    }

    /**
     * Clear All the Text Fields
     */
    private void clearAllTextFields() {
        textFieldSearchCustomerId.clear();
        textFieldCustomerId.clear();
        textFieldCustomerFullName.clear();
        textFieldCustomerEmail.clear();
        textFieldCustomerMobile.clear();
    }

    /**
     * Make Invisible All Error Images
     */
    private void clearAllErrorImages() {
        searchCustomerIdErrorImage.setVisible(false);
        customerIdErrorImage.setVisible(false);
        customerFullNameErrorImage.setVisible(false);
        customerEmailErrorImage.setVisible(false);
        customerMobileErrorImage.setVisible(false);
    }

}
