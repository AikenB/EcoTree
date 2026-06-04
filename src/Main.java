
import gameobjects.Organism.Species;
import gameobjects.Plant;
import gui.Screen;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;
import utilities.Game;
import utilities.Grid;

public class Main {
    
    
    
    public static void main(String[] args) throws Exception {

        // JFrame frame = new JFrame("Menu");
        // frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // frame.setSize(1280,720);
        // frame.setVisible(true);

        // initialize terrain generation before drawing map
        terrain.Generation.initialize();

        Screen menu = new Screen();
        
        //Map.loadMap();
        
        

        //Grid grid = new Grid();
        Grid.createPlant(Species.MAINTREE, 64,64);
        // Grid.createPlant(Species.GRASS, 0, 0);
        // Grid.createPlant(Species.GRASS, 0, Grid.grid.length - 1);
        // Grid.createPlant(Species.GRASS, Grid.grid[0].length - 1, 0);
        // Grid.createPlant(Species.GRASS, Grid.grid[0].length - 1, Grid.grid.length - 1);
        // tree gen
        ArrayList<int[]> treeLocs = terrain.Generation.getTreeLocations();
        System.out.println("SIZELOCS" + treeLocs.size());
        for (int i = 0; i < treeLocs.size(); i++)
        {
            // TODO: uncomment this when done with testing
            int x = treeLocs.get(i)[1];
            int y = treeLocs.get(i)[0];
            Plant plant = Grid.createPlant(Species.GRASS, x, y);
            plant.setPhotosynthesisEfficiency(0);
            
        }

        // create main tree
        
       //Grid.createPlant(Species.FERN,Grid.grid[0].length,Grid.grid.length);

        
        // for (int i = 10; i < 23; i++){
        //     for (int j = 10; j < 23; j++){
        //         if (Math.random() < 0.5){
        //             Grid.createOrganism(Species.GRASSHOPPER, j, i);
                   
        //         }
        //     }
        // }

        // for (int i = 45; i < 58; i++){
        //     for (int j = 45; j < 58; j++){
        //         if (Math.random() < 0.1){
        //             Grid.createOrganism(Species.FERN, j, i);
                   
        //         }
        //     }
        // }
        


       
        // for (int i = 35; i < 48; i++){
        //     for (int j = 40; j < 53; j++){
        //         if (Math.random() < 0.5){
        //             Grid.createOrganism(Species.ANT, j, i);
        //             Outbreak.antcount++;
        //         }
        //     }
        // }

        // for (int i = 10; i < 23; i++){
        //     for (int j = 40; j < 53; j++){
        //         if (Math.random() < 0.5){
        //             Grid.createOrganism(Species.MOUSE, j, i);
        //             Outbreak.mousecount++;
        //         }
                
        //     }
        // }

        // for (int i = 35; i < 53; i++){
        //     for (int j = 10; j < 27; j++){
        //         if (Math.random() < 0.5){
        //             Grid.createOrganism(Species.GRASSHOPPER, j, i);
                    
        //         } else if (Math.random() > 0.9){
        //             Grid.createOrganism(Species.BERRY_BUSH, j, i);
        //         }
               
        //     }
        // }

        // for (int i = 55; i < 73; i++){
        //     for (int j = 10; j < 27; j++){
        //         if (Math.random() < 0.5){
        //             Grid.createOrganism(Species.GRASSHOPPER, j, i);
                    
        //         } else if (Math.random() > 0.9){
        //             Grid.createOrganism(Species.BERRY_BUSH, j, i);
        //         }
               
        //     }
        // }

        // for (int i = 35; i < 75; i++){
        //     for (int j = 10; j < 45; j++){
        //         if (Math.random() < 0.02){
        //             Grid.createOrganism(Species.BERRY_BUSH, j, i);
                    
        //         } else if (Math.random() > 0.97){
        //             Grid.createOrganism(Species.APPLE_TREE, j, i);
        //         }
               
        //     }
        // }


        
        
        //printTimer.start();
        Timer timer = new Timer(1000, e -> {
            //System.out.println("outbreak cooldown: " + Game.outbreakCooldown);
            //System.out.println(Outbreak.andDeaths + "/" + Outbreak.antcount + " ants have died.");
            //System.out.println(Outbreak.mouseDeaths + "/" + Outbreak.mousecount + " mice have died.");
            //System.out.println(Outbreak.wormDeaths + "/" + Outbreak.wormcount + " worms have died.");
            //System.out.println(Menu.change);
            if (Game.change > Game.maxCurrencyRate) {
                Game.atMaxRate = true;
                Game.money += Game.maxCurrencyRate;
            } else {
                Game.atMaxRate = false;
            }
            // System.out.println("Active threads: " + Thread.activeCount());
            // for (Thread t : Thread.getAllStackTraces().keySet()) {
            //     if (t.getName().contains("pool")) {
            //         System.out.println(t.getName() + ": " + t.getState());
            //     }
            // }
    
            Game.change = 0;
            Game.determineCurrencyRate();
            //System.out.println("***" + Game.maxCurrencyRate);
            //System.out.println(Grid.speciesList);
            //System.out.println(Organism.trophicLevels);
            Game.timeElapsed++;
            Game.outbreakCooldown--;
            Game.invasiveCooldown--;
            if (Game.outbreakCooldown < 0){
                Game.outbreakCooldown = 0;
            }
            //System.out.println(Game.invasiveCooldown);

            //for outbreaks
            if (Game.isOutbreakTriggerable()){
                int chance = (int)(Math.random() * 75);
                if (chance == 0){
                    Game.createOutbreak();
                }
            }

            //for invasive species events
            if (Game.invasiveCooldown <= 0 && Game.timeElapsed > 30){//TODO: change time requirement when done testing
                try {
                    Game.createInvasiveSpeciesEvent();
                    
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        });
        timer.start();
    }
}
