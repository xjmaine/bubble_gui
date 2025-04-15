import javax.swing.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    private final GamePanel gamePanel;

    public GameWindow(int numMoves, MainMenu mainMenu) {
       setTitle("Bubble Burst Game : Game Window"); // set the title of the window
       setSize(600, 600); // set the size of the window
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the default close operation
         setLocationRelativeTo(null); // center the window on the screen

         gamePanel = new GamePanel(numMoves, this, mainMenu); // create a new game panel instance
         add(gamePanel); // add the game panel to the frame

         //enable restart btn
         addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainMenu.enableRestart(); // show the main menu when the game window is closed
            }
         });
    }
}
