package matradev;

import java.sql.*;

/**
 * Created by Mateusz on 03.10.2016.
 */
public class DatabaseHandling {

    private static Connection connection = null;
    private static Statement statement = null;

    public static void main(String[] args) {
/*        boolean tmp;
        tmp = connectWithDatabase();
        String[] strArray = new String[]{"0", "Dzony Rambo", "8.30", "94"};
        tmp = insertElementIntoDatabase(strArray);*/
    }

    public static boolean connectWithDatabase()
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

    // TODO: Zamkniecie polaczen (close())
    public static boolean insertElementIntoDatabase(MovieToSee movieToSee, boolean movieParameters)
    {
        String sqlCommand;

        if(movieParameters)
        {
            // Case if there are movie parameters
            sqlCommand = "INSERT INTO moviestosee (movie_title, imdb_rating, votes_count, metascore, premiere_date, " +
                    "length, genre, description, poster_url, imdb_id, source, version, container, resolution, audio_subs) " +
                    "VALUES ('" + movieToSee.getImdbMovie().getTitle() + "', " + movieToSee.getImdbMovie().getImdbRating() +
                    ", " + movieToSee.getImdbMovie().getVotesCount() + ", " + movieToSee.getImdbMovie().getMetascore() +
                    ", '" + movieToSee.getImdbMovie().getPremiereDate() + "', " + movieToSee.getImdbMovie().getLength() +
                    ", '" + movieToSee.getImdbMovie().getGenre() + "', '" + movieToSee.getImdbMovie().getDescription() +
                    "', '" + movieToSee.getImdbMovie().getPosterURL() + "', '" + movieToSee.getImdbMovie().getImdbID() +
                    "', " + movieToSee.getSource() + ", " + movieToSee.getVersion() + ", " + movieToSee.getContainer() +
                    ", " + movieToSee.getResolution() + ", " + movieToSee.getAudioSub() + ");";
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
                    "');";
        }

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    // TODO: Zamkniecie polaczen (close())
    // TODO: Zmienic zwracanie obiektu klasy MovieToSee na jakas liste
    public static MovieToSee getElementFromDatabase()
    {
        MovieToSee movieToSee = null;
        Controller controller = new Controller();

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM moviestosee");
            while(rs.next())
            {
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
                ImdbMovie imdbMovie = new ImdbMovie(title, imdbRating, votesCount, metascore, premiereDate, length,
                        genre, description, posterURL, imdbID);
                movieToSee = new MovieToSee(imdbMovie);

                Controller.processMovieToSeeObjectsToTableEntries(movieToSee);
                System.out.println(title + " " + imdbRating + " " + votesCount + " " + metascore);
            }
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return movieToSee;
    }

/*    public static boolean insertElementIntoDatabase(String[] args)
    {
        try {
            statement = connection.createStatement();
            String sqlCommand = "INSERT INTO movies VALUES (" + args[0] + ", '" + args[1] + "', " + args[2] + ", " + args[3] + ");";
            statement.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }*/
}
