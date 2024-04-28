package jeu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.lang.String;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

public final class DrawingPanel implements ActionListener, MouseMotionListener {

    private static final String TITLE = "Gomoko";
    private static final int DELAY = 100;
    private int width, height;
    private JFrame frame;
    private JPanel panel;
    private ImagePanel imagePanel;
    private BufferedImage image;
    private Graphics2D g2;
    private Timer timer;
    private Color backgroundColor = Color.WHITE;
    private int initialPixel;

    private class ImagePanel extends JPanel {
        private static final long serialVersionUID = 0;
        private Image image;

        public ImagePanel(Image image) {
            setImage(image);
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            setAlignmentX(0.0f);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, this);
        }

        public void setImage(Image image) {
            this.image = image;
            repaint();
        }
    }

    public DrawingPanel(int width, int height) {
        this.width = width;
        this.height = height;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        initialPixel = image.getRGB(0, 0);
        g2 = (Graphics2D) image.getGraphics();
        g2.setColor(Color.BLACK);

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(backgroundColor);
        panel.setBounds(0, 0, width, height);
        imagePanel = new ImagePanel(image);
        imagePanel.setBackground(backgroundColor);
        panel.add(imagePanel);

        panel.addMouseMotionListener(this);

        frame = new JFrame(TITLE);
        JScrollPane center = new JScrollPane(panel);
        frame.getContentPane().add(center);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void addButton(JButton b) {
        panel.add(b);
    }

    public void addLabel(JLabel l) {
        panel.add(l);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            panel.repaint();
        }
    }

    public void addKeyListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    public void addMouseListener(MouseListener listener) {
        panel.addMouseListener(listener);
    }

    public void addMouseListener(MouseMotionListener listener) {
        panel.addMouseMotionListener(listener);
    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        panel.addMouseMotionListener(listener);
    }

    public void addMouseListener(MouseInputListener listener) {
        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
    }

    public void clear() {
        int[] pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = initialPixel;
        }
        image.setRGB(0, 0, width, height, pixels, 0, 1);
    }

    public Graphics2D getGraphics() {
        return g2;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public int getWidth() {
        return width;
    }

    public void setBackground(Color c) {
        backgroundColor = c;
        panel.setBackground(c);
        imagePanel.setBackground(c);
    }

    public void setHeight(int height) {
        setSize(getWidth(), height);
    }

    public void setSize(int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        imagePanel.setImage(newImage);
        newImage.getGraphics().drawImage(image, 0, 0, imagePanel);
        this.width = width;
        this.height = height;
        image = newImage;
        g2 = (Graphics2D) newImage.getGraphics();
        g2.setColor(Color.BLACK);

        frame.pack();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void toFront() {
        toFront(frame);
    }

    private void toFront(final Window window) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (window != null) {
                    window.toFront();
                    window.repaint();
                }
            }
        });
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

}
