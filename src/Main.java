
import gameobjects.Organism.Species;
import gameobjects.Outbreak;
import gameobjects.Plant;
import gui.Menu;
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

        Menu menu = new Menu();
        
        //Map.loadMap();
        
        

        Grid grid = new Grid();

        // tree gen
        ArrayList<int[]> treeLocs = terrain.Generation.getTreeLocations();
        System.out.println("SIZELOCS" + treeLocs.size());
        for (int i = 0; i < treeLocs.size(); i++)
        {
            //TODO: uncomment this when done with testing
            int x = treeLocs.get(i)[1];
            int y = treeLocs.get(i)[0];
            Plant plant = Grid.createPlant(Species.GRASS, x, y);
            plant.setPhotosynthesisEfficiency(0);
            
        }

        // create main tree
        Grid.createPlant(Species.MAINTREE, 64,64);
      

        //Organism mouse = Grid.createOrganism(Species.MOUSE, 10, 10);
        //new Outbreak(mouse, Outbreak.type.CORONAVIRUS, new ArrayList<>(List.of(Species.MOUSE,Species.FERN)));
        for (int i = 10; i < 23; i++){
            for (int j = 10; j < 23; j++){
                if (Math.random() < 0.5){
                    Grid.createOrganism(Species.ANT, j, i);
                    Outbreak.mousecount++;
                }// } else if (Math.random() > 0.8){
                //     Grid.createOrganism(Species.FERN, j, i);
                // }
            }
        }
        


        //Organism mouse2 = Grid.createOrganism(Species.MOUSE, 40, 35);
        //new Outbreak(mouse2, Outbreak.type.FLU, new ArrayList<>(List.of(Species.MOUSE,Species.FERN)));
        for (int i = 35; i < 48; i++){
            for (int j = 40; j < 53; j++){
                if (Math.random() < 0.5){
                    Grid.createOrganism(Species.ANT, j, i);
                    Outbreak.antcount++;
                }// } else if (Math.random() > 0.8){
                //     Grid.createOrganism(Species.FERN, j, i);
                // }
            }
        }

        //Organism mouse3 = Grid.createOrganism(Species.MOUSE, 40, 10);
        //new Outbreak(mouse3, Outbreak.type.BACTERIAL_INFECTION, new ArrayList<>(List.of(Species.MOUSE, Species.FERN)));
        for (int i = 10; i < 23; i++){
            for (int j = 40; j < 53; j++){
                if (Math.random() < 0.5){
                    Grid.createOrganism(Species.MOUSE, j, i);
                    Outbreak.mousecount++;
                }
                //  else if (Math.random() > 0.8) {
                //     Grid.createOrganism(Species.FERN, j, i);
                // }
            }
        }


        //Organism mouse4 = Grid.createOrganism(Species.MOUSE, 10, 35);
        //new Outbreak(mouse4, Outbreak.type.FUNGUS_INFECTION, new ArrayList<>(List.of(Species.MOUSE,Species.FERN)));
        for (int i = 35; i < 48; i++){
            for (int j = 10; j < 23; j++){
                if (Math.random() < 0.5){
                    Grid.createOrganism(Species.MOUSE, j, i);
                    Outbreak.mousecount++;
                }
                //  else if (Math.random() > 0.8) {
                //     Grid.createOrganism(Species.FERN, j, i);
                // }
            }
        }

        // for (int i = 0; i < 50; i++){
        //     Grid.createOrganism(Species.GRASS, 30, i);
        // }
        // for (int j = 0; j < 70; j++){
        //     Grid.createOrganism(Species.GRASS, j, 30);
        // }

        


        
        
        //printTimer.start();
        Timer timer = new Timer(1000, e -> {
            System.out.println("outbreak cooldown: " + Game.outbreakCooldown);
            //System.out.println(Outbreak.andDeaths + "/" + Outbreak.antcount + " ants have died.");
            //System.out.println(Outbreak.mouseDeaths + "/" + Outbreak.mousecount + " mice have died.");
            //System.out.println(Outbreak.wormDeaths + "/" + Outbreak.wormcount + " worms have died.");
            //System.out.println(Menu.change);
            if (Menu.change > Menu.maxCurrencyRate) {
                Menu.atMaxRate = true;
                Menu.money += Menu.maxCurrencyRate;
            } else {
                Menu.atMaxRate = false;
            }
            
            Menu.change = 0;
            Game.determineCurrencyRate();
            //System.out.println("***" + Menu.maxCurrencyRate);
            //System.out.println(Grid.speciesList);
            //System.out.println(Organism.trophicLevels);
            Game.timeElapsed++;
            Game.outbreakCooldown--;
            if (Game.outbreakCooldown < 0){
                Game.outbreakCooldown = 0;
            }
            if (Game.isOutbreakTriggerable()){
                int chance = (int)(Math.random() * 100);
                if (chance == 0){
                    Game.createOutbreak();
                }
            }
        });
        timer.start();
    }
}
