package gameobjects;

/**
 * Class representing a mutation on an organism
 */
public class Mutation {


    public static enum Type {


        //plant mutations
        PHOTOSYNTHESIS_EFFICIENCY_BOOST,

        //organism mutations
        HEAT_TOLERANCE_BOOST,
        COLD_TOLERANCE_BOOST,
        STORM_RESISTANCE_BOOST,

        //animal mutations
        SPEED_BOOST,
        FOOD_CAPACITY_BOOST,
        THIRST_CAPACITY_BOOST
        
        
        
    }


    //yes I know I made these public so that I dont have to make a ton of getter methods
    public double healthBoost;
    public double speedBoost;
    public double foodCapacityBoost;
    public double thirstCapacityBoost;
    public double stormResistanceBoost;
    public double photosynthesisEfficiencyBoost;
    public double heatToleranceBoost;
    public double coldToleranceBoost;

    public Type type;

    public double value;

    

    public Mutation(Type type, double value) {
        this.type = type;
        this.value = value;

        switch(type) {
            case SPEED_BOOST:
                this.speedBoost = value;
                break;
            case FOOD_CAPACITY_BOOST:
                this.foodCapacityBoost = value;
                break;
            case THIRST_CAPACITY_BOOST:
                this.thirstCapacityBoost = value;
                break;
            case STORM_RESISTANCE_BOOST:
                this.stormResistanceBoost = value;
                break;
            case PHOTOSYNTHESIS_EFFICIENCY_BOOST:
                this.photosynthesisEfficiencyBoost = value;
                break;
            case HEAT_TOLERANCE_BOOST:
                this.heatToleranceBoost = value;
                break;
            case COLD_TOLERANCE_BOOST:
                this.coldToleranceBoost = value;
                break;
        }
    }

    public Type getType() {
        return type;
    }

}
