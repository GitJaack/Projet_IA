package jeu;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Ai {
    //
    private static DrawingPanel panel = new DrawingPanel(715, 715);
    private static Graphics g = panel.getGraphics();
    public static boolean isBlack = false;// couleur de piece actuelle
    public static int[][] chessBoard = new int[17][17]; // grille: 0 vide, 1 noir, -1 blanc
    private static HashSet<Point> toJudge = new HashSet<Point>(); // les pieces possibles d'IA
    private static int dr[] = new int[] { -1, 1, -1, 1, 0, 0, -1, 1 }; // directions
    private static int dc[] = new int[] { 1, -1, -1, 1, -1, 1, 0, 0 };
    public static final int MAXN = 1 << 28; // poids max
    public static final int MINN = -MAXN; // poids min
    private static int searchDeep = 3; // profondeur
    private static final int size = 15; // taille
    public static boolean isFinished = false; // finir ou pas

    // boutons
    static JButton easy, normal, difficult;
    static JLabel label;

    public static void main(String[] args) {
        MyMouseEvent myMouseEvent = new MyMouseEvent();
        panel.addMouseListener(myMouseEvent);
        initChessBoard();
    }

    // initialisation de la grille
    public static void initChessBoard() {
        isBlack = false;
        toJudge.clear();
        panel.clear();

        showDifficultyDialog();
        panel.setBackground(Color.ORANGE);
        g.setColor(Color.BLACK);
        for (int i = 45; i <= 675; i += 45) {
            g.drawLine(45, i, 675, i);
            g.drawLine(i, 45, i, 675);
        }

        // petits points noirs
        g.setColor(Color.BLACK);
        g.fillOval(353, 353, 13, 13);
        g.fillOval(218, 218, 13, 13);
        g.fillOval(488, 218, 13, 13);
        g.fillOval(488, 488, 13, 13);
        g.fillOval(218, 488, 13, 13);
        // initialisation
        for (int i = 1; i <= 15; ++i)
            for (int j = 1; j <= 15; ++j)
                chessBoard[i][j] = 0;
        // IA d'abord
        g.fillOval(337, 337, 45, 45);
        chessBoard[8][8] = 1;
        for (int i = 0; i < 8; ++i)
            if (1 <= 8 + dc[i] && 8 + dc[i] <= size && 1 <= 8 + dr[i] && 8 + dr[i] <= size) {
                Point now = new Point(8 + dc[i], 8 + dr[i]);
                if (!toJudge.contains(now))
                    toJudge.add(now);
            }
        isBlack = false;

    }

    // choix des difficultes
    private static void showDifficultyDialog() {
        JDialog difficultyDialog = new JDialog();
        difficultyDialog.setTitle("choose difficulty");
        difficultyDialog.setSize(300, 100);
        difficultyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        difficultyDialog.setResizable(false);
        difficultyDialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        easy = new JButton("easy");
        normal = new JButton("normal");
        difficult = new JButton("difficult");

        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDeep = 1;
                difficultyDialog.dispose();

            }
        });

        normal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDeep = 3;
                difficultyDialog.dispose();

            }
        });

        difficult.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchDeep = 5;
                difficultyDialog.dispose();

            }
        });

        panel.add(easy);
        panel.add(normal);
        panel.add(difficult);

        difficultyDialog.add(panel);
        difficultyDialog.setVisible(true);
    }

    // mettre la piece
    public static void putChess(int x, int y) {
        if (isBlack)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.WHITE);
        g.fillOval(x - 22, y - 22, 45, 45);//

        chessBoard[y / 45][x / 45] = isBlack ? 1 : -1;
        if (isEnd(x / 45, y / 45)) {
            String s = Ai.isBlack ? "noir a gagné" : "blanc a gagné";
            JOptionPane.showMessageDialog(null, s);
            isBlack = true;
            initChessBoard();
        } else {
            Point p = new Point(x / 45, y / 45);
            if (toJudge.contains(p))
                toJudge.remove(p);
            for (int i = 0; i < 8; ++i) {
                Point now = new Point(p.x + dc[i], p.y + dr[i]);
                if (1 <= now.x && now.x <= size && 1 <= now.y && now.y <= size && chessBoard[now.y][now.x] == 0)
                    toJudge.add(now);
            }
        }
    }

    // IA
    public static void myAI() {
        Node node = new Node();
        dfs(0, node, MINN, MAXN, null);
        Point now = node.bestChild.p;
        // toJudge.remove(now);
        putChess(now.x * 45, now.y * 45);
        isBlack = false;
    }

    // alpha beta dfs
    private static void dfs(int deep, Node root, int alpha, int beta, Point p) {
        // le profondeur de dfs
        if (deep == searchDeep) {
            root.mark = getMark();
            // System.out.printf("%d\t%d\t%d\n",p.x,p.y,root.mark);
            return;
        }
        ArrayList<Point> judgeSet = new ArrayList<Point>();
        Iterator it = toJudge.iterator(); // recuperer les positions possibles
        while (it.hasNext()) {
            Point now = new Point((Point) it.next());
            judgeSet.add(now);
        }
        it = judgeSet.iterator();
        while (it.hasNext()) { // ajouter les positions possibles dans l'arbre de recherche
            Point now = new Point((Point) it.next());
            Node node = new Node();
            node.setPoint(now);
            root.addChild(node);
            boolean flag = toJudge.contains(now);
            chessBoard[now.y][now.x] = ((deep & 1) == 1) ? -1 : 1;
            if (isEnd(now.x, now.y)) {
                root.bestChild = node;
                root.mark = MAXN * chessBoard[now.y][now.x];
                chessBoard[now.y][now.x] = 0;
                return;
            }

            boolean flags[] = new boolean[8]; // 8 orientations
            Arrays.fill(flags, true); // initialiser 8 orientations en true
            for (int i = 0; i < 8; ++i) { //
                Point next = new Point(now.x + dc[i], now.y + dr[i]);
                if (1 <= now.x + dc[i] && now.x + dc[i] <= size && 1 <= now.y + dr[i] && now.y + dr[i] <= size
                        && chessBoard[next.y][next.x] == 0) { // si position prochaine vide
                    if (!toJudge.contains(next)) { // si dans toJudge
                        toJudge.add(next);
                    } else
                        flags[i] = false; // sinon cette orientation fausse
                }
            }

            if (flag)
                toJudge.remove(now);
            dfs(deep + 1, root.getLastChild(), alpha, beta, now); // profondeur +1
            chessBoard[now.y][now.x] = 0;
            if (flag)
                toJudge.add(now);
            for (int i = 0; i < 8; ++i)
                if (flags[i])
                    toJudge.remove(new Point(now.x + dc[i], now.y + dr[i])); // supprime les positions true
            // alpha beta
            // niveau min
            if ((deep & 1) == 1) {
                if (root.bestChild == null || root.getLastChild().mark < root.bestChild.mark) {
                    root.bestChild = root.getLastChild();
                    root.mark = root.bestChild.mark;
                    if (root.mark <= MINN)
                        root.mark += deep;
                    beta = Math.min(root.mark, beta);
                }
                if (root.mark <= alpha)
                    return;
            }
            // niveau max
            else {
                if (root.bestChild == null || root.getLastChild().mark > root.bestChild.mark) {
                    root.bestChild = root.getLastChild();
                    root.mark = root.bestChild.mark;
                    if (root.mark == MAXN)
                        root.mark -= deep;
                    alpha = Math.max(root.mark, alpha);
                }
                if (root.mark >= beta)
                    return;
            }
        }
        // if(deep==0)
        // System.out.printf("******************************************\n");
    }

    // permet d'évaluer les scores
    public static int getMark() {
        int res = 0;
        // Créer une matrice pour évaluer le score de chaque position
        int[][] scoreMatrix = new int[size + 1][size + 1];
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                if (chessBoard[i][j] != 0) {
                    // Attribution de poids selon le type
                    int weight = getWeight(i, j, chessBoard[i][j]);
                    scoreMatrix[i][j] = weight;
                    res += weight * chessBoard[i][j];
                    // System.out.println(scoreMatrix[i][j]);
                }
            }
        }
        return res;
    }

    // obtenir le poid
    public static int getWeight(int row, int col, int chess) {
        int weight = 0;
        // evaluation
        int[] patternScores = { 0, 10, 50, 200, 1000, 10000 };

        // 4 directions
        int[][] directions = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 } };

        // Parcourir 4 directions, calculez séparément le score dans chaque direction
        for (int[] direction : directions) {
            int count = 1; // Nombre de pièces consécutives
            int empty = 0; // Nombre de case vide
            int blocked = 0; // Nombre de bouchons

            // verifier le type vers droite, haut et haut-droite
            for (int i = 1; i <= 4; i++) {
                int newRow = row + direction[0] * i;
                int newCol = col + direction[1] * i;
                if (newRow > 0 && newRow <= size && newCol > 0 && newCol <= size) {
                    if (chessBoard[newRow][newCol] == chess) {
                        count++;
                    } else {
                        if (chessBoard[newRow][newCol] == 0) {
                            empty++;
                        } else {
                            blocked++;
                        }
                        break;
                    }
                } else {
                    blocked++;
                    break;
                }
            }

            // verifier le type vers gauche, bas et bas-gauche
            for (int i = 1; i <= 4; i++) {
                int newRow = row - direction[0] * i;
                int newCol = col - direction[1] * i;
                if (newRow > 0 && newRow <= size && newCol > 0 && newCol <= size) {
                    if (chessBoard[newRow][newCol] == chess) {
                        count++;
                    } else {
                        if (chessBoard[newRow][newCol] == 0) {
                            empty++;
                        } else {
                            blocked++;
                        }
                        break;
                    }
                } else {
                    blocked++;
                    break;
                }
            }

            // donner l'evaluation à tous les types
            int score;
            if (count >= 5) {
                score = patternScores[5];
            } else if (count == 4) {
                if (empty == 2) {
                    score = patternScores[4];
                } else if (empty == 1) {
                    score = patternScores[3];
                } else {
                    score = 0;
                }
            } else if (count == 3) {
                if (empty == 2) {
                    score = patternScores[2];
                } else if (empty == 1) {
                    score = patternScores[1];
                } else {
                    score = 0;
                }
            } else {
                score = 0;
            }

            // mise a jour de score
            weight += score;
        }

        return weight;
    }

    // verifier si gagner
    public static boolean isEnd(int x, int y) {
        if (checkLine(x, y, 1, 0) || checkLine(x, y, 0, 1) || checkLine(x, y, 1, 1) || checkLine(x, y, 1, -1)) {
            isFinished = true;
            return true;
        }
        return false;
    }

    // Methode qui permet de verifier les types des pieces actuels( Nombre de pièces
    // consécutives,nombre de case vide, nombre de bouchon...)
    public static boolean checkLine(int x, int y, int dx, int dy) {
        int cnt = 1;
        int col = x, row = y;
        while (col + dx > 0 && col + dx <= size && row + dy > 0 && row + dy <= size
                && chessBoard[row + dy][col + dx] == chessBoard[y][x]) {
            cnt++;
            col += dx;
            row += dy;
        }
        col = x;
        row = y;
        while (col - dx > 0 && col - dx <= size && row - dy > 0 && row - dy <= size
                && chessBoard[row - dy][col - dx] == chessBoard[y][x]) {
            cnt++;
            col -= dx;
            row -= dy;
        }
        return cnt >= 5;
    }

}

