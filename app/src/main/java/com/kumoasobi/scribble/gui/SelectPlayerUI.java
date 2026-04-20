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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Dialog for entering player names before game start.
 */
public class SelectPlayerUI extends JDialog {

    private static final Color BG     = new Color( 45,  35,  25);
    private static final Color ACCENT = new Color(220, 180,  80);
    private static final Color FG     = new Color(240, 230, 210);

    private final List<JTextField> nameFields = new ArrayList<>();
    private List<String> playerNames = null;
    private int playerCount = 2;

    public SelectPlayerUI(Frame parent) {
        super(parent, "New Game — Select Players", true);
        setBackground(BG);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // Title
        JLabel title = new JLabel("Scrabble", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(ACCENT);
        title.setBorder(new EmptyBorder(20, 0, 4, 0));
        add(title, BorderLayout.NORTH);

        // Center: player count selector + name fields
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(0, 30, 10, 30));

        JLabel countLabel = new JLabel("Number of players:");
        countLabel.setForeground(FG);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(countLabel);
        center.add(Box.createVerticalStrut(6));

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        countPanel.setBackground(BG);
        countPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup countGroup = new ButtonGroup();
        for (int n = 2; n <= 4; n++) {
            int num = n;
            JRadioButton rb = new JRadioButton(String.valueOf(n));
            rb.setBackground(BG);
            rb.setForeground(FG);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
            rb.setSelected(n == 2);
            rb.addActionListener(e -> { playerCount = num; refreshNameFields(center); });
            countGroup.add(rb);
            countPanel.add(rb);
        }
        center.add(countPanel);
        center.add(Box.createVerticalStrut(14));

        JLabel namesLabel = new JLabel("Player names:");
        namesLabel.setForeground(FG);
        namesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        namesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(namesLabel);
        center.add(Box.createVerticalStrut(6));

        // Name field container (will be refreshed)
        JPanel nameContainer = new JPanel();
        nameContainer.setName("nameContainer");
        nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.Y_AXIS));
        nameContainer.setBackground(BG);
        nameContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(nameContainer);

        add(center, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnPanel.setBackground(BG);

        JButton startBtn = new JButton("Start Game");
        startBtn.setBackground(new Color(60, 150, 70));
        startBtn.setForeground(Color.BLACK);
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        startBtn.setFocusPainted(false);
        startBtn.setBorderPainted(false);
        startBtn.setBorder(new EmptyBorder(10, 24, 10, 24));
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> onStart());

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(100, 80, 50));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setBorder(new EmptyBorder(10, 24, 10, 24));
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(startBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        buildNameFields(nameContainer, 2);
        pack();
        setLocationRelativeTo(parent);
    }

    private void refreshNameFields(JPanel center) {
        for (Component comp : center.getComponents()) {
            if (comp instanceof JPanel nc && "nameContainer".equals(nc.getName())) {
                buildNameFields(nc, playerCount);
                pack();
                return;
            }
        }
    }

    private void buildNameFields(JPanel container, int count) {
        container.removeAll();
        nameFields.clear();
        String[] defaults = {"Charlie", "Juliett", "Romeo", "Victor"};
        for (int i = 0; i < count; i++) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setBackground(BG);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lbl = new JLabel("Player " + (i+1) + ":");
            lbl.setForeground(new Color(200, 180, 140));
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setPreferredSize(new Dimension(72, 28));

            JTextField tf = new JTextField(defaults[i]);
            tf.setBackground(new Color(30, 22, 14));
            tf.setForeground(FG);
            tf.setCaretColor(ACCENT);
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 80, 40)),
                new EmptyBorder(4, 6, 4, 6)
            ));
            nameFields.add(tf);

            row.add(lbl, BorderLayout.WEST);
            row.add(tf, BorderLayout.CENTER);
            container.add(row);
            container.add(Box.createVerticalStrut(6));
        }
        container.revalidate();
        container.repaint();
    }

    private void onStart() {
        playerNames = new ArrayList<>();
        for (JTextField tf : nameFields) {
            String name = tf.getText().trim();
            if (name.isEmpty()) name = "Player" + (playerNames.size() + 1);
            playerNames.add(name);
        }
        dispose();
    }

    /** Returns the list of names, or null if cancelled. */
    public List<String> getPlayerNames() { return playerNames; }
}
