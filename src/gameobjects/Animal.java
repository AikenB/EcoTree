package gameobjects;

import gameobjects.Organism.Species;
import gui.Grid;
import gui.Grid.Direction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import utilities.Hitbox;
import utilities.WeightVector;


public class Animal extends Organism {
    
    
    private ArrayList<Species> predators;
    private ArrayList<Species> prey;

    private double speed;
    private double foodCapacity;
    private double satiety;
    private double thirstCapacity;
    private double mass;
    private double fertility;
    //TODO: tune this variable for each species
    /**
     * required fertility to reproduce
     */
    private double rftr;
    
    // private Timer moveTimer;

    private ExecutorService executor;
    


    public Animal(Species species) {
        super(species);
        
        
        generateMutation();
        configureSpecies(species);
        mass = width * height;
        fertility = 0;
        // moveTimer = new Timer((int) (4000/speed), e -> move());
        // moveTimer.start();
        initializeBehavior();
    }

    /**
     * applies the stats for the species
     */
    private void configureSpecies(Species species) {
        switch(species){
            case ANT:
                energy = 5;
                foodCapacity = 10;
                rftr = 20.0;
                speed = 1.0;
                
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.FROG));
                prey = new ArrayList<Species>(Arrays.asList(Species.FERN, Species.GRASS));
                break;
            case SPIDER:
                energy = 15;
                foodCapacity = 20;
                rftr = 40.0;
                speed = 2;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.FROG));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.WORM));
                prey.add(Species.ANT);
                break;
            case FROG:
                energy = 25;
                foodCapacity = 45;
                rftr = 50.0;
                width = 2;
                height = 2;
                speed = 2;
                
                thirstCapacity = 20.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.SPIDER));
                break;
            case SNAKE:
                energy = 30;
                foodCapacity = 75;
                rftr = 50.0;
                width = 3;
                height = 2;
                speed = 2.5;
                thirstCapacity = 25.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.BOBCAT));
                prey = new ArrayList<Species>(Arrays.asList(Species.FROG));
                break;
            case WORM:
                energy = 20;
                foodCapacity = 25;
                rftr = 40;
                width = 1;
                height = 1;
                speed = 0.75;
                thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER));
                prey = new ArrayList<Species>(Arrays.asList(Species.APPLE_TREE));
                break;

        }
        satiety = 0.9 * foodCapacity;
        
    }
    /**
     * initializes the animal's behavior that will run while it's alive
     */
    private void initializeBehavior() {

        /*creates a new thread for each animal created. This makes it so that each animal
         can move independently and can move at different speeds and perform different behaviors at different times*/
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            
            
            while (!Thread.currentThread().isInterrupted()) {
                try {
    //====================EATING MECHANICS============================
                    if (contactingPrey() && isHungry()) {
                        Organism prey = getContactedPrey(this);
                        if (prey != null && prey.getClass() == Animal.class) {
                            
                            satiety += prey.energy;
                            fertility += prey.energy * 0.8;
                            kill(prey);
                        } else if (prey != null && prey.getClass() == Plant.class) {
                            satiety += prey.energy;
                            fertility += prey.energy * 0.8;
                            ((Plant) prey).updateProduce(-1);
                        }
                    } 
    //=====================REPRODUCTION MECHANICS===========================             
                    else if (canReproduce()) {
                        reproduce(getMate());
                        fertility = 0;
                        getMate().fertility = 0;
                    }
                    double speedboost = 1.0;
                    if (isHungry()) {
                        speedboost = 1.75 - (1.25 * ((foodCapacity - satiety) / foodCapacity));
                    }
                    
                    //movement
                    int dt = (int)(4000/(speed * speedboost));
                    Thread.sleep(dt);
                    move();
                    //hunger + fertility updating
                    //TODO: tune the rate of hunger and fertility increase for each species
                    satiety -= 0.1 * mass;
                    satiety = Math.min(foodCapacity, Math.max(satiety, 0)); //clamp satiety between 0 and its max capacity
                    fertility += 1;
                    fertility = Math.min(fertility, rftr);
                    if (satiety == 0) {
                        //animal dies if it reaches 0 satiety
                        kill(this);
                        stopBehavior();
                    }
                    
                    
                } catch (Exception e) {
                    System.err.println("Error in move() for " + species + ": " + e.getMessage());
                    e.printStackTrace();
                    
                }
            }
        });
    }

    
    /**
     * stops the animal's behavior thread
     */
    public void stopBehavior() {
        if (executor != null) {
            executor.shutdown();
        }
    }
    /**
     * adds a mutation to the animal and applies its effects
     */
    @Override
    public void addMutation(Mutation mutation) {
        super.addMutation(mutation);
        
    }

    /**
     * generates a random mutation for the animal.
     * The animal can recieve up to 3 mutations, with each mutation having a 1/15 chance of happening
     */
    public void generateMutation(){
        
        int m1 = (int) (Math.random() * 15);
        int m2 = (int) (Math.random() * 15);
        int m3 = (int) (Math.random() * 15);

        /* generate random mutations here. The mutation will be randomly selected from
        the list of possible mutations */
        if (m1 == 0){
            //selects a random mutation from the list of enums for mutations
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 6 + 1)];
            //generates a random multiplier between 0.75 and 1.25, rounded to 2 decimal places
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        if (m2 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 6 + 1)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        if (m3 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 6 + 1)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        
    }

    //#region MOVEMENT

    /**
     * used for move() method. This checks if the organism being detected as the animal scans its surroundings in its viewfield is a new organism that was not previously counted for
     * 
     */
    private boolean isNewOrganism(ArrayList<Organism> organisms, Organism o) {
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
        int count = 0;
        for (int i = 0; i < viewfield.length; i++) {
            for (int j = 0; j < viewfield[i].length; j++) {
                if (viewfield[i][j] != null) {
                    Organism o = viewfield[i][j].getOrganism();
                    if (o != null && prey.contains(o.getSpecies())) {
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
        Hitbox[][] viewField = new Hitbox[30][30];
        synchronized (Grid.grid) {
            for (int i = 0; i < viewField.length; i++) {
                for (int j = 0; j < viewField[i].length; j++) {
                    int x = this.x - 14 + j;
                    int y = this.y - 14 + i;
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
                    
                    int x =  j - 14;      
                    int y = i - 14;    
                    double d = Math.sqrt(x*x + y*y);  
                    /*formula for calculating the weight vector for each organism.
                    the impact of d can be tuned to prevent the organism from going in the middle of two prey*/
                    if (predators.contains(o.getSpecies())) {
                        
                        double weight = (double) (25 / Math.sqrt(d));
                        w.orient(x, y, weight);
                        w.doubleOrthogonalize();
                        // System.out.println("predator:" + o.getSpecies());
                        // System.out.println("predator: X: " + w.getX() + " Y: " + w.getY());
                        // System.out.println("-------------");
                        //System.err.printf("  Predator %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, final angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    else if (prey.contains(o.getSpecies())) {

    //======================IF THE ANIMAL IS HUNGRY=============================
                        if (isHungry()){
                            //FOR DEALING WITH PLANTS
                            if (o.getClass() == Plant.class && ((Plant) o).hasProduce()) {
                                
                                double weight = (double) (5 * o.energy / (Math.pow(d,1/4)));
                                w.orient(x, y, weight);
                            } else if (o.getClass() == Plant.class && !((Plant) o).hasProduce()) {
                                //makes the animal less motivated to go towards plants that don't have produce
                                int tendency = (int) (Math.random() * 3 * preySpotted());
                                if (tendency == 0){
                                    double weight = (double) (o.energy / (Math.pow(d,1/4)));
                                    w.orient(x, y, weight);
                                }
                            } else { //FOR DEALING WITH ANIMALS WHEN ANIMAL IS HUNGRY
                                double weight = (double) (o.energy / (Math.pow(d,1/4)));
                                w.orient(x, y, weight);
                            }
                        } 
//======================IF THE ANIMAL IS NOT HUNGRY=============================
                        else {
                            //makes organisms less motivated to follow prey if they aren't hungry
                            //since this calculation will be done for each prey spotted, it will be scaled with the amount of prey spotted to prevent itself from constantly following prey
                            int tendency = (int) (Math.random() * 3 * preySpotted());
                            if (tendency == 0){
                                double weight = (double) (o.energy / (Math.pow(d,1/4)));
                                w.orient(x, y, weight);
                            }
                        }
                        
                        //System.err.printf("  Prey %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    else if (!o.equals(this) && o.getSpecies() == this.getSpecies() && !this.isHungry()) {
                        //this is so that organisms will tend to stick with organisms of the same species

                        //this is to prevent the organisms from constantly following each other if there are no prey or predators around
                        int tendency = (int) (Math.random() * 3);
                        if (tendency == 0){
                            double weight = (double) (5 / d);
                            w.orient(x, y, weight);
                        }
                        
                        
                        //System.err.printf("  Ally %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    //add the vector contribution to the movement vector
                    movementVector = movementVector.add(w);

                }
            }   
        }
        // System.out.println("this species: " + this.getSpecies());
        // System.out.println("movement vector: X: " + v.getX() + " Y: " + v.getY());
        // System.out.println("-----***------");
        Grid.Direction direction = getDirection(movementVector);
        
        //makes sure that it has a valid direction to move in, if it doesn't it will move in a random valid direction
        if (movementVector.getWeight() != 0) {
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
            //if the animal can move in A direction:
            if (canMove()){
                //check each direction until it finds a direction where it can move int
                for (int i = (d.ordinal() + 1) % 8; i != d.ordinal() + 8; i++) {
                    Grid.Direction newDirection = Grid.Direction.values()[i % 8];
                    if (canMove(newDirection)) {
                        //if it can move in this direction, then call the method again. This time it should not run through this loop again
                        safeMove(newDirection);
                        return;
                    }
                }
            }
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
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                //check all directions for each item in the hitbox
                for (int k = 0; k < Grid.Direction.values().length; k++) {
                    if (canMove(Grid.Direction.values()[k])) {
                        return true;
                }
        }
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
                }
            }
        }
        return null;
    }

    //TODO: tune this mechanic
    public boolean isHungry() {
        if (satiety < 0.9 * foodCapacity && preySpotted() >= 5) {
            return true;
        } 
        else if (satiety < 0.85 * foodCapacity && preySpotted() >= 3) {
            return false;
        } else {
            return satiety < 0.8 * foodCapacity; 
        }
        
    }

    //#region REPRODUCTION
    //TODO: add reproduction mechanics
    public void reproduce(Animal parent) throws IOException {
        // Create offspring of the same species as parent1
        Animal offspring = new Animal(this.species);
        
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

        Hitbox hitbox = new Hitbox(offspring, this.x, this.y);
        if (offspring.canMove()){
            Direction d = Grid.Direction.values()[(int) (Math.random() * Grid.Direction.values().length)];
            offspring.safeMove(d);
            Grid.addOrganism(hitbox);
        }
        
    }

    public boolean foundPotentialMate(){
        for (int i = this.y - 2; i <= this.y + this.height + 2; i++) {
            for (int j = this.x - 2; j <= this.x + this.width + 2; j++) {
                if (i >= 0 && i < Grid.grid.length && j >= 0 && j < Grid.grid[0].length) {
                    Hitbox hitbox = Grid.grid[i][j];
                    if (hitbox != null) {
                        Organism o = hitbox.getOrganism();
                        if (o != null && o != this && o.getSpecies() == this.getSpecies()) {
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
                        if (o != null && o != this && o.getSpecies() == this.getSpecies()) {
                            return (Animal) o;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean canReproduce() {
        return fertility >= rftr && satiety >= 0.6 * foodCapacity && foundPotentialMate();
    }

    

    
    


    public String toString() {
        if (species == Species.ANT) {
            return "A";
        } else if (species == Species.SPIDER) {
            return "S";
        } else if (species == Species.FROG) {
            return "F";
        } else {
            return "O";
        }
    }


    

    

    

}


    