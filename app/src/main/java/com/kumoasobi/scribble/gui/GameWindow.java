package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.kumoasobi.scribble.ai.AIDifficulty;
import com.kumoasobi.scribble.ai.AIPlayer;
import com.kumoasobi.scribble.controller.GameController;
import com.kumoasobi.scribble.controller.MenuController;
import com.kumoasobi.scribble.exceptions.GameException;
import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.MoveResult;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.rules.config.GameConfig;
import com.kumoasobi.scribble.rules.config.GameConfigRequest;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;
import com.kumoasobi.scribble.save.LoadManager;
import com.kumoasobi.scribble.save.SaveManager;
import com.kumoasobi.scribble.util.SoundManager;

/**
 * Main application window.
 *
 * Flow:
 *   MenuUI → "New Game" → ConfigUI collects GameConfigRequest
 *            → MenuController.submitRequest(request) → GameConfig
 *            → MenuController.newGame(config)         → GameState
 *            → GameController wired up
 *            → GameWindow shows game screen
 * 
 * @author Peixuan Ding, Yutong Xiao, Yicheng Ying
 */
public class GameWindow extends JFrame {

    private static final Color BG = new Color(190, 217, 180);

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

    // ── Chat panel (null when no AI opponent) ───────────────────────────────
    private CharacterChatPanel chatPanel;
    private JPanel             chatWrapper;   // holds the chat panel in the layout

    // ── Game state ───────────────────────────────────────────────────────────
    private GameController  gameController;
    private GameState       gameState;
    private Move            currentMove;
    private Tile            selectedTile;
    private Set<String>     dictionary = new HashSet<>();
    private GameEndStrategy endStrategy;

