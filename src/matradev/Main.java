package matradev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TODO: Reorganize passing objects between classes
 * TODO: Later implement search results from IMDb API
 */
public class Main extends Application {

    public static Stage primaryStage;
    public static final String APP_NAME = "MoviesToSee";
    public static final String APP_VERSION = "0.4a";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("BasicApplication_css.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle(APP_NAME + " " + APP_VERSION);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
