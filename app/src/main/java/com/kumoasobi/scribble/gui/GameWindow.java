package com.kumoasobi.scribble.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import com.kumoasobi.scribble.controller.*;
import com.kumoasobi.scribble.rules.*;
import com.kumoasobi.scribble.models.*;
import com.kumoasobi.scribble.save.*;

/**
 * Main application window.
 * Uses a CardLayout to switch between MENU and GAME screens.
 * The game screen keeps a stable layout; only the board's data model changes
 * between turns (no panel replacement needed).
 */
public class GameWindow extends JFrame {

    private static final Color BG = new Color(45, 35, 22);

    // ── Layout ───────────────────────────────────────────────────────────────
    private final CardLayout cards   = new CardLayout();
    private final JPanel     root    = new JPanel(cards);

    // Menu screen
    private MenuUI menuUI;

    // Game screen – created once, reused every turn
    private JPanel       gamePanel;
    private BoardPanel   boardPanel;
    private RackPanel    rackPanel;
    private ControlPanel controlPanel;
    private JScrollPane  boardScroll;

    // ── Game state ──────────────────────────────────────────────────────────
    private GameController gameController;
    private GameState      gameState;
    private Move           currentMove;
    private Tile           selectedTile;
    private Set<String>    dictionary = new HashSet<>();

    // ── Constructor ─────────────────────────────────────────────────────────

    public GameWindow() {
        super("Scrabble");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setContentPane(root);

        buildMenu();
        buildGameScreen();   // build once, show only when a game starts

        pack();
        setMinimumSize(new Dimension(500, 450));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Menu ─────────────────────────────────────────────────────────────────

    private void buildMenu() {
        menuUI = new MenuUI();
        menuUI.addNewGameListener(e -> startNewGame());
        menuUI.addLoadGameListener(e -> loadGame());
        menuUI.addQuitListener(e -> System.exit(0));
        root.add(menuUI, "MENU");
        cards.show(root, "MENU");
    }

    // ── Game screen layout (built once) ─────────────────────────────────────

    private void buildGameScreen() {
        currentMove  = new Move();
        boardPanel   = new BoardPanel(null, currentMove);   // board set later
        rackPanel    = new RackPanel();
        controlPanel = new ControlPanel();

        // Tile selection
        rackPanel.setTileSelectListener((tile, idx) -> selectedTile = tile);

        // Board clicks
        boardPanel.setCellClickListener(this::onBoardClick);

        // Control buttons
        controlPanel.addSubmitListener(e  -> onSubmit());
        controlPanel.addRecallListener(e  -> onRecall());
        controlPanel.addSkipListener(e    -> onSkip());
        controlPanel.addShuffleListener(e -> onShuffle());

        // Layout
        gamePanel = new JPanel(new BorderLayout(8, 8));
        gamePanel.setBackground(BG);
        gamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Centre column: board + rack
        JPanel centerCol = new JPanel(new BorderLayout(0, 8));
        centerCol.setBackground(BG);

        boardScroll = new JScrollPane(boardPanel);
        boardScroll.setBorder(BorderFactory.createLineBorder(new Color(100, 80, 40), 2));
        boardScroll.getViewport().setBackground(BG);
        centerCol.add(boardScroll, BorderLayout.CENTER);

        JPanel rackWrapper = new JPanel(new BorderLayout(0, 4));
        rackWrapper.setBackground(BG);
        JLabel rackLabel = new JLabel("Your Tiles  (click to select, click again to deselect)",
                                      SwingConstants.CENTER);
        rackLabel.setForeground(new Color(180, 155, 100));
        rackLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rackWrapper.add(rackLabel, BorderLayout.NORTH);
        rackWrapper.add(rackPanel, BorderLayout.CENTER);
        centerCol.add(rackWrapper, BorderLayout.SOUTH);

        gamePanel.add(centerCol, BorderLayout.CENTER);
        controlPanel.setPreferredSize(new Dimension(250, 0));
        gamePanel.add(controlPanel, BorderLayout.EAST);

        root.add(gamePanel, "GAME");
    }

    /** Called every time a turn begins (or game is loaded). */
    private void activateGameScreen() {
        // Build/update menu bar
        JMenuBar mb = new JMenuBar();
        mb.setBackground(new Color(30, 22, 14));
        JMenu fileMenu = new JMenu("Game");
        fileMenu.setForeground(new Color(220, 200, 150));

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> onSave());
        JMenuItem menuItem = new JMenuItem("Main Menu");
        menuItem.addActionListener(e -> returnToMenu());
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(menuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);
        mb.add(fileMenu);
        setJMenuBar(mb);

        setMinimumSize(new Dimension(920, 720));
        pack();
        setLocationRelativeTo(null);
        cards.show(root, "GAME");
        refreshDisplay();
    }

