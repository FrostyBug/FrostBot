package frostbug.chess.visual;

import frostbug.chess.BoardState;
import frostbug.chess.ChessEngine;
import static frostbug.chess.Constants.*;
import frostbug.chess.MoveGenerator;
import frostbug.chess.Moves;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author FrostBug
 */
public class BoardPanel extends JPanel implements MouseListener {

    private final int gridSize;
    private final ChessEngine parent;
    private final ChessUI ui;

    private final Color WHITE_COL;
    private final Color BLACK_COL;

    private boolean editing;
    private int selectedSq, reverseSq;

    @SuppressWarnings("LeakingThisInConstructor")
    public BoardPanel(ChessEngine parent, ChessUI ui) {
        initComponents();
        this.parent = parent;
        this.ui = ui;
        this.gridSize = getPreferredSize().width / 8;
        this.WHITE_COL = new Color(171, 230, 252);
        this.BLACK_COL = new Color(101, 147, 247);

        this.editing = false;
        this.selectedSq = reverseSq = -1;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g1) {
        if (parent.isBusy()) {
            return;
        }
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(g.getFont().deriveFont(50f));
        BoardState bs = parent.getBoardState();

        //Draw grid
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            boolean white = (x % 2 == y % 2);
            g.setColor(white ? WHITE_COL : BLACK_COL);
            g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
        }

        g.setColor(Color.BLACK);
        for (ChessPiece cp : ChessPiece.values()) {
            if (cp == ChessPiece.EMPTY) {
                continue;
            }
            for (int i = 0; i < bs.pieceNum[cp.getId()]; i++) {
                int sq = bs.pieceArray[cp.getId()][i];
                int x = FILES_MAP[sq];
                int y = RANKS_MAP[sq];
                g.drawString(cp.getAscii(), x * gridSize, ((8 - y) * gridSize) - 5);
            }
        }

        if (selectedSq > -1) {
            int x = FILES_MAP[selectedSq];
            int y = RANKS_MAP[selectedSq];
            System.out.printf("Drawing at %d,%d\n", x, y);
            g.setStroke(new BasicStroke(3f));
            g.drawRect((x * gridSize) + 1, (y * gridSize) + 1, gridSize - 3, gridSize - 3);
        }
    }

    /**
     * Are we editing the board?
     *
     * @return
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * Enable or disable board editing
     *
     * @param editing
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	@Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / gridSize;
        int y = e.getY() / gridSize;
        int sq = BoardState.getSquareHex(x, y);
        int rsq = BoardState.getSquareHex(x, 7 - y);
        if (this.selectedSq == -1) {
            this.selectedSq = sq;
            this.reverseSq = rsq;
        } else {
            if (sq == selectedSq) {
                this.selectedSq = -1;
            } else if (!parent.isBusy()) {
                BoardState bs = parent.getBoardState();
                int pieceType = bs.pieces[reverseSq];
                int destType = bs.pieces[rsq];
                if (!editing || (pieceType != EMPTY && destType == EMPTY)) {
                    if(!editing) {
                        if(PIECE_COLORS[pieceType] != bs.turn) {
                            selectedSq = -1;
                            repaint();
                            return;
                        }
                        int spe = 0;
                        int diff = FILES_MAP[reverseSq] - FILES_MAP[rsq];
                        if(KING_ARRAY[pieceType]
                                && Math.abs(diff) > 1
                                && ((bs.turn == WHITE &&
                                ((diff < 0 && (bs.castlePerms & WKCA) != 0) 
                                || (diff > 0 && (bs.castlePerms & WQCA) != 0))) 
                                || (bs.turn == BLACK && ((diff < 0 
                                && (bs.castlePerms & BKCA) != 0) 
                                || (diff > 0 && (bs.castlePerms & BQCA) != 0))))) {
                            spe = 1;
                        } else if(bs.enPas == rsq && PAWN_ARRAY[pieceType]) {
                            spe = 1;
                        } else if(PAWN_ARRAY[pieceType]
                                && ((RANKS_MAP[rsq] == RANK_8 && bs.turn == WHITE)
                                || RANKS_MAP[rsq] == RANK_1 && bs.turn == BLACK)) {
                            spe = 1;
                            pieceType = (PIECE_COLORS[pieceType] == WHITE) ? W_QUEEN : B_QUEEN;
                        }
                        int cont = bs.pieces[rsq];
                        int pStart = 0;
                        if(PAWN_ARRAY[pieceType]
                                && Math.abs(RANKS_MAP[reverseSq] - RANKS_MAP[rsq]) > 1) {
                            pStart = 1;
                        }
                        int move = Moves.generateMove(reverseSq, rsq, spe, pieceType, cont, pStart);
                        if(MoveGenerator.isMoveLegal(bs, move)) {
                            bs.makeMove(move);
                            ui.updateDisplay(bs, move);
                        } else {
                            System.out.println("Move illegal");
                            Moves.printMoveLong(move);
                        }
                    } else {
                        bs.movePiece(reverseSq, rsq);
                    }
                } else {
                    System.out.println("Moving: " + pieceType + " on dest tile: " + destType);
                }
                selectedSq = -1;
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
