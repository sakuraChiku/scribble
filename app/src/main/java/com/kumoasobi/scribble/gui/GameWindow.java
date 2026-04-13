package com.kumoasobi.scribble.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import com.kumoasobi.scribble.controller.GameController;
import com.kumoasobi.scribble.controller.MenuController;
import com.kumoasobi.scribble.models.*;
import com.kumoasobi.scribble.rules.config.GameConfig;
import com.kumoasobi.scribble.rules.config.GameConfigRequest;
import com.kumoasobi.scribble.save.LoadManager;
import com.kumoasobi.scribble.save.SaveManager;

/**
 * Main application window.
 *
 * Flow:
 *   MenuUI → "New Game" → ConfigUI collects GameConfigRequest
 *            → MenuController.submitRequest(request) → GameConfig
 *            → MenuController.newGame(config)         → GameState
 *            → GameController wired up
 *            → GameWindow shows game screen
 */
public class GameWindow extends JFrame {

    private static final Color BG = new Color(45, 35, 22);

    // ── Layout ───────────────────────────────────────────────────────────────
    private final CardLayout cards = new CardLayout();
    private final JPanel     root  = new JPanel(cards);

    // Menu screen
    private MenuUI menuUI;

    // Game screen – created once, reused every turn
    private JPanel       gamePanel;
    private BoardPanel   boardPanel;
    private RackPanel    rackPanel;
    private ControlPanel controlPanel;

    // ── Game state ───────────────────────────────────────────────────────────
    private GameController gameController;
    private GameState      gameState;
    private Move           currentMove;
    private Tile           selectedTile;
    private Set<String>    dictionary = new HashSet<>();

