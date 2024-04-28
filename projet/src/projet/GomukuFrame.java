package projet;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class GomukuFrame extends JFrame{
	
	JButton       regrette;        //regrette node
	JButton       newGame;     //nouveaux jeu
	JCheckBox     showNumber;  //afficher les nombres
	GomukuPanel   gomukuPanel; 
	JRadioButton  humainContreH;      //humain contre humain
	JRadioButton  humainContreR;       //humain contre robot
	JRadioButton  evaluation;       //fonction d'évaluation
	JRadioButton  arbre;   //arbre de recherche
	JPanel        jPanel1;         
	TextArea      textArea;    //affichage test à droite

	JRadioButton  human;   //humain commence d'abord
	JRadioButton  robot;   //robot commence d'abord

	JComboBox<Integer> profondeur;  //recherche profondeur
	JComboBox<Integer> node;  //recherche node

	public void start() {
		gomukuPanel = new GomukuPanel();
		this.add(gomukuPanel, BorderLayout.WEST);  // petit fenetre à droite à cote du plateaux

		JPanel rightPanel = new JPanel();          // domaine fonctionnel
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		jPanel1 = new JPanel(new BorderLayout());   // zone de texte multiligne
		jPanel1.setBorder(new TitledBorder("Faites un clic droit sur le clavier pour afficher les évaluations individuelles"));
		textArea = new TextArea();
		textArea.setEditable(false);
		jPanel1.add(textArea);
		rightPanel.add(jPanel1);

		// mode
		JPanel jPanel2 = new JPanel();      
		jPanel2.setBorder(new TitledBorder("mode"));
		ButtonGroup bg = new ButtonGroup(); // il permet de choisir juste un radiobutton
		humainContreH = new JRadioButton("humain contre humain");
		humainContreR = new JRadioButton("humain contre robot");
		humainContreH.setSelected(true);           // defaut humain contre humain
		bg.add(humainContreH);
		bg.add(humainContreR);
		jPanel2.add(humainContreH);
		jPanel2.add(humainContreR);
		rightPanel.add(jPanel2);

		// IA
		JPanel jPanel3 = new JPanel();      
		jPanel3.setBorder(new TitledBorder("IA"));
		ButtonGroup bg1 = new ButtonGroup(); // il permet de choisir juste un radiobutton
		evaluation = new JRadioButton("fonction d'évaluation");
		arbre = new JRadioButton("fonction d'évaluation+arbre de recherche");
		evaluation.setSelected(true);
		bg1.add(evaluation);
		bg1.add(arbre);
		jPanel3.add(evaluation);
		jPanel3.add(arbre);
		rightPanel.add(jPanel3);

		// arbre de recherche
		JPanel jPanel4 = new JPanel();
		jPanel4.setBorder(new TitledBorder("arbre de recherche"));
		JLabel jLabel1 = new JLabel("recherche profondeur：");
		profondeur = new JComboBox<Integer>(new Integer[] { 1, 2, 3 });
		JLabel jLabel2 = new JLabel("recherche node：");
		node = new JComboBox<Integer>(new Integer[] { 3, 5, 10 });
		jPanel4.add(jLabel1);
		jPanel4.add(profondeur);
		jPanel4.add(jLabel2);
		jPanel4.add(node);
		rightPanel.add(jPanel4);

		// autre
		JPanel jPanel5 = new JPanel();
		regrette = new JButton("regrette de node");
		jPanel5.setBorder(new TitledBorder("autre"));
		JLabel jLabel3 = new JLabel("afficher l'ordre de node：");
		showNumber = new JCheckBox();
		jPanel5.add(jLabel3);
		jPanel5.add(showNumber);
		showNumber.addMouseListener(mouseListener);
		regrette.addMouseListener(mouseListener);    // ajouter action
		jPanel5.add(regrette);
		rightPanel.add(jPanel5);
		newGame = new JButton("nouveaux jeu");
		rightPanel.add(newGame);
		newGame.addMouseListener(mouseListener);  // ajouter action
		this.add(rightPanel);

		// mode humain contre robot
		JPanel jPanel6 = new JPanel();
		jPanel6.setBorder(new TitledBorder("humain contre robot"));
		ButtonGroup bg2 = new ButtonGroup(); 
		human = new JRadioButton("humain d'abord");
		robot = new JRadioButton("robot d'abord");
		robot.setSelected(true);
		bg2.add(human);
		bg2.add(robot);
		jPanel6.add(human);
		jPanel6.add(robot);
		rightPanel.add(jPanel6);

		this.setTitle("Gomuko");    // titre
		this.setSize(Constante.GAME_WIDTH, Constante.GAME_HEIGHT); // grand petit
		this.setLocation(450, 150);   // placement initial
		this.setResizable(false);     // pas possible de agrandir
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			Object object = e.getSource();
			if (object == regrette) {  
				if (humainContreH.isSelected()) {
					gomukuPanel.huiQi();
				} else if (humainContreR.isSelected()) {
					gomukuPanel.huiQi2();
				}
			} else if (object == newGame) {   // nouveaux jeu
				int level = (Integer) profondeur.getSelectedItem();
				int node1 = (Integer) node.getSelectedItem();
				gomukuPanel.playNewGame(showNumber.isSelected(), humainContreH.isSelected(), humainContreR.isSelected(), level, node1,
						evaluation.isSelected(), arbre.isSelected(), textArea, robot.isSelected());
			} else if (object == showNumber) {   // afficher les nombres
				gomukuPanel.shouOrderNumber(showNumber.isSelected());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
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
	};

}
