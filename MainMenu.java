
import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame{
    //components
    private JSlider dJSlider;
    private JButton startButton, restartButton;
    private GameWindow gameWindow;

    // constructor
    public MainMenu() {
        
        setTitle("Bubble Burst Game : Main Menu"); // set the title of the window
        
        setSize(800, 600); // set the size of the window
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the default close operation
        setLocationRelativeTo(null); // center the window on the screen
        
        setLayout(new GridLayout(3,1)); // set the layout manager

        // create a new game window instance
        // gameWindow = new GameWindow();

        // create and add components to the main menu
        dJSlider = new JSlider();
        startButton = new JButton("Start");
        restartButton = new JButton("Restart");

        // add components to the frame
        // add(dJSlider);
        // add(startButton);
        // add(restartButton);

        // set the visibility of the window
        setVisible(true);

        //JSlider element for slider
        dJSlider = new JSlider(JSlider.HORIZONTAL, 4, 6, 4);
        dJSlider.setMajorTickSpacing(1);
        dJSlider.setPaintTicks(true);
        dJSlider.setPaintLabels(true);
        dJSlider.setSnapToTicks(true);
        add(new JLabel("Select Difficulty Level: 4=Easy, 5=Medium, 6=Hard : "));
        add(dJSlider);
        
        // add action listener to the start button
        startButton = new JButton("Start");
        startButton.addActionListener(e -> {
           int numMoves = dJSlider.getValue(); // get the value from the slider
           if(gameWindow == null || !gameWindow.isVisible()) {
               gameWindow = new GameWindow(numMoves, this); // create a new game window instance
               gameWindow.setVisible(true);
           }

        });
        add(startButton); // add the start button to the frame


        //restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            if(gameWindow != null) {
                gameWindow.dispose(); // close the game window
                gameWindow =  new GameWindow(dJSlider.getValue(), this); // set the game window to null
                gameWindow.setVisible(true); // make the game window visible
            }
            
        });
        add(restartButton); // add the restart button to the frame
        // set the visibility of the window 
        setVisible(true);
}

    // method to enable the restart button
    public void enableRestart() {
        restartButton.setEnabled(true); // enable the restart button
    }
}
