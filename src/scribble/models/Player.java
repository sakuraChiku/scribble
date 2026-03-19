package scribble.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
    String name;
    int score;
    ArrayList<Tile> tiles;
    boolean isSkipped;

    public Player(String name) {
        //initialize the basic information of the player
        this.name = name;
        this.score = 0;

        //initialize the rack
        tiles = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            
        }
    }
}