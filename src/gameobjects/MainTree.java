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
        plantWeightFactor *= 10;

        System.out.println("plantWeightFactor: " + plantWeightFactor);
        

        
        HashMap<Species, Double> speciesWeights = new HashMap<>();
        for (Organism organism : organisms) {
            if (organism.getTrophicLevel() > 0) {
                double weight = 0;
                switch (organism.getTrophicLevel()) {
                    case 1: weight = 1; break;
                    case 2: weight = 0.25; break;
                    case 3: weight = 0.1; break;
                    case 4: weight = 0.05; break;
                    default: weight = 0; break;
                }
                speciesWeights.put(organism.getSpecies(), speciesWeights.getOrDefault(organism.getSpecies(), 0.0) + weight);

            }
        }

        
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
        int checkThreshold = 1;
        if (speciesWeights.size() > 2){
            checkThreshold = 3;

        } 
            
            // //eliminate the 2 least populous species
            // for (int i = 0; i < Math.min(speciesWeights.size(),2); i++){
                
            //     double min = Double.MAX_VALUE;
            //     Species minSpecies = null;
            //     for (Species species : speciesWeights.keySet()) {
            //         if (speciesWeights.get(species) < min) {
            //             min = speciesWeights.get(species);
            //             minSpecies = species;
            //         }
            //     }
            //     speciesWeights.remove(minSpecies);

            // }

        //check each species and see if it is significantly more populous than others. If it is significantly more populous than (threshold) number of species it will damage the ecosystem health
        for (Species species : speciesWeights.keySet()) {
            if (Organism.getTrophicLevel(species) == 0){
                continue; //skip plants
            }
            double weight = speciesWeights.get(species);
            int count = 0; //count how many times the species is significantly greater than another species
            for (Species checkSpecies : speciesWeights.keySet()) {
                if (checkSpecies != species && Organism.getTrophicLevel(checkSpecies) != 0) {
                    double checkWeight = speciesWeights.get(checkSpecies);
                    if (weight > 4 * checkWeight && Organism.getTrophicLevel(species) != Organism.getTrophicLevel(checkSpecies)) {
                        count++;
                    }
                }
            }
            if (count >= checkThreshold) {
                ecosystemHealthFactor -= 10;
            }
        }
        System.out.println("ecosystemHealth: " + ecosystemHealthFactor);
        int diversityFactor = speciesWeights.size();
        System.out.println("diversityFactor: " + diversityFactor);
        return Math.min(ecosystemHealthFactor + plantWeightFactor + diversityFactor, 2); //forces health rate to cap at 2 for balance

    }
    

    private ArrayList<Organism> getOrganismsList(){
        ArrayList<Organism> organisms = new ArrayList<>();
        for (Hitbox hitbox : Grid.hitboxes) {
            if (hitbox != null && hitbox.getOrganism() != null && hitbox.getOrganism().getSpecies() != Species.MAINTREE) {
                organisms.add(hitbox.getOrganism());
            }
        }
        return organisms;
    }


}
