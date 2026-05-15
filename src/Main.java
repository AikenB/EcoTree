
import gameobjects.Organism;
import gameobjects.Organism.Species;
import gameobjects.Plant;
import gui.Menu;
import java.util.ArrayList;
import javax.swing.Timer;
import utilities.Grid;

public class Main {
    
    public static void determineCurrencyRate(){
        int[] tLevels = new int[5];
        for (int i = 0; i < 5; i++) {
            tLevels[i] = Organism.trophicLevels.get(i);
        }

        if (tLevels[1] >= 5){
            Menu.maxCurrencyRate = 10;
        }
        if (tLevels[1] >= 5 && tLevels[2] >= 5){
            Menu.maxCurrencyRate = 15;
        }
        if (tLevels[1] >= 5 && tLevels[2] >= 5 && tLevels[3] >= 5){
            Menu.maxCurrencyRate = 20;
        }
        if (tLevels[1] >= 5 && tLevels[2] >= 5 && tLevels[3] >= 5 && tLevels[4] >= 5){
            Menu.maxCurrencyRate = 25;
        }
        if (tLevels[1] >= 10 
            && tLevels[2] >= 10
            && tLevels[3] >= 10
            && tLevels[4] >= 10 
            && tLevels[0] >= 5 
            && Grid.speciesList.size() >= 7){

            Menu.maxCurrencyRate = 100;
        } else {
            Menu.maxCurrencyRate = Menu.maxCurrencyRate * (1 + Grid.speciesList.size() * 0.05);
        }
        
        
    }
    
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
            Plant plant = Grid.createPlant(Species.GRASS, x, y);
            plant.setPhotosynthesisEfficiency(0);
            //Grid.addSprite(new gui.Sprite(x-2, y-2, 5, 5, "src/images/tree_1.png"));
        }

        // create main tree
        Grid.createPlant(Species.MAINTREE, 64,64);
        
        


        
        
        //printTimer.start();
        Timer timer = new Timer(1000, e -> {
            
            System.out.println(Menu.change);
            if (Menu.change > Menu.maxCurrencyRate) {
                Menu.atMaxRate = true;
                Menu.money += Menu.maxCurrencyRate;
            } else {
                Menu.atMaxRate = false;
            }
            System.out.println(Menu.atMaxRate);
            Menu.change = 0;
            determineCurrencyRate();
        });
        timer.start();
    }
}
