package matradev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    FXMLLoader mainWindowLoader;
    FXMLLoader addDialogLoader;

    public static void main(String[] args) {
        launch(args);
    }

    TextField txtTitleOriginal;
    TextField txtLength;
    TextField txtImdbRating;
    Label lblInfo;

    DatabaseHandling dbHandling;
    private static int licznik = 1;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("BasicApplication_css.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create the buttons
        Button btnAdd = new Button("Dodaj");
        btnAdd.setPrefWidth(120);
        btnAdd.setOnAction(event -> btnAdd_Click());

        Button btnCancel = new Button("Anuluj");
        btnCancel.setPrefWidth(120);
        btnCancel.setOnAction(event -> btnCancel_Click());

/*        // Create the scene and the stage
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Filmy i seriale");
        primaryStage.setMinWidth(500);
        primaryStage.setMaxWidth(900);
        primaryStage.show();*/


/*        Label label = new Label("Not clicked");

        Button button = new Button("_Click");
        button.setPrefSize(200, 100);
        button.setMnemonicParsing(true);
        button.setStyle("-fx-font-size: 2em; ");
        button.setOnAction(value -> {label.setText("Clicked!");});

        MenuItem menuItem1 = new MenuItem("Raz");
        MenuItem menuItem2 = new MenuItem("Dwa");
        MenuItem menuItem3 = new MenuItem("Trzy");

        menuItem1.setOnAction(event -> {
            System.out.println("Opcja nr jeden");
            label.setText("JEDYNKA");
        });
        */

    }

    public void btnAdd_Click()
    {
        boolean connectionStatus;
        connectionStatus = dbHandling.connectWithDatabase();

        if(connectionStatus)
            lblInfo.setText("Połączono z bazą");
        else
            lblInfo.setText("Problem z połączeniem");
    }

    public void btnCancel_Click()
    {
        boolean insertionStatus;

        String[] insertParameters = new String[4];
        insertParameters[0] = String.valueOf(licznik); // Primary key
        insertParameters[1] = txtTitleOriginal.getText(); // Title of imdbMovie
        insertParameters[2] = txtImdbRating.getText(); // IMDb rating
        insertParameters[3] = txtLength.getText(); // Length of imdbMovie

/*        //insertionStatus = dbHandling.insertElementIntoDatabase(insertParameters);
        licznik++;

        if(insertionStatus)
            lblInfo.setText("Dodano rekord do bazy danych");
        else
            lblInfo.setText("Problem z dodaniem");*/
    }
}
