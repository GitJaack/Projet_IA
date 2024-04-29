package page;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class GomokuBoard extends JFrame {
    GomokuPanel gomokuPanel;

    JButton newGame;

    JButton reverse;
    JCheckBox showNumber;

    JRadioButton humainContreH;
    JRadioButton humainContreR;
    JRadioButton robotContreR;

    JRadioButton difficulte;

    JRadioButton human;
    JRadioButton robot;

    JComboBox<String> profondeur1;
    JComboBox<String> profondeur2;
    JComboBox<String> profondeur3;

    JPanel rightPanel;

    public GomokuBoard() {
        GomokuPanel gomokuPanel = new GomokuPanel();
        this.add(gomokuPanel, BorderLayout.WEST);

        this.rightPanel = new JPanel();

        panelMode();
        panelDifficulte();
        panelDifficulteRobot();
        panelAutre();
        panelFirstPlay();

        newGame = new JButton("Nouveaux jeu");
        rightPanel.add(newGame);

        this.setTitle("Gomuko");
        this.setSize(Constante.GAME_WIDTH, Constante.GAME_HEIGHT);
        this.setLocation(450, 150);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void panelMode() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(new TitledBorder("Mode"));

        ButtonGroup bg = new ButtonGroup(); // il permet de choisir juste un radiobutton

        humainContreH = new JRadioButton("Humain contre Humain");
        humainContreR = new JRadioButton("Humain contre Robot");
        robotContreR = new JRadioButton("Robot contre Robot");

        humainContreH.setSelected(true); // defaut humain contre humain

        bg.add(humainContreH);
        bg.add(humainContreR);
        bg.add(robotContreR);

        jPanel.add(humainContreH);
        jPanel.add(humainContreR);
        jPanel.add(robotContreR);

        rightPanel.add(jPanel);
    }

    private void panelDifficulte() {
        JPanel jPanel = new JPanel();

        jPanel.setBorder(new TitledBorder("Difficulte Humain vs Robot"));

        JLabel jLabel1 = new JLabel("Recherche profondeur：");
        profondeur1 = new JComboBox<String>(new String[] { "Facile", "Moyen", "Difficile" });

        jPanel.add(jLabel1);
        jPanel.add(profondeur1);

        rightPanel.add(jPanel);
    }

    private void panelDifficulteRobot() {
        JPanel jPanel = new JPanel();

        jPanel.setBorder(new TitledBorder("Difficulte Robot vs Robot"));
        JLabel jLabel1 = new JLabel("Recherche profondeur：");
        profondeur2 = new JComboBox<String>(new String[] { "Facile", "Moyen", "Difficile" });

        JLabel jLabel2 = new JLabel("Recherche profondeur：");
        profondeur3 = new JComboBox<String>(new String[] { "Facile", "Moyen", "Difficile" });

        jPanel.add(jLabel1);
        jPanel.add(profondeur2);
        jPanel.add(jLabel2);
        jPanel.add(profondeur3);

        rightPanel.add(jPanel);
    }

    private void panelAutre() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(new TitledBorder("Autre"));
        reverse = new JButton("retourner en arrière");
        JLabel jLabel2 = new JLabel("Afficher l'ordre des noeuds : ");
        showNumber = new JCheckBox();
        jPanel.add(jLabel2);
        jPanel.add(showNumber);
        // showNumber.addMouseListener(mouseListener);
        // reverse.addMouseListener(mouseListener); // ajouter action
        jPanel.add(reverse);
        rightPanel.add(jPanel);
        // newGame.addMouseListener(mouseListener); // ajouter action
        this.add(rightPanel);
    }

    private void panelFirstPlay() {
        JPanel jPanel = new JPanel();

        jPanel.setBorder(new TitledBorder("Premier à jouer"));

        ButtonGroup bg2 = new ButtonGroup();
        human = new JRadioButton("Humain d'abord");
        robot = new JRadioButton("Robot d'abord");

        human.setSelected(true);

        bg2.add(human);
        bg2.add(robot);

        jPanel.add(human);
        jPanel.add(robot);

        rightPanel.add(jPanel);
    }

    // private MouseListener mouseListener = new MouseListener() {
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // Object object = e.getSource();

    // if (object == reverse) {
    // if (humainContreH.isSelected()) {
    // gomokuPanel.huiQi();
    // } else if (humainContreR.isSelected()) {
    // gomokuPanel.huiQi2();
    // } else if (robotContreR.isSelected()){
    // gomokuPanel.
    // }

    // } else if (object == newGame) {
    // int level = (Integer) profondeur.getSelectedItem();
    // gomokuPanel.playNewGame(showNumber.isSelected(), humainContreH.isSelected(),
    // humainContreR.isSelected(), level,
    // evaluation.isSelected(), difficulte.isSelected(), robot.isSelected());
    // } else if (object == showNumber) { // afficher les nombres
    // gomokuPanel.shouOrderNumber(showNumber.isSelected());
    // }
    // }

    // @Override
    // public void mousePressed(MouseEvent e) {
    // }

    // @Override
    // public void mouseReleased(MouseEvent e) {
    // }

    // @Override
    // public void mouseEntered(MouseEvent e) {
    // }

    // @Override
    // public void mouseExited(MouseEvent e) {

    // }
    // };

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GomokuBoard frame = new GomokuBoard();
            frame.setVisible(true);
        });
    }
}
