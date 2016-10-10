package matradev;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML private Button btnParse;
    @FXML private Button btnAdd;
    @FXML private Button btnModify;
    @FXML private Button btnDelete;
    @FXML private TextField txtIdToParse;
    @FXML private Label lblMovieTitle;
    @FXML private Label lblImdbRating;
    @FXML private Label lblVotesCount;
    @FXML private Label lblDescription;
    @FXML private ImageView imvPoster;
    @FXML private TableView<TableEntry> tbvMovieListFromDb;
    @FXML private TableColumn<TableEntry, String> tbcGenre;
    @FXML private TableColumn<TableEntry, Float> tbcImdbRating;
    @FXML private TableColumn<TableEntry, Integer> tbcLength;
    @FXML private TableColumn<TableEntry, String> tbcMovieTitle;
    @FXML private TableColumn<TableEntry, String> tbcPremiereDate;
    private String movieIdForParser;
    private Image poster;
    ImdbJsonReader imdbJsonReader = new ImdbJsonReader();
    private static Map<String, Object> userData = null;
    static ObservableList<TableEntry> moviesToSeeAsTableEntries = FXCollections.observableArrayList();
    static Map<Integer, MovieToSee> moviesToSee;

    public ObservableList<TableEntry> getMoviesToSeeAsTableEntries() {
        return moviesToSeeAsTableEntries;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnParse.setText("Parse");
        try {
            FileInputStream input = new FileInputStream("resources/images/noposter.png");
            poster = new Image(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imvPoster.setImage(poster);
        btnParse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                movieIdForParser = txtIdToParse.getText();
                System.out.println(movieIdForParser);
                imdbJsonReader.parseImdbId(movieIdForParser);
                imdbJsonReader.processParsedData();
                System.out.println("Button test");
                ImdbMovie movie = imdbJsonReader.getMovie();
                lblMovieTitle.setText(movie.getTitle());
                lblImdbRating.setText(String.valueOf(movie.getImdbRating()));
                lblVotesCount.setText(String.valueOf(movie.getVotesCount()));
                lblDescription.setText(movie.getDescription());
                imvPoster.setImage(new Image(movie.getPosterURL()));
            }
        });

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

        // TODO:
        // Test of showing table
        btnModify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
           //     tbvMovieListFromDb = new TableView<MovieToSee>();
               // tbcMovieTitle.setCellValueFactory(new PropertyValueFactory<TableEntry, String>("movieTitle"));

                DatabaseHandling.connectWithDatabase();
                MovieToSee movieToSee = DatabaseHandling.getElementFromDatabase();
                tbvMovieListFromDb.setItems(getMoviesToSeeAsTableEntries());
                tbcMovieTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
             //   tbcImdbRating.setCellValueFactory(cellData -> cellData.getValue().imdbRatingProperty());
                tbcGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
                tbvMovieListFromDb.setItems(getMoviesToSeeAsTableEntries());

               // moviesToSeeAsTableEntries.add(movieToSee);
                //tbcMovieTitle.setCellValueFactory(cellData -> cellData.getValue().getImdbMovie().getTitle());
                //tbcMovieTitle.setCellValueFactory(new PropertyValueFactory<MovieToSee, String>("title"));
            }
        });
    }

    public static void processMovieToSeeObjectsToTableEntries(MovieToSee movieToSee)
    {
        moviesToSeeAsTableEntries.add(new TableEntry(movieToSee.getImdbMovie().getTitle(), movieToSee.getImdbMovie().getImdbRating(),
                movieToSee.getImdbMovie().getPremiereDate(), movieToSee.getImdbMovie().getLength(), movieToSee.getImdbMovie().getGenre(),
                movieToSee.getImdbMovie().getImdbID()));
    }
}
