package matradev;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Created by Mateusz on 19.10.2016.
 */
public class SettingsDialogController implements Initializable {

    @FXML private Button btnConnectionTest;
    @FXML private Button btnOpenFileChooser;
    @FXML private Button btnSaveServer;
    @FXML private Label lblToolbar;
    @FXML private PasswordField pfServerPassword;
    @FXML private TextField txtChosenFile;
    @FXML private TextField txtServerAddress;
    @FXML private TextField txtServerDbName;
    @FXML private TextField txtServerLogin;
    @FXML private TextField txtServerPort;
    final FileChooser fileChooser = new FileChooser();

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

        btnOpenFileChooser.setOnAction(event -> {
            Stage stage = new Stage();
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);

            if(file != null)
            {
                Controller.setLocalDatabasePath(file.getAbsolutePath());
                txtChosenFile.setText(file.getAbsolutePath());
            }
        });
    }

    private static void configureFileChooser(final FileChooser fileChooser)
    {
        fileChooser.setTitle("Load local movie database");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // Sets initial directory in FileChooser
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DBW (.dbw)", "*.dbw")); // Sets available extension of file
    }
}
