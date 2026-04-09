package com.kumoasobi.scribble.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import com.kumoasobi.scribble.models.GameState;

/**
 * Save the gamestate using date
 * 
 * @version 1.0
 * @author Yicheng Ying
 */
public class SaveManager {
    private static FileOutputStream fos;
    private static ObjectOutputStream oos;
    private static String filename;
    public static void serializeGameState(GameState gs) {
        Calendar c = Calendar.getInstance();
        filename = "GameState"+"_"+c.get(Calendar.YEAR)+"_"+c.get(Calendar.MONTH)+"_"+c.get(Calendar.DATE)+"_"+c.get(Calendar.HOUR_OF_DAY)+"_"+c.get(Calendar.MINUTE)+"_"+c.get(Calendar.SECOND)+".ser";
        try {
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(gs);
            oos.close();
        } catch (IOException e) {
        }
    }
}
