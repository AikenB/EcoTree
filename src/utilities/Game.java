package utilities;

import gameobjects.Animal;
import gameobjects.Organism;
import gameobjects.Organism.Species;
import gameobjects.Outbreak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to manage general game mechanics not specific to organisms
 */
public class Game {
    public static int timeElapsed = 0;
    public static int outbreakCooldown = 300;
    public static int timePerInvasiveEvent = 30;
    public static int invasiveCooldown = timePerInvasiveEvent; //TODO: change

    //for currency
    public static double money = 10;
    public static double maxCurrencyRate = 2.5;
    public static double change = 0;
    public static boolean atMaxRate = false;

    //count counting how many organisms of each invasive species can spawn during an invasive species event
    private static HashMap<Species, Integer> invasiveSpeciesCounts = new HashMap<>();
    private static ArrayList<Species> invasiveSpecies = new ArrayList<Species>(List.of(Species.ANT, Species.SPIDER, Species.GRASSHOPPER, Species.SCORPION, Species.BEETLE, Species.SNAKE, Species.FROG, Species.MOUSE));

    public static void determineCurrencyRate(){
        int[] tLevels = new int[5];
        int biodiversity = Grid.speciesList.size(); //number of different species
        for (int i = 0; i < 5; i++) {
            tLevels[i] = Organism.trophicLevels.get(i);
        }
        double rate = 2.5;

        if (tLevels[1] >= 10){
            rate = 5;
        }
        if (tLevels[1] >= 10 && tLevels[2] >= 10){
            rate = 10;
        }
        if (tLevels[1] >= 10 && tLevels[2] >= 10 && tLevels[3] >= 10){
            rate = 20;
        }
        if (tLevels[1] >= 10 && tLevels[2] >= 10 && tLevels[3] >= 10 && tLevels[4] >= 10){
            rate = 25;
        }
        if (tLevels[1] >= 20 
            && tLevels[2] >= 20
            && tLevels[3] >= 20
            && tLevels[4] >= 20 
            && tLevels[0] >= 10 
            && Grid.speciesList.size() >= 7){

            rate = 100;
            maxCurrencyRate = rate;
        } else {
            maxCurrencyRate = rate * (1 + (biodiversity - 2) * 0.05);
        }
        
        
    }

    public static Outbreak createOutbreak(){
        outbreakCooldown = 150;
        int x = (int)(Math.random() * 3) + 1; // Random number of species to infect (1-3)
        Outbreak.type type = Outbreak.type.values()[(int)(Math.random() * 4)];

        
        HashMap<Species, Integer> speciesCounts = new HashMap<>();
        
        // Count organisms for each species
        for (Hitbox hitbox : Grid.hitboxes) {
            Species species = hitbox.getOrganism().getSpecies();
            speciesCounts.put(species, speciesCounts.getOrDefault(species, 0) + 1);
        }
        
        //creates a sorted arraylist of the species ranked by population (descending)
        List<Map.Entry<Species, Integer>> sortedSpecies = new ArrayList<>(speciesCounts.entrySet());
        sortedSpecies.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Get top x species
        ArrayList<Species> targetSpecies = new ArrayList<>();
        for (int i = 0; i < Math.min(x, sortedSpecies.size()); i++) {
            targetSpecies.add(sortedSpecies.get(i).getKey());
        }
        System.out.println("Creating outbreak targeting: " + targetSpecies);

        //find random organism to infect
        ArrayList<Hitbox> potentialTargets = new ArrayList<>();
        for (Hitbox hitbox : Grid.hitboxes) {
            if (targetSpecies.contains(hitbox.getOrganism().getSpecies()) && !(hitbox.getOrganism().getSpecies() == Species.MAINTREE)) { 
                potentialTargets.add(hitbox);
            }
        }

        if (potentialTargets.size() > 0) {
            Hitbox target1 = potentialTargets.get((int)(Math.random() * potentialTargets.size()));
            potentialTargets.remove(target1);
            Hitbox target2 = potentialTargets.get((int)(Math.random() * potentialTargets.size()));
            potentialTargets.remove(target2);
            System.out.println("target: " + target1.getOrganism().getSpecies() + " at " + target1.getX() + "," + target1.getY());
            return new Outbreak(target1.getOrganism(), type, targetSpecies);
            // System.out.println("target: " + target2.getOrganism().getSpecies() + " at " + target2.getX() + "," + target2.getY());
            // new Outbreak(target2.getOrganism(), type, targetSpecies);
            
        } else{
            return null; // No valid targets for outbreak
        }
        
    }