    private void returnToMenu() {
        setJMenuBar(null);
        setMinimumSize(new Dimension(500, 450));
        cards.show(root, "MENU");
        pack();
        setLocationRelativeTo(null);
    }

    // ── New game / load game ─────────────────────────────────────────────────

    private void startNewGame() {
        SelectPlayerUI dialog = new SelectPlayerUI(this);
        dialog.setVisible(true);
        List<String> names = dialog.getPlayerNames();
        if (names == null) return;

        MenuController mc  = new MenuController();
        gameState          = mc.newGame(names, new LimitedDrawStrategy());
        dictionary         = mc.loadDictionary("words.txt");

        // Deal initial tiles
        for (Player p : gameState.getPlayers())
            p.addTiles(gameState.getBag().drawTiles(Player.getRackSize()));

        gameController = new GameController();
        gameController.setGameState(gameState);
        gameController.setDict(dictionary);

        currentMove  = new Move();
        selectedTile = null;
        boardPanel.setBoard(gameState.getBoard());
        boardPanel.setCurrentMove(currentMove);

        activateGameScreen();
        controlPanel.log("Game started! " + names.size() + " players.");
        controlPanel.log("First word must cover the centre square (row 8, col 8).");
    }

    private void loadGame() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load saved game (.ser)");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            gameState      = LoadManager.deserializeGameState(fc.getSelectedFile().getAbsolutePath());
            gameController = new GameController();
            gameController.setGameState(gameState);
            gameController.setDict(new MenuController().loadDictionary("words.txt"));

            currentMove  = new Move();
            selectedTile = null;
            boardPanel.setBoard(gameState.getBoard());
            boardPanel.setCurrentMove(currentMove);