    // ── Constructor ──────────────────────────────────────────────────────────
    public GameWindow() {
        super("Scribble");
        SoundManager.init();
        SoundManager.playMenuBGM();
        SoundManager.playSenren();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setContentPane(root);

        buildMenu();
        buildGameScreen();

        pack();
        setMinimumSize(new Dimension(960, 540));
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() { // close the music when quitting game
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                SoundManager.shutdown();
            }
        });
    }

    // ── Menu ─────────────────────────────────────────────────────────────────
    private void buildMenu() {
        menuUI = new MenuUI();
        menuUI.addNewGameListener(e -> startNewGame());
        menuUI.addLoadGameListener(e -> loadGame());
        menuUI.addIntroListener(e -> enterInst());
        menuUI.addQuitListener(e -> quitGame());
        root.add(menuUI, "MENU");
        cards.show(root, "MENU");
    }

    // ── Game screen (built once) ──────────────────────────────────────────────
    private void buildGameScreen() {
        currentMove  = new Move();
        boardPanel   = new BoardPanel(null, currentMove);
        rackPanel    = new RackPanel();
        controlPanel = new ControlPanel();
        controlPanel.setBackground(new Color(255, 254, 248));

        rackPanel.setTileSelectListener((tile, idx) -> selectedTile = tile);
        boardPanel.setCellClickListener(this::onBoardClick);

        controlPanel.addSubmitListener(e  -> onSubmit());
        controlPanel.addRecallListener(e  -> onRecall());
        controlPanel.addSkipListener(e    -> onSkip());
        controlPanel.addShuffleListener(e -> onRefresh());

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
        rackLabel.setForeground(new Color(255, 254, 248));
        rackLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rackWrapper.add(rackLabel, BorderLayout.NORTH);
        rackWrapper.add(rackPanel, BorderLayout.CENTER);
        centerCol.add(rackWrapper, BorderLayout.SOUTH);
        
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        topBar.setBackground(new Color(255, 254, 248));

        JButton saveBtn  = makeTopBarBtn("Save");
        JButton menuBtn  = makeTopBarBtn("Main Menu");
        JButton quitBtn2 = makeTopBarBtn("Quit");

        saveBtn.addActionListener(e  -> onSave());
        menuBtn.addActionListener(e  -> returnToMenu());
        quitBtn2.addActionListener(e -> quitGame());

        topBar.add(saveBtn);
        topBar.add(menuBtn);
        topBar.add(quitBtn2);

        gamePanel.add(topBar, BorderLayout.NORTH);

        gamePanel.add(centerCol, BorderLayout.CENTER);
        controlPanel.setPreferredSize(new Dimension(260, 0));
        controlPanel.setOpaque(true);
        gamePanel.add(controlPanel, BorderLayout.EAST);

        // ── Chat panel placeholder (populated when game starts) ───────────────
        chatWrapper = new JPanel(new BorderLayout());
        chatWrapper.setBackground(BG);
        chatWrapper.setPreferredSize(new Dimension(220, 400));
        chatWrapper.setMinimumSize(new Dimension(220, 200));
        chatWrapper.setVisible(false);
        gamePanel.add(chatWrapper, BorderLayout.WEST);

        root.add(gamePanel, "GAME");
    }

    /** Called every time we enter the game screen (new game or load). */
    private void activateGameScreen() {
        
        SoundManager.stopMenuBGM();
        SoundManager.playGameBGM();

        setMinimumSize(new Dimension(960, 740));
        pack();
        gamePanel.revalidate();
        gamePanel.repaint();
        setLocationRelativeTo(null);
        cards.show(root, "GAME");
        refreshDisplay();
    }

    private void returnToMenu() {
        SoundManager.playDecide();
        SoundManager.stopGameBGM();
        SoundManager.playMenuBGM();

        controlPanel.stopClock();
        // Hide and clear chat panel
        chatWrapper.removeAll();
        chatWrapper.setVisible(false);
        chatPanel = null;

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
        String resourcePath = "/assets/dict/stan_dict.txt";
        if (request == null) return;   // user cancelled

        // 2. Build GameConfig via factory
        MenuController mc = new MenuController();
        GameConfig config = mc.submitRequest(request);

        // 3. Create GameState (tiles already dealt inside newGame)
        gameState  = mc.newGame(config);
        try {
            dictionary = mc.loadDictionary(resourcePath);
        } catch (Exception e) {
            controlPanel.log("Failed to load at " + resourcePath + ". Please check your dictionary path again!");
        }

        // 4. Wire up GameController
        gameController = new GameController(gameState, dictionary, config.getEndStrategy());

        // 5. Reset UI state
        currentMove  = new Move();
        selectedTile = null;
        boardPanel.setBoard(gameState.getBoard());
        boardPanel.setCurrentMove(currentMove);
        controlPanel.clearLog();

        // 6. Set up character chat panel for the first AI opponent found
        setupChatPanel();

        activateGameScreen();
        controlPanel.startClock(gameState.getStartTime());
        controlPanel.log("Game started! " + gameState.getPlayers().size() + " players.");
        controlPanel.log("Successfully loaded dictionary at " + resourcePath + ".");
        controlPanel.log("First word must cover the centre square (row 8, col 8).");
        checkAndRunAITurn();
    }

    private void loadGame() {

        SoundManager.playLoad();

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load saved game (.ser)");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            SoundManager.playCancel();
            return;
        }
        SoundManager.playStart();
        try {
            gameState      = LoadManager.deserializeGameState(fc.getSelectedFile().getAbsolutePath());
            dictionary     = new MenuController().loadDictionary("/assets/dict/stan_dict.txt");
            endStrategy    = gameState.getEndStrategy();

            gameController = new GameController();
            gameController.setGameState(gameState);
            gameController.setDict(dictionary);
            gameController.setGameEndStrategy(endStrategy);

            currentMove  = new Move();
            selectedTile = null;
            boardPanel.setBoard(gameState.getBoard());
            boardPanel.setCurrentMove(currentMove);
            controlPanel.clearLog();

            setupChatPanel();
            
            activateGameScreen();
            controlPanel.startClock(gameState.getStartTime());
            controlPanel.log("Game loaded.");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to load: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            SoundManager.playCancel();
        }
    }

    private void enterInst() {
        SoundManager.playInst();
        new IntroductionDialog(this).setVisible(true);
    }

    private void quitGame() {
        SoundManager.stopMenuBGM();
        SoundManager.stopGameBGM();
        SoundManager.playQuit();
        dispose();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }

    // ── Chat panel setup ──────────────────────────────────────────────────────

    /**
     * Finds the first AIPlayer in the game and creates the matching character
     * chat panel.  Safe to call even when there are no AI players.
     */
    private void setupChatPanel() {
        chatWrapper.removeAll();
        chatPanel = null;

        List<AIDifficulty> diffs = gameState.getPlayers().stream()
            .filter(p -> p instanceof com.kumoasobi.scribble.ai.AIPlayer)
            .map(p -> ((com.kumoasobi.scribble.ai.AIPlayer) p).getDifficulty())
            .toList();

        chatPanel = CharacterChatPanel.createForDifficulties(diffs);
        if (chatPanel != null) {
            chatWrapper.add(chatPanel, BorderLayout.CENTER);
            chatWrapper.setVisible(true);
        } else {
            chatWrapper.setVisible(false);
        }
        chatWrapper.revalidate();
        chatWrapper.repaint();
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

    private JButton makeTopBarBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(new Color(120, 140, 150));
        b.setBackground(new Color(60, 45, 25));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    // ── Game actions ──────────────────────────────────────────────────────────

    private void onBoardClick(int row, int col) {
        SoundManager.playSelect();
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
            if (input.length() != 1) { controlPanel.log("Please input only one letter."); return; }
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
            SoundManager.playCancel();
            controlPanel.log("Place at least one tile before submitting.");
            return;
        }
        currentMove.inferDirection();

        MoveResult result = gameController.validateMove(currentMove);
        if (!result.isValidMove()) {
            SoundManager.playCancel();
            controlPanel.log("❌  " + result.getInfo());
            return;
        }
        
        SoundManager.playSuccess();

        gameController.applyMove(currentMove, result);
        gameController.addScore(result);

        controlPanel.log("✅  " + currentPlayer().getName() + ": "
            + result.getWords() + "  +" + result.getTotalScore() + " pts");

        // Notify chat character about the player's scoring move
        if (chatPanel != null) {
            chatPanel.notifyGameEvent("the human player just scored "
                + result.getTotalScore() + " points with: " + result.getWords());
        }

        gameController.drawTiles();
        gameController.nextTurn();

        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        if (gameController.isGameEnd()) { showGameOver(); return; }

        refreshDisplay();
        controlPanel.log("— " + currentPlayer().getName() + "'s turn —");
        checkAndRunAITurn();
    }

    private void onRecall() {
        SoundManager.playCancel();
        if (currentMove.isEmpty()) {controlPanel.log("Nothing to recall."); return; }
        currentMove.recallPlacement();
        refreshRackDuringPlacement();
        boardPanel.repaint();
        controlPanel.log("Last tile recalled.");
    }

    private void onSkip() {
        SoundManager.playSelect();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Skip your turn?", "Skip Turn", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            SoundManager.playCancel();
            return;
        }
        SoundManager.playDecide();
        currentMove = new Move();
        boardPanel.setCurrentMove(currentMove);

        refreshDisplay();
        controlPanel.log(currentPlayer().getName() + " skipped their turn.");

        gameController.recordSkip();
        gameController.nextTurn();

        if (gameController.isGameEnd()) { showGameOver(); return; }

        refreshDisplay();
        controlPanel.log("— " + currentPlayer().getName() + "'s turn —");

        checkAndRunAITurn();
    }

    private void onRefresh() {
        SoundManager.playSelect();
        Player currentPlayer = currentPlayer();
        while (!currentMove.isEmpty()) {
            currentMove.recallPlacement();
        }
        refreshRackDuringPlacement();
        boardPanel.repaint();
        try {
            gameController.refreshTiles();
            refreshRackDuringPlacement();
            controlPanel.log("Rack refreshed! " + currentPlayer.getRemainingRefreshTimes() + " refresh times left.");
        } catch (GameException e) {
            controlPanel.log(e.getMessage());
        }
    }

    private void onSave() {
        SoundManager.playSuccess();
        Calendar c = Calendar.getInstance();
        String filename = String.format("GameState_%d_%02d_%02d_%02d_%02d_%02d.ser",
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH) + 1,
        c.get(Calendar.DATE),
        c.get(Calendar.HOUR_OF_DAY),
        c.get(Calendar.MINUTE),
        c.get(Calendar.SECOND));

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Game");
        fc.setSelectedFile(new java.io.File(filename));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Scribble Save Files (*.ser)", "ser"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fc.getSelectedFile();

        SaveManager.serializeGameState(gameState, file.getAbsolutePath());
        controlPanel.log("Game saved to: " + file.getName());
        JOptionPane.showMessageDialog(this, "Game saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── AI turn trigger ──────────────────────────────────────────────────────────
    /**
     * If the current player is an AI, schedule its turn on a background thread
     * so the UI doesn't freeze. Shows a brief "thinking..." message.
     */

    private void checkAndRunAITurn() {
        if (!gameController.isCurrentPlayerAI()) return;
        AIPlayer ai = (AIPlayer)currentPlayer();

        // use swingworker
        controlPanel.log("— " + ai.getName() + " is thinking… —");
        new javax.swing.SwingWorker<MoveResult, Void>() {
            @Override
            protected MoveResult doInBackground() {
                return gameController.executeAITurn();
            }

            @Override
            protected void done() {
                try {
                    MoveResult result = get();
                    if (result.isValidMove()) {
                        controlPanel.log("(AI)  " + currentPlayer().getName()
                            + ": " + result.getWords()
                            + "  +" + result.getTotalScore() + " pts");
                        // Spontaneous chat reaction to AI's own good move
                        if (chatPanel != null) {
                            chatPanel.notifyGameEvent("you (the AI character) just scored "
                                + result.getTotalScore() + " points with: " + result.getWords()
                                + " — react triumphantly in 1 short sentence");
                        }
                    } else {
                        controlPanel.log("(AI)  " + result.getInfo());
                    }

                    gameController.nextTurn();
                    currentMove = new Move();
                    boardPanel.setCurrentMove(currentMove);

                    if (gameController.isGameEnd()) { showGameOver(); return; }

                    refreshDisplay();
                    controlPanel.log("— " + currentPlayer().getName() + "'s turn —");

                    checkAndRunAITurn();

                } catch (InterruptedException | ExecutionException e) {
                    controlPanel.log("AI error: " + e.getMessage());
                }
            }
        }.execute();
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
        SoundManager.playDecide();
        returnToMenu();
    }
}