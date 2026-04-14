package com.kumoasobi.scribble.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Main menu screen shown on launch and after a game ends.
 */
public class MenuUI extends JPanel {

    private static final Color BG      = new Color( 35,  26,  16);
    private static final Color ACCENT  = new Color(220, 180,  80);
    private static final Color FG      = new Color(220, 210, 190);
    private static final Color BTN_BG  = new Color( 62,  48,  32);
    private static final Color BTN_HOV = new Color( 85,  65,  38);

    private final JButton newGameBtn, loadGameBtn, quitBtn;

    public MenuUI() {
        setLayout(new GridBagLayout());
        setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45, 35, 22));
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 80, 40), 2),
            new EmptyBorder(40, 60, 40, 60)
        ));

        JLabel title = new JLabel("SCRIBBLE");
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("A Cover of the Classic Word Game");
        subtitle.setFont(new Font("Serif", Font.ITALIC, 16));
        subtitle.setForeground(new Color(160, 140, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative tile row
        JPanel tiles = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        tiles.setBackground(new Color(45, 35, 22));
        String[] letters = {"S","C","R","I","B","B","L","E"};
        int[]    scores  = { 1,  3,  1,  1,  3,  3,  1,  1};
        for (int i = 0; i < letters.length; i++)
            tiles.add(makeTileDeco(letters[i], scores[i]));

        newGameBtn  = makeMenuBtn("New Game"); //▶  New Game
        loadGameBtn = makeMenuBtn("Load Game");//📂  Load Game
        quitBtn     = makeMenuBtn("Quit");//✕  Quit

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(tiles);
        card.add(Box.createVerticalStrut(36));
        card.add(newGameBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(loadGameBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(quitBtn);

        add(card);
    }

    private JButton makeMenuBtn(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BTN_HOV : BTN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(240, 44));
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setForeground(FG);
        b.setBackground(BTN_BG);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    private JPanel makeTileDeco(String letter, int score) {
        JPanel tile = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 235, 170));
                g2.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 6, 6);
                g2.setColor(new Color(180, 140, 60));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 6, 6);
                g2.setColor(new Color(30, 30, 30));
                g2.setFont(new Font("Serif", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(letter, (getWidth()-fm.stringWidth(letter))/2, getHeight()/2+fm.getAscent()/2-2);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 7));
                g2.drawString(String.valueOf(score), getWidth()-9, getHeight()-3);
            }
        };
        tile.setPreferredSize(new Dimension(36, 36));
        tile.setOpaque(false);
        return tile;
    }

    public void addNewGameListener(ActionListener l)  { newGameBtn.addActionListener(l); }
    public void addLoadGameListener(ActionListener l) { loadGameBtn.addActionListener(l); }
    public void addQuitListener(ActionListener l)     { quitBtn.addActionListener(l); }
}
