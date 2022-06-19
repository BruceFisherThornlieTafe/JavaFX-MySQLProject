package customermanagementgui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SMT Business Customer DataBase UI Application
 *
 * © Copyright - Deimos Coding Projects -> Programmer Bruce Fisher
 *
 * @author Email: deimoscodingprojects@gmail.com
 * 
 * Website: <a href="https://deimoscodingprojects.com/">Website</a>
 *
 * @author © Deimos Coding Projects -> Bruce Fisher
 */
public class CustomerManagementGUI extends Application {

    /**
     * Start - the main entry point to the JavaFX Application
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent mainView = FXMLLoader.load(getClass().getResource("FXMLController.fxml"));

        // Physical Content for JavaFX Application
        Scene scene = new Scene(mainView);
        stage.setTitle("SMT Business - Customer Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launch Application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
