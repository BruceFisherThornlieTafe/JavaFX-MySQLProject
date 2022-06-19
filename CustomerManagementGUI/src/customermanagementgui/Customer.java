package customermanagementgui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Customer Details
 *
 * © Copyright - Deimos Coding Projects -> Programmer Bruce Fisher
 *
 * Date 8-5-2022
 */
public class Customer {

    private SimpleIntegerProperty id; // customer id
    private SimpleStringProperty name; // customer name
    private SimpleStringProperty email; // customer email
    private SimpleStringProperty mobile; // allow for spaces in mobile number

    /**
     * @return the id
     */
    public int getId() {
        return id.get();
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile.get();
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = new SimpleStringProperty(mobile);
    }
}
