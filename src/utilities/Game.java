package utilities;

import gameobjects.Organism;
import gameobjects.Organism.Species;
import gameobjects.Outbreak;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to manage general game mechanics not specific to organisms
 */
public class Game {
    public static int timeElapsed = 0;
    public static int outbreakCooldown = 0;

    //for currency
    public static double money = 10000;
    public static double maxCurrencyRate = 2.5;
    public static double change = 0;
    public static boolean atMaxRate = false;

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
        if (speciesCounts.get(Species.GRASS) < 100 && timeElapsed <= 300){ //prevents starting grass from causing outbreak
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

    
}
