package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Reflect basic information, rack and score of a player, with some methods to initialize and modify them
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Player implements Serializable{
    private final String name;
    private final UUID id;
    private int score;
    private final List<Tile> rack;
    private static final int RACK_SIZE = 7;

    public Player(String name, UUID id) {
        //initialize the basic information of the player
        this.name = name;
        this.score = 0;
        this.id = id;
        
        //initialize the rack
        rack = new ArrayList<>();
    }
    
    public UUID getId() {
        return id;
    }

    public List<Tile> getRack() {
        return rack;
    }

    public String getName() {
        return name;
    }

    public static int getRackSize() {
        return RACK_SIZE;
    }

    public int getScore() {
        return score;
    }

    public void addTiles(List<Tile> tiles) throws IllegalStateException{
        if (tiles.size() + rack.size() > RACK_SIZE) throw new IllegalStateException("The maximum size of rack is 7!");
        rack.addAll(tiles);
    }

    public void removeTiles(List<Tile> tiles) {
        for (Tile t : tiles) {
            rack.remove(t);
        }
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void clearRack() {
        rack.clear();
    }
}