import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;// not to start game automatically
    private int score = 0;// initial score
    private int totalBricks = 21;// total no of bricks

    private Timer timer;
    private int delay = 0;

    private int playerX = 310;// starting pos of player

    private int ballposX = 120;// starting pos of ball
    private int ballposY = 350;

    private int ballXdir = -1;// direction of ball
    private int ballYdir = -2;

    private MapGenerator map;

    Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks <= 0) {
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.CYAN);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("You won!Your score is  " + score, 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press Enter to Restart :  ", 230, 350);

        }
        if (ballposY > 570) {
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Game Over, score : " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press Enter to Restart :  ", 230, 350);

        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        // for the movement of ball
        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (brickRect.intersects(ballRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;

                        }
                    }

                }

            }
            ballposX += ballXdir;
            ballposY -= ballYdir;
        }

        if (ballposX < 0) {
            ballXdir = -ballXdir;
        }
        if (ballposY < 0) {
            ballYdir = -ballYdir;
        }
        if (ballposX > 670) {
            ballXdir = -ballXdir;
        }
        repaint();// to repaint the screen after prforming the right left movement action

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override // checking which key is pressed and peforming right action
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveright();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            if (playerX < 10) {
                playerX = 10;
            } else {
                moveleft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    public void moveright() {
        play = true;
        playerX += 20;
    }

    public void moveleft() {
        play = true;
        playerX -= 20;
    }

}