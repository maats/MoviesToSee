package matradev;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Created by Mateusz on 19.10.2016.
 */
public class SettingsDialogController implements Initializable {

    @FXML private Button btnConnectionTest;
    @FXML private Button btnSaveServer;
    @FXML private Label lblToolbar;
    @FXML private PasswordField pfServerPassword;
    @FXML private TextField txtServerAddress;
    @FXML private TextField txtServerDbName;
    @FXML private TextField txtServerLogin;
    @FXML private TextField txtServerPort;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnConnectionTest.setOnAction(event -> {

            DatabaseHandling databaseHandling = new DatabaseHandling();

            String serverAddress = txtServerAddress.getText();
            String serverDbName = txtServerDbName.getText();
            String serverLogin = txtServerLogin.getText();
            String serverPassword = pfServerPassword.getText();
            String serverPort = txtServerPort.getText();

            databaseHandling.testConnectionWithDatabase(serverAddress, serverPort, serverDbName, serverLogin, serverPassword);

            Map<String, MovieToSee> moviesToSee = new TreeMap<String, MovieToSee>();
            moviesToSee = databaseHandling.getElementFromDatabase();

            for (String key :
                    moviesToSee.keySet()) {
                MovieToSee movieToSee2 = moviesToSee.get(key);
                System.out.println(movieToSee2.toString());
            }

            lblToolbar.setText("It works!");
        });
    }
}
