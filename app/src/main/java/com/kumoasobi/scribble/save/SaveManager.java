package com.kumoasobi.scribble.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import com.kumoasobi.scribble.models.GameState;

/**
 * Save the gamestate using date
 * 
 * @version 1.1
 * @author Yicheng Ying, Peixuan Ding
 */
public class SaveManager {

    public static String serializeGameState(GameState gs) {
        Calendar c = Calendar.getInstance();
        String filename = String.format("GameState_%d_%02d_%02d_%02d_%02d_%02d.ser",
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DATE),
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            c.get(Calendar.SECOND));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gs);
            System.out.println("Game saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
        return filename;
    }
}

