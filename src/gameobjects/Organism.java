package gameobjects;

import java.util.ArrayList;

public class Organism {
    private ArrayList<String> mutations;



    public Organism() {
        this.mutations = new ArrayList<>();
    }

    public void addMutation(String mutation) {
        this.mutations.add(mutation);
    }
}
