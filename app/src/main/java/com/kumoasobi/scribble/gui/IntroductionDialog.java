package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class IntroductionDialog extends JDialog {
    private static final Color BG = new Color(45, 35, 22);
    private static final Color CARD = new Color(62, 48, 32);
    private static final Color ACCENT = new Color(220, 180, 80);
    private static final Color FG = new Color(240, 230, 210);

    public IntroductionDialog(Frame parent) {
        super(parent, "How to Play", true);
        setBackground(BG);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Introduction", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(ACCENT);
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JTextArea content = new JTextArea();
        content.setEditable(false);
        content.setBackground(CARD);
        content.setForeground(FG);
        content.setFont(new Font("SansSerif", Font.PLAIN, 13));
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        String introduction =
                """
                Welcome to Scribble!
                
                The game is prepared for 2-4 players to maximize their scores by strategically placing tiles on a 15*15 game board to achieve the target score. Scores are calculated based on the points that are displayed on the letter tiles and bonus squares on the game board.
                
                Tile Distribution and Rack Management
                1. The quantities of tiles will be infinite, and the values for each letter will be predefined.
                2. Each player will start with 7 tiles in their \u201crack\u201d, and their will redraw tiles to 7 after each valid move.
                3. If a player cannot form a valid word, they may choose to exchange all their tiles or tiles.
                
                Scoring:
                1. Each letter has a point value shown on the tile.
                2. DL (Double Letter): Doubles the letter's score.
                3. TL (Triple Letter): Triples the letter's score.
                4. DW (Double Word): Doubles the entire word score.
                5. TW (Triple Word): Triples the entire word score.
                
                Word Placement Rules:
                1. All words must be formed by tiles that are placed on the players\u2019 racks, and the words can only be placed in a single vertical or horizontal straight line.
                2. The first word that is placed must cover the central grid of the game board, and subsequent word must connect to the tiles that have been placed on the board.
                3. All words placed on the board must be valid English words, and the words will be verified by a preloaded dictionary.
                
                Winning:
                The game ends when:
                1. A player uses all tiles and the bag is empty.
                2. The maximum number of skips has been reached.
                3. One of the player reaches the target score.
                4. The maximum number of terns has reached.
                5. The time limit has been reached
                The player with the highest score wins!
                
                AI:

                Every match is more than a game—it is a clash of personalities.
                Your opponent is not just an AI, but a character with their own way of thinking.

                Choose who you will face.


                
                Mako Hitachi (Easy AI) — Shadow of Playfulness
                “Hehe~ don’t underestimate me just because I’m having fun!”

                A lively ninja who hides sharp instincts behind a carefree smile.
                Mako plays with spontaneity—sometimes reckless, sometimes unexpectedly clever.
                Her moves may not always be optimal, but they are never predictable.



                Yoshino Tomotake (Medium AI) — Keeper of Balance
                “I will proceed carefully. Please do the same.”

                The shrine maiden who bears the weight of tradition and duty.
                Yoshino values harmony and precision, choosing each move with calm deliberation.
                She does not rush, nor does she falter easily.



                Murasame (Hard AI) — Eternal Blade Spirit
                “If you hesitate, you will lose.”

                A spirit bound to an ancient blade, carrying centuries of resolve.
                Murasame does not play for enjoyment—only for perfection.
                Every move is calculated, every weakness exploited without mercy.
                """;

        content.setText(introduction);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 80, 40), 2));
        scroll.getViewport().setBackground(CARD);
        scroll.setPreferredSize(new Dimension(500, 400));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        btnPanel.setBackground(BG);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(100, 80, 50));
        closeBtn.setForeground(FG);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setBorder(new EmptyBorder(10, 30, 10, 30));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
}
