package scribble.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class TileBag implements Serializable{
    /**
     * 
     */
    // initialize 100 letters in the bag
    final private ArrayList<Tile> letterPool;

    public TileBag() {
        // Define the score and type of different tile
        Tile aTile = new Tile('A', 1);
        Tile bTile = new Tile('B', 3);
        Tile cTile = new Tile('C', 3);
        Tile dTile = new Tile('D', 2);
        Tile eTile = new Tile('E', 1);
        Tile fTile = new Tile('F', 4);
        Tile gTile = new Tile('G', 2);
        Tile hTile = new Tile('H', 4);
        Tile iTile = new Tile('I', 1);
        Tile jTile = new Tile('J', 8);
        Tile kTile = new Tile('K', 5);
        Tile lTile = new Tile('L', 1);
        Tile mTile = new Tile('M', 3);
        Tile nTile = new Tile('N', 1);
        Tile oTile = new Tile('O', 1);
        Tile pTile = new Tile('P', 3);
        Tile qTile = new Tile('Q', 10);
        Tile rTile = new Tile('R', 1);
        Tile sTile = new Tile('S', 1);
        Tile tTile = new Tile('T', 1);
        Tile uTile = new Tile('U', 1);
        Tile vTile = new Tile('V', 4);
        Tile wTile = new Tile('W', 4);
        Tile xTile = new Tile('X', 8);
        Tile yTile = new Tile('Y', 4);
        Tile zTile = new Tile('Z', 10);
        Tile blankTile = new Tile('?', 0);

        // initialize the tilepool
        letterPool = new ArrayList<>();

        // add tiles to the bag
        //add 12 e tiles to pool
        for (int i = 0; i < 12; i++) {
            letterPool.add(eTile);
        }

        //add 9 a and i tiles
        for (int i = 0; i < 9; i++) {
            letterPool.add(aTile);
            letterPool.add(iTile);
        }

        //add 8 o tiles
        for (int i = 0; i < 8; i++) {
            letterPool.add(oTile);
        }

        //add 6 n,r and t tiles
        for (int i = 0; i < 6; i++) {
            letterPool.add(nTile);
            letterPool.add(rTile);
            letterPool.add(tTile);
        }

        //add 4 l,s,u and d tiles
        for (int i = 0; i < 4; i++) {
            letterPool.add(lTile);
            letterPool.add(sTile);
            letterPool.add(uTile);
            letterPool.add(dTile);
        }

        //add 3 g tiles
        for (int i = 0; i < 3; i++) {
            letterPool.add(gTile);
        }

        //add 2 b,c,m,p,f,h,v,w,y and blank tiles
        for (int i = 0; i < 2; i++) {
            letterPool.add(bTile);
            letterPool.add(cTile);
            letterPool.add(mTile);
            letterPool.add(pTile);
            letterPool.add(fTile);
            letterPool.add(hTile);
            letterPool.add(vTile);
            letterPool.add(wTile);
            letterPool.add(yTile);
            letterPool.add(blankTile);
        }

        //add 1 k,j,x,q and z tiles
        letterPool.add(kTile);
        letterPool.add(jTile);
        letterPool.add(xTile);
        letterPool.add(qTile);
        letterPool.add(zTile);
    } // initialization complete

    public int tilesRemaining() {
        return letterPool.size();
    }

    public Tile drawTile() {
        // return a random Tile from index 0-99
        Random rand = new Random();
        int randomIndex = rand.nextInt(letterPool.size());
        return letterPool.remove(randomIndex);
    }
}