    // ── Constructor ──────────────────────────────────────────────────────────
    public GameWindow() {
        super("Scrabble");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setContentPane(root);

        buildMenu();
        buildGameScreen();

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

    // ── Game screen (built once) ──────────────────────────────────────────────
    private void buildGameScreen() {
        currentMove  = new Move();
        boardPanel   = new BoardPanel(null, currentMove);
        rackPanel    = new RackPanel();
        controlPanel = new ControlPanel();

        rackPanel.setTileSelectListener((tile, idx) -> selectedTile = tile);
        boardPanel.setCellClickListener(this::onBoardClick);

        controlPanel.addSubmitListener(e  -> onSubmit());
        controlPanel.addRecallListener(e  -> onRecall());
        controlPanel.addSkipListener(e    -> onSkip());
        controlPanel.addShuffleListener(e -> onShuffle());

        gamePanel = new JPanel(new BorderLayout(8, 8));
        gamePanel.setBackground(BG);
        gamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerCol = new JPanel(new BorderLayout(0, 8));
        centerCol.setBackground(BG);

        JScrollPane boardScroll = new JScrollPane(boardPanel);
        boardScroll.setBorder(BorderFactory.createLineBorder(new Color(100, 80, 40), 2));
        boardScroll.getViewport().setBackground(BG);
        centerCol.add(boardScroll, BorderLayout.CENTER);

        JPanel rackWrapper = new JPanel(new BorderLayout(0, 4));
        rackWrapper.setBackground(BG);
        JLabel rackLabel = new JLabel(
            "Your Tiles  (click to select, click again to deselect)",
            SwingConstants.CENTER);
        rackLabel.setForeground(new Color(180, 155, 100));
        rackLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rackWrapper.add(rackLabel, BorderLayout.NORTH);
        rackWrapper.add(rackPanel, BorderLayout.CENTER);
        centerCol.add(rackWrapper, BorderLayout.SOUTH);

        gamePanel.add(centerCol, BorderLayout.CENTER);
        controlPanel.setPreferredSize(new Dimension(260, 0));
        gamePanel.add(controlPanel, BorderLayout.EAST);

        root.add(gamePanel, "GAME");
    }

    /** Called every time we enter the game screen (new game or load). */
    private void activateGameScreen() {
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

        setMinimumSize(new Dimension(960, 740));
        pack();
        setLocationRelativeTo(null);
        cards.show(root, "GAME");
        refreshDisplay();
    }

    private void returnToMenu() {
        controlPanel.stopClock();
        setJMenuBar(null);
        setMinimumSize(new Dimension(500, 450));
        cards.show(root, "MENU");
        pack();
        setLocationRelativeTo(null);
    }

    // ── New game / load ───────────────────────────────────────────────────────

    private void startNewGame() {
        // 1. Show configuration dialog
        ConfigUI configDialog = new ConfigUI(this);
        configDialog.setVisible(true);
        GameConfigRequest request = configDialog.getRequest();
        if (request == null) return;   // user cancelled

        // 2. Build GameConfig via factory
        MenuController mc = new MenuController();
        GameConfig config = mc.submitRequest(request);

        // 3. Create GameState (tiles already dealt inside newGame)
        gameState  = mc.newGame(config);
        dictionary = mc.loadDictionary("words.txt");

        // 4. Wire up GameController
        gameController = new GameController(gameState, dictionary, config.endStrategy);

        // 5. Reset UI state
        currentMove  = new Move();
        selectedTile = null;
        boardPanel.setBoard(gameState.getBoard());
        boardPanel.setCurrentMove(currentMove);
        controlPanel.clearLog();

        activateGameScreen();
        controlPanel.startClock(gameState.getStartTime());
        controlPanel.log("Game started! " + gameState.getPlayers().size() + " players.");
        controlPanel.log("First word must cover the centre square (row 8, col 8).");
    }

    private void loadGame() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load saved game (.ser)");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            gameState      = LoadManager.deserializeGameState(fc.getSelectedFile().getAbsolutePath());
            dictionary     = new MenuController().loadDictionary("words.txt");
            gameController = new GameController();
            gameController.setGameState(gameState);
            gameController.setDict(dictionary);

            currentMove  = new Move();
            selectedTile = null;
            boardPanel.setBoard(gameState.getBoard());
            boardPanel.setCurrentMove(currentMove);
            controlPanel.clearLog();

            activateGameScreen();
            controlPanel.startClock(gameState.getStartTime());
            controlPanel.log("Game loaded.");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Turn display ──────────────────────────────────────────────────────────

    private void refreshDisplay() {
        Player cur = currentPlayer();
        boardPanel.repaint();
        rackPanel.setRack(new ArrayList<>(cur.getRack()));
        controlPanel.setCurrentPlayer(cur.getName());
        controlPanel.updateScores(gameState.getPlayers(), gameState.getCurrentPlayerIndex());
        controlPanel.setTilesRemaining(gameState.getBag().tilesRemaining());
        controlPanel.setTurn(gameState.getTurns());
        selectedTile = null;
        rackPanel.clearSelection();
    }

    private void refreshRackDuringPlacement() {
        List<Tile> all = new ArrayList<>(currentPlayer().getRack());
        for (Placement p : currentMove.getPlacements()) all.remove(p.getTile());
        rackPanel.setRack(all);
    }

    private Player currentPlayer() {
        return gameState.getPlayers().get(gameState.getCurrentPlayerIndex());
    }

    // ── Game actions ──────────────────────────────────────────────────────────

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
        if (tileToPlace.getLetter() == '?') {
            String input = (String) JOptionPane.showInputDialog(
                this, "Enter letter for blank tile:", "Blank Tile",
                JOptionPane.PLAIN_MESSAGE, null, null, "A");
            if (input == null || input.isBlank()) return;
            char letter = input.trim().toUpperCase().charAt(0);
            if (letter < 'A' || letter > 'Z') { controlPanel.log("Invalid letter."); return; }
            tileToPlace = new Tile(letter, 0);
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
        currentMove.inferDirection();

        MoveResult result = gameController.validateMove(currentMove);
        if (!result.isValidMove()) {
            controlPanel.log("❌  " + result.getInfo());
            return;
        }

        gameController.applyMove(currentMove, result);
        gameController.addScore(result);

        controlPanel.log("✅  " + currentPlayer().getName() + ": "
            + result.getWords() + "  +" + result.getTotalScore() + " pts");

        gameController.drawTiles();
        gameController.nextTurn();

        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        if (gameController.isGameEnd()) { showGameOver(); return; }

        refreshDisplay();
        controlPanel.log("— " + currentPlayer().getName() + "'s turn —");
    }

    private void onRecall() {
        if (currentMove.isEmpty()) { controlPanel.log("Nothing to recall."); return; }
        currentMove.recallPlacement();
        refreshRackDuringPlacement();
        boardPanel.repaint();
        controlPanel.log("Last tile recalled.");
    }

    private void onSkip() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Skip your turn?", "Skip Turn", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        gameController.recordSkip();
        gameController.nextTurn();

        if (gameController.isGameEnd()) { showGameOver(); return; }

        refreshDisplay();
        controlPanel.log(currentPlayer().getName() + " skipped their turn.");
    }

    private void onShuffle() {
        Player cur  = currentPlayer();
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
        JOptionPane.showMessageDialog(this, "Game saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Game over ─────────────────────────────────────────────────────────────
    private void showGameOver() {
        controlPanel.stopClock();
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

        JOptionPane.showMessageDialog(this, msg.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        returnToMenu();
    }
}
