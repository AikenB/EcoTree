package gameobjects;

import java.util.ArrayList;

public class Organism {
    protected ArrayList<Mutation> mutations;

    protected double health;
    protected double stormResistance;


    public Organism() {
        this.mutations = new ArrayList<>();

    }

    public void addMutation(Mutation mutation) {
        this.mutations.add(mutation);

    }

    public ArrayList<Mutation> getMutations() {
        return mutations;
    }
    
}
