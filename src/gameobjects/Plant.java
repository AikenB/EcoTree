package gameobjects;
import gui.Menu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import utilities.Grid;
import utilities.Sprite;


public class Plant extends Organism {
    
    private double photosynthesisEfficiency;
    private double cost;
    
    private int produce;
    private int maxProduce;
    private double productionRarity; //NOTE: the higher this value the harder it is to get fruit
    private boolean hasProduce;
    private ArrayList<Organism> predators;
    //public static double currencyRate;
    private String SPRITE_0;
    private String SPRITE_1;

    //public static int t = 0;

    private ExecutorService executor;

    

    // public static enum PlantSpecies {
        



    // }


    public Plant(Species species) {
        super(species);
        this.species = species;
        produce = 0;
        hasProduce = false;
        generateMutation();
        configureSpecies(species);
        initializeBehavior();
        
        

    }

    private void configureSpecies(Species species){
        switch (species){
            case FERN:
                energy = 5;
                width = 2;
                height = 2;
                photosynthesisEfficiency = 0.25;
                maxProduce = 3;
                productionRarity = 50;
                SPRITE_0 = Sprite.FERN_SPRITE_0;
                SPRITE_1 = Sprite.FERN_SPRITE_1;
                break;
            
            case GRASS:
                energy = 5;
                width = 1;
                height = 1;
                photosynthesisEfficiency = 0.1;
                maxProduce = 1;
                productionRarity = 50;
                SPRITE_0 = Sprite.GRASS_SPRITE_0;
                SPRITE_1 = Sprite.GRASS_SPRITE_1;
                break;
            case APPLE_TREE:
                energy = 20;
                width = 3;
                height = 3;
                photosynthesisEfficiency = 2;
                maxProduce = 8;
                productionRarity = 100;
                SPRITE_0 = Sprite.APPLE_TREE_SPRITE_0;
                SPRITE_1 = Sprite.APPLE_TREE_SPRITE_1;
                break;
            case BERRY_BUSH:
                energy = 10;
                width = 2;
                height = 1;
                photosynthesisEfficiency = 2;
                maxProduce = 12;
                productionRarity = 50;
                SPRITE_0 = Sprite.BERRY_BUSH_SPRITE_0;
                SPRITE_1 = Sprite.BERRY_BUSH_SPRITE_1;
                break;
            case MOSS:
                energy = 3;
                width = 1;
                height = 1;
                photosynthesisEfficiency = 0.05;
                maxProduce = 1;
                productionRarity = 25;
                SPRITE_0 = Sprite.MOSS_SPRITE_0;
                SPRITE_1 = Sprite.MOSS_SPRITE_1;
                break;
            case MAINTREE:
                energy = 1000;
                width = 10;
                height = 10;
                photosynthesisEfficiency = 1;
                maxProduce = 0;
                productionRarity = 0;
                SPRITE_0 = Sprite.APPLE_TREE_SPRITE_0;
                SPRITE_1 = Sprite.APPLE_TREE_SPRITE_1;
                break; 
        }
        trophicLevel = 0;
        trophicLevels.set(trophicLevel, trophicLevels.get(trophicLevel) + 1);
        
    }

