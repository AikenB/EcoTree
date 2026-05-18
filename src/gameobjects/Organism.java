package gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import utilities.Grid;
import utilities.Hitbox;

public class Organism {
    protected ArrayList<Mutation> mutations;

    protected double health;
    protected double infectionResistance;
    protected double stormResistance;
    public double heatToleranceBoost;
    public double coldToleranceBoost;
    protected Species species;
    protected int trophicLevel;
    protected HashMap<Outbreak, Double> diseases;
    protected double maxHealth;


    public static CopyOnWriteArrayList<Integer> trophicLevels = new CopyOnWriteArrayList<Integer>(Arrays.asList(0,0,0,0,0,0));

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
        GRASSHOPPER,
        SCORPION,
        BEETLE,

        GRASS,
        FLOWER,
        APPLE_TREE,
        OAK_TREE,
        PINE_TREE,
        CACTUS,
        FERN,
        BERRY_BUSH,
        MOSS,
        BUSH,

        MAINTREE

    }
    
    
    



    public Organism(Species species) {

        this.species = species;
        this.mutations = new ArrayList<>();
        this.diseases = new HashMap<>();
        this.health = 100 + (int) (Math.random() * 40 -20); // Base health between 100 and 150
        this.infectionResistance = 1.0; // Base infection resistance
        this.stormResistance = 1.0; // Base storm resistance
        this.heatToleranceBoost = 1.0; // Base heat tolerance
        this.coldToleranceBoost = 1.0; // Base cold tolerance
        this.energy = 10;
        this.infectionResistance = 1.0; // Base infection resistance

    }

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);
        stormResistance *= mutation.stormResistanceBoost;
        heatToleranceBoost *= mutation.heatToleranceBoost;
        coldToleranceBoost *= mutation.coldToleranceBoost;
        infectionResistance *= mutation.infectionResistanceBoost;
        


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

    public double getInfectionResistance() {
        return infectionResistance;
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



    public HashMap<Outbreak, Double> getDiseases() {
        return diseases;
    }

    public void addInfection(Outbreak disease, double severity) {
        if (isInfectedWith(disease)) {
            diseases.replace(disease, diseases.get(disease) + severity);
            if (diseases.get(disease) > 1) {
                    diseases.replace(disease, 1.0); 
            }
        } else {
            diseases.put(disease, severity);
             if (diseases.get(disease) > 1) {
                    diseases.replace(disease, 1.0); 
            }
        }
    }

    public boolean isInfectedWith(Outbreak disease) {
        Outbreak.type type = disease.getDiseaseType();
        for (Outbreak d : diseases.keySet()) {
            if (d.getDiseaseType() == type) {
                return true;
            }
        }
        return false;
    }

    public void applyDiseaseEffect(Outbreak disease) {
        if (isInfectedWith(disease)) {
            double severity = diseases.get(disease);
            double magnitude = disease.getLethality() * severity * (1/infectionResistance); 
            health -= magnitude;
    }
    }

    public static void kill(Organism organism) {
        if (organism instanceof Animal) {
            ((Animal) organism).stopBehavior();
        }
        if (organism instanceof Plant) {
            ((Plant) organism).stopBehavior();
        }
        
        trophicLevels.set(organism.trophicLevel, trophicLevels.get(organism.trophicLevel) - 1);
        Grid.killOrganism(organism.getHitbox());
        organism.hitbox = null; // Clear hitbox reference to help GC
    }
    
    
}
