package com.matradev;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Local database
 * Created by Mateusz on 20.10.2016.
 */
public class LocalDatabase {

    private static String dbFilePath;
    private static Map<String, MovieToSee> moviesToSee = new TreeMap<>();

    public static void setDbFilePath(String dbFilePath) {
        LocalDatabase.dbFilePath = dbFilePath;
    }

    public static Map<String, MovieToSee> getMoviesToSee() {
        return moviesToSee;
    }

    static void createDatabaseFile()
    {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = new FileOutputStream(dbFilePath);
            oos = new ObjectOutputStream(fos);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveMoviesDatabase(Map<String, MovieToSee> moviesToSee)
    {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = new FileOutputStream(dbFilePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(moviesToSee);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void addMovieToLocalDatabase(MovieToSee movieToSee)
    {
        moviesToSee.put(movieToSee.getImdbMovie().getImdbID(), movieToSee);
    }

    static Map<String, MovieToSee> loadMoviesDatabase()
    {
        FileInputStream fis;
        ObjectInputStream ois;
        File file = new File(dbFilePath);

        if (file.exists()) {
            try {
                fis = new FileInputStream(dbFilePath);
                ois = new ObjectInputStream(fis);
                moviesToSee = (Map) ois.readObject();
                ois.close();
                fis.close();
            } catch (FileNotFoundException e) {
                System.out.println("Load: Nie odnaleziono pliku podczas ładowania");
            } catch (IOException e) {
                System.out.println("Load: Wyjątek IO");
            } catch (ClassNotFoundException e) {
                System.out.println("Load: Klasa nie odnaleziona");
            }
        }
        return moviesToSee;
    }
}
