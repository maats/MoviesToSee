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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
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
    @FXML private Menu menuKindOfWork;
    @FXML private Menu menuMovie;
    @FXML private MenuItem miAboutApp;
    @FXML private MenuItem miAdd;
    @FXML private MenuItem miCreateLocalDatabase;
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
    private static String selectedItemInTable;
    private final FileChooser fileChooser = new FileChooser();
    private LocalDatabase localDatabase;
    private static boolean isAppWorksOnline;

    public ObservableList<TableEntry> getMoviesToSeeAsTableEntries() {
        return moviesToSeeAsTableEntries;
    }

    public static boolean isAppWorksOnline() {
        return isAppWorksOnline;
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

        /**
         * MenuItem: File > Create new local database
         * Creates file with database in selected by user directory
         */
        miCreateLocalDatabase.setOnAction(event -> {
            Stage stage = new Stage();
            fileChooser.setTitle("Nowa baza filmów do obejrzenia");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop")); // Sets initial directory in FileChooser
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DBW (.dbw)", "*.dbw")); // Sets available extension of file
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                menuKindOfWork.setText("Status pracy: offline");
                isAppWorksOnline = false;
                menuMovie.setDisable(false);
                Main.primaryStage.setTitle(Main.APP_NAME + " " + Main.APP_VERSION + " [ db: " + file.getName() + " ]");
                localDatabase = new LocalDatabase();
                localDatabase.setDbFilePath(file.getPath());
                localDatabase.setDbFileName(file.getName());
                localDatabase.createDatabaseFile();
            }
        });

        /**
         * MenuItem: File > Load local database
         * Load informations from internal database (local file)
         */
        miLoadLocalDb.setOnAction(event -> {

            Stage stage = new Stage();
            fileChooser.setTitle("Wybierz plik z bazą filmów do obejrzenia");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop")); // Sets initial directory in FileChooser
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DBW (.dbw)", "*.dbw")); // Sets available extension of file
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                menuKindOfWork.setText("Status pracy: offline");
                isAppWorksOnline = false;
                menuMovie.setDisable(false);
                Main.primaryStage.setTitle(Main.APP_NAME + " " + Main.APP_VERSION + " [db: " + file.getName() + "]");
                localDatabase = new LocalDatabase();
                localDatabase.setDbFilePath(file.getPath());
                localDatabase.setDbFileName(file.getName());
                moviesToSee = localDatabase.loadMoviesDatabase();
                fillTableInMainWindow(isAppWorksOnline);
                lblToolbar.setText("Pomyślnie załadowano lokalną bazę danych");
            }
        });

        /**
         * MenuItem: File > Load external database
         * Load informations from external (SQL) database
         * TODO: Improve sorting by premiere date
         */
        miLoadExternalDb.setOnAction(event -> {

            DatabaseHandling.connectWithDatabase();
            menuKindOfWork.setText("Status pracy: online");
            isAppWorksOnline = true;
            menuMovie.setDisable(false);
            fillTableInMainWindow(isAppWorksOnline);

            lblToolbar.setText("Pomyślnie załadowano bazę danych online");
        });

        /**
         * MenuItem: File > Settings
         * Opens new window with settings
         */
        miSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("matradev/SettingsDialog.fxml"), resources);
                    Stage stage = new Stage();
                    stage.initModality(Modality.WINDOW_MODAL); // Parent stage is unavailable when child is opened
                    stage.initOwner(Main.primaryStage); // Parent stage is unavailable when child is opened
                    stage.setTitle("Ustawienia");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * MenuItem: File > Exit
         * Closes application
         */
        miExit.setOnAction(event -> {
            //
        });

        /**
         * MenuItem: Movie > Add
         * Opens new window with adding movie form
         */
        miAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("matradev/AddDialog.fxml"), resources);
                    Stage stage = new Stage();
                    stage.initModality(Modality.WINDOW_MODAL); // Parent stage is unavailable when child is opened
                    stage.initOwner(Main.primaryStage); // Parent stage is unavailable when child is opened
                    stage.setTitle("Wyszukaj film");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                    stage.setOnCloseRequest(event1 -> {
                        fillTableInMainWindow(isAppWorksOnline);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * MenuItem: Movie > Modify
         */
        miModify.setOnAction(event ->
        {
            //
        });

        /**
         * MenuItem: Movie > Delete
         */
        miDelete.setOnAction(event -> {
            deleteMovieFromDatabase(isAppWorksOnline);
        });

        /**
         * MenuItem: Help > About Application
         */
        miAboutApp.setOnAction(event -> {
            //
        });

        /**
         * Handles event when selected item in table changed
         */
        tbvMovieListFromDb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TableEntry>() {
            @Override
            public void changed(ObservableValue<? extends TableEntry> observable, TableEntry oldValue, TableEntry newValue) {
                if(tbvMovieListFromDb.getSelectionModel().getSelectedItem() != null)
                {
                    lblToolbar.setText(null);
                    btnSeen.setDisable(false);
                    selectedItemInTable = newValue.getImdbID();
                    setMovieInformationsInMainWindow(newValue.getImdbID());
                    System.out.println(newValue.toString());
                }
            }
        });

        /**
         * Button: Set as seen
         */
        btnSeen.setOnAction(event -> {

            // Show alert window with question about setting movie parameters
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie akcji");
            alert.setHeaderText("Czy na pewno chcesz oznaczyć film jako obejrzany?");

            ButtonType buttonTypeYes = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                DatabaseHandling.connectWithDatabase();
                DatabaseHandling.updateRecordInDatabase(selectedItemInTable, true);
                lblToolbar.setText("Film został oznaczony jako obejrzany");
                btnSeen.setDisable(true);
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

        if (movieToSee.isMovieParameters())
            setLogotypesOfParameters(movieToSee.getSource(), movieToSee.getVersion(), movieToSee.getContainer(), movieToSee.getResolution(), movieToSee.getAudioSub());
        // If there aren't movie parameters, method is called with -1 to disable logotypes
        else
            setLogotypesOfParameters(-1, -1, -1, -1, -1);
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


    private void setMovieAsSeen()
    {

    }

    private void deleteMovieFromDatabase(boolean isAppWorksOnline)
    {
        boolean status = true;

        // Show alert window with confirmation of action
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie akcji");
        alert.setHeaderText("Czy na pewno chcesz usunąć film z bazy danych?");

        ButtonType buttonTypeYes = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            if (isAppWorksOnline) {
                status = DatabaseHandling.deleteRecordFromDatabase(selectedItemInTable);
                fillTableInMainWindow(isAppWorksOnline);
            } else {
                //
            }

            if (status)
                lblToolbar.setText("Film został pomyślnie usunięty z bazy danych");
            else
                lblToolbar.setText("Wystąpił błąd podczas usuwania filmu z bazy danych");
        }
    }

    private void fillTableInMainWindow(boolean isAppWorksOnline)
    {
        if (isAppWorksOnline) {
            DatabaseHandling databaseHandling = new DatabaseHandling();
            moviesToSee = databaseHandling.getMoviesFromExternalDatabase();
        }
        moviesToSeeAsTableEntries = convertMapToObservableList();

        tbcMovieTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        tbcImdbRating.setCellValueFactory(cellData -> cellData.getValue().imdbRatingProperty());
        tbcImdbRating.setStyle("-fx-alignment: CENTER;");
        tbcImdbRating.setSortType(TableColumn.SortType.DESCENDING); // Sort by IMDb rating
        tbcPremiereDate.setCellValueFactory(cellData -> cellData.getValue().premiereDateProperty());
        tbcPremiereDate.setStyle("-fx-alignment: CENTER-RIGHT;");
        tbcLength.setCellValueFactory(cellData -> cellData.getValue().lengthProperty());
        tbcLength.setStyle("-fx-alignment: CENTER;");
        tbcGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        tbvMovieListFromDb.setItems(getMoviesToSeeAsTableEntries());
        tbvMovieListFromDb.getSortOrder().add(tbcImdbRating); // Sort by IMDb rating
        tbvMovieListFromDb.getSelectionModel().selectFirst();
    }

    private ObservableList<TableEntry> convertMapToObservableList() {

        ObservableList<TableEntry> moviesToSeeAsTableEntries = FXCollections.observableArrayList();

        for (String key :
                moviesToSee.keySet()) {
            MovieToSee movieToSee = moviesToSee.get(key);
            moviesToSeeAsTableEntries.add(new TableEntry(movieToSee.getImdbMovie().getTitle(), movieToSee.getImdbMovie().getImdbRating(),
                    movieToSee.getImdbMovie().getPremiereDate(), movieToSee.getImdbMovie().getLength(), movieToSee.getImdbMovie().getGenre(),
                    movieToSee.getImdbMovie().getImdbID()));
        }
        return moviesToSeeAsTableEntries;
    }
}
