package com.kumoasobi.scribble.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.BonusType;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.Tile;

public class BoardPanel extends JPanel {

    public interface CellClickListener {
        void onCellClicked(int row, int col);
    }

    private static final int CELL = 42;
    private static final int SIZE = 15;

    private static final Color NONE_COLOR = new Color(245, 235, 200);
    private static final Color TW_COLOR   = new Color(200,  70,  90);
    private static final Color DW_COLOR   = new Color(230, 160, 120);
    private static final Color TL_COLOR   = new Color( 80, 130, 210);
    private static final Color DL_COLOR   = new Color(160, 200, 245);
    private static final Color GRID_COLOR = new Color(160, 150, 130);
    private static final Color TILE_BG    = new Color(255, 235, 170);
    private static final Color TILE_BORDER= new Color(180, 140,  60);
    private static final Color TILE_TEXT  = new Color( 30,  30,  30);
    private static final Color PREVIEW_BG = new Color(200, 240, 200, 210);
    private static final Color PREVIEW_BD = new Color( 60, 160,  60);
    private static final Color CENTER_DOT = new Color(190,  60,  60);

    private Board board;
    private Move currentMove;
    private CellClickListener listener;
    private int hoverRow = -1, hoverCol = -1;
    private Image bgImage;

    private static final BonusType[][] BONUS_MAP = {
        {BonusType.TW,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.TW},
        {BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE},
        {BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE},
        {BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.DL},
        {BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE},
        {BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE},
        {BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE},
        {BonusType.TW,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.TW},
        {BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE},
        {BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE},
        {BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.NONE},
        {BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.DL},
        {BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE},
        {BonusType.NONE,BonusType.DW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DW,BonusType.NONE},
        {BonusType.TW,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.TW,BonusType.NONE,BonusType.NONE,BonusType.NONE,BonusType.DL,BonusType.NONE,BonusType.NONE,BonusType.TW},
    };

    public BoardPanel(Board board, Move currentMove) {
        this.board = board;
        this.currentMove = currentMove;
        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/img/gui/title_background.png"));
        } catch (IOException e) {
            bgImage = null;
        }
        setPreferredSize(new Dimension(CELL * SIZE + 1, CELL * SIZE + 1));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int DX = (getWidth()  - SIZE * CELL) / 2;
                int DY = (getHeight() - SIZE * CELL) / 2;
                int col = (e.getX() - DX) / CELL + 1;
                int row = (e.getY() - DY) / CELL + 1;
                if (row >= 1 && row <= SIZE && col >= 1 && col <= SIZE && listener != null)
                    listener.onCellClicked(row, col);
            }
            @Override public void mouseExited(MouseEvent e) { hoverRow = hoverCol = -1; repaint(); }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int DX = (getWidth()  - SIZE * CELL) / 2;
                int DY = (getHeight() - SIZE * CELL) / 2;
                int newRow = (e.getY() - DY) / CELL + 1;
                int newCol = (e.getX() - DX) / CELL + 1;
                // 鼠标在棋盘外时清除 hover
                if (newRow < 1 || newRow > SIZE || newCol < 1 || newCol > SIZE) {
                    hoverRow = hoverCol = -1;
                } else {
                    hoverRow = newRow;
                    hoverCol = newCol;
                }
                repaint();
            }
        });
    }

    public void setBoard(Board board) { this.board = board; repaint(); }
    public void setCurrentMove(Move m) { this.currentMove = m; repaint(); }
    public void setCellClickListener(CellClickListener l) { this.listener = l; }

    @Override
    protected void paintComponent(Graphics g) {
        int DX = (getWidth() - SIZE * CELL) / 2;
        int DY = (getHeight() - SIZE * CELL) / 2;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (board == null) return;
        for (int r = 1; r <= SIZE; r++)
            for (int c = 1; c <= SIZE; c++)
                drawCell(g2, DX+(c-1)*CELL, DY+(r-1)*CELL, r, c); // grids
        g2.setColor(GRID_COLOR);
        g2.setStroke(new BasicStroke(0.8f));
        for (int i = 0; i <= SIZE; i++) {
            g2.drawLine(DX+i*CELL, DY, DX+i*CELL, DY+SIZE*CELL); // verticla line
            g2.drawLine(DX, DY+i*CELL, DX+SIZE*CELL, DY+i*CELL); // horizontal line
        }
    }

    private void drawCell(Graphics2D g2, int x, int y, int r, int c) {
        BonusType bonus = BONUS_MAP[r-1][c-1];
        Color bg = getBonusColor(bonus);
        if (r == hoverRow && c == hoverCol && !board.hasTile(r,c) && !currentMove.hasPlacement(r,c))
            bg = bg.brighter();
        g2.setColor(bg);
        g2.fillRect(x, y, CELL, CELL);

        if (r == 8 && c == 8 && !board.hasTile(8,8) && !currentMove.hasPlacement(8,8)) {
            g2.setColor(CENTER_DOT);
            g2.fillOval(x+CELL/2-6, y+CELL/2-6, 12, 12);
        }
        if (!board.hasTile(r,c) && !currentMove.hasPlacement(r,c) && bonus != BonusType.NONE) {
            g2.setColor(bg.darker().darker());
            g2.setFont(new Font("SansSerif", Font.BOLD, 8));
            String label = bonusLabel(bonus);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, x+(CELL-fm.stringWidth(label))/2, y+CELL-5);
        }
        if (currentMove.hasPlacement(r,c)) { drawTile(g2,x,y,getTileFromMove(r,c),true); return; }
        if (board.hasTile(r,c)) drawTile(g2,x,y,board.getTile(r,c),false);
    }

    private void drawTile(Graphics2D g2, int x, int y, Tile tile, boolean preview) {
        int pad = 3;
        g2.setColor(preview ? PREVIEW_BG : TILE_BG);
        g2.fillRoundRect(x+pad, y+pad, CELL-pad*2, CELL-pad*2, 6, 6);
        g2.setColor(preview ? PREVIEW_BD : TILE_BORDER);
        g2.setStroke(new BasicStroke(preview ? 1.8f : 1.2f));
        g2.drawRoundRect(x+pad, y+pad, CELL-pad*2, CELL-pad*2, 6, 6);
        g2.setColor(TILE_TEXT);
        g2.setFont(new Font("Serif", Font.BOLD, 20));
        String letter = String.valueOf(tile.getLetter());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(letter, x+(CELL-fm.stringWidth(letter))/2, y+CELL/2+fm.getAscent()/2-2);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        g2.drawString(String.valueOf(tile.getScore()), x+CELL-10, y+CELL-4);
    }

    private Tile getTileFromMove(int r, int c) {
        for (Placement p : currentMove.getPlacements())
            if (p.getRow()==r && p.getCol()==c) return p.getTile();
        return null;
    }

    private Color getBonusColor(BonusType b) {
        return switch(b) { case TW->TW_COLOR; case DW->DW_COLOR; case TL->TL_COLOR; case DL->DL_COLOR; default->NONE_COLOR; };
    }

    private String bonusLabel(BonusType b) {
        return switch(b) { case TW->"TW"; case DW->"DW"; case TL->"TL"; case DL->"DL"; default->""; };
    }
}
