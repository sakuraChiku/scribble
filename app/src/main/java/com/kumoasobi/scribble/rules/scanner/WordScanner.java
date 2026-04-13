package com.kumoasobi.scribble.rules.scanner;

import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.Direction;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.WordInfo;

/**
 * Scan the cells on the board (including existing cells) and calculate its score at the same time
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class WordScanner {
    /**
     * Scan the word from a specific direction and return the word and its score
     * @param board
     * @param row
     * @param col
     * @param dir
     * @return new WordInfo
     */
    public static WordInfo scanWord(Board board, Placement p, Direction dir) {
        int row = p.getRow();
        int col = p.getCol();
        StringBuilder neg = new StringBuilder();
        StringBuilder pos = new StringBuilder();
        int wordMultiplier = 1;
        int wordScore = 0;

        // scan left words for HORIZONTAL and upper words for VERTICAL
        int r = row - dir.getDy();
        int c = col - dir.getDx();
        while (board.hasTile(r, c)) {
            // get the letter on board (r, c)
            neg.append(board.getTile(r, c).getLetter());

            // get the score on board (r, c)
            int letterScore = board.getTile(r, c).getScore();
            if (!board.getTile(r, c).isBonusUsed()) { // if the bonus is not used
                switch (board.getCell(r, c).getBonus()) {
                    case NONE -> letterScore *= 1;
                    case DL -> letterScore *= 2;
                    case TL -> letterScore *= 3;
                    case DW -> wordMultiplier *= 2;
                    case TW -> wordMultiplier *= 3;
                }
            }
            wordScore += letterScore;
            r -= dir.getDy(); 
            c -= dir.getDx(); // switch to the cell next to it
        }
        
        // scan right words for HORIZONTAL and lower words for VERTICAL
        r = row + dir.getDy();
        c = col + dir.getDx();
        while (board.hasTile(r, c)) {
            pos.append(board.getTile(r, c).getLetter());
            int letterScore = board.getTile(r, c).getScore();
            if (!board.getTile(r, c).isBonusUsed()) {
                switch (board.getCell(r, c).getBonus()) {
                    case NONE -> letterScore *= 1;
                    case DL -> letterScore *= 2;
                    case TL -> letterScore *= 3;
                    case DW -> wordMultiplier *= 2;
                    case TW -> wordMultiplier *= 3;
                }
            }
            wordScore += letterScore;
            r += dir.getDy();
            c += dir.getDx();
        }

        // do not forget the center score
        int centerScore = board.getTile(row, col).getScore();
        if (!board.getTile(row, col).isBonusUsed()) {
            switch (board.getCell(row, col).getBonus()) {
                case NONE -> centerScore *= 1;
                case DL -> centerScore *= 2;
                case TL -> centerScore *= 3;
                case DW -> wordMultiplier *= 2;
                case TW -> wordMultiplier *= 3;
            }
        }
        wordScore += centerScore;

        //finally apply wordMultiplier to wordScore
        wordScore *= wordMultiplier;

        // piece the word together, including the negative side (left & up), the letter itself, and the positive side (right & down)
        String word = neg.reverse().toString() + board.getTile(row, col).getLetter() + pos.toString();

        return new WordInfo(word, wordScore);
    }
}