            activateGameScreen();
            controlPanel.log("Game loaded.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Turn display ─────────────────────────────────────────────────────────

    private void refreshDisplay() {
        Player cur = currentPlayer();
        boardPanel.repaint();
        rackPanel.setRack(new ArrayList<>(cur.getRack()));
        controlPanel.setCurrentPlayer(cur.getName());
        controlPanel.updateScores(gameState.getPlayers(), gameState.getCurrentPlayerIndex());
        controlPanel.setTilesRemaining(gameState.getBag().tilesRemaining());
        selectedTile = null;
        rackPanel.clearSelection();
    }

    /** Rack shown during placement: exclude tiles already in currentMove */
    private void refreshRackDuringPlacement() {
        List<Tile> all = new ArrayList<>(currentPlayer().getRack());
        for (Placement p : currentMove.getPlacements()) all.remove(p.getTile());
        rackPanel.setRack(all);
    }

    private Player currentPlayer() {
        return gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
    }

    // ── Game actions ─────────────────────────────────────────────────────────

    private void onBoardClick(int row, int col) {
        if (selectedTile == null) {
            controlPanel.log("Select a tile from your rack first.");
            return;
        }
        if (gameState.getBoard().hasTile(row, col)) {
            controlPanel.log("That cell is already occupied.");
            return;
        }
        if (currentMove.hasPlacement(row, col)) {
            controlPanel.log("Tile already placed there — use Recall to undo.");
            return;
        }

        Tile tileToPlace = selectedTile;

        // Blank tile: ask user which letter it represents
        if (tileToPlace.getLetter() == '?') {
            String input = (String) JOptionPane.showInputDialog(
                this, "Enter letter for blank tile:", "Blank Tile",
                JOptionPane.PLAIN_MESSAGE, null, null, "A");
            if (input == null || input.isBlank()) return;
            char letter = input.trim().toUpperCase().charAt(0);
            if (letter < 'A' || letter > 'Z') {
                controlPanel.log("Invalid letter for blank tile.");
                return;
            }
            tileToPlace = new Tile(letter, 0); // blank scores 0
        }

        try {
            currentMove.addPlacement(new Placement(tileToPlace, row, col));
            selectedTile = null;
            rackPanel.clearSelection();
            refreshRackDuringPlacement();
            boardPanel.repaint();
            controlPanel.log("Placed '" + tileToPlace.getLetter() + "' at (" + row + ", " + col + ").");
        } catch (IllegalArgumentException ex) {
            controlPanel.log("Error: " + ex.getMessage());
        }
    }

    private void onSubmit() {
        if (currentMove.isEmpty()) {
            controlPanel.log("Place at least one tile before submitting.");
            return;
        }

        // Direction inference (handles single-tile correctly via BoardValidator)
        currentMove.inferDirection();

        MoveResult result = gameController.validateMove(currentMove);
        if (!result.isValidMove()) {
            controlPanel.log("❌  " + result.getInfo());
            return;
        }

        gameController.applyMove(currentMove, result);
        gameController.addScore(result);

        StringBuilder sb = new StringBuilder();
        sb.append("✅  ").append(currentPlayer().getName()).append(": ");
        sb.append(result.getWords()).append("  +").append(result.getTotalScore()).append(" pts");
        controlPanel.log(sb.toString());

        gameController.drawTiles();
        gameController.nextTurn();

        // Reset move for new turn
        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        if (gameController.isGameEnd()) {
            showGameOver();
            return;
        }

        refreshDisplay();
        controlPanel.log("— " + currentPlayer().getName() + "'s turn —");
    }

    private void onRecall() {
        if (currentMove.isEmpty()) {
            controlPanel.log("Nothing to recall.");
            return;
        }
        currentMove.recallPlacement();
        refreshRackDuringPlacement();
        boardPanel.repaint();
        controlPanel.log("Last tile recalled.");
    }

    private void onSkip() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Skip your turn?", "Skip Turn", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Return any placed-but-not-submitted tiles to rack
        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        gameController.recordSkip();
        gameController.nextTurn();
        refreshDisplay();
        controlPanel.log(currentPlayer().getName() + " skipped their turn.");
    }

    private void onShuffle() {
        Player cur = currentPlayer();
        List<Tile> rack = new ArrayList<>(cur.getRack());
        Collections.shuffle(rack);
        cur.clearRack();
        cur.addTiles(rack);
        refreshRackDuringPlacement();
        controlPanel.log("Rack shuffled.");
    }

    private void onSave() {
        SaveManager.serializeGameState(gameState);
        controlPanel.log("Game saved.");
        JOptionPane.showMessageDialog(this,
            "Game saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Game over ────────────────────────────────────────────────────────────

    private void showGameOver() {
        Player winner = gameState.getPlayers().stream()
            .max(Comparator.comparingInt(Player::getScore))
            .orElse(null);

        StringBuilder msg = new StringBuilder("Game Over!\n\nFinal Scores:\n");
        gameState.getPlayers().stream()
            .sorted(Comparator.comparingInt(Player::getScore).reversed())
            .forEach(p -> msg.append("  ").append(p.getName())
                             .append(":  ").append(p.getScore()).append(" pts\n"));
        if (winner != null)
            msg.append("\n🏆  Winner: ").append(winner.getName()).append("!");

        JOptionPane.showMessageDialog(this, msg.toString(),
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
        returnToMenu();
    }
}
