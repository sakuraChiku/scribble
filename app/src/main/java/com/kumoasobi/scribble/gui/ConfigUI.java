package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.kumoasobi.scribble.ai.AIDifficulty;
import com.kumoasobi.scribble.ai.AIPlayer;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.rules.config.DrawMode;
import com.kumoasobi.scribble.rules.config.EndMode;
import com.kumoasobi.scribble.rules.config.GameConfigRequest;
import com.kumoasobi.scribble.util.SoundManager;

/**
 * New-game configuration dialog.
 *
 * Fills a GameConfigRequest with:
 *   endMode / scoreLimit / turnLimit / timeLimitMillis
 *   drawMode
 *   playerNum / playerInfo (names)
 *
 * After the dialog is dismissed, call getRequest(); returns null if cancelled.
 */
public class ConfigUI extends JDialog {

    // ── colours ──────────────────────────────────────────────────────────────
    private static final Color BG     = new Color( 217,  237,  210);
    private static final Color CARD   = new Color( 231,  231,  219);
    private static final Color ACCENT = new Color(0, 0,  0);
    private static final Color FG     = new Color(0, 0, 0);
    private static final Color DIM    = new Color(160, 145, 120);
    private static final Color INPUT  = new Color( 255,  255,  255);

    // ── state ────────────────────────────────────────────────────────────────
    private GameConfigRequest result = null;

    // ── widgets ──────────────────────────────────────────────────────────────
    private final ButtonGroup         endModeGroup  = new ButtonGroup();
    private final JSpinner            scoreLimitSpin = makeSpinner(100, 1, 9999, 50);
    private final JSpinner            turnLimitSpin  = makeSpinner(20,  1,  999,  5);
    private final JSpinner            timeLimitSpin  = makeSpinner(10,  1,  180,  5); // minutes
    private final JSpinner            maxRefreshTimesSpin = makeSpinner(3, 1, 99, 1);
    private final JRadioButton        rbScore  = makeRadio("Score limit");
    private final JRadioButton        rbTurn   = makeRadio("Turn limit");
    private final JRadioButton        rbTime   = makeRadio("Time limit");
    private final JRadioButton        rbTile   = makeRadio("Classic (tiles run out)");
    private final JRadioButton        rbLimited   = makeRadio("Limited (consume tiles)");
    private final JRadioButton        rbUnlimited = makeRadio("Unlimited (reuse tiles)");
    private final ButtonGroup         drawGroup   = new ButtonGroup();
    private int                       playerCount = 2;
    private final List<JTextField>    nameFields  = new ArrayList<>();
    private JPanel                    nameContainer;
    private final List<JComboBox<String>> aiToggles = new ArrayList<>();

