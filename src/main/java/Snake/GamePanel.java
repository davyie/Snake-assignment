package Snake;

/**
 * Graphics library
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Utils library
 */
import java.util.Random;

/**
 * This is an extra assignment for my mentees that wants to improve their programming.
 * The assignment is to add functionality where the snake has a body. You can use whatever solution you want
 * but I suggest to follow the ambiguous TODO comments.
 * Right now the snake only has a head that is moving. Look out for the two TODO comments
 * because they give you guidelines on how to add the functionality.
 *
 * Author: Software Development Academy
 * Date: 2020-10-22
 */
public class GamePanel extends JPanel implements ActionListener {

    // General game properties
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    boolean running = false; // Game is running
    Timer timer; // Timer that controls the speed of the game
    Random random; // Random generator number.

    /**
     * Properties of Snake
     * Four directions are
     * 1) North - 'N'
     * 2) South - 'S'
     * 3) West - 'W'
     * 4) East - 'E'
     * Important variables for this assignment is bodyParts, x[] and y[].
     */
    final int x[] = new int[GAME_UNITS]; // This is the X positions of the Snake
    final int y[] = new int[GAME_UNITS]; // This is the Y positions of the snake
    int bodyParts = 1; // This is the number of body parts the snake has
    char direction = 'E'; // Start pointing to east
    int applesEaten; // The number of apples the snake has eaten

    /**
     * Properties of apple
      */
    int appleX; // The apple X position
    int appleY; // The apple Y position

    /**
     * Constructor
     */
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Sets the size of the panel
        this.setBackground(Color.BLACK); // Sets the background
        this.setFocusable(true); // sets focus
        this.addKeyListener(new MykeyAdapter()); // add a key listener so we can get user input
        startGame(); // start the game
    }

    /**
     * Start the game.
     * We can randomize a position for the snake here but as for now the snake starts at (0,0).
     */
    public void startGame() {
        newApple(); // Generate apple on the board
        running = true;
        timer = new Timer(DELAY, this); // Create the time with a delay and takes a listener that is passed.
        timer.start();
    }

    /**
     * This paints components on the panel
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Draw everything on the board. Both apple and snake
    }

    public void draw(Graphics g) {
        if (running) {

            drawGrid(g); // Draw a grid if you want. Comment out if you want

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // TODO: Draw the body parts on the panel. Right now it only draws the head.
            // TODO: To draw all the body parts we will have to check for each body part where the position is
            // TODO: Remember that x[] and y[] holds all the positions.
            // TODO: Once you have gotten the positions you can use g.setColor() and g.fillRect() to draw the snake.
            // TODO: You can look at how the head is drawn.

            g.setColor(Color.green); // Set the color of the snake

            // draw the whole body. x[0], y[0] is a head, x[1], y[1] is a neck and so on
            for (int i = 0; i < bodyParts; i++) {
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Write the text on the panel
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    /**
     * Helper method that draws the grid on the panel
     * @param g
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.gray);
        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
    }

    /**
     * Generate a new apple on the board
     */
    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    /**
     * Moves the body part(s) of the snake.
     * The logic is that we call this method each time an action is performed in the actionPerfomed method.
     */
    public void move() {
        // TODO: Move the body parts in this method. The idea is to utilise x[] and y[].
        // TODO: We have to start from the back and move everything to where the next body part is.
        // TODO: For example, x[1] y[1] is the neck of the snake. The neck should take the position of
        // TODO: the head x[0] y[0]. The back x[2] y[2] should take the position of neck x[1] y[1] etc.
        // TODO: And we have to do this for all the body parts.
        // TODO: After you have moved the parts we have to render them in the draw() method.
        // TODO: You will only need to edit between these comments

        // Edit here
        int prevX = x[0];
        int prevY = y[0];
        int tmpX;
        int tmpY;
        for (int i = 1; i < bodyParts; i++) {
            tmpX = x[i];
            tmpY = y[i];
            x[i] = prevX;
            y[i] = prevY;
            prevX = tmpX;
            prevY = tmpY;
        }

        // This moves the head only. The body has to follow the head
        switch (direction) {
            case 'N':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'W':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'E':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }
    }

    /**
     * Check if the snake has eaten the apple
     */
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            applesEaten++; // Increments the number of eaten apples
            bodyParts++; // Increment the number of bodyparts that the snake has in this method.
            newApple();
        }
    }

    /**
     * This checks for collisions
     */
    public void checkCollisions() {
        // Checks for collision with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Top border
        if (y[0] < 0) {
            running = false;
        }
        // bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    /**
     * Prints out the game over screen
     * @param g
     */
    public void gameOver(Graphics g) {
        // Game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }

    /**
     * Checks if an action has been done then we do all the checks and moves.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Necessary class for grabbing user input.
     */
    public class MykeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'E'){
                        direction = 'W';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'W'){
                        direction = 'E';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'S'){
                        direction = 'N';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'N'){
                        direction = 'S';
                    }
                    break;
            }
        }
    }
}
