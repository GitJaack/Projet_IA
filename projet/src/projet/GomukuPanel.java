package projet;

import java.awt.BasicStroke;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GomukuPanel extends JPanel {
	// 1 est noir, blanc est 2, on commence par noir d'abord
	private int currentPlayer = Constante.BLACK;

	private final int CENTUEN = Constante.LINE_COUNT / 2; // position plateaux centre

	private int x = Constante.LINE_COUNT / 2; // Enregistrez les coordonnées des points d'intersection des lignes
	private int y = Constante.LINE_COUNT / 2; //

	private Node[][] chessBeans = new Node[Constante.LINE_COUNT][Constante.LINE_COUNT];

	private int count = 0;

	private boolean isGameOver = true; // enregistrer si le jeu est commencer ou pas

	private boolean isShowOrder = false; // enregistrer si voulez numero de nudosoit afficher

	private boolean mode; // humain contre humain
	private boolean inter; // humain contre robot

	private boolean evaluation;
	private boolean arbre;

	int level; // recherche profondeur
	int node; // node par couche

	Node chessBeansForTree;

	private TextArea area; // affichage test à droite

	public GomukuPanel() { // Constructeur, initialisation
		this.setPreferredSize(new Dimension(Constante.PANEL_WIDTH, Constante.PANEL_HEIGHT));
		this.setBackground(Color.ORANGE); // couleur de fond
		this.addMouseMotionListener(mouseMotionListener); // événement de déplacement de la souris
		this.addMouseListener(mouseListener); // événement de clic de souris

		for (int i = 0; i < Constante.LINE_COUNT; i++) { // initialisation
			for (int j = 0; j < Constante.LINE_COUNT; j++) {
				Node chessBean = new Node(i, j, Constante.EMPUTY, 0);
				chessBeans[i][j] = chessBean;
			}
		}
	}

	public void paintComponent(Graphics g) { // dessiner
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2)); // ligne grasse
		drawLine(g2d);// dessiner un échiquier
		drawStar(g2d); // 画天元和星
		drawTrips(g2d, x, y); // boîte de rappel
		drawNumber(g2d); // Coordonnées à côté de l'échiquier
		drawChess1(g2d);// Dessiner des pièces d'échecs
		drawOrderNum(g2d); // Imprimez les numéros sur les pièces d'échecs
	}

	// dessiner un échiquier
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
				Constante.LINE_COUNT / 4 * Constante.CELL_WIDTH + Constante.OFFSET - Constante.STAR_WIDTH / 2,Constante.STAR_WIDTH, Constante.STAR_WIDTH);
		// Étoile en bas à droite
		g2d.fillOval(
				(Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
				(Constante.LINE_COUNT - Constante.LINE_COUNT / 4) * Constante.CELL_WIDTH - Constante.STAR_WIDTH / 2,
				Constante.STAR_WIDTH, Constante.STAR_WIDTH);
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

	// boîte de rappel
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

	// afficher les chiffres
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
						int x = chssBean.getX() * Constante.CELL_WIDTH + Constante.OFFSET - width / 2; // Obtenez le centre de la pièce d'échecs
						int y = chssBean.getY() * Constante.CELL_WIDTH + Constante.OFFSET + height / 4;
						g2d.drawString(chssBean.getOrderNumber() + "", x, y); // Imprimez le numéro au milieu de la pièce d'échecs
					}
				}
			}
		}
		g2d.setColor(Color.BLACK); // Revenir à la couleur noire
	}

	// Obtenir le dernier échec
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

	// Obtenir les cordonnes dans le text rappel
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {
			int cx = e.getX(); // Obtenez les coordonnées avec la souris
			int cy = e.getY();
			if (cx >= Constante.OFFSET && cx <= Constante.OFFSET + (Constante.LINE_COUNT - 1) * Constante.CELL_WIDTH
					&& cy >= Constante.OFFSET
					&& cy <= Constante.OFFSET + (Constante.LINE_COUNT - 1) * Constante.CELL_WIDTH) { // Déterminez s'il se trouve sur l'échiquier
				x = (cx - Constante.OFFSET / 2) / Constante.CELL_WIDTH; // Convertir les coordonnées en points d'intersection des lignes internes de l'échiquier
				y = (cy - Constante.OFFSET / 2) / Constante.CELL_WIDTH;
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	};

	// Commencer un nouveau jeu
	public void playNewGame(boolean showNumber, boolean mode, boolean inter, int level, int node, boolean evaluation,
			boolean arbre, TextArea area, boolean robot) {
		// TODO 自动生成的方法存根
		this.mode = mode;
		this.inter = inter;
		this.level = level;
		this.node = node;
		this.evaluation = evaluation;
		this.arbre = arbre;
		this.area = area;

		area.setText("");

		isGameOver = false;
		count = 0;
		JOptionPane.showMessageDialog(GomukuPanel.this, "Le jeu a commencé！");
		for (int i = 0; i < Constante.LINE_COUNT; i++) {
			for (int j = 0; j < Constante.LINE_COUNT; j++) {
				Node chessBean = new Node(i, j, Constante.EMPUTY, count);
				chessBeans[i][j] = chessBean;
			}
		}
		if (inter && robot) {
			currentPlayer = Constante.BLACK;
			count++;
			Node temBean = new Node(CENTUEN, CENTUEN, currentPlayer, count);
			chessBeans[CENTUEN][CENTUEN] = temBean;
			currentPlayer = 3 - currentPlayer;
		}
		shouOrderNumber(showNumber);
		repaint();
	}

	// Regrettez les échecs en mode humain contre humain (Repentez-vous les échecs un par un)
	public void huiQi() {
		if (isGameOver) {
			JOptionPane.showMessageDialog(GomukuPanel.this, "Veuillez d'abord commencer un nouveau jeu！");
		} else {
			if (count > 0) {
				Node tempBean = getMaxNum();
				currentPlayer = tempBean.getPlayer();
				chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(Constante.EMPUTY); //
				chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
				count--;
				repaint();
			} else {
				JOptionPane.showMessageDialog(GomukuPanel.this, "Il n’y a plus de pièces d’échecs à regretter.！");
			}
		}
	}
	
	// Regrettez les échecs en mode humain contre robot (Repentez-vous les échecs un par un)
	public void huiQi2() { 
		if (isGameOver) {
			JOptionPane.showMessageDialog(GomukuPanel.this, "Veuillez d'abord commencer un nouveau jeu！");
		} else {
			if (count > 2) {
				for (int i = 0; i < 2; i++) {
					Node tempBean = getMaxNum();
					currentPlayer = tempBean.getPlayer();
					chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(Constante.EMPUTY); //
					chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
					count--;
					System.out.println("regretter les échecs");
					repaint();
				}
			} else {
				JOptionPane.showMessageDialog(GomukuPanel.this, "Tu n'as pas encore joué aux échecs！");
			}
		}
	}
	
	// Afficher les numéros sur les pièces d'échecs
	public void shouOrderNumber(boolean showNumber) {
		isShowOrder = showNumber;
		repaint();
	}

	private MouseListener mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (isGameOver) {
				JOptionPane.showMessageDialog(GomukuPanel.this, "Veuillez commencer un nouveau jeu");
				return;
			}

			int cx = e.getX(); // Obtenez les coordonnées de la souris
			int cy = e.getY();
			int a = (cx - Constante.OFFSET / 2) / Constante.CELL_WIDTH; // Convertir les coordonnées en points d'intersection des lignes internes de l'échiquier
			int b = (cy - Constante.OFFSET / 2) / Constante.CELL_WIDTH;

			if (e.getButton() == MouseEvent.BUTTON1) { // bouton gauche de la souris
				if (mode) { // mode humain contre humain
					if (a >= 0 && a < Constante.LINE_COUNT && b >= 0 && b < Constante.LINE_COUNT) { // Déterminez s'il se trouve sur l'échiquier
						if (chessBeans[a][b].getPlayer() == 0) { // Déterminez s'il y a une pièce d'échecs à ce stade
							count++;
							Node chessBean = new Node(a, b, currentPlayer, count);
							currentPlayer = 3 - currentPlayer; // apres noir est blanc
							chessBeans[a][b] = chessBean; // enregistrer l'échec de joueur actuel
							if (chessBeans[a][b].getPlayer() != 0)
								System.out.println(
										chessBeans[x][y].getPlayer() == Constante.BLACK ? "La pièce noire a fini de jouer aux échecs" : "La pièce blanc a fini de jouer aux échecs");
							checkWin(a, b, chessBeans[a][b].getPlayer());
						}
					}
				} else if (inter) {// mode humain contre robot,
					if (evaluation) { // fonction d'évaluation
						if (a >= 0 && a < Constante.LINE_COUNT && b >= 0 && b < Constante.LINE_COUNT) { // Déterminez s'il se trouve sur l'échiquier
							if (chessBeans[a][b].getPlayer() == 0) { // Déterminez s'il y a une pièce d'échecs à ce stade
								count++;
								chessBeans[a][b] = new Node(a, b, currentPlayer, count);
								System.out.println("La pièce blanc a fini de jouer aux échecs");
								currentPlayer = 3 - currentPlayer;
								checkWin(a, b, chessBeans[a][b].getPlayer());
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// L'ordinateur joue aux échecs, trouve celui qui a la valeur la plus élevée
									List<Node> orderList = getSortList(currentPlayer, chessBeans);
									Node bean = orderList.get(0);
									count++;
									a = bean.getX();
									b = bean.getY();
									bean.setPlayer(currentPlayer);
									bean.setOrderNumber(count);
									chessBeans[a][b] = bean;
									System.out.println("Echec noir（odinateur）a fini de jouer");
									currentPlayer = 3 - currentPlayer;
									checkWin(a, b, chessBeans[a][b].getPlayer());
								}
							}
						}
					} else if (arbre) { // Fonction de valorisation + arbre de recherche
						if (a >= 0 && a < Constante.LINE_COUNT && b >= 0 && b < Constante.LINE_COUNT) { // Déterminez s'il se trouve sur l'échiquier
							if (chessBeans[a][b].getPlayer() == 0) { // Déterminer si le point est représenté par son enfant
								count++;
								chessBeans[a][b] = new Node(a, b, currentPlayer, count);
								System.out.println("Pièce blanche a fini de jouer aux échecs");
								currentPlayer = 3 - currentPlayer;
								repaint();
								if (!checkWin(a, b, chessBeans[a][b].getPlayer())) {
									// L'ordinateur joue aux échecs, trouve celui qui a la valeur la plus élevée
									Node bean;

//									if (level == 3 && node >= 5 && count < 5) { // Uniquement pour réduire le temps passé sur l'ordinateur dans les étapes précédentes
//										List<ChessBean> orderList = getSortList(currentPlayer, chessBeans);
//										bean = orderList.get(0);
//									} else {
//										getValueByTrees2(0, currentPlayer, chessBeans, -Integer.MAX_VALUE,
//												Integer.MAX_VALUE);
//										bean = chessBeansForTree;
//									}
									getValueByTrees2(0, Constante.BLACK, chessBeans, -Integer.MAX_VALUE,
											Integer.MAX_VALUE);
									bean = chessBeansForTree;
									count++;
									a = bean.getX();
									b = bean.getY();
									bean.setPlayer(currentPlayer);
									bean.setOrderNumber(count);
									chessBeans[a][b] = bean;
									System.out.println("Echec noir（odinateur forte）a fini de jouer");
									// System.out.println(bean);
									currentPlayer = 3 - currentPlayer;
									checkWin(a, b, chessBeans[a][b].getPlayer());
									repaint();
								}
							}
						}
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				// clic-droit
				if (a >= 0 && a < Constante.LINE_COUNT && b >= 0 && b < Constante.LINE_COUNT) { // Déterminez s'il se trouve sur l'échiquier
					Node[][] temBeans = GomukuPanel.this.clone(chessBeans);
					int offense = getValue(a, b, currentPlayer, temBeans);
					// Calculer la valeur du point pour l'adversaire
					int defentse = getValue(a, b, 3 - currentPlayer, temBeans);

					temBeans[a][b].getBuffer().append("points(" + (a + 1) + "," + (b - 1) + ")de" + "attaque:" + offense + " "
							+ "défenfre:" + defentse + " " + "somme:" + (defentse + offense) + "\n\n");
					area.append(temBeans[a][b].getBuffer().toString());
					// }

				}
			}
			repaint();
		}
	};

	protected int getValueByTrees2(int d, int player, Node[][] chessBeans2, int alpha, int beta) { //
		Node[][] temBeans = clone(chessBeans2);
		List<Node> orderList = getSortList(player, temBeans);
		if (d == level) {
			// Atteignez la profondeur et la fin de recherche spécifiées. Revenir à l'étape actuelle. Le point avec la valeur la plus élevée obtenue.
			return orderList.get(0).getSum();
		}
		// Parcourez toutes les positions disponibles sur l'échiquier actuel (traverse getSortList）
		for (int i = 0; i < node; i++) {
			Node bean = orderList.get(0);
			int score;
			if (bean.getSum() > Level.ALIVE_4.score) {
				// trouver objectif
				score = bean.getSum();
			} else {
				// Cette étape consiste à simuler une partie d’échecs. Vous ne pouvez plus effectuer de mouvements sur le véritable échiquier.
				temBeans[bean.getX()][bean.getY()].setPlayer(player);
				// temBeans[bean.getX()][bean.getY()] = bean;
				score = getValueByTrees2(d + 1, 3 - player, temBeans, alpha, beta);
			}
			if (d % 2 == 0) {
				// Trouvez vous-même la valeur maximale
				if (score > alpha) {
					alpha = score;
					if (d == 0) {
						// resultat
						chessBeansForTree = bean;
						// System.out.println(chessBeansForTree);
					}
				}
				if (alpha >= beta) {
					// coupe
					score = alpha;
					return score;
				}
			} else {
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					// coupe
					score = beta;
					return score;
				}
			}
		}
		return d % 2 == 0 ? alpha : beta;
	}

	protected Node getValueByTrees(int d, int player, Node[][] chessBeans) {
		Node[][] tempBeans = clone(chessBeans);
		List<Node> orderList = getSortList(player, tempBeans);
		if (d == level) {
			return orderList.get(0); // La recherche se termine lorsque la profondeur spécifiée est atteinte. Revenir à l'étape actuelle. Obtenez le point de valorisation le plus élevé。
		}
		for (int i = 0; i < orderList.size(); i++) {
			// Parcourez toutes les positions disponibles sur l'échiquier actuel (traverse getSortList)
			System.out.println("i" + i);
			Node bean = orderList.get(i);
			if (bean.getSum() > Level.ALIVE_4.score) {
				return bean;
			} else {
				// Cette étape consiste à simuler une partie d’échecs. Vous ne pouvez plus effectuer de mouvements sur le véritable échiquier.
				tempBeans[bean.getX()][bean.getY()].setPlayer(player);
				return getValueByTrees(d + 1, 3 - player, tempBeans);
			}
		}
		return null;
	}

	private Node[][] clone(Node[][] chessBeans2) { // Cloner toutes les pièces d'échecs sur l'échiquier
		Node[][] temBeans = new Node[Constante.LINE_COUNT][Constante.LINE_COUNT];
		for (int i = 0; i < Constante.LINE_COUNT; i++) {
			for (int j = 0; j < Constante.LINE_COUNT; j++) {
				temBeans[i][j] = new Node(chessBeans2[i][j].getX(), chessBeans2[i][j].getY(),
						chessBeans2[i][j].getPlayer(), chessBeans2[i][j].getOrderNumber());
			}
		}
		return temBeans;
	}

	// Calculez les estimations des inconnues restantes, puis triez-les
	private List<Node> getSortList(int player, Node[][] tempBeans) { // <ChessBean>

		List<Node> list = new ArrayList<>();
		for (Node[] chessBeans2 : tempBeans) {
			for (Node chessBean : chessBeans2) {
				// Trouver point vide
				if (chessBean.getPlayer() == 0) {
					// Calculez la valeur du point de vous
					int offense = getValue(chessBean.getX(), chessBean.getY(), player, tempBeans);
					// Calculer la valeur du point pour l'adversaire
					int defentse = getValue(chessBean.getX(), chessBean.getY(), 3 - player, tempBeans);
					chessBean.setOffense(offense); // Obtenez la valeur d'attaque de ce point
					chessBean.setDefentse(defentse); // Obtenez la valeur de défense de ce point
					chessBean.setSum(offense + defentse); // Obtenez la somme du point
					list.add(chessBean);
				}
			}
		}
		Collections.sort(list); // Trier par valeur totale par ordre décroissant
		return list;
	}

	// Calculez le modèle d'échecs dans quatre directions et obtenez le niveau
	private int getValue(int x2, int y2, int player, Node[][] tempBeans) {
		Level level1 = getLevel(x2, y2, Direction.HENG, player, tempBeans);
		Level level2 = getLevel(x2, y2, Direction.SHU, player, tempBeans);
		Level level3 = getLevel(x2, y2, Direction.PIE, player, tempBeans);
		Level level4 = getLevel(x2, y2, Direction.NA, player, tempBeans);
		return levelScore(level1, level2, level3, level4) + position[x2][y2];
	}

	// Calculer le score d'un modèle d'échecs
	private int levelScore(Level level1, Level level2, Level level3, Level level4) {
		int[] levelCount = new int[Level.values().length];
		for (int i = 0; i < Level.values().length; i++) {
			levelCount[i] = 0;
		}
		// Comptez le nombre de fois qu'un certain modèle d'échecs apparaît
		levelCount[level1.index]++;
		levelCount[level2.index]++;
		levelCount[level3.index]++;
		levelCount[level4.index]++;

		int score = 0;
		if (levelCount[Level.GO_4.index] >= 2
				|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 双活4，冲4活三 / Deux 4 ouverts ou un 4 ouvert et un 3 ouvert
			score = 10000;
		else if (levelCount[Level.ALIVE_3.index] >= 2)// 双活3 / Deux 3 ouverts
			score = 5000;
		else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 活3眠3 / Un 3 ouvert et un 3 endormi
			score = 1000;
		else if (levelCount[Level.ALIVE_2.index] >= 2)// 双活2 / Deux 2 ouverts
			score = 100;
		else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// 活2眠2 / Un 2 ouvert et un 2 endormi
			score = 10;
		score = Math.max(score, Math.max(Math.max(level1.score, level2.score), Math.max(level3.score, level4.score)));
		return score;
	}

	private Level getLevel(int x2, int y2, Direction direction, int player, Node[][] tempBeans) {
		String leftString = "";
		String rightString = "";

		String str;

		if (direction == Direction.HENG) {
			leftString = getStringSeq(x2, y2, -1, 0, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, 0, player, tempBeans);
		} else if (direction == Direction.SHU) {
			leftString = getStringSeq(x2, y2, 0, -1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 0, 1, player, tempBeans);
		} else if (direction == Direction.PIE) {
			leftString = getStringSeq(x2, y2, -1, 1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, -1, player, tempBeans);
		} else if (direction == Direction.NA) {
			leftString = getStringSeq(x2, y2, -1, -1, player, tempBeans);
			rightString = getStringSeq(x2, y2, 1, 1, player, tempBeans);
		}

		str = leftString + player + rightString;

		tempBeans[x2][y2].getBuffer().append("(" + (x2 + 1) + "," + (y2 - 1) + ")" + direction + "\t" + str + "\t");

		// Obtenez la chaîne inversée du modèle d'échecs
		String rstr = new StringBuilder(str).reverse().toString();
		// Utiliser str et rstr pour effectuer une correspondance des modèles de pièces dans Level
		for (Level level : Level.values()) {
			Pattern pat = Pattern.compile(level.regex[player - 1]);
			Matcher mat = pat.matcher(str); // Sens normal
			// Si vrai, alors la correspondance est réussie
			boolean r1 = mat.find();
			mat = pat.matcher(rstr); // inverse
			// Si vrai, alors la correspondance est réussie
			boolean r2 = mat.find();
			if (r1 || r2) {
				tempBeans[x2][y2].getBuffer().append(level.name + "\n");
				if (direction == Direction.NA) {
					tempBeans[x2][y2].getBuffer().append("\n");
				}
				return level;
			}
		}
		return Level.NULL;
	}

	/// À partir de (x, y), il y a 8 lignes de directions, dont 4 restent les mêmes. Échangez le début et la fin de 4 chaînes
	private String getStringSeq(int x2, int y2, int i, int j, int player, Node[][] tempBeans) {
		String sum = "";
		boolean isRight = false;
		if (i < 0 || (i == 0 && j < 0)) {
			isRight = true;
		}
		for (int k = 0; k < 5; k++) {
			x2 += i;
			y2 += j;
			if (x2 > 0 && x2 < Constante.LINE_COUNT && y2 > 0 && y2 < Constante.LINE_COUNT) {
				if (isRight) {
					sum = tempBeans[x2][y2].getPlayer() + sum;
				} else {
					sum = sum + tempBeans[x2][y2].getPlayer();
				}
			}
		}
		return sum;
	}

	// Vérifiez s'il y a cinq pièces connectées
	protected boolean checkWin(int x2, int y2, int player) {
		boolean win = false;
		if (check(x2, y2, 1, 0, player) + check(x2, y2, -1, 0, player) >= 4) {// 检查横是否有五子相连
			win = true;
		} else if (check(x2, y2, 0, 1, player) + check(x2, y2, 0, -1, player) >= 4) {// 检查竖是否有五子相连
			win = true;
		} else if (check(x2, y2, 1, 1, player) + check(x2, y2, -1, -1, player) >= 4) {// 检查横是否有五子相连
			win = true;
		} else if (check(x2, y2, -1, 1, player) + check(x2, y2, 1, -1, player) >= 4) {// 检查横是否有五子相连
			win = true;
		}
		if (win) {
			// if(player==1)
			JOptionPane.showMessageDialog(GomukuPanel.this, "游戏已经结束");
			isGameOver = true;
			return true;
		}
		return false;
	}

	private int check(int x2, int y2, int i, int j, int player) {
		// Vérifiez 4 pièces dans une certaine direction (i, j)。
		int sum = 0;
		for (int k = 0; k < 4; k++) {
			x2 += i;
			y2 += j;
			if (x2 >= 0 && x2 < Constante.LINE_COUNT && y2 >= 0 && y2 < Constante.LINE_COUNT) {
				if (chessBeans[x2][y2].getPlayer() == player) {
					sum++;
				} else {
					break;
				}
			}
		}
		return sum;
	}

	// Informations sur les types de motifs d'échecs
	public static enum Level {
		CON_5("长连/Long cinq", 0, new String[] { "11111", "22222" }, 100000),
		ALIVE_4("活四/Quatre vivants", 1, new String[] { "011110", "022220" }, 10000),
		GO_4("冲四/Quatre forcé", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
		DEAD_4("死四/Quatre mort", 3, new String[] { "211112", "122221" }, -5),
		ALIVE_3("活三/Trois vivants", 4, new String[] { "01110|010110", "02220|020220" }, 200),
		SLEEP_3("眠三/Trois endormi", 5,
				new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
				50),
		DEAD_3("死三/Trois mort", 6, new String[] { "21112", "12221" }, -5),
		ALIVE_2("活二/Deux vivants", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
		SLEEP_2("眠二/Deux endormi", 8,
				new String[] { "000112|001012|010012|10001|2010102|2011002",
						"000221|002021|020021|20002|1020201|1022001" },
				3),
		DEAD_2("死二/Deux mort", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
		private String name;
		private int index;
		private String[] regex;// Expression régulière pour détecter le motif
		int score;// score

		// Méthode de construction
		private Level(String name, int index, String[] regex, int score) {
			this.name = name;
			this.index = index;
			this.regex = regex;
			this.score = score;
		}

		// Méthode de remplacement
		@Override
		public String toString() {
			return this.name;
		}
	};

	// direction
	private static enum Direction {
		HENG, SHU, PIE, NA
	};

	// Points de positionnement
	private static int[][] position = { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	

}