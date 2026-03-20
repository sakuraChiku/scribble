package scribble.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable{
    private String name;
    private int score;
    private ArrayList<Tile> rack;
    boolean isSkipped;
    TileBag bag = new TileBag();

    public Player(String name) {
        //initialize the basic information of the player
        this.name = name;
        this.score = 0;
        this.isSkipped = false;

        //initialize the rack
        rack = new ArrayList<>();
    }

    public ArrayList<Tile> getRack() {
        return rack;
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

    public void addTiles(List<Tile> tiles) {
        rack.addAll(tiles);
    }
}