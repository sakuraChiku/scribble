package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.kumoasobi.scribble.models.Player;

/**
 * Right-side panel: player scores, turn/time info, action log, and game buttons.
 */
public class ControlPanel extends JPanel {

    private static final Color BG         = new Color( 45,  35,  25);
    private static final Color CARD_BG    = new Color( 62,  48,  32);
    private static final Color ACCENT     = new Color(220, 180,  80);
    private static final Color TEXT_LIGHT = new Color(240, 230, 210);
    private static final Color TEXT_DIM   = new Color(160, 145, 120);
    private static final Color BTN_GREEN  = new Color( 60, 150,  70);
    private static final Color BTN_AMBER  = new Color(190, 130,  30);

    private final JLabel currentPlayerLabel;
    private final JTextArea logArea;
    private final JPanel scorePanel;
    private final JLabel tilesRemainingLabel;
    private final JLabel turnLabel;
    private final JLabel timeLabel;
    private final JButton submitBtn, recallBtn, skipBtn, shuffleBtn;

    // Timer for elapsed time display
    private javax.swing.Timer clockTimer;
    private long gameStartTime = 0;

    public ControlPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // ── Top: current player + stats ──────────────────────────────────────
        JPanel topPanel = new JPanel(new BorderLayout(4, 4));
        topPanel.setBackground(BG);

        currentPlayerLabel = new JLabel("Player's Turn", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        currentPlayerLabel.setForeground(ACCENT);
        currentPlayerLabel.setBorder(new EmptyBorder(4, 0, 6, 0));
        topPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(BG);
        topPanel.add(scorePanel, BorderLayout.CENTER);

        // Stats row: turns + time + tiles
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 4, 0));
        statsRow.setBackground(BG);
        statsRow.setBorder(new EmptyBorder(6, 0, 0, 0));

        turnLabel           = statLabel("Turn: 0");
        timeLabel           = statLabel("Time: 0:00");
        tilesRemainingLabel = statLabel("Tiles: 100");

        statsRow.add(turnLabel);
        statsRow.add(timeLabel);
        statsRow.add(tilesRemainingLabel);
        topPanel.add(statsRow, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ── Centre: action log ───────────────────────────────────────────────
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(BG);
        JLabel logTitle = new JLabel("Game Log");
        logTitle.setForeground(TEXT_DIM);
        logTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        logTitle.setBorder(new EmptyBorder(0, 0, 4, 0));
        logPanel.add(logTitle, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 22, 14));
        logArea.setForeground(TEXT_LIGHT);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 60, 30), 1));
        logPanel.add(scroll, BorderLayout.CENTER);
        add(logPanel, BorderLayout.CENTER);

        // ── Bottom: action buttons ───────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        btnPanel.setBackground(BG);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        submitBtn  = makeBtn("✓ Submit",    BTN_GREEN);
        recallBtn  = makeBtn("↩ Recall",    BTN_AMBER);
        skipBtn    = makeBtn("⏭ Skip Turn", BTN_AMBER);
        shuffleBtn = makeBtn("🔀 Shuffle",  new Color(70, 100, 160));

        btnPanel.add(submitBtn);
        btnPanel.add(recallBtn);
        btnPanel.add(skipBtn);
        btnPanel.add(shuffleBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setCurrentPlayer(String name) {
        currentPlayerLabel.setText(name + "'s Turn");
    }

    public void updateScores(List<Player> players, int currentIdx) {
        scorePanel.removeAll();
        for (int i = 0; i < players.size(); i++) {
            Player p  = players.get(i);
            boolean active = (i == currentIdx);

            JPanel card = new JPanel(new BorderLayout(6, 0));
            card.setBackground(active ? CARD_BG : BG);
            card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(active ? ACCENT : new Color(70, 55, 35), active ? 2 : 1),
                new EmptyBorder(6, 8, 6, 8)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            JLabel nameL  = new JLabel(p.getName());
            nameL.setForeground(active ? ACCENT : TEXT_DIM);
            nameL.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));

            JLabel scoreL = new JLabel(String.valueOf(p.getScore()));
            scoreL.setForeground(active ? ACCENT : TEXT_DIM);
            scoreL.setFont(new Font("Serif", Font.BOLD, 16));
            scoreL.setHorizontalAlignment(SwingConstants.RIGHT);

            card.add(nameL,  BorderLayout.WEST);
            card.add(scoreL, BorderLayout.EAST);
            scorePanel.add(card);
            scorePanel.add(Box.createVerticalStrut(4));
        }
        scorePanel.revalidate();
        scorePanel.repaint();
    }

    public void setTilesRemaining(int n) {
        tilesRemainingLabel.setText("Tiles: " + n);
    }

    public void setTurn(int turn) {
        turnLabel.setText("Turn: " + turn);
    }

    /** Start (or restart) the elapsed-time clock. */
    public void startClock(long startTimeMillis) {
        gameStartTime = startTimeMillis;
        if (clockTimer != null) clockTimer.stop();
        clockTimer = new javax.swing.Timer(1000, e -> tickClock());
        clockTimer.start();
        tickClock();
    }

    public void stopClock() {
        if (clockTimer != null) clockTimer.stop();
    }

    private void tickClock() {
        long elapsed = System.currentTimeMillis() - gameStartTime;
        long secs    = (elapsed / 1000) % 60;
        long mins    = elapsed / 60_000;
        timeLabel.setText(String.format("Time: %d:%02d", mins, secs));
    }

    public void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void clearLog() { logArea.setText(""); }

    public void addSubmitListener(ActionListener l)  { submitBtn.addActionListener(l); }
    public void addRecallListener(ActionListener l)  { recallBtn.addActionListener(l); }
    public void addSkipListener(ActionListener l)    { skipBtn.addActionListener(l); }
    public void addShuffleListener(ActionListener l) { shuffleBtn.addActionListener(l); }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 6, 8, 6));
        return b;
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TEXT_DIM);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return l;
    }
}
