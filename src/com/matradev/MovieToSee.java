package com.matradev;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * MovieToSee object implementation
 * Created by Mateusz on 05.10.2016.
 */

class MovieToSee implements Serializable {

    private ImdbMovie imdbMovie;
    private int source;
    private int version;
    private int container;
    private int resolution;
    private int audioSub;
    private boolean seen;
    private boolean movieParameters;

    private static final Map<Integer, String> containers = new TreeMap<Integer, String>(){{
        put(0, "x264");
        put(1, "x265");
        put(2, "XviD");
    }};
    private static final Map<Integer, String> sources = new TreeMap<Integer, String>(){{
        put(0, "BluRay");
        put(1, "WEBRip");
        put(2, "WEB-DL");
        put(3, "HDRip");
        put(4, "DVDRip");
    }};
    private static final Map<Integer, String> resolutions = new TreeMap<Integer, String>(){{
        put(0, "2160p");
        put(1, "1080p");
        put(2, "720p");
        put(3, "480p");
    }};
    private static final Map<Integer, String> audioSubs = new TreeMap<Integer, String>(){{
        put(0, "polskie napisy");
        put(1, "polski lektor / polskie napisy");
        put(2, "polski dubbing / polskie napisy");
        put(3, "polski lektor");
        put(4, "polski dubbing");
    }};
    private static final Map<Integer, String> versions = new TreeMap<Integer, String>(){{
        put(0, "2D");
        put(1, "3D");
    }};

    MovieToSee() { }

    MovieToSee(ImdbMovie imdbMovie, int source, int version, int container, int resolution, int audioSub, boolean seen, boolean movieParameters) {
        this.imdbMovie = imdbMovie;
        this.source = source;
        this.version = version;
        this.container = container;
        this.resolution = resolution;
        this.audioSub = audioSub;
        this.seen = seen;
        this.movieParameters = movieParameters;
    }

    MovieToSee(ImdbMovie imdbMovie, boolean seen, boolean movieParameters) {
        this.imdbMovie = imdbMovie;
        this.seen = seen;
        this.movieParameters = movieParameters;
    }

    ImdbMovie getImdbMovie() {
        return imdbMovie;
    }

    int getSource() {
        return source;
    }

    int getVersion() {
        return version;
    }

    int getContainer() {
        return container;
    }

    int getResolution() {
        return resolution;
    }

    int getAudioSub() {
        return audioSub;
    }

    boolean isSeen() {
        return seen;
    }

    void setSeen(boolean seen) {
        this.seen = seen;
    }

    boolean isMovieParameters() {
        return movieParameters;
    }

    @Override
    public String toString() {
        return "MovieToSee{" +
                "imdbMovie=" + imdbMovie +
                ", source=" + source +
                ", version=" + version +
                ", container=" + container +
                ", resolution=" + resolution +
                ", audioSub=" + audioSub +
                ", seen=" + seen +
                ", movieParameters=" + movieParameters +
                '}';
    }

    static Map<Integer, String> getContainers() {
        return containers;
    }

    static Map<Integer, String> getSources() {
        return sources;
    }

    static Map<Integer, String> getResolutions() {
        return resolutions;
    }

    static Map<Integer, String> getAudioSubs() {
        return audioSubs;
    }

    static Map<Integer, String> getVersions() {
        return versions;
    }

}
