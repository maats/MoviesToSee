package matradev;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Mateusz on 04.10.2016.
 */
public class AddDialogController implements Initializable {

    @FXML private Button btnAddMovieToSee;
    @FXML private Button btnCancel;
    @FXML private Button btnOk;
    @FXML private Button btnSaveInDb;
    @FXML private Button btnSaveLocal;
    @FXML private Button btnSearch;
    @FXML private Button btnWrongMovieFound;
    @FXML private ChoiceBox<String> cebAudioSubtitles;
    @FXML private ChoiceBox<String> cebContainer;
    @FXML private ChoiceBox<String> cebResolution;
    @FXML private ChoiceBox<String> cebSource;
    @FXML private ChoiceBox<String> cebVersion;
    @FXML private VBox vboxSearchResult;
    @FXML private ImageView imvPoster;
    @FXML private Label lblCheckSearchResult;
    @FXML private Label lblGenre;
    @FXML private Label lblPremiereDate;
    @FXML private Label lblTitle;
    @FXML private Label lblPreciseMoreYourSearch;
    @FXML private TextField txtMovieId;
    @FXML private TextField txtMovieTitle;
    @FXML private TextField txtMovieYear;
    @FXML private VBox vboxMovieParameters;
    @FXML private VBox vboxSaveButtons;
    private Image poster;
    ImdbMovie imdbMovie = null;
    MovieToSee movieToSee = new MovieToSee();
    static Map<Integer, String> mapAudioSubtitles;
    static Map<Integer, String> mapContainers;
    static Map<Integer, String> mapResolutions;
    static Map<Integer, String> mapSources;
    static Map<Integer, String> mapVersions;
    private boolean setMovieParameters;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            FileInputStream input = new FileInputStream("resources/images/noposter.png");
            poster = new Image(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imvPoster.setImage(poster);
        loadMovieParametersToChoiceBoxes();

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String movieId = txtMovieId.getText();
                String movieTitle = txtMovieTitle.getText();
                String movieYear = txtMovieYear.getText();
                ImdbJsonReader imdbJsonReader = new ImdbJsonReader();

                // Mozna dodac sprawdzanie czy format ID imdb (tt1234567) jest wprowadzany
                if(movieId.length() != 0)
                {
                    imdbJsonReader.parseImdbId(movieId);
                    imdbJsonReader.processParsedData();
                    imdbMovie = imdbJsonReader.getMovie();
                }
                else if(movieTitle.length() != 0)
                {
                    imdbJsonReader.parseImdbByMovieTitleAndYear(movieTitle, movieYear);
                    imdbJsonReader.processParsedData();
                    imdbMovie = imdbJsonReader.getMovie();
                }

                if(imdbMovie != null)
                {
                    lblTitle.setText(imdbMovie.getTitle());
                    lblPremiereDate.setText(imdbMovie.getPremiereDate());
                    lblGenre.setText(imdbMovie.getGenre());
                    imvPoster.setImage(new Image(imdbMovie.getPosterURL()));
                    lblCheckSearchResult.setVisible(true);
                    vboxSearchResult.setDisable(false);
                    lblCheckSearchResult.setDisable(false);

                }
                else
                {
                    System.out.println("Nie podano argumentu");
                }
            }
        });

        btnSaveInDb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DatabaseHandling.connectWithDatabase();
                // Check if user chose setting up movie parameters
                if(setMovieParameters)
                {
                    int[] movieParams = getMovieParametersFromChoiceBoxes();
                    movieToSee = new MovieToSee(imdbMovie, movieParams[0], movieParams[1], movieParams[2], movieParams[3], movieParams[4]);
                }
                else
                {
                    movieToSee = new MovieToSee(imdbMovie);
                }
                DatabaseHandling.insertElementIntoDatabase(movieToSee, setMovieParameters);
                System.out.println(movieToSee.toString());

            }
        });

        btnAddMovieToSee.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Show alert window with question about setting movie parameters
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potwierdzenie ustawienia parametrów filmu");
                alert.setHeaderText("Czy chcesz ustawić parametry filmu?");

                ButtonType buttonTypeYes = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
                ButtonType buttonTypeNo = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeYes){
                    setMovieParameters = true;
                    vboxMovieParameters.setDisable(false); // Unlock VBox with choicebox controls
                } else {
                    setMovieParameters = false;
                }
                vboxSaveButtons.setDisable(false); // Unlock VBox with save buttons
            }
        });

        btnWrongMovieFound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblPreciseMoreYourSearch.setVisible(true);
            }
        });
    }

    public ImdbMovie getImdbMovie() {
        return imdbMovie;
    }

    public MovieToSee getMovieToSee() {
        return movieToSee;
    }

    public int[] getMovieParametersFromChoiceBoxes()
    {
        int[] movieParams = new int[5];

        for (Map.Entry<Integer, String> entry :
                mapSources.entrySet()){
            if(entry.getValue().equals(cebSource.getValue())) {
                movieParams[0] = entry.getKey();
            }
        }
        for (Map.Entry<Integer, String> entry :
                mapVersions.entrySet()){
            if(entry.getValue().equals(cebVersion.getValue())) {
                movieParams[1] = entry.getKey();
            }
        }
        for (Map.Entry<Integer, String> entry :
                mapContainers.entrySet()){
            if(entry.getValue().equals(cebContainer.getValue())) {
                movieParams[2] = entry.getKey();
            }
        }
        for (Map.Entry<Integer, String> entry :
                mapResolutions.entrySet()){
            if(entry.getValue().equals(cebResolution.getValue())) {
                movieParams[3] = entry.getKey();
            }
        }
        for (Map.Entry<Integer, String> entry :
                mapAudioSubtitles.entrySet()){
            if(entry.getValue().equals(cebAudioSubtitles.getValue())) {
                movieParams[4] = entry.getKey();
            }
        }

        return movieParams;
    }

    public void loadMovieParametersToChoiceBoxes()
    {
        // Processing maps
        mapAudioSubtitles = movieToSee.getAudioSubs();
        mapContainers = movieToSee.getContainers();
        mapResolutions = movieToSee.getResolutions();
        mapSources = movieToSee.getSources();
        mapVersions = movieToSee.getVersions();

        for (Integer key :
                mapAudioSubtitles.keySet()) {
            cebAudioSubtitles.getItems().add(mapAudioSubtitles.get(key));
        }
        cebAudioSubtitles.getSelectionModel().selectFirst();

        for (Integer key :
                mapContainers.keySet()) {
            cebContainer.getItems().add(mapContainers.get(key));
        }
        cebContainer.getSelectionModel().selectFirst();

        for (Integer key :
                mapResolutions.keySet()) {
            cebResolution.getItems().add(mapResolutions.get(key));
        }
        cebResolution.getSelectionModel().selectFirst();

        for (Integer key :
                mapSources.keySet()) {
            cebSource.getItems().add(mapSources.get(key));
        }
        cebSource.getSelectionModel().selectFirst();

        for (Integer key :
                mapVersions.keySet()) {
            cebVersion.getItems().add(mapVersions.get(key));
        }
        cebVersion.getSelectionModel().selectFirst();
    }
}