    // ── constructor ──────────────────────────────────────────────────────────
    public ConfigUI(Frame parent) {
        super(parent, "New Game — Configuration", true);
        setBackground(BG);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Title
        JLabel title = new JLabel("Game Configuration", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(ACCENT);
        title.setBorder(new EmptyBorder(20, 0, 4, 0));
        add(title, BorderLayout.NORTH);

        // Main scrollable content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(0, 24, 0, 24));

        content.add(buildEndModeSection());
        content.add(Box.createVerticalStrut(12));
        content.add(buildDrawModeSection());
        content.add(Box.createVerticalStrut(12));
        content.add(buildPlayerSection());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        btnRow.setBackground(BG);

        JButton startBtn = styledButton("Start Game", new Color(0, 0, 0));
        startBtn.addActionListener(e -> onStart());

        JButton cancelBtn = styledButton("Cancel", new Color(0, 0, 0));
        cancelBtn.addActionListener(e -> onCancel());

        btnRow.add(startBtn);
        btnRow.add(cancelBtn);
        add(btnRow, BorderLayout.SOUTH);

        // Select defaults
        rbTile.setSelected(true);
        rbLimited.setSelected(true);
        updateLimitSpinners();

        pack();
        setLocationRelativeTo(parent);
    }

    // ── section builders ─────────────────────────────────────────────────────

    private JPanel buildEndModeSection() {
        JPanel p = card("End Condition");

        endModeGroup.add(rbScore);
        endModeGroup.add(rbTurn);
        endModeGroup.add(rbTime);
        endModeGroup.add(rbTile);

        // score row
        JPanel scoreRow = hRow(rbScore, scoreLimitSpin, "pts");
        // turn row
        JPanel turnRow  = hRow(rbTurn,  turnLimitSpin,  "turns");
        // time row
        JPanel timeRow  = hRow(rbTime,  timeLimitSpin,  "minutes");
        // tile row (no spinner)
        JPanel tileRow  = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tileRow.setBackground(CARD);
        tileRow.add(rbTile);

        for (JRadioButton rb : new JRadioButton[]{rbScore, rbTurn, rbTime, rbTile}) {
            rb.addActionListener(e -> updateLimitSpinners());
        }

        p.add(scoreRow);
        p.add(turnRow);
        p.add(timeRow);
        p.add(tileRow);
        return p;
    }

    private JPanel buildDrawModeSection() {
        JPanel p = card("Draw Mode");
        drawGroup.add(rbLimited);
        drawGroup.add(rbUnlimited);
        p.add(hSingle(rbLimited));
        p.add(hSingle(rbUnlimited));
        return p;
    }

    private JPanel buildPlayerSection() {
        JPanel outer = card("Players");

        // Player count radio buttons
        JPanel countRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        countRow.setBackground(CARD);
        JLabel countLbl = new JLabel("Number of players:");
        countLbl.setForeground(FG);
        countLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        countRow.add(countLbl);

        ButtonGroup cg = new ButtonGroup();
        for (int n = 2; n <= 4; n++) {
            final int num = n;
            JRadioButton rb = makeRadio(String.valueOf(n));
            rb.setSelected(n == 2);
            rb.addActionListener(e -> { playerCount = num; rebuildNames(); });
            cg.add(rb);
            countRow.add(rb);
        }
        outer.add(countRow);
        outer.add(Box.createVerticalStrut(8));

        nameContainer = new JPanel();
        JPanel refreshRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        refreshRow.setBackground(CARD);
        refreshRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel refreshLbl = new JLabel("Max refresh times:");
        refreshLbl.setForeground(FG);
        refreshLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        maxRefreshTimesSpin.setPreferredSize(new Dimension(72, 26));
        styleSpinner(maxRefreshTimesSpin);
        refreshRow.add(refreshLbl);
        refreshRow.add(maxRefreshTimesSpin);
        outer.add(refreshRow);
        outer.add(Box.createVerticalStrut(8));
        nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.Y_AXIS));
        nameContainer.setBackground(CARD);
        outer.add(nameContainer);

