package gameobjects;

import gui.Grid;
import java.util.ArrayList;


public class Animal extends Organism {
    
    private double photosynthesisEfficiency;
    private AnimalSpecies species;
    private ArrayList<Organism> predators;
    private ArrayList<Organism> prey;

    private double speed;
    private double foodCapacity;
    private double thirstCapacity;

    


    public static enum AnimalSpecies {
        RABBIT,
        DEER,
        WOLF,
        BEAR,
        COW,
        SNAKE,
        TARDIGRADE,
        ANT,
        WORM,
        MOUSE,

    }


    public Animal(AnimalSpecies species) {
        super();
        this.species = species;
        
        generateMutation();
        switch(species){

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

    private void move() {
        Organism[][] viewField = new Organism[51][51];
        WeightVector v = new WeightVector(0, 0, 0);


        for (int i = 0; i < viewField.length; i++) {
            for (int j = 0; j < viewField[i].length; j++) {
                Organism o = viewField[i][j];
                WeightVector w = new WeightVector(0,0,0);
                if (o != null || o != this) {
                    int x =  j - 24;
                    int y = i - 24;
                    double d = Math.sqrt(x*x + y*y);
                    
                    if (predators.contains(o)) {
                        double weight = (double) 250 / d;
                        w.orient(x, y, weight);
                        
                    }
                    else if (prey.contains(o)) {
                        double weight = (double) o.energy / d;
                        w.orient(x, y, weight);
                    }
                    v = v.add(w);

                }
            }   
        }
        Grid.Direction direction = getDirection(v);
        if (v.getWeight() != 0) {
            safeMove(direction);
        } else {
            Grid.Direction randomD = Grid.Direction.values()[(int) (Math.random() * Grid.Direction.values().length)];
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
            Grid.removeOrganism(x,y);
            x = newX;
            y = newY;
            Grid.addOrganism(this);
        }
    }
    /**
     * checks if the animal can move in any direction at all
     * 
     */
    private boolean canMove(){
        for (int i = 0; i < Grid.Direction.values().length; i++) {
            if (canMove(Grid.Direction.values()[i])) {
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
        return (
            newX >= 0 
            && newX < Grid.grid.length
            && newY >= 0 
            && newY < Grid.grid[0].length 
            && Grid.grid[newX][newY] == null);
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



}


    