package com.kumoasobi.scribble.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.kumoasobi.scribble.models.GameState;

/**
 * Save the gamestate using date
 * 
 * @version 1.1
 * @author Yicheng Ying, Peixuan Ding
 */
public class SaveManager {

    public static String serializeGameState(GameState gs, String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(gs);
            System.out.println("Game saved to: " + filepath);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
        return filepath;
    }
}

