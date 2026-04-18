package com.kumoasobi.scribble.save;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.kumoasobi.scribble.models.GameState;

/**
 * Load the serialized gamestate file and de-serialize it
 * 
 * @version 1.1
 * @author Yicheng Ying, Peixuan Ding
 */
public class LoadManager {
    public static GameState deserializeGameState(String filename)
            throws ClassNotFoundException, IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameState) ois.readObject();
        }
    }
}