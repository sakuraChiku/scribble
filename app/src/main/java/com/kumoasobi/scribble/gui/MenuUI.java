package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.kumoasobi.scribble.util.SoundManager;

/**
 * Main menu screen shown on launch and after a game ends.
 * 
 * Copyright Notice
 *
 * This project ("Scribble") is a non-commercial academic assignment.
 *
 * Some image assets used in this project are sourced from the visual novel
 * "Senren * Banka" developed by Yuzusoft.
 *
 * All rights to these audio materials belong to their original creators
 * and copyright holders (Yuzusoft).
 *
 * These assets are used solely for educational and non-commercial purposes.
 * No copyright infringement is intended.
 *
 * If any copyright holder has concerns regarding the use of these materials,
 * please contact the author and the content will be removed promptly.
 * 
 * @author Peixuan Ding
 * @version 1.0
 */
public class MenuUI extends JPanel {

    private static final Color BG      = new Color( 35,  26,  16);
    private static final Color FG      = new Color(220, 210, 190);
    private static final Color BTN_BG  = new Color( 192,  80,  0);
    private static final Color BTN_HOV = new Color( 243,  243,  91);

    private final JButton newGameBtn, loadGameBtn, quitBtn, introBtn;
    @SuppressWarnings("FieldMayBeFinal")
    private Image bgImage;
    @SuppressWarnings("FieldMayBeFinal")
    private Image headImage;
    @SuppressWarnings("FieldMayBeFinal")
    private Image logoImage;

    public MenuUI() {
        setLayout(new GridBagLayout());
        setBackground(BG);

        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/img/gui/title_charall.png"));
            headImage = ImageIO.read(getClass().getResourceAsStream("/assets/img/gui/title_head.png"));
            logoImage = ImageIO.read(getClass().getResourceAsStream("/assets/img/gui/title_logo.png"));
        } catch (IOException e) {
            bgImage = null;
            headImage = null;
            logoImage = null;
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1260, 480));

        JPanel leftCard = new JPanel();
        leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
        leftCard.setBackground(new Color(255, 255, 255, 0));
        leftCard.setOpaque(false);
        leftCard.setBorder(new EmptyBorder(60, 40, 60, 40));
        leftCard.setPreferredSize(new Dimension(250, 0));

        newGameBtn  = makeMenuBtn("New Game"); //▶  New Game
        loadGameBtn = makeMenuBtn("Load Game");//📂  Load Game
        quitBtn     = makeMenuBtn("Quit");//✕  Quit
        introBtn    = makeMenuBtn("Instruction");

        leftCard.add(Box.createVerticalStrut(180));
        leftCard.add(newGameBtn);
        leftCard.add(Box.createVerticalStrut(80));
        leftCard.add(loadGameBtn);
        leftCard.add(Box.createVerticalStrut(80));
        leftCard.add(introBtn);
        leftCard.add(Box.createVerticalStrut(80));
        leftCard.add(quitBtn);

        add(leftCard, BorderLayout.WEST);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (logoImage != null) {
            g.drawImage(logoImage, 25, 25, 310, 150, this);
        }
        if (headImage != null) {
            g.drawImage(headImage, 0, 225, 10, 450, this);
        }
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

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                SoundManager.playDecide();
            }
        });

        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(240, 44));
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setForeground(FG);
        b.setBackground(BTN_BG);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    public void addNewGameListener(ActionListener l)  { newGameBtn.addActionListener(l); }
    public void addLoadGameListener(ActionListener l) { loadGameBtn.addActionListener(l); }
    public void addQuitListener(ActionListener l)     { quitBtn.addActionListener(l); }
    public void addIntroListener(ActionListener l) { introBtn.addActionListener(l); }
}
