import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
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
        this.timer = new Timer();
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
            timer.cancel(); // cancel any existing timer
           // timer.purge()
        }
        timeLeft = 15 - (round - 1); // set the time left based on the round
        timer = new Timer(1000, e -> {
            timeLeft--;
            if(timeLeft <= 0) {
                gameOver("Time's up!"); // show game over message
            }
            repaint(); // repaint the panel to update the timer
        });
        timer.start(); // start the timer

        // move bubbles periodically
        new Timer(500, e->{
            if(!settingOriginds) {
                repositionLocal();
                repaint(); // repaint the panel to update the bubbles
            }
        }).start();
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
            int minX = Math.max(BUBBLE_RADIUS, bubble.x() - neighborhoodRadius/2);
            int maxX = Math.min(getWidth() - BUBBLE_RADIUS, bubble.x() + neighborhoodRadius/2);
            int minY = Math.max(BUBBLE_RADIUS, bubble.y() - neighborhoodRadius/2);

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

    // handle mouse click
}
