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
        this.isSkipped = false;

        //initialize the rack
        tiles = new ArrayList<>();
        TileBag bag = new TileBag();
        for (int i = 0; i < 7; i++) {
            Tile tile = bag.drawTile();
            tiles.add(tile);
        }
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean isIsSkipped() {
        return isSkipped;
    }
}