    /**
     * initializes the plant's behavior that will run while it's alive
     */
    private void initializeBehavior() {

        /*creates a new thread for each animal created. This makes it so that each animal
         can move independently and can move at different speeds and perform different behaviors at different times*/
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            
            
            while (!Thread.currentThread().isInterrupted()) {
                try {
                   
                    int dt = 1000;
                    // t++;
                    Thread.sleep(dt);
                    Menu.updateMoney(photosynthesisEfficiency);
                    int chance = (int) (Math.random() * (int)(productionRarity));
                    if (chance == 0 && produce < maxProduce){
                        produce++;
                        // System.out.println("produce made after " + t + " seconds");
                    }
                    if (produce > 0){
                        hasProduce = true;
                        Sprite.updateSprite(this, SPRITE_1);
                    } else {
                        hasProduce = false;
                        Sprite.updateSprite(this, SPRITE_0);
                    }
                    if (health <= 0){
                        kill(this);
                        Grid.updateSpeciesList();
                        stopBehavior();
                    }

                    
                } catch (Exception e) {
                    System.err.println("Error in move() for " + species + ": " + e.getMessage());
                    e.printStackTrace();
                    
                }
            }
        });
    }

    public void stopBehavior() {
    if (executor != null) {
        executor.shutdownNow();  // Interrupt the thread immediately
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

    /**
     * adds a mutation to the plant and applies its effects
     */
    @Override
    public void addMutation(Mutation mutation) {
        super.addMutation(mutation);
        photosynthesisEfficiency *= mutation.photosynthesisEfficiencyBoost;
    }

    /**
     * generates a random mutation for the plant.
     * The plant can recieve up to 3 mutations, with each mutation having a 1/15 chance of happening
     */
    public void generateMutation(){
        
        int m1 = (int) (Math.random() * 15);
        int m2 = (int) (Math.random() * 15);
        int m3 = (int) (Math.random() * 15);

        /* generate random mutations here. The mutation will be randomly selected from
        the list of possible mutations */
        if (m1 == 0){
            //selects a random mutation from the list of enums for mutations
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            //generates a random multiplier between 0.75 and 1.25, rounded to 2 decimal places
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }
        if (m2 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }
        if (m3 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        
    }

    public Plant reproduce() {

        Plant newPlant = new Plant(this.species);
        for (Mutation mutation : this.mutations) {
            newPlant.addMutation(mutation);
        }
        return newPlant;
    }

    public double getCost() {
        return cost;
    }

    public double getHealth(){
        return health;
    }
    public void updateHealth(double amount){
        health += amount;
    }

    public boolean hasProduce() {
        return hasProduce;
    }

    public int getProduce() {
        return produce;
    }

    public double getPhotosynthesisEfficiency() {
        return photosynthesisEfficiency;
    }

    public int getMaxProduce() {
        return maxProduce;
    }

    public void updateProduce(int amount) {
        produce += amount;
        if (produce < 0) {
            produce = 0;
        }
    }

    public void setPhotosynthesisEfficiency(double photosynthesisEfficiency) {
        this.photosynthesisEfficiency = photosynthesisEfficiency;
    }


        public boolean canReproduce(){
            for (int i = x - 3 * width; i < x + 3 * width; i++){
                for (int j = y - 3 * height; j < y + 3 * height; j++){
                    if (Grid.canFit(j, i,width,height)){
                        return true;
                    }
                }
            }
            return false;
        }

        public void reproduce(Plant parent) throws IOException {
            // Create offspring of the same species as parent1
            Plant offspring = new Plant(this.species);
        
            // Combine mutations from both parents into a gene pool
            ArrayList<Mutation> genePool = new ArrayList<>();
            genePool.addAll(this.getMutations());
            genePool.addAll(parent.getMutations());
        
            // Randomly inherit half of the mutations from the gene pool
            int inheritCount = genePool.size() / 2;
            for (int i = 0; i < inheritCount; i++) {
                int randomIndex = (int) (Math.random() * genePool.size());
                Mutation inheritedMutation = genePool.get(randomIndex);
                offspring.addMutation(inheritedMutation);
                genePool.remove(randomIndex);  // Remove to avoid inheriting twice
            }

            ArrayList<int[]> possibleLocations = new ArrayList<>();
            for (int i = x - 3 * width; i < x + 3 * width; i++){
                for (int j = y - 3 * height; j < y + 3 * height; j++){
                    if (Grid.canFit(j, i,width,height)){
                        possibleLocations.add(new int[]{j, i});
                    }
                }
            }
            int index = (int) (Math.random() * possibleLocations.size());
            int newX = possibleLocations.get(index)[0];
            int newY = possibleLocations.get(index)[1];
            Grid.createPlant(species, newX, newY);

            
        }

    
}
