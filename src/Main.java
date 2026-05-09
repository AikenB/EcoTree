
import gameobjects.Animal;
import gameobjects.Organism.Species;
import gui.Grid;
import gui.Menu;
import java.util.ArrayList;

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

        // tree gen
        ArrayList<int[]> treeLocs = terrain.Generation.getTreeLocations();
        System.out.println("SIZELOCS" + treeLocs.size());
        for (int i = 0; i < treeLocs.size(); i++)
        {
            int x = treeLocs.get(i)[1];
            int y = treeLocs.get(i)[0];
            Grid.addSprite(new gui.Sprite(x-2, y-2, 5, 5, "src/images/tree_1.png"));
        }
        
        Animal ant1 = Grid.createAnimal(Species.ANT, 7, 4);
        Animal ant2 = Grid.createAnimal(Species.ANT, 20, 8);
        Animal spider1 = Grid.createAnimal(Species.SPIDER, 10, 30);
        Animal spider2 = Grid.createAnimal(Species.SPIDER, 15, 15);
        Animal frog1 = Grid.createAnimal(Species.FROG, 10, 10);
        Animal frog2 = Grid.createAnimal(Species.FROG, 10, 17);
        Animal ant3 = Grid.createAnimal(Species.ANT, 0, 0);
        Animal ant4 = Grid.createAnimal(Species.ANT, 20, 25);
        Animal ant5 = Grid.createAnimal(Species.ANT, 5, 25);
        Animal ant6 = Grid.createAnimal(Species.ANT, 5, 10);
        Animal spider3 = Grid.createAnimal(Species.SPIDER, 3, 10);
        Grid.createAnimal(Species.ANT, 25, 25);
        Grid.createAnimal(Species.ANT, 27, 10);
        Grid.createAnimal(Species.ANT, 15, 7);
        Grid.createAnimal(Species.SPIDER, 30, 30);
        Grid.createPlant(Species.FERN, 5,27);
        Grid.createPlant(Species.FERN, 6,27);
        Grid.createPlant(Species.FERN, 7,27);
        Grid.createPlant(Species.FERN, 6,28);
        Grid.createPlant(Species.FERN, 20,2);
        Grid.createPlant(Species.FERN, 21,2);
        Grid.createPlant(Species.FERN, 21,3);
        Grid.createPlant(Species.FERN, 30,2);
        Grid.createPlant(Species.FERN, 32,3);
        Grid.createPlant(Species.GRASS, 30,30);
        Grid.createPlant(Species.GRASS, 31,31);
        Grid.createPlant(Species.GRASS, 31,20);
        Grid.createPlant(Species.GRASS, 32,20);
        Grid.createPlant(Species.GRASS, 32,21);


        
        //Grid.printGrid();

        //Grid.addSprite(new Sprite(7, 4, 5, 5, "src/images/frog.jpg"));

        
        // Timer to print grid every second
        //printTimer.start();
        
    }
}