    /**
     * Checks if there is at least one species with a population over 35
     * @return true if any species has population > 35, false otherwise
     */
    public static boolean isOutbreakTriggerable() {
        HashMap<Species, Integer> speciesCounts = new HashMap<>();
        
        boolean populated = false;
        // Count organisms for each species
        for (Hitbox hitbox : Grid.hitboxes) {
            Species species = hitbox.getOrganism().getSpecies();
            speciesCounts.put(species, speciesCounts.getOrDefault(species, 0) + 1);
        }
        if (!speciesCounts.isEmpty() && speciesCounts.get(Species.GRASS) < 100 && timeElapsed <= 300){ //prevents starting grass from causing outbreak
            
            speciesCounts.remove(Species.GRASS);
        }
        // Check if any species exceeds the threshold
        for (int count : speciesCounts.values()) {
            if (count > 40) {
                populated = true;
                break;
            }
        }
        
        return populated && outbreakCooldown <= 0;
    }



    public static void createInvasiveSpeciesEvent() throws IOException {
        Game.invasiveCooldown = timePerInvasiveEvent; //TODO: change
        //width and length of the spawn area for the swarm of the invasive animals
        int width = (int)(Math.random() * 2) + 4;
        int height = (int)(Math.random() * 2) + 4;
        ArrayList<int[]> potentialLocations = new ArrayList<>();

        //check along top and bottom edges of map
        for (int x = 0; x < Grid.grid[0].length - width; x++){
            if (Grid.canFit(x,0, width, height)){
                potentialLocations.add(new int[]{x,0});
            }
            if (Grid.canFit(x,Grid.grid.length - height, width, height)){
                potentialLocations.add(new int[]{x,Grid.grid.length - height});
            }
            
        }

        //check along the remaining left and right edges of the map
        for (int y = Grid.grid.length - height; y < Grid.grid.length -  2 * height; y++){
            if (Grid.canFit(0,y, width, height)){
                potentialLocations.add(new int[]{0,y});
            }
            if (Grid.canFit(Grid.grid[0].length - width,y, width, height)){
                potentialLocations.add(new int[]{Grid.grid[0].length - width,y});
            }
        }

        //System.out.println(potentialLocations.size());

        if (potentialLocations.isEmpty()) {
            return;  //skip if there are no valid spawn locations
        }
        int i = (int)(Math.random() * potentialLocations.size());
        int x = potentialLocations.get(i)[0];
        int y = potentialLocations.get(i)[1];
        initializeInvasiveSpeciesCounts(width, height);
        Species species = invasiveSpecies.get((int)(Math.random() * invasiveSpecies.size()));
        System.out.println(species);
        int count = invasiveSpeciesCounts.get(species);
        System.out.println(count);
        Animal testAnimal = new Animal(species);
        int animalWidth = testAnimal.getWidth();
        int animalHeight = testAnimal.getHeight();
        testAnimal.stopBehavior(); //this animal is just used to get width and height
        //int possibleTries = width * height;
        while (count > 0){
            //xx represents x position of the animal to be spawned, yy represents y position
            //this checks each potential spot in the spawn area to see if it can fit the animal there

            int checkX = x + (int)(Math.random() * width);
            int checkY = y + (int)(Math.random() * height);
            if (Grid.canFit(checkX, checkY, animalWidth, animalHeight)){
                Grid.createOrganism(species, checkX, checkY);
                count--;
            }
            //possibleTries--;
            // if (possibleTries <= 0){
            //     break; //prevents infinite loop in case there are more animals to spawn than available space
            // }
            // if (count <= 0){
            //     break; //breaks loop once all animals have been spawned
            // }
            // for (int xx = x; xx < x + width; xx++){
            //     for (int yy = y; yy < y + height; yy++){
            //         if (count > 0 && Grid.canFit(xx, yy, animalWidth, animalHeight)){
            //             try {
            //                 Grid.createOrganism(species, xx, yy);
            //             } catch (IOException e) {
            //                 e.printStackTrace();
            //             }
            //             count--;
            //         }
            //     }
            // }
        }

    }
    
    private static void initializeInvasiveSpeciesCounts(int w, int h){
        int area = w * h;

        //most species are going to cover 75% of the spawn area
        invasiveSpeciesCounts.put(Species.ANT, (int)(3.0/4 * area)); 
        invasiveSpeciesCounts.put(Species.SPIDER, (int)(3.0/4 * area)); 
        invasiveSpeciesCounts.put(Species.GRASSHOPPER, (int)(3.0/4 * area)); 
        invasiveSpeciesCounts.put(Species.SCORPION, (int)(3.0/8 * area)); 
        invasiveSpeciesCounts.put(Species.BEETLE, (int)(3.0/4 * area)); 
        invasiveSpeciesCounts.put(Species.SNAKE, (int)(Math.min(area/6,(3.0/12 * area)))); 
        invasiveSpeciesCounts.put(Species.FROG, (int)(3.0/16 * area));
        invasiveSpeciesCounts.put(Species.MOUSE, (int)(3.0/4 * area));
    

    }

    
}
