package snakejava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Graphics gscore;
    private int score;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        Carregar_imagens();
        initGame();
    }

    private void Carregar_imagens() {

        ImageIcon iid = new ImageIcon(this.getClass().getResource("dot.png"));
        Image imgBall = iid.getImage();
        BufferedImage biBall = new BufferedImage(imgBall.getWidth(null), imgBall.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics gBall = biBall.createGraphics();
        gBall.drawImage(imgBall, 0, 0, 10, 10, null);
        ImageIcon newBall = new ImageIcon(biBall);
        ball = newBall.getImage();

        ImageIcon iia = new ImageIcon(this.getClass().getResource("apple.png"));
        Image imgApple = iia.getImage();
        BufferedImage biApple = new BufferedImage(imgApple.getWidth(null), imgApple.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics gApple = biApple.createGraphics();
        gApple.drawImage(imgApple, 0, 0, 12, 12, null);
        ImageIcon newApple = new ImageIcon(biApple);
        apple = newApple.getImage();

        ImageIcon iih = new ImageIcon(this.getClass().getResource("head.png"));
        Image imgHead = iih.getImage();
        BufferedImage biHead = new BufferedImage(imgHead.getWidth(null), imgHead.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics gHead = biHead.createGraphics();
        gHead.drawImage(imgHead, 0, 0, 15, 15, null);
        ImageIcon newHead = new ImageIcon(biHead);        
        head = newHead.getImage();
    }
    
    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        Desenhar_maca();
        
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Desenha_snake(g);
    }
    
    private void Tela_Inicial(Graphics g){
        String Objetivo = "Faça o maior score possível";
        String Regra1 = "Use as setas do teclado para movimentar a snake.";
        String Regra2 = "Não colida com o próprio corpo";
        String Starta = "Aperte ENTER para começar";
        Font small = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(Objetivo, (B_WIDTH - metr.stringWidth(Objetivo) ) / 2, B_HEIGHT / 2);
        g.drawString(Regra1, (B_WIDTH - metr.stringWidth(Regra1)) / 2, (B_HEIGHT / 2) + 20);
        g.drawString(Regra2, (B_WIDTH - metr.stringWidth(Regra2)) / 2, (B_HEIGHT / 2) + 40);
        g.drawString(Starta, (B_WIDTH - metr.stringWidth(Starta)) / 2, (B_HEIGHT / 2) + 60);
        
        
           }
    private void Desenha_snake(Graphics g) {
        if (inGame) {
            Font small = new Font("Helvetica", Font.BOLD, 10);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString("" + score, 5, 10);
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        g.drawString("Score : " + score, (B_WIDTH - metr.stringWidth(msg)) / 2 , (B_HEIGHT / 2) + 20);
    }

    private void Colisao_maca() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            score++;
            Desenhar_maca();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void Colisao_parede() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] > B_HEIGHT) {
            y[0] = 0;
            Colisao_maca();
            inGame = true;
        }

        if (y[0] < 0) {
            y[0] = B_HEIGHT;
            inGame = true;
        }

        if (x[0] > B_WIDTH) {
            x[0] = 0;
            Colisao_maca();
            inGame = true;
        }

        if (x[0] < 0) {
            x[0] = B_WIDTH;
            inGame = true;
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void Desenhar_maca() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            Colisao_maca();
            //score();
            Colisao_parede();
            move();
        }

        repaint();
    }
 
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
        }
    }
}