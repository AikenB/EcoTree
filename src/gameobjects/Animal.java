package gameobjects;

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

    public void move() {
        int[][] grid = new int[48][48];


        

    }


    public Animal reproduce() {
        Animal newAnimal = new Animal(this.species);
        for (Mutation mutation : this.mutations) {
            newAnimal.addMutation(mutation);
        }
        return newAnimal;
}


    