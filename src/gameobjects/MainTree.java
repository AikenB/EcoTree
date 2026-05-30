package gameobjects;

import gameobjects.Organism.Species;
import gui.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utilities.Grid;
import utilities.Hitbox;

public class MainTree extends Plant {
    
    private ExecutorService executor;
    double maxHealth;


    public MainTree()
    {

        super(Organism.Species.MAINTREE);
        maxHealth = health;
        super.stopBehavior();
        initializeBehavior();

    }




    public void initializeBehavior(){

        executor = Executors.newSingleThreadExecutor();
        System.out.println("MainTree initializeBehavior called");

        executor.submit(() -> {
            System.out.println("MainTree executor started");
            
            while (!Thread.currentThread().isInterrupted()) {
                try {
                   
                    int dt = 1000;
                    // t++;
                    Thread.sleep(dt);
                    Menu.updateMoney(photosynthesisEfficiency);
                    health += calculateHealthRate();
                    if (health > maxHealth) {
                        health = maxHealth;
                    }
                    System.out.println("Health rate: " + calculateHealthRate());
                    if (health <= 0){
                        kill(this);
                        Grid.updateSpeciesList();
                        stopBehavior();
                    }

                    
                } catch (Exception e) {
                    System.err.println("Error in MainTree: " + e.getMessage());
                    e.printStackTrace();
                    
                }
            }
            
        });
    }

    public double calculateHealthRate(){
        // double rate = 0;
        // if (Game.timeElapsed < 300){
        //     return rate;
        // }
        double ecosystemHealthFactor = 0;
        ArrayList<Organism> organisms = getOrganismsList();

        double plantWeightFactor = 0;
        for (Organism organism : organisms) {
            
            if (organism instanceof Plant) {
                Plant p = (Plant) organism;
                double d = Math.hypot(organism.getX() - 69, organism.getY() - 69);
                plantWeightFactor += p.getPhotosynthesisEfficiency() / d;
            }
            
        }
        plantWeightFactor *= 5;
        plantWeightFactor = Math.min(plantWeightFactor, 15); //cap plant weight factor 
        

        
        HashMap<Species, Double> speciesWeights = new HashMap<>();
        for (Organism organism : organisms) {
            if (organism.getTrophicLevel() > 0) {
                double weight = 0;
                switch (organism.getTrophicLevel()) {
                    case 1: weight = 1; break;
                    case 2: weight = 3; break;
                    case 3: weight = 5; break;
                    case 4: weight = 20; break;
                    default: weight = 0; break;
                }
                speciesWeights.put(organism.getSpecies(), speciesWeights.getOrDefault(organism.getSpecies(), 0.0) + weight);

            }
        }

        int diversityFactor = Math.min(speciesWeights.size(), 10); //cap diversity bonus
        // HashMap<Species,Double> speciesCounts = new HashMap<>();
        // for (Species species : speciesWeights.keySet()) {
        //     double speciesCount = speciesWeights.get(species);
        //     switch (getTrophicLevel(species)){
        //         case 1: 
        //             break;
        //         case 2:
        //             speciesCount *= 4;
        //             break;
        //         case 3:
        //             speciesCount *= 10;
        //             break;
        //         case 4:
        //             speciesCount *= 20;
        //             break;
        //         default:
        //             speciesCount = 0;
        //             break;
        //     }
        //     speciesWeights.put(species, speciesWeights.getOrDefault(species, 0.0) + weight);

        // }
        if (speciesWeights.size() == 0){
            return 0.0;
        }

        //determine a threshold based on the amount of species. This is to prevent the player from abusing the formula below and having very few species
        int checkThreshold = (int) Math.round(speciesWeights.size() / 3.0);
        // if (speciesWeights.size() > 2){
        //     checkThreshold = 3;

        // } 
            
        //eliminate the 2 least populous species
        //the purpose of this is to prevent discouraging the player from introducing new species to the ecosystem to eat the overpopulated species
        int cutoffAmount = 2; 
        if (speciesWeights.size() <= 2){
            cutoffAmount = 0;
        } else if (speciesWeights.size() <= 3){
            cutoffAmount = 1;
        }
        for (int i = 0; i < Math.min(speciesWeights.size(),cutoffAmount); i++){
                
            double min = Double.MAX_VALUE;
            Species minSpecies = null;
            for (Species species : speciesWeights.keySet()) {
                if (speciesWeights.get(species) < min) {
                    min = speciesWeights.get(species);
                    minSpecies = species;
                }
            }
            speciesWeights.remove(minSpecies);

        }

        //check each species and see if it is significantly more populous than others. If it is significantly more populous than (threshold) number of species it will damage the ecosystem health
        //HashMap<Species, Integer> speciesCounts = getSpeciesCounts(organisms);
        
        for (Species species : speciesWeights.keySet()) {
            if (Organism.getTrophicLevel(species) == 0){
                continue; //skip plants
            }
            int count = 0;
            double weight = speciesWeights.get(species);
            
            for (Species checkSpecies : speciesWeights.keySet()) {
                if (checkSpecies != species && Organism.getTrophicLevel(checkSpecies) != 0) {

                    double checkWeight = speciesWeights.get(checkSpecies);
                    if (weight > 4 * checkWeight && Organism.getTrophicLevel(species) != Organism.getTrophicLevel(checkSpecies)) {
                        count++;
                    }
                }
            }
            System.out.println("Species: " + species + " Weight: " + weight + " Count: " + count);
            if (count >= checkThreshold) {
                ecosystemHealthFactor -= 10;
            }
        }


        ecosystemHealthFactor = Math.max(ecosystemHealthFactor, -30); //cap overpopulation penalty 
        System.out.println("ecosystemHealth: " + ecosystemHealthFactor);
        System.out.println("plantWeightFactor: " + plantWeightFactor);
        System.out.println("diversityFactor: " + diversityFactor);
        return Math.min(ecosystemHealthFactor + plantWeightFactor + diversityFactor, 2); //forces health rate to cap at 2 for balance

    }