// classe noeud
class Node {
    public Node bestChild = null; // le meilleur coup
    public ArrayList<Node> child = new ArrayList<Node>(); // liste qui stocke tous les fils
    public Point p = new Point(); // coordonnnees du point actuel
    public int mark; // score de point actuel

    Node() {
        this.child.clear();
        bestChild = null;
        mark = 0;
    }

    public void setPoint(Point r) {
        p.x = r.x;
        p.y = r.y;
    }

    public void addChild(Node r) {
        this.child.add(r);
    }

    public Node getLastChild() {
        return child.get(child.size() - 1);
    }
}

// MouseListenner
class MyMouseEvent implements MouseListener {
    public void mouseClicked(MouseEvent e) {
        int x = round(e.getX()), y = round(e.getY());
        if (x >= 45 && x <= 675 && y >= 45 && y <= 675 && Ai.chessBoard[y / 45][x / 45] == 0 && Ai.isBlack == false) {
            Ai.putChess(x, y);
            if (!Ai.isFinished) {
                Ai.isBlack = true;
                Ai.myAI();
            }
            Ai.isFinished = false;
        }
    }

    // obtenir les coordonnees des points autour de point actuel
    public static int round(int x) {
        return (x % 45 < 22) ? x / 45 * 45 : x / 45 * 45 + 45;
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}
