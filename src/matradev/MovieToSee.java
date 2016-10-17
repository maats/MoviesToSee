package matradev;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Mateusz on 05.10.2016.
 */
public class MovieToSee {

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

    public MovieToSee() {
    }

    public MovieToSee(ImdbMovie imdbMovie) {
        this.imdbMovie = imdbMovie;
    }

    public MovieToSee(ImdbMovie imdbMovie, int source, int version, int container, int resolution, int audioSub) {
        this.imdbMovie = imdbMovie;
        this.source = source;
        this.version = version;
        this.container = container;
        this.resolution = resolution;
        this.audioSub = audioSub;
    }

    public MovieToSee(ImdbMovie imdbMovie, int source, int version, int container, int resolution, int audioSub, boolean seen, boolean movieParameters) {
        this.imdbMovie = imdbMovie;
        this.source = source;
        this.version = version;
        this.container = container;
        this.resolution = resolution;
        this.audioSub = audioSub;
        this.seen = seen;
        this.movieParameters = movieParameters;
    }

    public ImdbMovie getImdbMovie() {
        return imdbMovie;
    }

    public int getSource() {
        return source;
    }

    public int getVersion() {
        return version;
    }

    public int getContainer() {
        return container;
    }

    public int getResolution() {
        return resolution;
    }

    public int getAudioSub() {
        return audioSub;
    }

    public boolean isSeen() {
        return seen;
    }

    public boolean isMovieParameters() {
        return movieParameters;
    }

    public void setImdbMovie(ImdbMovie imdbMovie) {
        this.imdbMovie = imdbMovie;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setContainer(int container) {
        this.container = container;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public void setAudioSub(int audioSub) {
        this.audioSub = audioSub;
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
                '}';
    }

    public static Map<Integer, String> getContainers() {
        return containers;
    }

    public static Map<Integer, String> getSources() {
        return sources;
    }

    public static Map<Integer, String> getResolutions() {
        return resolutions;
    }

    public static Map<Integer, String> getAudioSubs() {
        return audioSubs;
    }

    public static Map<Integer, String> getVersions() {
        return versions;
    }

}
