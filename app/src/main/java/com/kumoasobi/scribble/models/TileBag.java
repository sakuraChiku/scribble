package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.kumoasobi.scribble.rules.strategy.DrawStrategy;

/**
 * Put 100 tiles into the bag and shuffle them, with methods to draw tiles and check if the bag is empty
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class TileBag implements Serializable {
    // initialize 100 letters in the bag
    final private List<Tile> letterPool;
    final private DrawStrategy strategy;

    public TileBag(DrawStrategy strategy) {
        // initialize the tilepool
        letterPool = new LinkedList<>();
        this.strategy = strategy;

        // add tiles to the bag
        //add 12 e tiles to pool
        for (int i = 0; i < 12; i++) {
            letterPool.add(new Tile('E', 1));
        }

        //add 9 a and i tiles
        for (int i = 0; i < 9; i++) {
            letterPool.add(new Tile('A', 1));
            letterPool.add(new Tile('I', 1));
        }

        //add 8 o tiles
        for (int i = 0; i < 8; i++) {
            letterPool.add(new Tile('O', 1));
        }

        //add 6 n,r and t tiles
        for (int i = 0; i < 6; i++) {
            letterPool.add(new Tile('N', 1));
            letterPool.add(new Tile('R', 1));
            letterPool.add(new Tile('T', 1));
        }

        //add 4 l,s,u and d tiles
        for (int i = 0; i < 4; i++) {
            letterPool.add(new Tile('L', 1));
            letterPool.add(new Tile('S', 1));
            letterPool.add(new Tile('U', 1));
            letterPool.add(new Tile('D', 2));
        }

        //add 3 g tiles
        for (int i = 0; i < 3; i++) {
            letterPool.add(new Tile('G', 2));
        }

        //add 2 b,c,m,p,f,h,v,w,y and blank tiles
        for (int i = 0; i < 2; i++) {
            letterPool.add(new Tile('B', 3));
            letterPool.add(new Tile('C', 3));
            letterPool.add(new Tile('M', 3));
            letterPool.add(new Tile('P', 3));
            letterPool.add(new Tile('F', 4));
            letterPool.add(new Tile('H', 4));
            letterPool.add(new Tile('V', 4));
            letterPool.add(new Tile('W', 4));
            letterPool.add(new Tile('Y', 4));
            letterPool.add(new Tile('?', 0));
        }

        //add 1 k,j,x,q and z tiles
        letterPool.add(new Tile('K', 5));
        letterPool.add(new Tile('J', 8));
        letterPool.add(new Tile('X', 8));
        letterPool.add(new Tile('Q', 10));
        letterPool.add(new Tile('Z', 10));

        // shuffle the letterpool
        Collections.shuffle(letterPool);
    } // initialization complete

    public int tilesRemaining() {
        return letterPool.size();
    }

    public List<Tile> drawTiles(int n){
        // return a list of random Tiles from index 0-99
        List<Tile> tiles = new ArrayList<>();
        if (n > letterPool.size()) {
            for (Tile t : letterPool) {
                tiles.add(t); // directly add t, because when n > letterPool.size() happens, it must be limited draw strategy.
            }
        } else {
            for (int i = 0; i < n; i++) {
                tiles.add(strategy.draw(this));
            }
        }
        return tiles;
    }

    public void flowbackTiles(List<Tile> playerRack) {
        strategy.flowback(this, playerRack);
        Collections.shuffle(letterPool);
    }

    // Game controller will refill the tiles for the player

    public List<Tile> getLetterPool() {
        return letterPool;
    }
}
