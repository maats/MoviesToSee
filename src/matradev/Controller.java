package matradev;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Controller of Main Window
 * Created by Mateusz on 03.10.2016.
 * TODO: Caching images
 * TODO: Improve UI
 */
public class Controller implements Initializable{

    @FXML private Button btnParse;
    @FXML private Button btnAdd;
    @FXML private Button btnModify;
    @FXML private Button btnDelete;
    @FXML private Label lblDescription;
    @FXML private Label lblGenre;
    @FXML private Label lblImdbRating;
    @FXML private Label lblLength;
    @FXML private Label lblMetascore;
    @FXML private Label lblMovieTitle;
    @FXML private Label lblPremiereDate;
    @FXML private Label lblVotesCount;
    @FXML private Label lblSource;
    @FXML private Label lblVersion;
    @FXML private Label lblContainer;
    @FXML private Label lblResolution;
    @FXML private Label lblAudioSubtitles;
    @FXML private ImageView imvPoster;
    @FXML private TableView<TableEntry> tbvMovieListFromDb;
    @FXML private TableColumn<TableEntry, String> tbcGenre;
    @FXML private TableColumn<TableEntry, Number> tbcImdbRating;
    @FXML private TableColumn<TableEntry, Number> tbcLength;
    @FXML private TableColumn<TableEntry, String> tbcMovieTitle;
    @FXML private TableColumn<TableEntry, String> tbcPremiereDate;
    private String movieIdForParser;
    private Image poster;
    static ObservableList<TableEntry> moviesToSeeAsTableEntries = FXCollections.observableArrayList();
    static Map<String, MovieToSee> moviesToSee = new TreeMap<String, MovieToSee>();

    public ObservableList<TableEntry> getMoviesToSeeAsTableEntries() {
        return moviesToSeeAsTableEntries;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            FileInputStream input = new FileInputStream("resources/images/noposter.png");
            poster = new Image(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imvPoster.setImage(poster);

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("matradev/AddDialog.fxml"), resources);
                    Stage stage = new Stage();
                    stage.setTitle("Wyszukaj film");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Test of showing table
        // TODO: Improve filling table to avoid making duplication of the whole list
        btnModify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DatabaseHandling.connectWithDatabase();
                DatabaseHandling.getElementFromDatabase();

                // Fills the table
                tbcMovieTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
                tbcImdbRating.setCellValueFactory(cellData -> cellData.getValue().imdbRatingProperty());
                tbcPremiereDate.setCellValueFactory(cellData -> cellData.getValue().premiereDateProperty());
                tbcLength.setCellValueFactory(cellData -> cellData.getValue().lengthProperty());
                tbcGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
                tbvMovieListFromDb.setItems(getMoviesToSeeAsTableEntries());
            }
        });

        // Selected item in table changed
        tbvMovieListFromDb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TableEntry>() {
            @Override
            public void changed(ObservableValue<? extends TableEntry> observable, TableEntry oldValue, TableEntry newValue) {
                if(tbvMovieListFromDb.getSelectionModel().getSelectedItem() != null)
                {
                    setMovieInformationsInMainWindow(newValue.getImdbID());
                    System.out.println(newValue.toString());
                }
            }
        });

    }

    public static void processMovieToSeeObjectsToTableEntries(MovieToSee movieToSee)
    {
        moviesToSeeAsTableEntries.add(new TableEntry(movieToSee.getImdbMovie().getTitle(), movieToSee.getImdbMovie().getImdbRating(),
                movieToSee.getImdbMovie().getPremiereDate(), movieToSee.getImdbMovie().getLength(), movieToSee.getImdbMovie().getGenre(),
                movieToSee.getImdbMovie().getImdbID()));
    }

/*    // Przekazywać IMDb ID, tymczasowe rozwiązanie z przekazywaniem całego obiektu dla testu
    public void setMovieInformationsInMainWindow(TableEntry tableEntry)
    {
        lblMovieTitle.setText(tableEntry.getTitle());
        lblImdbRating.setText(String.valueOf(tableEntry.getImdbRating()));
        lblPremiereDate.setText(tableEntry.getPremiereDate());
        lblLength.setText(String.valueOf(tableEntry.getLength() + " min"));
        lblGenre.setText(tableEntry.getGenre());
    }*/

    public void setMovieInformationsInMainWindow(String imdbID)
    {
        MovieToSee movieToSee = moviesToSee.get(imdbID);
        movieToSee.toString();

        imvPoster.setImage(new Image(movieToSee.getImdbMovie().getPosterURL()));
        lblMovieTitle.setText(movieToSee.getImdbMovie().getTitle());
        lblImdbRating.setText(String.valueOf(movieToSee.getImdbMovie().getImdbRating()));
        lblVotesCount.setText(String.valueOf(movieToSee.getImdbMovie().getVotesCount()));
        lblMetascore.setText(String.valueOf(movieToSee.getImdbMovie().getMetascore()));
        lblPremiereDate.setText(movieToSee.getImdbMovie().getPremiereDate());
        lblLength.setText(String.valueOf(movieToSee.getImdbMovie().getLength() + " min"));
        lblGenre.setText(movieToSee.getImdbMovie().getGenre());
        lblDescription.setText(movieToSee.getImdbMovie().getDescription());

        lblSource.setText(String.valueOf("Źródło: " + movieToSee.getSource()));
        lblVersion.setText(String.valueOf("Wersja: " + movieToSee.getVersion()));
        lblContainer.setText(String.valueOf("Kodek: " + movieToSee.getContainer()));
        lblResolution.setText(String.valueOf("Rozdzielczość: " + movieToSee.getResolution()));
        lblAudioSubtitles.setText(String.valueOf("Audio / Napisy: " + movieToSee.getAudioSub()));
    }

    public static void saveMovieToMap(MovieToSee movieToSee)
    {
        moviesToSee.put(movieToSee.getImdbMovie().getImdbID(), movieToSee);
    }
}
