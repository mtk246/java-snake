package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 120;
    final int x[] = new int[GAME_UNIT];
    final int y[] = new int[GAME_UNIT];
    int bodyPart = 5;
    int appleEaten;
    int appleX;
    int appleY;

    int appleX1;
    int appleY1;
    int appleX2;
    int appleY2;
    int boomX;
    int boomY;
    int boomX1;
    int boomY1;
    int boomX2;
    int boomY2;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    private ImageIcon bgIcon;
    private boolean paused = true;
    private String pauseMessage = "Press Space Bar To Start"; // message to display when game is paused
    private Clip backgroundMusic;

    GamePanel() {
        try {
            URL imageUrl = getClass().getResource("/snake/Snake/grass.jpg");
            if (imageUrl != null) {
                BufferedImage image = ImageIO.read(imageUrl);
                bgIcon = new ImageIcon(image);
            } else {
                // Handle the case where the resource is not found
                // You can set a default image from the web here
                imageUrl = new URL("https://repository-images.githubusercontent.com/272254927/61ad4580-ae9f-11ea-9e05-309ce17f1d80");
                BufferedImage image = ImageIO.read(imageUrl);
                bgIcon = new ImageIcon(image);
            }
        } catch (IOException e) {
            // Handle the case where there is an IO error
            e.printStackTrace();
        }

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
        playBackgroundMusic();
    }


    public void startGame() {
        newApple();
        newApple_1();
        newApple_2();
        newBoom();
        newBoom1();
        newBoom2();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void playBackgroundMusic() {
        try {
            File audioFile = new File("src/snake/Res/Deer.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            backgroundMusic.start(); // Start playing
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void togglepause() {
        paused = !paused;
        if (!paused) {
            timer.restart();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = bgIcon.getImage();
        g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_WIDTH, this);
        draw(g);

    }

    public void draw(Graphics g) {

        if (paused) {
            g.setColor(Color.white);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            int messageWidth = metrics.stringWidth(pauseMessage);
            g.drawString(pauseMessage, (SCREEN_HEIGHT - messageWidth) / 2, SCREEN_HEIGHT / 2);
        }

        if (running) {
//            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
//                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); // width, height draw line
//                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//            }

            ImageIcon appleIcon = new ImageIcon(getClass().getResource("/snake/Snake/apple_07.png"));
            Image appleImage = appleIcon.getImage();
            g.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);

            ImageIcon appleIcon_1 = new ImageIcon(getClass().getResource("/snake/Snake/apple_17.png"));
            Image appleImage_1 = appleIcon_1.getImage();
            g.drawImage(appleImage_1, appleX1, appleY1, UNIT_SIZE, UNIT_SIZE, this);

            ImageIcon appleIcon_2 = new ImageIcon(getClass().getResource("/snake/Snake/apple_06.png"));
            Image appleImage_2 = appleIcon_2.getImage();
            g.drawImage(appleImage_2, appleX2, appleY2, UNIT_SIZE, UNIT_SIZE, this);

//            g.setColor(Color.white);
//            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            ImageIcon boomIcon = new ImageIcon(getClass().getResource("/snake/Snake/bomb.png"));
            Image boomImage = boomIcon.getImage();
            g.drawImage(boomImage, boomX, boomY, UNIT_SIZE, UNIT_SIZE, this);
//            g.setColor(Color.white);
//            g.fillRect(boomX, boomY, UNIT_SIZE , UNIT_SIZE );

            ImageIcon boom_1_Icon = new ImageIcon(getClass().getResource("/snake/Snake/bomb.png"));
            Image boom_1_Image = boom_1_Icon.getImage();
            g.drawImage(boom_1_Image, boomX1, boomY1, UNIT_SIZE, UNIT_SIZE, this);

//            g.setColor(Color.red);
//            g.fillRect(boomX1, boomY1, UNIT_SIZE , UNIT_SIZE );

            ImageIcon boom_2_Icon = new ImageIcon(getClass().getResource("/snake/Snake/bomb.png"));
            Image boom_2_Image = boom_2_Icon.getImage();
            g.drawImage(boom_2_Image, boomX2, boomY2, UNIT_SIZE, UNIT_SIZE, this);

//            g.setColor(Color.blue);
//            g.fillRect(boomX2, boomY2, UNIT_SIZE , UNIT_SIZE );

            ImageIcon snakeHead = new ImageIcon(getClass().getResource("/snake/Snake/snakeHead.png"));
            Image snakeImage = snakeHead.getImage();


            for (int i = 0; i < bodyPart; i++) {
                if (i == 0) {
                    g.drawImage(snakeImage, x[i], y[i], UNIT_SIZE, UNIT_SIZE, this);
//                    g.setColor(Color.blue);
//                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(231, 157, 19));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, (SCREEN_HEIGHT - metrics.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);

        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newApple_1() {
        appleX1 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY1 = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newApple_2() {
        appleX2 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY2 = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newBoom() {
        boomX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        boomY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newBoom1() {
        boomX1 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        boomY1 = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newBoom2() {
        boomX2 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        boomY2 = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyPart; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyPart++;
            appleEaten += 5;
            newApple();
            newBoom();
        }
    }

    public void checkApple_1() {
        if (x[0] == appleX1 && y[0] == appleY1) {
            bodyPart++;
            appleEaten += 5;
            newApple_1();
            newBoom1();
        }
    }

    public void checkApple_2() {
        if (x[0] == appleX2 && y[0] == appleY2) {
            bodyPart++;
            appleEaten += 5;
            newApple_2();
            newBoom2();
        }
    }

    public void checkCollisions() {
        for (int i = bodyPart; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (x[0] == boomX1 && y[0] == boomY1) {
            running = false;
        }
        if (x[0] == boomX && y[0] == boomY) {
            running = false;
        }
        if (x[0] == boomX2 && y[0] == boomY2) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }


    public void gameOver(Graphics g) {
//        Game Over text
        g.setColor(Color.red);
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over, You're Noob", (SCREEN_HEIGHT - metrics.stringWidth("Game Over, You're Noob")) / 2, SCREEN_HEIGHT / 2);
        g.drawString("Your Score: " + appleEaten, (SCREEN_HEIGHT - metrics.stringWidth("Your Score: " + appleEaten)) / 2, SCREEN_HEIGHT / 4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running && !paused) { // check if not paused
            move();
            checkApple();
            checkApple_1();
            checkApple_2();
            checkCollisions();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    togglepause();
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }
    }
}