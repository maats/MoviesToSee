package matradev;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Local database
 * Created by Mateusz on 20.10.2016.
 */
public class LocalDatabase {

    private static MovieToSee movieToSee;
    private static ArrayList<MovieToSee> movies = new ArrayList<>();
    private static String fileName = "dbLocal.stm";
    //private static File file = new File(fileName);

    public static void main(String[] args) {

    }

    void addToDb(Map<String, MovieToSee> moviesToSee)
    {
        FileOutputStream fos;
        ObjectOutputStream oos = null;
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(moviesToSee);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
