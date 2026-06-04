package gameobjects;

import gameobjects.Organism.Species;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import utilities.Grid;
import utilities.Grid.Direction;
import utilities.Hitbox;
import utilities.WeightVector;


public class Animal extends Organism {
    
    
    protected ArrayList<Species> predators;
    protected ArrayList<Species> prey;

    protected double speed;
    protected double foodCapacity;
    protected double satiety;
    protected double thirstCapacity;
    protected double mass;
    protected double fertility;
    //TODO: tune this variable for each species
    /**
     * required fertility to reproduce
     */
    protected double rftr;
    
    // private Timer moveTimer;

    private ExecutorService executor;
    //FOR TESTING PURPOSES
    public static int id = 1;
    private int uniqueID;
    public boolean wasBorn;
    public boolean gaveBirth;
    

    public Animal(Species species) {
        wasBorn = false;
        gaveBirth = false;
        super(species);
        
        fertility = 0;
        uniqueID = id;
        id++;
        
        
        
        configureSpecies(species);
        generateMutation();
        
        maxHealth = health;
        // moveTimer = new Timer((int) (4000/speed), e -> move());
        // moveTimer.start();
        //initializeBehavior();
    }

    /**
     * applies the stats for the species
     */
    private void configureSpecies(Species species) {
        switch(species){
            case ANT:
                trophicLevel = 1;
                energy = 5;
                foodCapacity = 10;
                rftr = 20.0;
                speed = 1.0;
                
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.FROG, Species.BEAR, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.FERN, Species.GRASS));
                break;
            case SPIDER:
                trophicLevel = 2;
                energy = 15;
                foodCapacity = 20;
                rftr = 40.0;
                speed = 2;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.FROG, Species.SCORPION, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.WORM, Species.GRASSHOPPER));
                prey.add(Species.ANT);
                break;
            case FROG:
                trophicLevel = 3;
                energy = 30;
                foodCapacity = 55;
                rftr = 50.0;
                width = 2;
                height = 2;
                speed = 2;
                
                thirstCapacity = 20.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT, Species.BEAR, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.SPIDER, Species.BEETLE));
                break;
            case SNAKE:
                trophicLevel = 4;
                energy = 40;
                foodCapacity = 75;
                rftr = 50.0;
                width = 3;
                height = 2;
                speed = 2.5;
                thirstCapacity = 25.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.BOBCAT, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.MOUSE, Species.SCORPION, Species.HUMMINGBIRD));
                break;
            case WORM:
                trophicLevel = 1;
                energy = 20;
                foodCapacity = 25;
                rftr = 40;
                width = 1;
                height = 1;
                speed = 0.75;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.SCORPION, Species.BEETLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.APPLE_TREE, Species.BERRY_BUSH));
                break;
            case MOUSE:
                trophicLevel = 1;
                energy = 30;
                foodCapacity = 50;
                rftr = 50;
                width = 1;
                height = 1;
                speed = 2;
                thirstCapacity = 15.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT, Species.BEAR, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.BERRY_BUSH, Species.FERN, Species.APPLE_TREE, Species.SCORPION));
                break;
            case GRASSHOPPER:
                trophicLevel = 1;
                energy = 15;
                foodCapacity = 25;
                rftr = 20;
                width = 1;
                height = 1;
                speed = 2;
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.SPIDER, Species.SCORPION, Species.BEETLE, Species.BEAR));
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.FERN, Species.GRASS,Species.APPLE_TREE,Species.BERRY_BUSH));
                break;
            case SCORPION:
                trophicLevel = 3;
                energy = 20;
                foodCapacity = 30;
                mass = 2;
                rftr = 50;
                width = 1;
                height = 1;
                speed = 2.0;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.SNAKE, Species.BEETLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.GRASSHOPPER,Species.WORM));
                break;
            case BEETLE:
                trophicLevel = 3;
                energy = 20;
                mass = 2;
                foodCapacity = 30;
                rftr = 40;
                width = 1;
                height = 1;
                speed = 2.5;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.BEAR, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.GRASSHOPPER, Species.MOONFLOWER, Species.MOSS, Species.FERN, Species.GRASS, Species.SCORPION, Species.DRAGONFRUIT_CACTUS, Species.WORM, Species.FLOWER));
                break;
            case BEE:
                trophicLevel = 1;
                energy = 300; //energy for bees are used as its lifespan since it will have no predators
                foodCapacity = 25;
                rftr = 0; //bees cannot reproduce
                width = 1;
                height = 1;
                speed = 10;
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE));
                break;
            case BOBCAT:
                trophicLevel = 4;
                energy = 50;
                foodCapacity = 100;
                rftr = 60.0;
                width = 4;
                height = 2;
                speed = 3.0;
                thirstCapacity = 30.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.MOUSE, Species.SNAKE, Species.FROG, Species.HUMMINGBIRD));
                break;
            case BEAR:
                trophicLevel = 4;
                energy = 100;
                foodCapacity = 200;
                rftr = 70.0;
                width = 4;
                height = 4;
                speed = 3;
                thirstCapacity = 50.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.BERRY_BUSH, Species.ANT, Species.GRASSHOPPER, Species.BEETLE, Species.FROG));
                break;
            case DEER:
                trophicLevel = 3;
                energy = 50;
                foodCapacity = 75;
                rftr = 70.0;
                width = 3;
                height = 3;
                speed = 2.5;
                thirstCapacity = 30.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT));
                prey = new ArrayList<Species>(Arrays.asList(Species.GRASS, Species.FERN, Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE, Species.MOSS, Species.BEETLE, Species.GRASSHOPPER, Species.DRAGONFRUIT_CACTUS));
                break;
            case HUMMINGBIRD:
                trophicLevel = 1;
                energy = 25; 
                foodCapacity = 50;
                rftr = 120.0; //TODO: change to 120
                width = 1;
                height = 1;
                speed = 10;
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.BOBCAT, Species.SNAKE));
                prey = new ArrayList<Species>(Arrays.asList(Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE, Species.ANT, Species.SPIDER, Species.DRAGONFRUIT_CACTUS, Species.MOONFLOWER, Species.BEETLE));
                break;
            case BALD_EAGLE:
                trophicLevel = 4;
                energy = 50;
                foodCapacity = 150;
                rftr = 300;
                width = 1;
                height = 1;
                speed = 12;
                thirstCapacity = 40.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.SNAKE, Species.FROG));
                break;
             default:
                 trophicLevel = 1;
                 energy = 20;
                 foodCapacity = 30;
                 rftr = 40;
                 width = 1;
                 height = 1;
                 speed = 1.5;
                 thirstCapacity = 10.0;
                 predators = new ArrayList<Species>();
                 prey = new ArrayList<Species>();
            break;

        }
        satiety = 0.9 * foodCapacity; //TODO: fix when done testing
        mass = width * height;
        if (species == Species.BOBCAT || species == Species.DEER){
            mass = 4;
        }
        
        trophicLevels.set(trophicLevel, trophicLevels.get(trophicLevel) + 1);
        
    }
    /**
     * initializes the animal's behavior that will run while it's alive. This method is also where the animal's behavior mechanics runs
     */
    public void initializeBehavior() {

        /*creates a new thread for each animal created. This makes it so that each animal
         can move independently and can move at different speeds and perform different behaviors at different times*/
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    
    //====================EATING MECHANICS============================
                    if (contactingPrey() && isHungry()) {
                        
                        Organism prey = getContactedPrey(this);
                        if (prey != null && (prey.getClass() == Animal.class || prey.getClass() == FlyingAnimal.class)) {
                            
                            satiety += prey.energy;
                            fertility += prey.energy * 0.8;
                            kill(prey);
                            
                            Grid.updateSpeciesList();
                        } else if (prey != null && prey.getClass() == Plant.class && ((Plant) prey).hasProduce()) {
                            
                            
                            satiety += prey.energy;
                            fertility += prey.energy * 0.8;
                            ((Plant) prey).updateProduce(-1);
                            if (prey.species == Species.GRASS){
                                ((Plant) prey).updateHealth(-(Math.random()*10 + 30));
                            } else if (prey.species == Species.FERN){
                                ((Plant) prey).updateHealth(-(Math.random()*10 + 15));
                            }
                        }
                    } 
    //=====================REPRODUCTION MECHANICS===========================             
                    else if (canReproduce()) {
                        
                        int chance = (int) (Math.random() * 3);
                        if (chance == 0 && getMate() != null) {
                            reproduce(getMate());
                            fertility = 0;
                            getMate().fertility = 0;
                        }
                    }
                    double speedboost = 1.0;
                    if (isHungry()) { //give the animal a speed boost if they are hungry. The hungrier they are the slower they will move however
                        speedboost = 1.75 - (0.75 * ((foodCapacity - satiety) / foodCapacity));
                    } else {
                        speedboost = 1 - (0.5 * (foodCapacity - satiety) / foodCapacity);
                    }
                    //movement
                    int dt = (int)(4000/(speed * speedboost));
                    
                    
                    int xi = this.x;
                    int yi = this.y;
                    move();
                    if (xi == this.x && yi == this.y){
                        System.out.println("froze - id: " + uniqueID);
                    }
                    Thread.sleep(dt);
                    //hunger + fertility updating
                    //TODO: tune the rate of hunger and fertility increase for each species
                    satiety -= 0.1 * mass;
                    satiety = Math.min(foodCapacity, Math.max(satiety, 0)); //clamp satiety between 0 and its max capacity
                    fertility += 0.5;
                    fertility = Math.min(fertility, rftr);
                    if (satiety == 0) {
                        //System.out.println("starved to death for id " + uniqueID);
                        //animal dies if it reaches 0 satiety
                        kill(this);
                        Grid.updateSpeciesList();
                        stopBehavior();
                    } else if (health <= 0) {
                        //System.out.println("health depleted for id " + uniqueID);
                        //animal dies if it reaches 0 health
                        kill(this);
                        Grid.updateSpeciesList();
                        stopBehavior();
                    }
                    
                    
                } catch (InterruptedException e) {
                    // Thread was interrupted, exit the loop gracefully
                    //System.err.println("thread interruped for id " + uniqueID);
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error in move() for " + species + ": " + e.getMessage());
                    e.printStackTrace();
                    
                } catch (Throwable e) {  // change Exception to Throwable
                    System.err.println("FATAL error for id " + uniqueID + ": " + e.getClass().getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    
    /**
     * stops the animal's behavior thread
     */
    public void stopBehavior() {
        //System.out.println("stopping behavior for id " + uniqueID);
        if (executor != null) {
        executor.shutdownNow();  // Interrupt the thread immediately
        // try {
        //     if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
        //         executor.shutdownNow();
        //     }
        // } catch (InterruptedException e) {
        //     executor.shutdownNow();
        //     Thread.currentThread().interrupt();
        // }
    }
    }
    /**
     * adds a mutation to the animal and applies its effects
     */
    @Override
    public void addMutation(Mutation mutation) {
        super.addMutation(mutation);
        this.speed *= mutation.speedBoost;
        this.foodCapacity *= mutation.foodCapacityBoost;
        this.thirstCapacity *= mutation.thirstCapacityBoost;
        this.rftr *= (2 -  mutation.fertilityBoost);
        
    }

    /**
     * generates a random mutation for the animal.
     * The animal can recieve up to 3 mutations, with each mutation having a 1/15 chance of happening
     */
    public void generateMutation(){
        
        int m1 = (int) (Math.random() * 5);
        int m2 = (int) (Math.random() * 5);
        int m3 = (int) (Math.random() * 5);

        /* generate random mutations here. The mutation will be randomly selected from
        the list of possible mutations */
        if (m1 == 0){
            //selects a random mutation from the list of enums for mutations
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 8 + 1)];
            //generates a random multiplier between 0.75 and 1.25, rounded to 2 decimal places
            double amplifier = Math.round(0.8 + (Math.random() * 0.55)) * 100 / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        if (m2 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 8 + 1)];
            double amplifier = Math.round((0.8 + Math.random() * 0.55) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        if (m3 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 8 + 1)];
            double amplifier = Math.round((0.8 + Math.random() * 0.55) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        
    }

    //#region MOVEMENT

    /**
     * used for move() method. This checks if the organism being detected as the animal scans its surroundings in its viewfield is a new organism that was not previously counted for
     * 
     */
    protected boolean isNewOrganism(ArrayList<Organism> organisms, Organism o) {
        for (Organism organism : organisms) {
            if (organism == o) {
                return false;
            }
        }
        if (o == null) {
            return false;
        } else {
            return true;
        }
        
    }

    private int preySpotted() {
        Hitbox[][] viewfield = getViewField();
        
        ArrayList<Organism> organismsInSight = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < viewfield.length; i++) {
            for (int j = 0; j < viewfield[i].length; j++) {
                if (viewfield[i][j] != null) {
                    Organism o = viewfield[i][j].getOrganism();
                    if (o != null && prey.contains(o.getSpecies()) && isNewOrganism(organismsInSight, o)) {
                        count++;
                        organismsInSight.add(o);
                    }
                } 
                int x = this.x - 17 + j;
                int y = this.y - 17 + i;
                if (hasGroundedPrey(x, y) && isNewOrganism(organismsInSight, getGroundedPrey(x, y))) {
                    count++;
                    organismsInSight.add(getGroundedPrey(x, y));
                }
            }
        }
        return count;
    }

    public int sameSpeciesSpotted() {
        Hitbox[][] viewfield = getViewField();
        int count = 0;
        for (int i = 0; i < viewfield.length; i++) {
            for (int j = 0; j < viewfield[i].length; j++) {
                if (viewfield[i][j] != null) {
                    Organism o = viewfield[i][j].getOrganism();
                    if (o != null && o != this &&o.getSpecies() == this.species) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * returns the viewfield of the animal. The viewfield is a coordinate grid of hitboxes relative to the animal, with the animal at the center. Used for move() method.
     */
    private Hitbox[][] getViewField() {
        Hitbox[][] viewField = new Hitbox[35][35];
        synchronized (Grid.grid) {
            for (int i = 0; i < viewField.length; i++) {
                for (int j = 0; j < viewField[i].length; j++) {
                    int x = this.x - 17 + j;
                    int y = this.y - 17 + i;
                    // Grid is [height][width], so y is row index, x is column index
                    if (y >= 0 && y < Grid.grid.length && x >= 0 && x < Grid.grid[0].length) {
                        viewField[i][j] = Grid.grid[y][x];
                    } else {
                        viewField[i][j] = null;
                    }
                }
            }
        }
        return viewField;
    }

    // /**
    //  * returns the viewfield of the animal for flying animals that are grounded. The viewfield is a coordinate grid of hitboxes relative to the animal, with the animal at the center. Used for move() method.
    //  */
    // private FlyingAnimal[][] getViewField_groundedAnimals() {
    //     FlyingAnimal[][] viewField = new FlyingAnimal[35][35];
    //     synchronized (Grid.grid) {
    //         for (int i = 0; i < viewField.length; i++) {
    //             for (int j = 0; j < viewField[i].length; j++) {
    //                 int x = this.x - 17 + j;
    //                 int y = this.y - 17 + i;
    //                 viewField[i][j] = getGroundedPrey(x, y);
    //             }
    //         }
    //     }
    //     return viewField;
    // }

    private boolean hasGroundedPrey(int x, int y){
        for (FlyingAnimal flyingAnimal : Grid.flyingAnimals) {
            if (flyingAnimal.isGrounded() && prey.contains(flyingAnimal.getSpecies()) && flyingAnimal.getX() == x && flyingAnimal.getY() == y) {
                //System.out.println("has grounded prey: ");
                return true;
            }
        }
        return false;

    }
    private FlyingAnimal getGroundedPrey(int x, int y){
        for (FlyingAnimal flyingAnimal : Grid.flyingAnimals) {
            if (flyingAnimal.isGrounded() && prey.contains(flyingAnimal.getSpecies()) && flyingAnimal.getX() == x && flyingAnimal.getY() == y) {
                return flyingAnimal;
            }
        }
        return null;

    }
    /**
     * moves the organism. It will scan its surroundings and weigh in how much it will 
     * want to go towards or away from each organism it sees, then sums up all of these 
     * weightvectors to then determine its direction of movement. It will then use safemove() to make sure it at least moves in a direction that is possible to move in
     */
    private void move() {
        // System.out.println("------///------");
        //get field of view of organism. This is a 2d array of hitboxes where we can get the organism that the hitbox represents when we access the individual hitboxes inside the array
        Hitbox[][] viewField = getViewField();
        WeightVector movementVector = new WeightVector(0, 0, 0);
        //create list of organisms in sight to avoid counting the same organism multiple times
        ArrayList<Organism> organismsInSight = new ArrayList<>();

        double closestPredatorDistance = Double.MAX_VALUE;
        WeightVector closestPredatorVector = new WeightVector(0,0);
        double highestPreyWeight = Double.MIN_VALUE;
        double closestPreyDistance = Double.MAX_VALUE;
        WeightVector closestPreyVector = new WeightVector(0,0);

        //scan viewfield
        for (int i = 0; i < viewField.length; i++) {
            for (int j = 0; j < viewField[i].length; j++) {
                
                //if the hitbox detected is null, skip it
                if (viewField[i][j] == null)
                    continue;

                //get organism evaluated for pathfinding
                Organism o = viewField[i][j].getOrganism();
                WeightVector w = new WeightVector(0,0,0);
                //makes sure it will only add a weight vector if it is a new organism that has not been counted yet
                if (isNewOrganism(organismsInSight, o)) {
                    organismsInSight.add(o);
                    
                    int x =  j - 17;      
                    int y = i - 17;    
                    double d = Math.sqrt(x*x + y*y);  
                    double weight = 0;
                    
                    /*formula for calculating the weight vector for each organism.
                    the impact of d can be tuned to prevent the organism from going in the middle of two prey*/
                    if (predators.contains(o.getSpecies()) && d < 13.0) { //d<10 to prevent animals from running away when predator isnt that close
                        
                        weight = (double) (250.0 / Math.sqrt(d));
                        w.orient(x, y, weight);
                        w.doubleOrthogonalize();

                        if (d < closestPredatorDistance) {
                            closestPredatorDistance = d;
                            closestPredatorVector = w;
                        }
                        
                        // System.out.println("predator:" + o.getSpecies());
                        // System.out.println("predator: X: " + w.getX() + " Y: " + w.getY());
                        // System.out.println("-------------");
                        
                    }
                    else if (prey.contains(o.getSpecies())) {

    //======================IF THE ANIMAL IS HUNGRY=============================
                        if (isHungry()){
                            //FOR DEALING WITH PLANTS
                            if (o.getClass() == Plant.class && ((Plant) o).hasProduce()) {
                                
                                weight = (double) (5.0 * o.energy / (Math.pow(d,4)));
                                w.orient(x, y, weight);
                            } else if (o.getClass() == Plant.class && !((Plant) o).hasProduce()) {
                                //makes the animal less motivated to go towards plants that don't have produce
                                int tendency = (int) (Math.random() * 2 * preySpotted());
                                if (tendency == 0){
                                    weight = (double) ( 2 *o.energy / (Math.pow(d,4)));
                                    w.orient(x, y, weight);
                                }
                            } else { //FOR DEALING WITH ANIMALS WHEN ANIMAL IS HUNGRY
                                weight = (double) (o.energy / (Math.pow(d,4)));
                                w.orient(x, y, weight);
                            }
                        } 
//======================IF THE ANIMAL IS NOT HUNGRY=============================
                        else {
                            //makes organisms less motivated to follow prey if they aren't hungry
                            //since this calculation will be done for each prey spotted, it will be scaled with the amount of prey spotted to prevent itself from constantly following prey
                            int tendency = (int) (Math.random() * 2 * preySpotted());
                            if (tendency == 0){
                                weight = (double) (5 * o.energy / (Math.pow(d,4)));
                                w.orient(x, y, weight);
                            }
                        }
                        //System.err.printf("  Prey %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    else if (!o.equals(this) && o.getSpecies() == this.getSpecies() && !this.isHungry()) {
                        //this is so that organisms will tend to stick with organisms of the same species

                        //this is to prevent the organisms from constantly following each other if there are no prey or predators around
                        int tendency = (int) ((Math.random() * 5) * ((sameSpeciesSpotted()) / 2.0));
                        if (tendency == 0 && d <= 15){
                            weight = 5.0 / d;
                            w.orient(x, y, weight);
                        }
                        
                    }
                    int absoluteX = this.x - 17 + j;
                    int absoluteY = this.y - 17 + i;
                    if (hasGroundedPrey(j, i)){
                        Organism groundedPrey = getGroundedPrey(absoluteX, absoluteY);
                         weight = (double) (groundedPrey.energy / (Math.pow(d,4)));
                         w.orient(x, y, weight);
                         movementVector = movementVector.add(w);
                         if (weight > highestPreyWeight && prey.contains(groundedPrey.getSpecies())) {
                            closestPreyVector = w;
                            closestPreyDistance = d;
                            highestPreyWeight = weight;
                        }
                    }
                    if (weight > highestPreyWeight && prey.contains(o.getSpecies())) {
                        closestPreyVector = w;
                        closestPreyDistance = d;
                        highestPreyWeight = weight;
                    }
                    //add the vector contribution to the movement vector
                    movementVector = movementVector.add(w);

                }
            }   
        }
        // System.out.println("this species: " + this.getSpecies());
        // System.out.println("movement vector: X: " + v.getX() + " Y: " + v.getY());
        // System.out.println("-----***------");
        //if the animal may be surrounded by prey it will just go to the closest
        if (preySpotted() > 7){
            
            if (closestPredatorDistance < closestPreyDistance){
                movementVector = closestPredatorVector;
            } else {
                movementVector = closestPreyVector;
            }
            
            Grid.Direction direction = getDirection(movementVector);
            safeMove(direction);
        } else if (movementVector.getWeight() != 0){
            //makes sure that it has a valid direction to move in, if it doesn't it will move in a random valid direction
       
            Grid.Direction direction = getDirection(movementVector);
            safeMove(direction);
        } else {
            Grid.Direction randomD = Grid.Direction.values()[(int) (Math.random() * Grid.Direction.values().length)];
            //System.err.printf("  -> Weight is 0, choosing RANDOM direction: %s%n", randomD);
            safeMove(randomD);
        }
    }
    /**
     * Ensures the animal moves in a direction if possible. If the animal can't move in the intended direction, it will iterate through the possible directions until it finds a direction that it can move in. If no direction is possible, the animal will not move.
     * @param direction The intended direction of movement
     */
    private void safeMove(Grid.Direction direction) {
        Grid.Direction d = direction;

        
        int newX = x;
        int newY = y;
        switch (d) {
            case UP:
                newY -= 1;
                break;
            case DOWN:
                newY += 1;
                break;
            case LEFT:
                newX -= 1;
                break;
            case RIGHT:
                newX += 1;
                break;
            case UP_LEFT:
                newX -= 1;
                newY -= 1;
                break;
            case UP_RIGHT:
                newX += 1;
                newY -= 1;
                break;
            case DOWN_LEFT:
                newX -= 1;
                newY += 1;
                break;
            case DOWN_RIGHT:
                newX += 1;
                newY += 1;
                break;
        }


        /*if the animal can't move in the specific direction, iterate through 
        each possible direction until it finds one that is available*/
        if (!canMove(d)) {
            //System.out.println("cant move in intended direction: " + d);
            //if the animal can move in A direction:
            if (canMove()){
                //check each direction until it finds a direction where it can move int
                // for (int i = (d.ordinal() + 1) % 8; i != d.ordinal() + 8; i++) {
                //     Grid.Direction newDirection = Grid.Direction.values()[i % 8];
                //     if (canMove(newDirection)) {
                //         //if it can move in this direction, then call the method again. This time it should not run through this loop again
                //         safeMove(newDirection);
                //         return;
                //     }
                // }

                //this algorithm makes it so that if the organism can't move its original intended direction it will check the closest directions around it until it finds one it can move in
                int i = d.ordinal();
                //System.out.println("intended direction: " + d);
                int k = 1;
                for (int j = 0; j < 7; j++){
                    i += k * (int) Math.pow(-1, j);
                    //System.out.println("i: " + i + " k: " + k * (int) Math.pow(-1, j));
                    k++;
                    //makes i go in the order of 1, -2, 3, -4
                    //this is so that it checks the next closest directions around the initial direction
                    
                    int index = (i) % 8;
                    //System.out.println("index" + index);
                    if (index < 0) {
                        index = 8 + index;
                    }
                    Grid.Direction newDirection = Grid.Direction.values()[index];
                    //System.out.println(newDirection + " " + j);
                    if (canMove(newDirection)) {
                        //System.out.println("moved in direction: " + newDirection);
                        //if it can move in this direction, then call the method again. This time it should not run through this loop again
                        safeMove(newDirection);
                        return;
                    }
                }
            }
            //System.out.println("not moving");
            //if it can't then it won't move at all
            
        }
        else {

            //move the organism
            
            Hitbox hitbox = this.getHitbox();
            if (hitbox == null) {
                // Organism has been removed from grid or hitbox is null
                return;
            }
            Grid.removeOrganism(hitbox);
            x = newX;
            y = newY;
            hitbox.setX(newX);
            hitbox.setY(newY);
            Grid.addOrganism(hitbox);
        }
    }

    /**
     * checks if the animal can move in any direction at all
     * 
     */
    private boolean canMove(){
        for (int k = 0; k < Grid.Direction.values().length; k++) {
            if (canMove(Grid.Direction.values()[k])) {
                return true;
            }
        }
        return false;
    }
    /**
     * checks if the animal can move in a specific given direction
     *
     */
    private boolean canMove(Grid.Direction direction) {
        int newX = x;
        int newY = y;
        switch (direction) {
            case UP:
                newY -= 1;
                break;
            case DOWN:
                newY += 1;
                break;
            case LEFT:
                newX -= 1;
                break;
            case RIGHT:
                newX += 1;
                break;
            case UP_LEFT:
                newX -= 1;
                newY -= 1;
                break;
            case UP_RIGHT:
                newX += 1;
                newY -= 1;
                break;
            case DOWN_LEFT:
                newX -= 1;
                newY += 1;
                break;
            case DOWN_RIGHT:
                newX += 1;
                newY += 1;
                break;
        }

        // Check all grid spaces the organism would occupy after the move
        synchronized (Grid.grid) {
            for (int i = newY; i < newY + height; i++) {
                for (int j = newX; j < newX + width; j++) {
                    // Check if position is in bounds
                    if (i < 0 || i >= Grid.grid.length || j < 0 || j >= Grid.grid[0].length) {
                        return false;
                    }
                    // Check if cell is empty or occupied by this organism
                    if (Grid.grid[i][j] != null && Grid.grid[i][j].getOrganism() != this) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * converts a weight vector into a valid direction that can be used
     * @param v The weight vector
     * @return The corresponding direction
     */
    public Grid.Direction getDirection(WeightVector v){
        double angle = v.getTheta();
        if (angle >= -Math.PI/8 && angle < Math.PI/8) {
            return Grid.Direction.RIGHT;
        } else if (angle >= Math.PI/8 && angle < 3*Math.PI/8) {
            return Grid.Direction.DOWN_RIGHT;
        } else if (angle >= 3*Math.PI/8 && angle < 5*Math.PI/8) {
            return Grid.Direction.DOWN;
        } else if (angle >= 5*Math.PI/8 && angle < 7*Math.PI/8) {
            return Grid.Direction.DOWN_LEFT;
        } else if (angle >= 7*Math.PI/8 || angle < -7*Math.PI/8) {
            return Grid.Direction.LEFT;
        } else if (angle >= -7*Math.PI/8 && angle < -5*Math.PI/8) {
            return Grid.Direction.UP_LEFT;
        } else if (angle >= -5*Math.PI/8 && angle < -3*Math.PI/8) {
            return Grid.Direction.UP;
        } else {
            return Grid.Direction.UP_RIGHT;
        }
    }
    

    //#region BEHAVIOR

    /**
     * checks if the animal is touching any prey around its borders
     * @return true if touching any prey, false otherwise
     */
    public boolean contactingPrey() {
        // Check a range around the entire organism footprint, not just the top-left corner
        for (int i = x - 1; i <= x + width + 1; i++) {
            for (int j = y - 1; j <= y + height + 1; j++) {
                if (i >= 0 && i < Grid.grid[0].length && j >= 0 && j < Grid.grid.length) {
                    Hitbox hitbox = Grid.grid[j][i];
                    if (hitbox == null) 
                        continue;
                    Organism o = hitbox.getOrganism();
                    if (o != null && prey.contains(o.getSpecies())) {
                        return true;
                    }
                    if (hasGroundedPrey(i, j)) {
                        //System.out.println("contacting grounded prey");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * returns the prey that is being contacted by the animal
     * @return The prey organism, or null if none is being contacted
     */
    public static Organism getContactedPrey(Animal animal) {
        // Check a range around the entire organism footprint
        for (int i = animal.x - 1; i <= animal.x + animal.width + 1; i++) {
            for (int j = animal.y - 1; j <= animal.y + animal.height + 1; j++) {
                if (i >= 0 && i < Grid.grid[0].length && j >= 0 && j < Grid.grid.length) {
                    Hitbox hitbox = Grid.grid[j][i];
                    if (hitbox != null) {
                        Organism o = hitbox.getOrganism();
                        if (o != null && o != animal && animal.prey.contains(o.getSpecies())) {
                            return o;
                        }
                    }
                    if (animal.hasGroundedPrey(i, j)) {
                        //System.out.println("got contacting grounded prey");
                        return animal.getGroundedPrey(i, j);
                    }
                }
            }
        }
        return null;
    }

    //TODO: tune this mechanic
    public boolean isHungry() {
        //System.out.println("prey spotted: " + preySpotted());
        if (satiety < 0.9 * foodCapacity && preySpotted() >= 5) {
            //System.out.println("case1: satiety = " + (satiety / foodCapacity));
            return true;
        } 
        else if (satiety < 0.85 * foodCapacity && preySpotted() >= 3) {
            //System.out.println("case2: satiety = " + (satiety / foodCapacity));
            return true;
            
        } else {
            //if (satiety < 0.8 * foodCapacity) 
                //System.out.println("case3: satiety = " + (satiety / foodCapacity));
            return satiety < 0.8 * foodCapacity; 
        }
        
    }

    //#region REPRODUCTION
    
    // public void reproduce(Animal parent) throws IOException {
    //     // Create offspring of the same species as parent1
    //     Animal offspring = new Animal(this.species);
        
    //     // Combine mutations from both parents into a gene pool
    //     ArrayList<Mutation> genePool = new ArrayList<>();
    //     genePool.addAll(this.getMutations());
    //     genePool.addAll(parent.getMutations());
        
    //     // Randomly inherit half of the mutations from the gene pool
    //     int inheritCount = genePool.size() / 2;
    //     for (int i = 0; i < inheritCount; i++) {
    //         int randomIndex = (int) (Math.random() * genePool.size());
    //         Mutation inheritedMutation = genePool.get(randomIndex);
    //         offspring.addMutation(inheritedMutation);
    //         genePool.remove(randomIndex);  // Remove to avoid inheriting twice
    //     }

    //     Hitbox hitbox = new Hitbox(offspring, this.x, this.y);
    //     if (offspring.canMove()){
    //         Direction d = Grid.Direction.values()[(int) (Math.random() * Grid.Direction.values().length)];
    //         offspring.safeMove(d);
            
    //         Grid.createAnimal(offspring.species, offspring.x, offspring.y);
    //         offspring.wasBorn = true;
    //         this.gaveBirth = true;
    //     }
        
    // }


    public void reproduce(Animal parent) throws IOException {
        // build and shuffle direction list
        Direction[] dirs = Grid.Direction.values().clone();
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            Direction tmp = dirs[i]; dirs[i] = dirs[j]; dirs[j] = tmp;
        }

        for (Direction d : dirs) {
            int newX = this.x, newY = this.y;
            switch (d) {
                case UP:         
                    newY--; break;
                case DOWN:       
                    newY++; break;
                case LEFT:       
                    newX--; break;
                case RIGHT:      
                    newX++; break;
                case UP_LEFT:    
                    newX--; 
                    newY--; 
                    break;
                case UP_RIGHT:   
                    newX++; 
                    newY--; 
                    break;
                case DOWN_LEFT:  
                    newX--; 
                    newY++; 
                    break;
                case DOWN_RIGHT: 
                    newX++; 
                    newY++; 
                    break;
            }
        Animal offspring = Grid.createAnimal(this.species, newX, newY);
        if (offspring != null) {
            // inherit mutations
            ArrayList<Mutation> genePool = new ArrayList<>();
            genePool.addAll(this.getMutations());
            genePool.addAll(parent.getMutations());
            int inheritCount = genePool.size() / 2;
            for (int i = 0; i < inheritCount; i++) {
                int idx = (int)(Math.random() * genePool.size());
                offspring.addMutation(genePool.get(idx));
                genePool.remove(idx);
            }
            return;
        }
        
    }
    // no free adjacent space, reproduction fails
    }

    public boolean foundPotentialMate(){
        for (int i = this.y - 2; i <= this.y + this.height + 2; i++) {
            for (int j = this.x - 2; j <= this.x + this.width + 2; j++) {
                if (i >= 0 && i < Grid.grid.length && j >= 0 && j < Grid.grid[0].length) {
                    Hitbox hitbox = Grid.grid[i][j];
                    if (hitbox != null) {
                        Organism o = hitbox.getOrganism();
                        if (o != null && o != this && o.getSpecies() == this.getSpecies() && ((Animal) o).canReproduce()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Animal getMate(){
        for (int i = this.y - 2; i <= this.y + this.height + 2; i++) {
            for (int j = this.x - 2; j <= this.x + this.width + 2; j++) {
                if (i >= 0 && i < Grid.grid.length && j >= 0 && j < Grid.grid[0].length) {
                    Hitbox hitbox = Grid.grid[i][j];
                    if (hitbox != null) {
                        Organism o = hitbox.getOrganism();
                        if (o != null && o != this && o.getSpecies() == this.getSpecies() && ((Animal) o).canReproduce()) {
                            return (Animal) o;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean canReproduce() {

        if (diseases.keySet().contains(Outbreak.type.FUNGUS_INFECTION)) {
            return false;
        } else {
            return fertility >= rftr 
            && satiety >= 0.9 * foodCapacity 
            && health >= 0.5 * maxHealth;
        }
        
    }


    //#region GETTERS

    public double getRFTR() {
        return  rftr;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public double getFertility() {
        return  fertility;
    }

    public double getFertilityPercentage() {
        return (fertility / rftr) * 100;
    }

    public double getSatiety() {
        return satiety;
    }

    public double getThirstCapacity() {
        return thirstCapacity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getFoodCapacity() {
        return foodCapacity;
    }

    public double getSatietyPercentage() {
        return (satiety / foodCapacity) * 100;
    }

    public ArrayList<Species> getPredators() {
        return predators;
    }
    public ArrayList<Species> getPrey() {
        return prey;
    }

    public int getUniqueID() {
        return uniqueID;
    }
    

    

    

    

}


    