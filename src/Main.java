
import gameobjects.Animal;
import gameobjects.Organism.Species;
import gui.Grid;
import gui.Menu;
import javax.swing.*;
import utilities.Hitbox;

public class Main {
    public static void main(String[] args) throws Exception {

        // JFrame frame = new JFrame("Menu");
        // frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // frame.setSize(1280,720);
        // frame.setVisible(true);
        Menu menu = new Menu();
        //ComponentBuilder b = new ComponentBuilder(menu.getFrame(),menu.getMap());
        //BufferedImage frogImage = ImageIO.read(new File("src/images/frog.jpg"));
        //g2d.drawImage(frogImage, 100, 100, 200, 200, null); // x, y, width, height
        //Map.loadMap();
        
        

        Grid grid = new Grid();
        
        Animal animal = new Animal(Species.ANT);
        Hitbox hitbox = new Hitbox(animal, 7, 4);
        Animal animal3 = new Animal(Species.ANT);
        Hitbox hitbox3 = new Hitbox(animal3, 4, 8);
        Animal animal2 = new Animal(Species.SPIDER);
        Hitbox hitbox2 = new Hitbox(animal2, 4, 5);
        Animal animal4 = new Animal(Species.FROG);
        Hitbox hitbox4 = new Hitbox(animal4, 7, 7);
        Animal animal5 = new Animal(Species.ANT);
        Hitbox hitbox5 = new Hitbox(animal5, 0, 0);
        Animal animal6 = new Animal(Species.ANT);
        Hitbox hitbox6 = new Hitbox(animal6, 14, 14);

        Grid.addOrganism(hitbox);
        Grid.addOrganism(hitbox2);
        Grid.addOrganism(hitbox3);
        Grid.addOrganism(hitbox4);
        Grid.addOrganism(hitbox5);
        Grid.addOrganism(hitbox6);
        Grid.printGrid();

        Grid.addSprite(new gui.Sprite(7, 4, 5, 5, "src/images/frog.jpg"));


        // Timer to print grid every second
        Timer printTimer = new Timer(2000, e -> {
            System.out.println("================================");
            Grid.printGrid();
            
        });
        printTimer.start();
        
    }
}
