package gameobjects;

import gui.Grid;
import java.util.ArrayList;
import utilities.Hitbox;

public class Organism {
    protected ArrayList<Mutation> mutations;

    protected double health;
    protected double stormResistance;
    public double heatToleranceBoost;
    public double coldToleranceBoost;
    protected Species species;

    protected int energy;

    protected int x;
    protected int y;
    protected int width = 1;
    protected int height = 1;
    protected Hitbox hitbox; 

    public static enum Species {
        RABBIT,
        DEER,
        WOLF,
        BEAR,
        COW,
        SNAKE,
        FROG,
        BOBCAT,
        ANT,
        WORM,
        MOUSE,
        SPIDER,

        GRASS,
        FLOWER,
        APPLE_TREE,
        OAK_TREE,
        PINE_TREE,
        CACTUS,
        FERN,
        BERRYBUSH,
        MOSS,
        BUSH

    }
    
    
    



    public Organism(Species species) {

        this.species = species;
        this.mutations = new ArrayList<>();
        this.health = 100 + (int) (Math.random() * 40 -20); // Base health between 100 and 150
        this.stormResistance = 1.0; // Base storm resistance
        this.heatToleranceBoost = 1.0; // Base heat tolerance
        this.coldToleranceBoost = 1.0; // Base cold tolerance
        this.energy = 10;

    }

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);
        stormResistance *= mutation.stormResistanceBoost;
        heatToleranceBoost *= mutation.heatToleranceBoost;
        coldToleranceBoost *= mutation.coldToleranceBoost;


    }

    public static void kill(Organism organism) {
        if (organism instanceof Animal) {
            ((Animal) organism).stopMovement();
        }
        
        Grid.killOrganism(organism.getHitbox());
        organism.hitbox = null; // Clear hitbox reference to help GC
    }

    public ArrayList<Mutation> getMutations() {
        return mutations;
    }

    public double getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Hitbox getHitbox() {
        // Return cached hitbox if available
        if (hitbox != null) {
            return hitbox;
        }
        
        for (Hitbox hitbox : Grid.hitboxes) {
            if (hitbox.getOrganism() == this) {
                this.hitbox = hitbox;  // Update cache
                return hitbox;
            }
        }
        return null;
    }
    
    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }
    
    public Species getSpecies() {
        return species;
    }

    /**
     * ONLY USE THIS METHOD FOR THE HITBOX CLASS. THIS IS USED TO SYNC THE ORGANISM'S COORDINATES TO THE HITBOX
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * ONLY USE THIS METHOD FOR THE HITBOX CLASS. THIS IS USED TO SYNC THE ORGANISM'S COORDINATES TO THE HITBOX
     * @param x
     */
    public void setY(int y) {
        this.y = y;
    }

    
    
}