        rebuildNames();
        return outer;
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void rebuildNames() {
        nameContainer.removeAll();
        nameFields.clear();
        aiToggles.clear();
        String[] defaults = {"Alpha", "Bravo", "Charlie", "Delta"};
        for (int i = 0; i < playerCount; i++) {
            JPanel row = new JPanel(new BorderLayout(6, 0));
            row.setBackground(CARD);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lbl = new JLabel("P" + (i + 1) + ":");
            lbl.setForeground(DIM);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setPreferredSize(new Dimension(28, 28));

            JTextField tf = new JTextField(defaults[i]);
            tf.setPreferredSize(new Dimension(220, 28));
            tf.setBackground(INPUT);
            tf.setForeground(FG);
            tf.setCaretColor(ACCENT);
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 80, 40)),
                new EmptyBorder(4, 6, 4, 6)));
            nameFields.add(tf);

            JComboBox<String> aiBox = new JComboBox<>(
                new String[]{"Human", "AI — Easy", "AI — Medium", "AI — Hard"});
            aiBox.setBackground(INPUT);
            aiBox.setForeground(FG);
            aiBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
            aiBox.setPreferredSize(new Dimension(100, 28));
            aiBox.setMaximumSize(new Dimension(100, 28));
            final JTextField ftf = tf;
            aiBox.addActionListener(e -> {
                boolean isHuman = aiBox.getSelectedIndex() == 0;
                ftf.setEnabled(isHuman);
                if (!isHuman) {
                    String d = ((String)aiBox.getSelectedItem()).replace("AI — ","");
                    switch (d) {
                        case "Easy" -> ftf.setText("Mako");
                        case "Medium" -> ftf.setText("Yoshino");
                        case "Hard" -> ftf.setText("Murasame");
                    }
                }
            });
            aiToggles.add(aiBox);

            row.add(lbl, BorderLayout.WEST);
            row.add(tf, BorderLayout.CENTER);
            row.add(aiBox, BorderLayout.EAST);
            nameContainer.add(row);
            nameContainer.add(Box.createVerticalStrut(4));
        }
        nameContainer.revalidate();
        nameContainer.repaint();
        pack();
    }

    private void updateLimitSpinners() {
        scoreLimitSpin.setEnabled(rbScore.isSelected());
        turnLimitSpin.setEnabled(rbTurn.isSelected());
        timeLimitSpin.setEnabled(rbTime.isSelected());
    }

    private void onStart() {
        SoundManager.playStart();

        GameConfigRequest req = new GameConfigRequest();

        // endMode
        if (rbScore.isSelected())      { req.endMode = EndMode.SCORE_LIMIT; req.scoreLimit = (int) scoreLimitSpin.getValue(); }
        else if (rbTurn.isSelected())  { req.endMode = EndMode.TURN_LIMIT;  req.turnLimit  = (int) turnLimitSpin.getValue(); }
        else if (rbTime.isSelected())  { req.endMode = EndMode.TIME_LIMIT;  req.timeLimitMillis = (long)((int) timeLimitSpin.getValue()) * 60_000L; }
        else                           { req.endMode = EndMode.TILE_LIMIT; }

        // drawMode
        req.drawMode = rbUnlimited.isSelected() ? DrawMode.UNLIMITED : DrawMode.LIMITED;

        // players (human or AI)
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < nameFields.size(); i++) {
            String name = nameFields.get(i).getText().trim();
            if (name.isEmpty()) name = "Player " + (players.size() + 1);
            int aiIdx = aiToggles.get(i).getSelectedIndex();
            if (aiIdx == 0) {
                players.add(new Player(name, UUID.randomUUID(), (int)maxRefreshTimesSpin.getValue()));
            } else {
                AIDifficulty diff = switch (aiIdx) {
                    case 1  -> AIDifficulty.EASY;
                    case 2  -> AIDifficulty.MEDIUM;
                    default -> AIDifficulty.HARD;
                };
                players.add(new AIPlayer(name, diff));
            }
        }
        req.allPlayers = players;

        result = req;
        dispose();
    }

    private void onCancel() {
        SoundManager.playCancel();
        dispose();
    }

    /** Returns the filled request, or null if cancelled. */
    public GameConfigRequest getRequest() { return result; }

    // ── widget factories ─────────────────────────────────────────────────────

    private JPanel card(String heading) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 80, 40), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel hdr = new JLabel(heading);
        hdr.setForeground(ACCENT);
        hdr.setFont(new Font("SansSerif", Font.BOLD, 14));
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(hdr);
        p.add(Box.createVerticalStrut(8));
        return p;
    }

    private JPanel hRow(JRadioButton rb, JSpinner spin, String unit) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setBackground(CARD);
        row.add(rb);
        spin.setPreferredSize(new Dimension(72, 26));
        styleSpinner(spin);
        row.add(spin);
        JLabel u = new JLabel(unit);
        u.setForeground(DIM);
        u.setFont(new Font("SansSerif", Font.PLAIN, 12));
        row.add(u);
        return row;
    }

    private JPanel hSingle(JRadioButton rb) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setBackground(CARD);
        row.add(rb);
        return row;
    }

    private JRadioButton makeRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBackground(CARD);
        rb.setForeground(FG);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rb.setFocusPainted(false);
        return rb;
    }

    private static JSpinner makeSpinner(int val, int min, int max, int step) {
        return new JSpinner(new SpinnerNumberModel(val, min, max, step));
    }

    private void styleSpinner(JSpinner sp) {
        sp.setBackground(INPUT);
        sp.getEditor().getComponent(0).setBackground(INPUT);
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setForeground(FG);
        ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(10, 24, 10, 24));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}