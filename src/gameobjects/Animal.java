package gameobjects;

import gameobjects.Organism.Species;
import gui.Grid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import utilities.Hitbox;
import utilities.WeightVector;


public class Animal extends Organism {
    
    private double photosynthesisEfficiency;
    
    private ArrayList<Species> predators;
    private ArrayList<Species> prey;

    private double speed;
    private double foodCapacity;
    private double thirstCapacity;
    // private Timer moveTimer;

    private ExecutorService executor;

    


    


    public Animal(Species species) {
        super(species);
        
        
        generateMutation();
        configureSpecies(species);

        // moveTimer = new Timer((int) (4000/speed), e -> move());
        // moveTimer.start();
        startBackgroundMovement();
    }

    private void configureSpecies(Species species) {
        switch(species){
            case ANT:
                speed = 1.0;
                foodCapacity = 5.0;
                thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER));
                prey = new ArrayList<>();
                break;
            case SPIDER:
                speed = 1.5;
                foodCapacity = 10.0;
                thirstCapacity = 10.0;
                predators = new ArrayList<>();
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT));
                prey.add(Species.ANT);
                break;
            case FROG:
                speed = 1.5;
                foodCapacity = 15.0;
                thirstCapacity = 20.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.SPIDER));
                break;

        }

        
    }

    private void startBackgroundMovement() {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int dt = (int)(4000/speed);
                    Thread.sleep(dt);
                    move();
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error in move() for " + species + ": " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
        });
    }

    

    public void stopMovement() {
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

    /**
     * used for move() method
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
    private Hitbox[][] getViewField() {
        Hitbox[][] viewField = new Hitbox[15][15];
        for (int i = 0; i < viewField.length; i++) {
            for (int j = 0; j < viewField[i].length; j++) {
                int x = this.x - 7 + j;
                int y = this.y - 7 + i;
                if (x >= 0 && x < Grid.grid.length && y >= 0 && y < Grid.grid[0].length) {
                    viewField[i][j] = Grid.grid[x][y];
                } else {
                    viewField[i][j] = null;
                }
            }
        }
        return viewField;
    }

    private void move() {
        //get field of view of organism (27 x 27 square with organism in the middle)
        Hitbox[][] viewField = getViewField();
        WeightVector v = new WeightVector(0, 0, 0);
        //create list of organisms in sight to avoid counting the same organism multiple times
        ArrayList<Organism> organismsInSight = new ArrayList<>();


        for (int i = 0; i < viewField.length; i++) {
            for (int j = 0; j < viewField[i].length; j++) {
                if (viewField[i][j] == null)
                    continue;
                Organism o = viewField[i][j].getOrganism();
                WeightVector w = new WeightVector(0,0,0);
                //makes sure it will only add a weight vector if it is a new organism that has not been counted yet
                if (isNewOrganism(organismsInSight, o)) {
                    organismsInSight.add(o);
                    int x =  j - 7;      
                    int y = i - 7;    
                    double d = Math.sqrt(x*x + y*y);  
                    /*formula for calculating the weight vector for each organism.
                    the impact of d can be tuned to prevent the organism from going in the middle of two prey*/
                    if (predators.contains(o.getSpecies())) {
                        double weight = (double) 250 / d;
                        w.orient(x, y, weight);
                        w.doubleOrthogonalize();
                        //System.err.printf("  Predator %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, final angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    else if (prey.contains(o.getSpecies())) {
                        double weight = (double) o.energy / (5*d);
                        w.orient(x, y, weight);
                        //System.err.printf("  Prey %s at rel pos (%d,%d), dist=%.2f, weight=%.2f, angle=%.2f%n", o.getSpecies(), x, y, d, weight, w.getTheta());
                    }
                    v = v.add(w);

                }
            }   
        }

        Grid.Direction direction = getDirection(v);
        //System.err.printf("[%s at (%d,%d)] Organisms in sight: %d, v.weight=%.2f, v.theta=%.2f, direction=%s%n", 
            //species, x, y, organismsInSight.size(), v.getWeight(), v.getTheta(), direction);
        if (v.getWeight() != 0) {
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
                for (int i = (d.ordinal() + 1) % 8; i != d.ordinal() + 8; i++) {
                    Grid.Direction newDirection = Grid.Direction.values()[i % 8];
                    if (canMove(newDirection)) {
                        safeMove(newDirection);
                        return;
                    }
                }
            }
            //if it can't then it won't move at all
            
        }
        else {
            Hitbox hitbox = this.getHitbox();
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
        for (int i = newX; i < newX + width; i++) {
            for (int j = newY; j < newY + height; j++) {
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
    

    public static Animal reproduce(Animal parent1, Animal parent2) {
        // Create offspring of the same species as parent1
        Animal offspring = new Animal(parent1.species);
        
        // Combine mutations from both parents into a gene pool
        ArrayList<Mutation> genePool = new ArrayList<>();
        genePool.addAll(parent1.getMutations());
        genePool.addAll(parent2.getMutations());
        
        // Randomly inherit half of the mutations from the gene pool
        int inheritCount = genePool.size() / 2;
        for (int i = 0; i < inheritCount; i++) {
            int randomIndex = (int) (Math.random() * genePool.size());
            Mutation inheritedMutation = genePool.get(randomIndex);
            offspring.addMutation(inheritedMutation);
            genePool.remove(randomIndex);  // Remove to avoid inheriting twice
        }
        
        return offspring;
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


    