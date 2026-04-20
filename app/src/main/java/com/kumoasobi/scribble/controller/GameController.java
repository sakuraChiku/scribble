package com.kumoasobi.scribble.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kumoasobi.scribble.ai.AIMove;
import com.kumoasobi.scribble.ai.AIPlayer;
import com.kumoasobi.scribble.ai.ScrabbleAI;
import com.kumoasobi.scribble.exceptions.GameException;
import com.kumoasobi.scribble.exceptions.ReachMaxRefreshTimes;
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

    private GameState gs;
    private Set<String> dict;
    private GameEndStrategy ges;
    private int consecutiveSkips;

    // ── No-arg constructor for GameWindow usage ──────────────────────────────
    public GameController() {
        consecutiveSkips = 0;
    }

    // ── Full constructor (for direct use with strategies) ────────────────────
    public GameController(GameState gs, Set<String> dict, GameEndStrategy ges) {
        this.gs = gs;
        this.dict = dict;
        this.ges = ges;
        consecutiveSkips = 0;
    }

    // ── Setters used by GameWindow ───────────────────────────────────────────
    public void setGameState(GameState gs)     { this.gs = gs; }
    public void setDict(Set<String> dict)      { this.dict = dict; }
    public void setGameEndStrategy(GameEndStrategy ges) { this.ges = ges; }

    // ── Turn management ──────────────────────────────────────────────────────

    public void nextTurn() {
        gs.setCurrentPlayerIndex((gs.getCurrentPlayerIndex() + 1) % gs.getPlayers().size());
        gs.setTurns(gs.getTurns() + 1);
    }

    /**
     * Returns true when the configured end condition is met,
     * OR when all players have consecutively skipped/refreshed twice each.
     */
    public boolean isGameEnd() {
        if (ges != null && ges.isGameOver(gs)) return true;
        return consecutiveSkips >= gs.getPlayers().size() * 2;
    }

    public void endGame() {
        // placeholder – handled by GameWindow.showGameOver()
    }

    /**
     * Returns true if the current player is an AI.
     * GameWindow uses this to trigger AI thinking automatically.
     */
    public boolean isCurrentPlayerAI() {
        return gs.getPlayers().get(gs.getCurrentPlayerIndex()) instanceof AIPlayer;
    }

    /**
     * Compute and execute the AI's move for the current player.
     * Returns the MoveResult so GameWindow can log it.
     * Returns a "skip" MoveResult if no valid move is found.
     */
    public MoveResult executeAITurn() {
        AIPlayer ai  = (AIPlayer) gs.getPlayers().get(gs.getCurrentPlayerIndex());
        ScrabbleAI engine = new ScrabbleAI(ai.getDifficulty(), dict);

        AIMove best = engine.findBestMove(gs.getBoard(), ai);

        if (best == null) {
            // No valid move found → AI skips
            consecutiveSkips++;
            return new MoveResult(false, 0, new ArrayList<>(), ai.getName() + " has no valid move and skips.");
        }

        Move move = best.getMove();
        MoveResult result = validateMove(move);
        if (!result.isValidMove()) {
            // Fallback safety: if our quick-validate was slightly off
            consecutiveSkips++;
            return new MoveResult(false, 0, new ArrayList<>(), ai.getName() + " could not play. Skipping.");
        }

        applyMove(move, result);
        addScore(result);
        drawTiles();
        consecutiveSkips = 0;
        return result;
    }

    // ── Move validation & application ────────────────────────────────────────

    public MoveResult validateMove(Move currentMove) {
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        Board  currentBoard  = gs.getBoard();

        try {
            PlayerValidator.canMakeWord(currentMove, currentPlayer);
            BoardValidator.validateBoard(currentMove, currentBoard);
        } catch (GameException e) {
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }
        
        currentBoard.placeMove(currentMove);

        Direction currentDir = currentMove.getDirection();
        List<WordInfo> wordInfoList = new ArrayList<>();
        try {
            WordInfo parallelInfo = WordScanner.scanWord(currentBoard, currentMove.getPlacements().get(0), currentDir);
            if (parallelInfo.getWord().length() > 1) {
                wordInfoList.add(parallelInfo);
            }
            for (Placement p : currentMove.getPlacements()) {
                WordInfo verticalInfo = WordScanner.scanWord(currentBoard, p, currentDir.flip());
                if (verticalInfo.getWord().length() > 1) {
                    wordInfoList.add(verticalInfo);
                }
            }
        } catch (GameException e) {
            currentBoard.recallMove(currentMove);
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }

        currentBoard.recallMove(currentMove);

        try {
            for (WordInfo wi : wordInfoList) {
                DictValidator.isValidWord(wi, dict);
            }
        } catch (GameException e) {
            return new MoveResult(false, 0, new ArrayList<>(), "Invalid move: " + e.getMessage());
        }

        List<String> words = new ArrayList<>();
        int totalScore = 0;
        for (WordInfo wi : wordInfoList) {
            words.add(wi.getWord());
            totalScore += wi.getScore();
        }
        return new MoveResult(true, totalScore, words, "Valid move");
    }

    public void applyMove(Move currentMove, MoveResult result) {
        Board  currentBoard  = gs.getBoard();
        Player currentPlayer = gs.getPlayers().get(gs.getCurrentPlayerIndex());

        if (result.isValidMove()) {
            List<Tile> tiles = new ArrayList<>();
            for (Placement p : currentMove.getPlacements()) tiles.add(p.getTile());
            currentBoard.placeMove(currentMove);
            currentPlayer.removeTiles(tiles);
        }
    }

    public void addScore(MoveResult result) {
        gs.getPlayers().get(gs.getCurrentPlayerIndex()).addScore(result.getTotalScore());
    }

    public void drawTiles() {
        Player player = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        TileBag bag = gs.getBag();
        int need = Player.getRackSize() - player.getRack().size();
        if (need > 0) player.addTiles(bag.drawTiles(need));
    }

    public void refreshTiles() throws GameException {
        Player player = gs.getPlayers().get(gs.getCurrentPlayerIndex());
        TileBag bag = gs.getBag();
        if (player.getRemainingRefreshTimes() <= 0) {
            throw new ReachMaxRefreshTimes("You have reached maximun refreshing times!");
        } else {
            player.consumeMaxRefreshTimes();
            bag.flowbackTiles(player.getRack());
            player.clearRack();
            player.addTiles(bag.drawTiles(Player.getRackSize()));
        }
    }

    public void recordSkip() { consecutiveSkips++; }

    public void resetConsecutiveSkips() { consecutiveSkips = 0; }
}
