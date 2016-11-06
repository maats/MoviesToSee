package com.matradev;

import javafx.beans.property.*;

/**
 * Class used for filling the table in main window of application
 * Created by Mateusz on 10.10.2016.
 */
public class TableEntry {

    private final StringProperty title;
    private final FloatProperty imdbRating;
    private final StringProperty premiereDate;
    private final IntegerProperty length;
    private final StringProperty genre;
    private final String imdbID;

    public TableEntry(String title, Float imdbRating, String premiereDate, Integer length, String genre, String imdbID) {
        this.title = new SimpleStringProperty(title);
        this.imdbRating = new SimpleFloatProperty(imdbRating);
        this.premiereDate = new SimpleStringProperty(premiereDate);
        this.length = new SimpleIntegerProperty(length);
        this.genre = new SimpleStringProperty(genre);
        this.imdbID = imdbID;
    }

    @Override
    public String toString() {
        return "TableEntry{" +
                "title=" + title +
                ", imdbRating=" + imdbRating +
                ", premiereDate=" + premiereDate +
                ", length=" + length +
                ", genre=" + genre +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public float getImdbRating() {
        return imdbRating.get();
    }

    public FloatProperty imdbRatingProperty() {
        return imdbRating;
    }

    public String getPremiereDate() {
        return premiereDate.get();
    }

    public StringProperty premiereDateProperty() {
        return premiereDate;
    }

    public int getLength() {
        return length.get();
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating.set(imdbRating);
    }

    public void setPremiereDate(String premiereDate) {
        this.premiereDate.set(premiereDate);
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }
}
