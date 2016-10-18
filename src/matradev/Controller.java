package matradev;

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

    @FXML private Button btnSeen;
    @FXML private Label lblDescription;
    @FXML private Label lblGenre;
    @FXML private Label lblImdbRating;
    @FXML private Label lblLength;
    @FXML private Label lblMetascore;
    @FXML private Label lblMovieTitle;
    @FXML private Label lblPremiereDate;
    @FXML private Label lblVotesCount;
    @FXML private Label lblToolbar;
    @FXML private ImageView imvPoster;
    @FXML private ImageView imvSource;
    @FXML private ImageView imvVersion;
    @FXML private ImageView imvContainer;
    @FXML private ImageView imvResolution;
    @FXML private ImageView imvAudioSub1;
    @FXML private ImageView imvAudioSub2;
    @FXML private MenuItem miAboutApp;
    @FXML private MenuItem miAdd;
    @FXML private MenuItem miDelete;
    @FXML private MenuItem miExit;
    @FXML private MenuItem miLoadExternalDb;
    @FXML private MenuItem miLoadLocalDb;
    @FXML private MenuItem miModify;
    @FXML private MenuItem miSettings;
    @FXML private TableView<TableEntry> tbvMovieListFromDb;
    @FXML private TableColumn<TableEntry, String> tbcGenre;
    @FXML private TableColumn<TableEntry, Number> tbcImdbRating;
    @FXML private TableColumn<TableEntry, Number> tbcLength;
    @FXML private TableColumn<TableEntry, String> tbcMovieTitle;
    @FXML private TableColumn<TableEntry, String> tbcPremiereDate;

    private Image poster;
    private ObservableList<TableEntry> moviesToSeeAsTableEntries = FXCollections.observableArrayList();
    private Map<String, MovieToSee> moviesToSee = new TreeMap<String, MovieToSee>();

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

        // Selected item in table changed
        tbvMovieListFromDb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TableEntry>() {
            @Override
            public void changed(ObservableValue<? extends TableEntry> observable, TableEntry oldValue, TableEntry newValue) {
                if(tbvMovieListFromDb.getSelectionModel().getSelectedItem() != null)
                {
                    lblToolbar.setText(null);
                    btnSeen.setDisable(false);
                    setMovieInformationsInMainWindow(newValue.getImdbID());
                    System.out.println(newValue.toString());
                }
            }
        });

        // Shows new dialog with adding movie form
        miAdd.setOnAction(new EventHandler<ActionEvent>() {
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

        /**
         * Load informations from external (SQL) database
         * TODO: Improve sorting by premiere date
         */
        miLoadExternalDb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseHandling databaseHandling = new DatabaseHandling();

                DatabaseHandling.connectWithDatabase();
                moviesToSee = databaseHandling.getElementFromDatabase();
                moviesToSeeAsTableEntries = databaseHandling.getMoviesToSeeAsTableEntries();

                // Fills the table
                tbcMovieTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
                tbcImdbRating.setCellValueFactory(cellData -> cellData.getValue().imdbRatingProperty());
                tbcPremiereDate.setCellValueFactory(cellData -> cellData.getValue().premiereDateProperty());
                tbcLength.setCellValueFactory(cellData -> cellData.getValue().lengthProperty());
                tbcGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
                tbvMovieListFromDb.setItems(getMoviesToSeeAsTableEntries());
                lblToolbar.setText("Pomyślnie załadowano bazę danych online");
            }
        });

    }

    /**
     * Method sets controls in main window
     * @param imdbID Movie ID
     */
    private void setMovieInformationsInMainWindow(String imdbID)
    {
        MovieToSee movieToSee = moviesToSee.get(imdbID);
        System.out.println(movieToSee.toString());

        imvPoster.setImage(new Image(movieToSee.getImdbMovie().getPosterURL()));
        lblMovieTitle.setText(movieToSee.getImdbMovie().getTitle());
        lblImdbRating.setText(String.valueOf(movieToSee.getImdbMovie().getImdbRating()));
        lblVotesCount.setText(String.valueOf(movieToSee.getImdbMovie().getVotesCount()));
        lblMetascore.setText(String.valueOf(movieToSee.getImdbMovie().getMetascore()));
        lblPremiereDate.setText(movieToSee.getImdbMovie().getPremiereDate());
        lblLength.setText(String.valueOf(movieToSee.getImdbMovie().getLength() + " min"));
        lblGenre.setText(movieToSee.getImdbMovie().getGenre());
        lblDescription.setText(movieToSee.getImdbMovie().getDescription());

        if(movieToSee.isMovieParameters())
        {
            setLogotypesOfParameters(movieToSee.getSource(), movieToSee.getVersion(), movieToSee.getContainer(), movieToSee.getResolution(), movieToSee.getAudioSub());
        }
        // If there aren't movie parameters, method is called with -1 to disable logotypes
        else
        {
            setLogotypesOfParameters(-1, -1, -1, -1, -1);
        }

    }

    /**
     * Method takes parameters and set icons of movie parameters
     * @param source ID of source
     * @param version ID of version
     * @param container ID of container
     * @param resolution ID of resolution
     * @param audioSub ID of audio/subtitles
     */
    private void setLogotypesOfParameters(int source, int version, int container, int resolution, int audioSub)
    {
        FileInputStream input;

        switch (source)
        {
            case -1:
                imvSource.setImage(null);
                break;
            case 0:
                try {
                    input = new FileInputStream("resources/images/sources/bluray.png");
                    imvSource.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    input = new FileInputStream("resources/images/sources/webrip.png");
                    imvSource.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    input = new FileInputStream("resources/images/sources/webdl.png");
                    imvSource.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    input = new FileInputStream("resources/images/sources/hdrip.png");
                    imvSource.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    input = new FileInputStream("resources/images/sources/dvdrip.png");
                    imvSource.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        switch (version)
        {
            case -1:
                imvVersion.setImage(null);
                break;
            case 0:
                try {
                    input = new FileInputStream("resources/images/versions/2d.png");
                    imvVersion.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    input = new FileInputStream("resources/images/versions/3d.png");
                    imvVersion.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        switch (container)
        {
            case -1:
                imvContainer.setImage(null);
                break;
            case 0:
                try {
                    input = new FileInputStream("resources/images/containers/x264.png");
                    imvContainer.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    input = new FileInputStream("resources/images/containers/x265.png");
                    imvContainer.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    input = new FileInputStream("resources/images/containers/xvid.png");
                    imvContainer.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        switch (resolution)
        {
            case -1:
                imvResolution.setImage(null);
                break;
            case 0:
                try {
                    input = new FileInputStream("resources/images/resolutions/2160p.png");
                    imvResolution.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    input = new FileInputStream("resources/images/resolutions/1080p.png");
                    imvResolution.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    input = new FileInputStream("resources/images/resolutions/720p.png");
                    imvResolution.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    input = new FileInputStream("resources/images/resolutions/480p.png");
                    imvResolution.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        switch (audioSub)
        {
            case -1:
                imvAudioSub1.setImage(null);
                imvAudioSub2.setImage(null);
                break;
            case 0:
                try {
                    input = new FileInputStream("resources/images/audiosubs/plsub.png");
                    imvAudioSub1.setImage(new Image(input));
                    imvAudioSub2.setImage(null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    input = new FileInputStream("resources/images/audiosubs/plsub.png");
                    imvAudioSub1.setImage(new Image(input));
                    input = new FileInputStream("resources/images/audiosubs/pllek.png");
                    imvAudioSub2.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    input = new FileInputStream("resources/images/audiosubs/plsub.png");
                    imvAudioSub1.setImage(new Image(input));
                    input = new FileInputStream("resources/images/audiosubs/pldub.png");
                    imvAudioSub2.setImage(new Image(input));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    input = new FileInputStream("resources/images/audiosubs/pllek.png");
                    imvAudioSub1.setImage(new Image(input));
                    imvAudioSub2.setImage(null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    input = new FileInputStream("resources/images/audiosubs/pldub.png");
                    imvAudioSub1.setImage(new Image(input));
                    imvAudioSub2.setImage(null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

/*    public static void saveMovieToMap(MovieToSee movieToSee)
    {
        moviesToSee.put(movieToSee.getImdbMovie().getImdbID(), movieToSee);
    }*/
}
