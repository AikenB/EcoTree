package gameobjects;
import java.util.ArrayList;


public class Plant extends Organism {
    
    private double photosynthesisEfficiency;
    private PlantSpecies species;
    private ArrayList<Organism> predators;

    public static enum PlantSpecies {
        GRASS,
        FLOWER,
        APPLE_TREE,
        OAK_TREE,
        PINE_TREE,
        CACTUS,
        FERN,
        ALGAE,
        MOSS,
        BUSH



    }


    public Plant(PlantSpecies species) {
        super();
        this.species = species;
        photosynthesisEfficiency = 1.0;
        generateMutation();
        switch(species){

        }

    }
    /**
     * adds a mutation to the plant and applies its effects
     */
    @Override
    public void addMutation(Mutation mutation) {
        super.addMutation(mutation);
        photosynthesisEfficiency *= mutation.photosynthesisEfficiencyBoost;
    }

    /**
     * generates a random mutation for the plant.
     * The plant can recieve up to 3 mutations, with each mutation having a 1/15 chance of happening
     */
    public void generateMutation(){
        
        int m1 = (int) (Math.random() * 15);
        int m2 = (int) (Math.random() * 15);
        int m3 = (int) (Math.random() * 15);

        /* generate random mutations here. The mutation will be randomly selected from
        the list of possible mutations */
        if (m1 == 0){
            //selects a random mutation from the list of enums for mutations
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            //generates a random multiplier between 0.75 and 1.25, rounded to 2 decimal places
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }
        if (m2 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }
        if (m3 == 0){
            Mutation.Type type = Mutation.Type.values()[(int) (Math.random() * 4)];
            double amplifier = Math.round((0.75 + Math.random() * 0.5) * 100) / 100.0;
            addMutation(new Mutation(type, amplifier));
        }

        
    }

    public Plant reproduce() {

        Plant newPlant = new Plant(this.species);
        for (Mutation mutation : this.mutations) {
            newPlant.addMutation(mutation);
        }
        return newPlant;
    }
}
