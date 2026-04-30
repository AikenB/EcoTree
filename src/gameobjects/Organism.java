package gameobjects;

import java.util.ArrayList;

public class Organism {
    protected ArrayList<Mutation> mutations;

    protected double health;
    protected double stormResistance;
    public double heatToleranceBoost;
    public double coldToleranceBoost;


    protected int energy;



    public Organism() {
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

    public ArrayList<Mutation> getMutations() {
        return mutations;
    }

    public double getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }

    

    
    
}
