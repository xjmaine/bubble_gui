// game luncher
// modular concept

import javax.swing.SwingUtilities;

public class Main{
    public static void main(String[] args) {
        // create a new game instance with event dispatch thread
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}