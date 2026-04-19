package com.kumoasobi.scribble.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.kumoasobi.scribble.models.Tile;

/**
 * Displays the current player's tile rack (up to 7 tiles).
 * Click a tile to select it; click again to deselect.
 */
public class RackPanel extends JPanel {

    public interface TileSelectListener {
        void onTileSelected(Tile tile, int index);
    }

    private static final int CELL   = 52;
    private static final int PAD    = 6;
    private static final Color TILE_BG     = new Color(245, 250, 245);
    private static final Color TILE_BORDER = new Color(180, 200, 190);
    private static final Color SEL_BG      = new Color(220, 240, 230);
    private static final Color SEL_BORDER  = new Color(120, 160, 140);
    private static final Color BLANK_BG    = new Color(250, 250, 250);
    private static final Color TILE_TEXT   = new Color(60, 80, 90);
    private static final Color SCORE_TEXT  = new Color(120, 140, 150);

    private List<Tile> rack;
    private int selectedIndex = -1;
    private TileSelectListener listener;

    public RackPanel() {
        setPreferredSize(new Dimension(CELL * 7 + PAD * 2, CELL + PAD * 2));
        setBackground(new Color(255, 254, 248));
        setBorder(BorderFactory.createEmptyBorder(PAD, PAD, PAD, PAD));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (rack == null) return;
                int idx = (e.getX() - PAD) / CELL;
                if (idx < 0 || idx >= rack.size()) return;
                if (selectedIndex == idx) {
                    selectedIndex = -1;
                    if (listener != null) listener.onTileSelected(null, -1);
                } else {
                    selectedIndex = idx;
                    if (listener != null) listener.onTileSelected(rack.get(idx), idx);
                }
                repaint();
            }
        });
    }

    public void setRack(List<Tile> rack) {
        this.rack = rack;
        selectedIndex = -1;
        repaint();
    }

    public void clearSelection() { selectedIndex = -1; repaint(); }
    public int getSelectedIndex() { return selectedIndex; }
    public void setTileSelectListener(TileSelectListener l) { this.listener = l; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (rack == null) return;
        for (int i = 0; i < rack.size(); i++) {
            int x = PAD + i * CELL;
            int y = PAD;
            boolean sel = (i == selectedIndex);
            drawTile(g2, x, y, rack.get(i), sel);
        }
    }

    private void drawTile(Graphics2D g2, int x, int y, Tile tile, boolean selected) {
        int pad = 4;
        g2.setColor(selected ? SEL_BG : (tile.getLetter() == '?' ? BLANK_BG : TILE_BG));
        g2.fillRoundRect(x+pad, y+pad, CELL-pad*2, CELL-pad*2, 8, 8);
        g2.setColor(selected ? SEL_BORDER : TILE_BORDER);
        g2.setStroke(new BasicStroke(selected ? 2.5f : 1.5f));
        g2.drawRoundRect(x+pad, y+pad, CELL-pad*2, CELL-pad*2, 8, 8);

        g2.setColor(TILE_TEXT);
        g2.setFont(new Font("Serif", Font.BOLD, 24));
        String letter = String.valueOf(tile.getLetter() == '?' ? ' ' : tile.getLetter());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(letter, x+(CELL-fm.stringWidth(letter))/2, y+CELL/2+fm.getAscent()/2-2);

        g2.setColor(SCORE_TEXT);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        String score = String.valueOf(tile.getScore());
        g2.drawString(score, x+CELL-14, y+CELL-6);
    }
}
