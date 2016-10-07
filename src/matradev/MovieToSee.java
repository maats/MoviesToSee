package matradev;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final Map<Integer, String> containers = new TreeMap<>();
    private static final Map<Integer, String> sources = new TreeMap<>();
    private static final Map<Integer, String> resolutions = new TreeMap<>();
    private static final Map<Integer, String> audioSubs = new TreeMap<>();
    private static final Map<Integer, String> versions = new TreeMap<>();

    static{

    }

    public MovieToSee() {
        containers.put(0, "x264");
        containers.put(1, "XviD");
        containers.put(2, "AVC");
        sources.put(0, "BluRay");
        sources.put(1, "WEBRip");
        sources.put(2, "WEB-DL");
        sources.put(3, "HDRip");
        sources.put(4, "DVDRip");
        resolutions.put(0, "1080p");
        resolutions.put(1, "720p");
        resolutions.put(2, "480p");
        audioSubs.put(0, "polskie napisy");
        audioSubs.put(1, "polski lektor / polskie napisy");
        audioSubs.put(2, "polski dubbing / polskie napisy");
        audioSubs.put(3, "polski lektor");
        audioSubs.put(4, "polski dubbing");
        versions.put(0, "2D");
        versions.put(1, "3D");
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