    private ArrayList<Species> getOverPopulatedList(HashMap<Species, Double> speciesWeights){
        ArrayList<Species> overPopulatedList = new ArrayList<>();
        for (Species species : speciesWeights.keySet()) {
            if (Organism.getTrophicLevel(species) == 0){
                continue; //skip plants
            }
            double weight = speciesWeights.get(species);
            int count = 0;
            for (Species checkSpecies : speciesWeights.keySet()) {
                if (checkSpecies != species && Organism.getTrophicLevel(checkSpecies) != 0) {

                    double checkWeight = speciesWeights.get(checkSpecies);
                    if (weight > 4 * checkWeight && Organism.getTrophicLevel(species) != Organism.getTrophicLevel(checkSpecies)) {
                        count++;
                    }
                }
            }
            if (count >= Math.round(speciesWeights.size()/3)) {
                overPopulatedList.add(species);
            }
        }
        return overPopulatedList;
    }

    // private ArrayList<Species> getIgnoreList(ArrayList<Species> overPopulatedList){
    //     ArrayList<Species> ignoreList = new ArrayList<>();
    //     for (Species species : overPopulatedList){
    //         ArrayList<Species> predators = Animal.getPredatorsList(species);
    //         for (Species predator : predators){
    //             if (!overPopulatedList.contains(predator) && overPopulated){
    //                 ignoreList.add(predator);
    //             }
    //         }
    //     }
    //     return ignoreList;
    // }
    

    private ArrayList<Organism> getOrganismsList(){
        ArrayList<Organism> organisms = new ArrayList<>();
        for (Hitbox hitbox : Grid.hitboxes) {
            if (hitbox != null && hitbox.getOrganism() != null && hitbox.getOrganism().getSpecies() != Species.MAINTREE) {
                organisms.add(hitbox.getOrganism());
            }
        }
        return organisms;
    }

    private HashMap<Species, Integer> getSpeciesCounts(ArrayList<Organism> organisms){
        HashMap<Species, Integer> speciesCounts = new HashMap<>();
        for (Organism organism : organisms) {
            Species species = organism.getSpecies();
            speciesCounts.put(species, speciesCounts.getOrDefault(species, 0) + 1);
        }
        return speciesCounts;
    }

}


//#region KYLE'S CODE
//sorry kyle I am commenting this out cuz I need to test my code you can make a new branch and delete my code on that branch to test your code

// package tree;

// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

// import javax.swing.JFrame;

// import gameobjects.Organism;
// import gameobjects.Plant;
// import utilities.Game;

// public class MainTree extends Plant {
//     // default health
    
    
    
//     public MainTree()
//     {

//         super(Organism.Species.MAINTREE);

//     }
//     private ExecutorService executor;  
//     public void initializeTree(JFrame frame)
//     {
//         executor = Executors.newSingleThreadExecutor();
//         executor.submit(() -> {
//             while (!Thread.currentThread().isInterrupted()) {
//                 try {
//                     // calculate diversity index
//                     double diversityIndex = 0;
//                     double sum = 0;

                    

//                     // every second
//                     Thread.sleep(1000);
//                 }
//                  catch (Exception e) {
//                     System.err.println(e);
//                     e.printStackTrace();
                    
//                 }
//             } 
//         });
//     }
// }
