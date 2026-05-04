package ui;
import javax.swing.JFrame;
// screen -size 1920*10090
public class Menu {
    JFrame frame;
    public Menu() {
        setup();
        
        
    }
    public void setup {
        frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setVisible(true);
    }
    
}
