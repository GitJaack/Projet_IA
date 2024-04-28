package page;

import javax.swing.JFrame;

public class GomokuBoard extends JFrame {
    public GomokuBoard() {
        setTitle("Gomoku Game");

        GomokuPanel panel = new GomokuPanel();
        add(panel);
        pack();

        setSize(panel.getPreferredSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GomokuBoard frame = new GomokuBoard();
            frame.setVisible(true);
        });
    }
}
