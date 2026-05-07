
import gameobjects.Animal;
import gameobjects.Organism.Species;
import gui.Grid;
import gui.Menu;
import utilities.Hitbox;

public class Main {
    public static void main(String[] args) throws Exception {

        // JFrame frame = new JFrame("Menu");
        // frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // frame.setSize(1280,720);
        // frame.setVisible(true);

        // initialize terrain generation before drawing map
        terrain.Generation.initialize();

        Menu menu = new Menu();
        
        //Map.loadMap();
        
        

        Grid grid = new Grid();
        
        Animal ant1 = new Animal(Species.ANT);
        Hitbox hitboxAnt1 = new Hitbox(ant1, 7, 4);
        Animal ant2 = new Animal(Species.ANT);
        Hitbox hitboxAnt2 = new Hitbox(ant2, 20, 8);
        Animal spider1 = new Animal(Species.SPIDER);
        Hitbox hitboxSpider1 = new Hitbox(spider1, 10, 30);
        Animal spider2 = new Animal(Species.SPIDER);
        Hitbox hitboxSpider2 = new Hitbox(spider2, 15, 15);
        Animal frog1 = new Animal(Species.FROG);
        Hitbox hitboxFrog1 = new Hitbox(frog1, 10, 10);
        Animal ant3 = new Animal(Species.ANT);
        Hitbox hitboxAnt3 = new Hitbox(ant3, 0, 0);
        Animal ant4 = new Animal(Species.ANT);
        Hitbox hitboxAnt4 = new Hitbox(ant4, 20, 25);
        Animal ant5 = new Animal(Species.ANT);
        Hitbox hitboxAnt5 = new Hitbox(ant5, 5, 25);
        Animal ant6 = new Animal(Species.ANT);
        Hitbox hitboxAnt6 = new Hitbox(ant6, 5, 10);
        Animal spider3 = new Animal(Species.SPIDER);
        Hitbox hitboxSpider3 = new Hitbox(spider3, 3, 10);


        Grid.addOrganism(hitboxAnt1);
        Grid.addOrganism(hitboxAnt2);
        Grid.addOrganism(hitboxSpider1);
        Grid.addOrganism(hitboxSpider2);
        Grid.addOrganism(hitboxFrog1);
        Grid.addOrganism(hitboxAnt3);
        Grid.addOrganism(hitboxAnt4);
        Grid.addOrganism(hitboxAnt5);
        Grid.addOrganism(hitboxAnt6);
        Grid.addOrganism(hitboxSpider3);
        //Grid.printGrid();

        //Grid.addSprite(new Sprite(7, 4, 5, 5, "src/images/frog.jpg"));

        
        // Timer to print grid every second
        //printTimer.start();
        
    }
}
