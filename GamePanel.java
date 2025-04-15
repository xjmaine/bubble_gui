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
    }
}
