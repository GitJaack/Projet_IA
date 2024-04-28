package page;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GomokuPanel extends JPanel {
    private Gomoku game;
    private static final int TILE_SIZE = 30;
    private static final int SIZE = 20;

    public GomokuPanel() {
        setPreferredSize(new Dimension(TILE_SIZE * SIZE, TILE_SIZE * SIZE));
        game = new Gomoku();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TILE_SIZE;
                int y = e.getY() / TILE_SIZE;

                if (x >= 0 && x < 15 && y >= 0 && y < 15) {
                    if (game.placeStone(x, y)) {
                        repaint(); // Redraw the board after each valid move
                        if (game.isWin(y, x)) {
                            JOptionPane.showMessageDialog(null, "Player " + game.getCurrentPlayer() + " wins!");
                            game.reset();
                        }
                        game.switchPlayer();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        char[][] board = game.getBoard();
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                if (board[i][j] == game.getCurrentPlayer() && game.getCurrentPlayer() == 'O') {
                    g.setColor(Color.BLACK);
                    g.fillOval(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (board[i][j] == game.getCurrentPlayer() && game.getCurrentPlayer() == 'X') {
                    g.setColor(Color.WHITE);
                    g.fillOval(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
