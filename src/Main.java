
import gameobjects.Animal;
import gameobjects.Organism.Species;
import gui.Grid;
import gui.Menu;

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
        
        Animal ant1 = Grid.createAnimal(Species.ANT, 7, 4);
        Animal ant2 = Grid.createAnimal(Species.ANT, 20, 8);
        Animal spider1 = Grid.createAnimal(Species.SPIDER, 10, 30);
        Animal spider2 = Grid.createAnimal(Species.SPIDER, 15, 15);
        Animal frog1 = Grid.createAnimal(Species.FROG, 10, 10);
        Animal ant3 = Grid.createAnimal(Species.ANT, 0, 0);
        Animal ant4 = Grid.createAnimal(Species.ANT, 20, 25);
        Animal ant5 = Grid.createAnimal(Species.ANT, 5, 25);
        Animal ant6 = Grid.createAnimal(Species.ANT, 5, 10);
        Animal spider3 = Grid.createAnimal(Species.SPIDER, 3, 10);


        
        //Grid.printGrid();

        //Grid.addSprite(new Sprite(7, 4, 5, 5, "src/images/frog.jpg"));

        
        // Timer to print grid every second
        //printTimer.start();
        
    }
}
