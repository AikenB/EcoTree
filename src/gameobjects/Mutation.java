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
        FERTILITY_BOOST,
        INFECTION_RESISTANCE_BOOST,

        //animal mutations
        SPEED_BOOST,
        FOOD_CAPACITY_BOOST,
        THIRST_CAPACITY_BOOST
        
    }


    //yes I know I made these public so that I dont have to make a ton of getter methods
    //mutation boosts will be a multiplier from 0.75 to 1.25
    //public double healthBoost;
    public double speedBoost;
    public double foodCapacityBoost;
    public double thirstCapacityBoost;
    public double stormResistanceBoost;
    public double photosynthesisEfficiencyBoost;
    public double heatToleranceBoost;
    public double coldToleranceBoost;
    public double fertilityBoost;
    public double infectionResistanceBoost;
    public Type type;

    public double value;

    

    public Mutation(Type type, double value) {
        this.type = type;
        this.value = value;

        // Initialize all boosts to 1.0 (no change) by default
        this.speedBoost = 1.0;
        this.foodCapacityBoost = 1.0;
        this.thirstCapacityBoost = 1.0;
        this.stormResistanceBoost = 1.0;
        this.photosynthesisEfficiencyBoost = 1.0;
        this.heatToleranceBoost = 1.0;
        this.coldToleranceBoost = 1.0;
        this.fertilityBoost = 1.0;
        this.infectionResistanceBoost = 1.0;

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
            case FERTILITY_BOOST:
                this.fertilityBoost = value;
                break;
            case INFECTION_RESISTANCE_BOOST:
                this.infectionResistanceBoost = value;
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        String name;
        switch(type) {
            case SPEED_BOOST:
                name = "Speed";
                break;
            case FOOD_CAPACITY_BOOST:
                name = "Food Capacity";
                break;
            case THIRST_CAPACITY_BOOST:
                name = "Thirst Capacity";
                break;
            case STORM_RESISTANCE_BOOST:
                name = "Storm Resistance";
                break;
            case PHOTOSYNTHESIS_EFFICIENCY_BOOST:
                name = "Photosynthesis Efficiency";
                break;
            case HEAT_TOLERANCE_BOOST:
                name = "Heat Tolerance";
                break;
            case COLD_TOLERANCE_BOOST:
                name = "Cold Tolerance";
                break;
            case FERTILITY_BOOST:
                name = "Fertility";
                break;
            case INFECTION_RESISTANCE_BOOST:
                name = "Infection Resistance";
                break;
            default:
                name = "Unknown Mutation";
        }
        return name + "x" + value;
    }

}
