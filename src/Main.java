import gameobjects.Animal;
import gameobjects.Organism.Species;
import gui.Grid;
import javax.swing.*;
import utilities.Hitbox;
public class Main {
    public static void main(String[] args) throws Exception {

        // JFrame frame = new JFrame("Menu");
        // frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // frame.setSize(1280,720);
        // frame.setVisible(true);

        Grid grid = new Grid();
        
        Animal animal = new Animal(Species.ANT);
        Hitbox hitbox = new Hitbox(animal, 7, 2);
        Animal animal3 = new Animal(Species.ANT);
        Hitbox hitbox3 = new Hitbox(animal3, 10, 10);
        Animal animal2 = new Animal(Species.SPIDER);
        Hitbox hitbox2 = new Hitbox(animal2, 4, 5);
        Animal animal4 = new Animal(Species.FROG);
        Hitbox hitbox4 = new Hitbox(animal4, 7, 7);

        Grid.addOrganism(hitbox);
        Grid.addOrganism(hitbox2);
        Grid.addOrganism(hitbox3);
        Grid.addOrganism(hitbox4);
        Grid.printGrid();

        // Timer to print grid every second
        Timer printTimer = new Timer(2000, e -> {
            System.out.println("================================");
            Grid.printGrid();
            
        });
        printTimer.start();
        
    }
}
