package scribble.models;

import java.io.Serializable;

public class Board implements Serializable{
    Cell[][] grid;

    public Board() {
        grid = new Cell[15][15];
    }
}
