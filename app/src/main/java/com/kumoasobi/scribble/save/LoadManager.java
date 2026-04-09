package com.kumoasobi.scribble.save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.kumoasobi.scribble.models.GameState;

/**
 * Load the serialized gamestate file and de-serialize it
 * 
 * @version 1.0
 * @author Yicheng Ying
 */
public class LoadManager {
    private static FileInputStream fis;
    private static ObjectInputStream ois;
    public static GameState deserializeGameState(String filename) throws ClassNotFoundException, FileNotFoundException, IOException {
        fis = new FileInputStream(filename);
        ois = new ObjectInputStream(fis);
        GameState gameState = (GameState)ois.readObject();
        ois.close();
        return gameState;
    }
}