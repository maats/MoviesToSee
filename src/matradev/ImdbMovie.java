package matradev;

import java.io.Serializable;

/**
 * Created by Mateusz on 03.10.2016.
 */
public class ImdbMovie implements Serializable {

    private String title;
    private float imdbRating;
    private int votesCount;
    private int metascore;
    private String premiereDate;
    private int length;
    private String genre;
    private String description;
    private String posterURL;
    private String imdbID;

    public ImdbMovie(String title, float imdbRating, int votesCount, int metascore, String premiereDate, int length, String genre, String description, String posterURL, String imdbID) {
        this.title = title;
        this.imdbRating = imdbRating;
        this.votesCount = votesCount;
        this.metascore = metascore;
        this.premiereDate = premiereDate;
        this.length = length;
        this.genre = genre;
        this.description = description;
        this.posterURL = posterURL;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public int getMetascore() {
        return metascore;
    }

    public String getPremiereDate() {
        return premiereDate;
    }

    public int getLength() {
        return length;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getImdbID() {
        return imdbID;
    }
}
