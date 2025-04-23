import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GamePanel extends JPanel {
    private int numMoves;
    private MainMenu mainMenu;
    private GameWindow gameWindow;
    private ArrayList<Bubble> bubbles;
    private Random random;
    private int round;
    private int score;
    private int timeLeft;
    private Timer timer;
    private boolean settingOriginds;
    private int clicksMade;

    //size definition for bubbles
    private static final int BUBBLE_RADIUS = 20;
    private static final int INITIAL_NEIGHBOR = 50;
    private static final int NEIGHBOR_INCREMENT = 10;

    // constructor
    public GamePanel(int numMoves, GameWindow gameWindow, MainMenu mainMenu) {
        this.numMoves = numMoves;
        this.gameWindow = gameWindow;
        this.mainMenu = mainMenu;
        this.bubbles = new ArrayList<>();
        this.random = new Random();
        this.round = 1;
        this.score = 0;
        this.timeLeft = 15; // set the initial time left to 60 seconds
        // this.timer = new Timer();
        this.settingOriginds = true; // flag to indicate if the origins are being set
        this.clicksMade = 0;

        //playing field dimension
        int fieldWidth = 600;
        int fieldHeight = 600;
        setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        setBackground(Color.WHITE);

        //mouse click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        startRound();
    }

    //set the timer
    private void startTimer() {
        if(timer != null) {
            timer.stop();; // cancel any existing timer
           // timer.purge()
        }
        timeLeft = 15 - (round - 1); // set the time left based on the round
        timer = new Timer(1000, e-> {
           timeLeft--; // decrement the time left
           if(timeLeft<= 0){
            gameOver("Time is up!"); // show game over message
           }
           repaint();
        });
        timer.start(); // start the timer

        new Timer(500, e-> {
            if(!settingOriginds) {
                repositionLocal(); // reposition the bubbles
                repaint();
            }
        }).start(); // start the timer for repositioning
    }

    //start the round
    private void startRound(){
        bubbles.clear(); // clear the existing bubbles
        clicksMade = 0; // reset the clicks made
        // set the flag to indicate that origins are being set
        if(round ==1){
            settingOriginds = true; 
            JOptionPane.showMessageDialog(this, "Click to set origin for each bubble");
        } else{
        settingOriginds = false; // set the flag to indicate that origins are not being set
        // randomly position bubbles
        for(int i = 0; i < numMoves; i++) {
            boolean validPosition;
            int x, y;
            do{
                x = random.nextInt(getWidth() - BUBBLE_RADIUS * 2) + BUBBLE_RADIUS;
                y = random.nextInt(getHeight() - BUBBLE_RADIUS * 2) + BUBBLE_RADIUS;
                validPosition = isValidPosition(x, y); // check if the position is valid
            }while(!validPosition);
            bubbles.add(new Bubble(x, y)); // add the bubble to the list
        }
        startTimer(); // start the timer

    }
    repaint(); // repaint the panel to show the bubbles
}
    //check if the position is valid
    private boolean isValidPosition(int x, int y) {
        if(x - BUBBLE_RADIUS < 0 || x + BUBBLE_RADIUS > getWidth() ||
                y - BUBBLE_RADIUS < 0 || y + BUBBLE_RADIUS > getHeight()) {
            return false; // position is out of bounds
        }

        for(Bubble bubble : bubbles) {
            double distance = Math.sqrt(Math.pow(x - bubble.getX(), 2) + Math.pow(y - bubble.getY(), 2));
            if(distance < INITIAL_NEIGHBOR) {
                return false; // position is too close to another bubble
            }
        }
        return true; // position is valid
    }

    // reposition local
    private void repositionLocal(){
        int neighborhoodRadius = INITIAL_NEIGHBOR + (round - 1) * NEIGHBOR_INCREMENT;
        for(Bubble bubble : bubbles) {
            int minX = Math.max(BUBBLE_RADIUS, bubble.getX() - neighborhoodRadius/2);
            int maxX = Math.min(getWidth() - BUBBLE_RADIUS, bubble.getX() + neighborhoodRadius/2);
            int minY = Math.max(BUBBLE_RADIUS, bubble.getY() - neighborhoodRadius/2);

            int newX, newY;
            boolean validPosition;
            do {
                newX = random.nextInt(maxX - minX) + minX;
                newY = random.nextInt(getHeight() - BUBBLE_RADIUS * 2) + BUBBLE_RADIUS;
                validPosition = isValidPosition(newX, newY); // check if the position is valid
            } while (!validPosition);
            bubble.x = newX; // update the bubble's x position
            bubble.y = newY; // update the bubble's y position
        }
    }

    // handle mouse click6
    private void handleMouseClick(int x, int y) {
        if(settingOriginds) {
            if(isValidPosition(x, y)) {
                bubbles.add(new Bubble(x, y)); // add the bubble to the list
                clicksMade++; // increment the clicks made
                repaint();
                if(clicksMade == numMoves) {
                    settingOriginds = false; // set the flag to indicate that origins are not being set
                    JOptionPane.showMessageDialog(this, "Click to burst the bubbles!"); // show message
                    startTimer(); // start the timer
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid position!"); // show error message
            }
        } else {
            boolean hit = false;
            for(int i = bubbles.size()-1; i >= 0; i--) {
                Bubble bubble = bubbles.get(i);
                double distance = Math.hypot(x-bubble.x, y-bubble.y);
                if(distance < BUBBLE_RADIUS) {
                    bubbles.remove(i); // remove the bubble from the list
                    hit = true;
                    repaint(); // repaint the panel to update the bubbles
                    break;
                }
            }
            if(!hit && !bubbles.isEmpty()){
                gameOver("You clicked outide the box!");
            }else{
                startRound(); // start a new round
            }
        }
        repaint(); // repaint the panel to update the score and bubbles
    }

    private void gameOver(String message) {
        if(timer != null) {
            timer.stop(); // stop the timer
        }
        JOptionPane.showMessageDialog(this, "Game over" + message); // show game over message
        gameWindow.dispose();
        mainMenu.enableRestart(); // enable the restart button in the main menu
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);

        for(Bubble bubble : bubbles) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(bubble.x - BUBBLE_RADIUS, bubble.y - BUBBLE_RADIUS, BUBBLE_RADIUS * 2, BUBBLE_RADIUS * 2);

            // draw the bubble outline
            int neighborhoodRadius = INITIAL_NEIGHBOR + (round - 1) * NEIGHBOR_INCREMENT;
            g2d.setColor(Color.RED);
            g2d.drawRect(
                bubble.x - neighborhoodRadius / 2,
                bubble.y - neighborhoodRadius / 2,
                neighborhoodRadius,
                neighborhoodRadius
            );
        }

        //display round
        g2d.setColor(Color.BLACK);
        g2d.drawString("Round: " + round, 10, 20); // display the round number
        g2d.drawString("Score: " + score, 10, 40); // display the score
        g2d.drawString("Time left: " + timeLeft, 10, 60); // display the time left

}
}
