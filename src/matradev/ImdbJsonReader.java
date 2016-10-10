package matradev;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Class used for parsing data from IMDb API (omdbapi.com)
 * Created by Mateusz on 03.10.2016.
 */
public class ImdbJsonReader {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode node;
    private ImdbMovie movie;

    public ImdbJsonReader(){ }

    public ImdbMovie getMovie() {
        return movie;
    }

    /**
     * Parses data from API by IMDb ID
     * @param movieId IMDb ID
     * @return Node with movie data
     */
    public JsonNode parseImdbId(String movieId)
    {
        String parseUrl = "http://www.omdbapi.com/?i=" + movieId + "&plot=short&r=json";
        try {
            node = objectMapper.readValue(new URL(parseUrl), JsonNode.class);
        } catch (IOException e) {
            System.out.println("Connection problem with IMDb API. Check your internet connection.");
        }
        return node;
    }

    /**
     * Parses data from API by movie title and year
     * @param movieTitle Title of movie
     * @param movieYear Premiere year of movie (optional)
     * @return Node with movie data
     */
    public JsonNode parseImdbByMovieTitleAndYear(String movieTitle, String movieYear)
    {
        movieTitle = movieTitle.replaceAll(" ", "+");
        String parseUrl = "http://www.omdbapi.com/?t=" + movieTitle + "&y=" + movieYear + "&plot=short&r=json";

        try {
            node = objectMapper.readValue(new URL(parseUrl), JsonNode.class);
        } catch (IOException e) {
            System.out.println("Connection problem with IMDb API. Check your internet connection.");
        }
        return node;
    }

    /**
     * Method process data from node to proper types
     * TODO: Exception handling when some data in node is N/A
     */
    public void processParsedData()
    {
        // Process title from parser and convert to string
        JsonNode titleNode = node.get("Title");
        String title = titleNode.asText();
        System.out.println("Tytuł: " + title);

        // Process IMDb rating from parser and convert to float
        JsonNode imdbRatingNode = node.get("imdbRating");
        String imdbRatingAsString = imdbRatingNode.asText();
        float imdbRating = 0;
        try {
            imdbRating = Float.parseFloat(imdbRatingAsString);
        } catch (NumberFormatException e) {
            System.out.println("Problem with converting IMDb rating to float value");
        }
        System.out.println("Ocena IMDb: " + imdbRating);

        // Process votes count from parser and convert to int
        JsonNode votesCountNode = node.get("imdbVotes");
        String votesCountAsString = votesCountNode.asText();
        votesCountAsString = votesCountAsString.replaceAll(",", "");
        int votesCount = 0;
        try {
            votesCount = Integer.parseInt(votesCountAsString);
        } catch (NumberFormatException e) {
            System.out.println("Problem with converting votes count to int value");
        }
        System.out.println("Liczba głosów: " + votesCount);

        // Process Metascore from parser and convert to int
        JsonNode metascoreNode = node.get("Metascore");
        int metascore = metascoreNode.asInt();
        System.out.println("Metascore: " + metascore);

        // Process premiere date from parser and convert to String
        JsonNode premiereDateNode = node.get("Released");
        String premiereDate = premiereDateNode.asText();
        System.out.println("Data premiery: " + premiereDate);

        // Process length from parser and convert to int
        JsonNode lengthNode = node.get("Runtime");
        String lengthAsString = lengthNode.asText();
        lengthAsString = lengthAsString.replace(" min", "");
        int length = Integer.parseInt(lengthAsString);
        System.out.println("Czas trwania: " + length);

        // Process genre from parser and convert to String
        JsonNode genreNode = node.get("Genre");
        String genre = genreNode.asText();
        System.out.println("Gatunek: " + genre);

        // Process imdbMovie description from parser and convert to String
        JsonNode descriptionNode = node.get("Plot");
        String description = descriptionNode.asText();
        description = description.replaceAll("'", "''");
        description = description.replaceAll("\"", "\\\"");
        System.out.println("Opis: " + description);

        // Process poster URL from parser and convert to String
        JsonNode posterURLNode = node.get("Poster");
        String posterURL = posterURLNode.asText();
        System.out.println("URL okładki: " + posterURL);

        // Process IMDb ID from parser and convert to String
        JsonNode imdbIDNode = node.get("imdbID");
        String imdbID = imdbIDNode.asText();
        System.out.println("IMDb ID: " + imdbID);

        movie = new ImdbMovie(title, imdbRating, votesCount, metascore, premiereDate, length, genre, description, posterURL, imdbID);
    }

    /**
     * Method used to perform tests
     * @param args
     */
    public static void main(String[] args) {

        ImdbJsonReader reader = new ImdbJsonReader();
        reader.parseImdbId("tt3708886");
        reader.processParsedData();
    }

}
