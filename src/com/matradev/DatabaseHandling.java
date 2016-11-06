package com.matradev;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * PostgreSQL database handler
 * Created by Mateusz on 03.10.2016.
 * TODO: Later add dialog with entering server and login data to database
 */
public class DatabaseHandling {

    private static Connection connection = null;
    private ObservableList<TableEntry> moviesToSeeAsTableEntries = FXCollections.observableArrayList();

    public ObservableList<TableEntry> getMoviesToSeeAsTableEntries() {
        return moviesToSeeAsTableEntries;
    }

    public void setMoviesToSeeAsTableEntries(ObservableList<TableEntry> moviesToSeeAsTableEntries) {
        this.moviesToSeeAsTableEntries = moviesToSeeAsTableEntries;
    }

    static boolean connectWithDatabase()
    {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.
                    getConnection("jdbc:postgresql://localhost:5432/test2", "postgres", "qwerty");
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Nie odnaleziono klasy Driver kontrolera PostgreSQL!");
            return false;
        }
        System.out.println("Connection successful");
        return true;
    }

    boolean testConnectionWithDatabase(String serverAddress, String serverPort, String serverDbName, String serverLogin, String serverPassword)
    {

        String connectionString = "jdbc:postgresql://" + serverAddress + ":" + serverPort + "/" + serverDbName;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.
                    getConnection(connectionString, serverLogin, serverPassword);
            System.out.println(connection.isValid(3));
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Nie odnaleziono klasy Driver kontrolera PostgreSQL!");
            return false;
        }
        System.out.println("Connection successful");
        return true;
    }

    static boolean insertElementIntoDatabase(MovieToSee movieToSee, boolean movieParameters)
    {
        Statement statement;
        String sqlCommand;

        if(movieParameters)
        {
            // Case if there are movie parameters
            sqlCommand = "INSERT INTO moviestosee (movie_title, imdb_rating, votes_count, metascore, premiere_date, " +
                    "length, genre, description, poster_url, imdb_id, source, version, container, resolution, audio_subs, " +
                    "movie_parameters) VALUES ('" + movieToSee.getImdbMovie().getTitle() + "', " + movieToSee.getImdbMovie().getImdbRating() +
                    ", " + movieToSee.getImdbMovie().getVotesCount() + ", " + movieToSee.getImdbMovie().getMetascore() +
                    ", '" + movieToSee.getImdbMovie().getPremiereDate() + "', " + movieToSee.getImdbMovie().getLength() +
                    ", '" + movieToSee.getImdbMovie().getGenre() + "', '" + movieToSee.getImdbMovie().getDescription() +
                    "', '" + movieToSee.getImdbMovie().getPosterURL() + "', '" + movieToSee.getImdbMovie().getImdbID() +
                    "', " + movieToSee.getSource() + ", " + movieToSee.getVersion() + ", " + movieToSee.getContainer() +
                    ", " + movieToSee.getResolution() + ", " + movieToSee.getAudioSub() + ", " + movieToSee.isMovieParameters() + ");";
        }
        else
        {
            // Case if there aren't movie parameters
            sqlCommand = "INSERT INTO moviestosee (movie_title, imdb_rating, votes_count, metascore, premiere_date, " +
                    "length, genre, description, poster_url, imdb_id) " +
                    "VALUES ('" + movieToSee.getImdbMovie().getTitle() + "', " + movieToSee.getImdbMovie().getImdbRating() +
                    ", " + movieToSee.getImdbMovie().getVotesCount() + ", " + movieToSee.getImdbMovie().getMetascore() +
                    ", '" + movieToSee.getImdbMovie().getPremiereDate() + "', " + movieToSee.getImdbMovie().getLength() +
                    ", '" + movieToSee.getImdbMovie().getGenre() + "', '" + movieToSee.getImdbMovie().getDescription() +
                    "', '" + movieToSee.getImdbMovie().getPosterURL() + "', '" + movieToSee.getImdbMovie().getImdbID() +
                    "', " + movieToSee.isMovieParameters() + ");";
        }

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    Map<String, MovieToSee> getMoviesFromExternalDatabase()
    {
        Statement statement;
        MovieToSee movieToSee;
        Map<String, MovieToSee> moviesToSee = new TreeMap<>();

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM moviestosee");
            while(rs.next()) {
                String title = rs.getString("movie_title");
                float imdbRating = rs.getFloat("imdb_rating");
                int votesCount = rs.getInt("votes_count");
                int metascore = rs.getInt("metascore");
                String premiereDate = rs.getString("premiere_date");
                int length = rs.getInt("length");
                String genre = rs.getString("genre");
                String description = rs.getString("description");
                String posterURL = rs.getString("poster_url");
                String imdbID = rs.getString("imdb_id");
                boolean seen = rs.getBoolean("seen");
                boolean movieParameters = rs.getBoolean("movie_parameters");

                ImdbMovie imdbMovie = new ImdbMovie(title, imdbRating, votesCount, metascore, premiereDate, length,
                        genre, description, posterURL, imdbID);

                if (movieParameters)
                {
                    int source = rs.getInt("source");
                    int version = rs.getInt("version");
                    int container = rs.getInt("container");
                    int resolution = rs.getInt("resolution");
                    int audioSubs = rs.getInt("audio_subs");

                    movieToSee = new MovieToSee(imdbMovie, source, version, container, resolution, audioSubs, seen, movieParameters);
                }
                else
                    movieToSee = new MovieToSee(imdbMovie, seen, movieParameters);

                moviesToSee.put(movieToSee.getImdbMovie().getImdbID(), movieToSee);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return moviesToSee;
    }

    /**
     * Method updates record in database
     * @param imdbID Unique ID of movie
     * @param seen Boolean value to check what kind of update to do
     */
    static void updateRecordInDatabase(String imdbID, boolean seen)
    {
        Statement statement;
        String sqlCommand;

        if(seen)
        {
            sqlCommand = "UPDATE moviestosee SET seen = true WHERE imdb_id = '" + imdbID + "';";
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sqlCommand);
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method deletes record from database
     * @param imdbID Unique ID of movie
     * @return True - if everything went fine, otherwise false
     */
    static boolean deleteRecordFromDatabase(String imdbID)
    {
        Statement statement;
        String sqlCommand;

        sqlCommand = "DELETE from moviestosee WHERE imdb_id = '" + imdbID + "';";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            statement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
