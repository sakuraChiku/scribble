package com.kumoasobi.scribble.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kumoasobi.scribble.exceptions.GameException;
import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.Direction;
import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.MoveResult;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.models.TileBag;
import com.kumoasobi.scribble.models.WordInfo;
import com.kumoasobi.scribble.rules.scanner.WordScanner;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;
import com.kumoasobi.scribble.rules.validator.BoardValidator;
import com.kumoasobi.scribble.rules.validator.DictValidator;
import com.kumoasobi.scribble.rules.validator.PlayerValidator;

public class GameController {
    private final GameState gs;
    private final Set<String> dict;
    private final boolean running;
    private final GameEndStrategy ges;

    public GameController(GameState gs, Set<String> dict, GameEndStrategy ges) {
        this.gs = gs;
        this.dict = dict;
        running = true;
        this.ges = ges;
    }

    public void startGame() {
        while(running) {
            
        }
    }
    
    public void nextTurn() {
        gs.setCurrentPlayerIndex((gs.getCurrentPlayerIndex()+1) % gs.getPlayers().size());
        gs.setTurns(gs.getTurns() + 1);
    }

    public void endGame() {
        if (ges.isGameOver(gs)) {
            
        }
    }

    public MoveResult validateMove(Move currentMove) {
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        Board currentBoard = gs.getBoard();

        /**
         *First, validate the player and the board
         */
        try {
            PlayerValidator.canMakeWord(currentMove, currentPlayer);
            BoardValidator.validateBoard(currentMove, currentBoard);
        } catch (GameException e) {
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }
        
        /**
         * Second, place the move on the board first in order to scan words
         */
        currentBoard.placeMove(currentMove);

        /**
         * Third, scan all the words using WordScanner from different directions
         */
        Direction currentDir = currentMove.getDirection();
        List<WordInfo> wordInfoList = new ArrayList<>();
            // scan the parallel word first from currentMove.getPlacements().get(0)
        try {
            WordInfo parallelInfo = WordScanner.scanWord(currentBoard, currentMove.getPlacements().get(0), currentDir);
            wordInfoList.add(parallelInfo);
            for (Placement p : currentMove.getPlacements()) {
                WordInfo verticalInfo = WordScanner.scanWord(currentBoard, p, currentDir.flip());
                wordInfoList.add(verticalInfo);
            }
        } catch (GameException e) {
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }

        /**
         * Fourth, remove the placement
         */
        currentBoard.recallMove(currentMove);

        /**
         * Fifth, take out all the words in wordInfoList, and validate them through DictValidator
         */
        try {
            for (WordInfo wi : wordInfoList) {
                DictValidator.isValidWord(wi, dict);
            }
        } catch (GameException e) {
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }

        /**
         * Finally, create a MoveResult
         */
        List<String> words = new ArrayList<>();
        int totalScore = 0;
        for (WordInfo wi : wordInfoList) {
            words.add(wi.getWord());
            totalScore += wi.getScore();
        }
        return new MoveResult(true, totalScore, words, "Valid move");
    }

    public void applyMove(Move currentMove, MoveResult currentMoveResult) {
        Board currentBoard = gs.getBoard();
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        
        if (currentMoveResult.isValidMove()) {
            List<Tile> currentTiles = new ArrayList<>();
            for (Placement p : currentMove.getPlacements()) {
                currentTiles.add(p.getTile());
            }
            currentBoard.placeMove(currentMove); // place the move on the board
            currentPlayer.removeTiles(currentTiles); // remove tiles from the player
        }
    }

    public void addScore(MoveResult currentMoveResult) {
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());

        currentPlayer.addScore(currentMoveResult.getTotalScore());
    }

    public void drawTiles() {
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        TileBag currentBag = gs.getBag();

        currentPlayer.addTiles(currentBag.drawTiles(Player.getRackSize()-currentPlayer.getRack().size()));
    }

    public void refreshTiles() {
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        TileBag currentBag = gs.getBag();

        // clear the tiles in player's rack
        currentBag.flowbackTiles(currentPlayer.getRack());
        currentPlayer.clearRack();
        
        // draw tiles and add them to the rack
        currentPlayer.addTiles(currentBag.drawTiles(Player.getRackSize()-currentPlayer.getRack().size()));
    }

    public void saveGame(String dirPath) {
        
    }

    public void loadGame(String filePath) {

    }
}
