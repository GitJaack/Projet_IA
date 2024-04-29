package page;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GomokuPanel extends JPanel {

    private int x = Constante.LINE_COUNT / 2;
    private int y = Constante.LINE_COUNT / 2;

    private Node[][] chessBeans = new Node[Constante.LINE_COUNT][Constante.LINE_COUNT];

    private int count = 0;

    private boolean isGameOver = true;

    private boolean isShowOrder = false;

    private Gomoku game;

    public GomokuPanel() {
        this.setPreferredSize(new Dimension(Constante.PANEL_WIDTH, Constante.PANEL_HEIGHT));
        this.setBackground(Color.ORANGE);
        this.addMouseMotionListener(mouseMotionListener);
        this.addMouseListener(mouseListener);

        for (int i = 0; i < Constante.LINE_COUNT; i++) { // initialisation
            for (int j = 0; j < Constante.LINE_COUNT; j++) {
                Node chessBean = new Node(i, j, Constante.EMPTY, 0);
                chessBeans[i][j] = chessBean;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        drawLine(g2d);
        drawStar(g2d);
        drawTrips(g2d, x, y);
        drawNumber(g2d);
        drawChess1(g2d);
        drawOrderNum(g2d);
    }

    private void drawLine(Graphics2D g2d) {
        for (int i = 0; i < Constante.LINE_COUNT; i++) { // ligne horizontale
            g2d.drawLine(Constante.OFFSET, Constante.OFFSET + i * Constante.CELL_WIDTH,
                    Constante.OFFSET + (15 - 1) * Constante.CELL_WIDTH,
                    Constante.OFFSET + i * Constante.CELL_WIDTH);
        }
        for (int i = 0; i < 15; i++) { // ligne verticale
            g2d.drawLine(Constante.OFFSET + i * Constante.CELL_WIDTH, Constante.OFFSET,
                    Constante.OFFSET + i * Constante.CELL_WIDTH,
                    Constante.OFFSET + (15 - 1) * Constante.CELL_WIDTH);
        }
    }

    // Dessiner le point central et les étoiles
    private void drawStar(Graphics2D g2d) {
        // Point central
        g2d.fillOval(Constante.LINE_COUNT / 2 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                Constante.LINE_COUNT / 2 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                Constante.STAR_WIDTH, Constante.STAR_WIDTH);
        // Étoile en haut à gauche
        g2d.fillOval(Constante.LINE_COUNT / 4 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                Constante.LINE_COUNT / 4 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                Constante.STAR_WIDTH, Constante.STAR_WIDTH);
        // Étoile en bas à gauche
        g2d.fillOval(Constante.LINE_COUNT / 4 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                (Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
                Constante.STAR_WIDTH, Constante.STAR_WIDTH);
        // Étoile en haut à droite
        g2d.fillOval(
                (Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
                Constante.LINE_COUNT / 4 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,
                Constante.STAR_WIDTH, Constante.STAR_WIDTH);
        // Étoile en bas à droite
        g2d.fillOval(
                (Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
                (Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
                Constante.STAR_WIDTH, Constante.STAR_WIDTH);
    }

    private void drawTrips(Graphics2D g2d, int i, int j) {
        i = i * Constante.CELL_WIDTH + Constante.OFFSET;
        j = j * Constante.CELL_WIDTH + Constante.OFFSET;
        g2d.setColor(Color.red);
        // gauche haut vers la droite
        g2d.drawLine(i - Constante.OFFSET / 2, j - Constante.OFFSET / 2, i - Constante.OFFSET / 4,
                j - Constante.OFFSET / 2);
        // gauche haut vers en bas
        g2d.drawLine(i - Constante.OFFSET / 2, j - Constante.OFFSET / 2, i - Constante.OFFSET / 2,
                j - Constante.OFFSET / 4);
        // gauche bas vers la droite
        g2d.drawLine(i - Constante.OFFSET / 2, j + Constante.OFFSET / 2, i - Constante.OFFSET / 4,
                j + Constante.OFFSET / 2);
        // gauche bas vers en haut
        g2d.drawLine(i - Constante.OFFSET / 2, j + Constante.OFFSET / 2, i - Constante.OFFSET / 2,
                j + Constante.OFFSET / 4);
        // droit haut vers la gauche
        g2d.drawLine(i + Constante.OFFSET / 2, j - Constante.OFFSET / 2, i + Constante.OFFSET / 4,
                j - Constante.OFFSET / 2);
        // droit haut vers en bas
        g2d.drawLine(i + Constante.OFFSET / 2, j - Constante.OFFSET / 2, i + Constante.OFFSET / 2,
                j - Constante.OFFSET / 4);
        // droit haut vers la gauche
        g2d.drawLine(i + Constante.OFFSET / 2, j + Constante.OFFSET / 2, i + Constante.OFFSET / 4,
                j + Constante.OFFSET / 2);
        // droit haut vers en bas
        g2d.drawLine(i + Constante.OFFSET / 2, j + Constante.OFFSET / 2, i + Constante.OFFSET / 2,
                j + Constante.OFFSET / 4);
        g2d.setColor(Color.black);
    }

    // Dessinez les coordonnées à côté de l'échiquier
    private void drawNumber(Graphics2D g2d) {

        for (int i = Constante.LINE_COUNT; i > 0; i--) {
            FontMetrics fn = g2d.getFontMetrics();
            int height = fn.getAscent();
            g2d.drawString(16 - i + "", 10, i * Constante.CELL_WIDTH + height / 2); // Chiffres à gauche

            int width = fn.stringWidth(((char) (64 + i)) + "");// lettre
            g2d.drawString(((char) (64 + i)) + "", Constante.CELL_WIDTH * i - width / 2,
                    Constante.OFFSET + Constante.LINE_COUNT * Constante.CELL_WIDTH);
        }

    }

    // Dessiner des pièces d'échecs
    private void drawChess1(Graphics2D g2d) {
        int width = Constante.OFFSET / 4 * 3; // largeur échec
        for (Node[] chessBeans2 : chessBeans) {
            for (Node chessBean : chessBeans2) {
                if (chessBean.getPlayer() != 0) {
                    if (chessBean.getPlayer() == Constante.BLACK) { // joueur noir
                        g2d.setColor(Color.BLACK);
                    } else if (chessBean.getPlayer() == Constante.WHILE) { // joueur blanc
                        g2d.setColor(Color.WHITE);
                    }
                    int a = chessBean.getX() * Constante.CELL_WIDTH + Constante.OFFSET;
                    int b = chessBean.getY() * Constante.CELL_WIDTH + Constante.OFFSET;
                    g2d.fillOval(a - width / 2, b - width / 2, width, width);
                }
            }
            if (count > 0 && !isShowOrder) {
                g2d.setColor(Color.RED); // Il y a une petite matrice rouge au milieu de la dernière pièce d'échecs
                int a = getMaxNum().getX() * Constante.CELL_WIDTH + Constante.OFFSET - Constante.CELL_WIDTH / 10;
                int b = getMaxNum().getY() * Constante.CELL_WIDTH + Constante.OFFSET - Constante.CELL_WIDTH / 10;
                g2d.fillRect(a, b, Constante.CELL_WIDTH / 5, Constante.CELL_WIDTH / 5);
                g2d.setColor(Color.black);
            }
        }
    }

    // Afficher les chiffres
    private void drawOrderNum(Graphics2D g2d) {
        if (isShowOrder) {
            g2d.setColor(Color.RED); // Mettre les chiffres en rouge
            FontMetrics fn = g2d.getFontMetrics();
            for (Node[] chssBean2 : chessBeans) {
                for (Node chssBean : chssBean2) {
                    if (chssBean.getOrderNumber() != 0) {
                        String str = chssBean.getOrderNumber() + ""; // Convertir un nombre en chaîne
                        int width = fn.stringWidth(str); // Obtenir la largeur du numéro
                        int height = fn.getHeight();
                        int x = chssBean.getX() * Constante.CELL_WIDTH + Constante.OFFSET - width / 2; // Obtenez le
                                                                                                       // centre de la
                                                                                                       // pièce d'échecs
                        int y = chssBean.getY() * Constante.CELL_WIDTH + Constante.OFFSET + height / 4;
                        g2d.drawString(chssBean.getOrderNumber() + "", x, y); // Imprimez le numéro au milieu de la
                                                                              // pièce d'échecs
                    }
                }
            }
        }
        g2d.setColor(Color.BLACK);
    }

    private Node getMaxNum() {
        Node tempBean = null;
        for (Node[] chssBean2 : chessBeans) {
            for (Node chssBean : chssBean2) {
                if (chessBeans != null && chssBean.getOrderNumber() == count) {
                    return chssBean;
                }
            }
        }
        return tempBean;
    }

    private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
            int cx = e.getX(); // Obtenez les coordonnées avec la souris
            int cy = e.getY();
            if (cx >= Constante.OFFSET && cx <= Constante.OFFSET + (Constante.LINE_COUNT - 1) * Constante.CELL_WIDTH
                    && cy >= Constante.OFFSET
                    && cy <= Constante.OFFSET + (Constante.LINE_COUNT - 1) * Constante.CELL_WIDTH) { // Déterminez s'il
                                                                                                     // se trouve sur
                                                                                                     // l'échiquier
                x = (cx - Constante.OFFSET / 2) / Constante.CELL_WIDTH; // Convertir les coordonnées en points
                                                                        // d'intersection des lignes internes de
                                                                        // l'échiquier
                y = (cy - Constante.OFFSET / 2) / Constante.CELL_WIDTH;
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    };

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            int cx = e.getX();
            int cy = e.getY();
            int a = (cx - Constante.OFFSET / 2) / Constante.CELL_WIDTH;
            int b = (cy - Constante.OFFSET / 2) / Constante.CELL_WIDTH;

            if (e.getButton() == MouseEvent.BUTTON1) {

            }
            if (a >= 0 && a < Constante.LINE_COUNT && b >= 0 && b < Constante.LINE_COUNT) {
                if (game.placeStone(a, b)) {
                    repaint(); // Redraw the board after each valid move
                    if (game.isWin(y, x)) {
                        JOptionPane.showMessageDialog(null, "Player " + game.getCurrentPlayer() + " wins!");
                        game.reset();
                    }
                    game.switchPlayer();

                }
            }
        }
    };

}
