package gameobjects;
import gameobjects.Organism.Species;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import utilities.Grid;
import utilities.Grid.Direction;
import utilities.Hitbox;
import utilities.WeightVector;

public class Bee extends Animal{

    private ExecutorService beeExecuter;
    private Plant targetPlant;
    private ArrayList<Plant> previousTargets;
    private ArrayList<Plant> contactedPlants;
    WeightVector moveVector;
    HashMap<Organism, Double> plantWeights;
    private int cooldown;

    public Bee(){
        contactedPlants = new ArrayList<Plant>();
        super(Species.BEE);
        super.stopBehavior();
        targetPlant = null;
        initializeBehavior();
        cooldown = 25;
        previousTargets = new ArrayList<Plant>();
        previousTargets.add(null);
        
    }

    public void initializeBehavior() {
        beeExecuter = Executors.newSingleThreadExecutor();
        beeExecuter.submit(() -> {
            while (true) {
                try {
                    int dt = (int)(4000/speed);
                    cooldown -= dt/4000;
                    if (cooldown < 0){
                        cooldown = 0;
                    }
                    Thread.sleep(dt); 
                    move();
                    if (touchingTarget()) {
                        contactedPlants.add(targetPlant);
                        //makes sure bee doesn't go to the last 3 plants it followed
                        previousTargets.add(targetPlant);
                        if (previousTargets.size() > 3) {
                            previousTargets.remove(0);
                        }
                        
                        if (isHungry() && targetPlant.hasProduce()){
                            satiety += targetPlant.energy;
                            targetPlant.updateProduce(targetPlant.getProduce() - 1);
                        }
                        if (cooldown == 0 && isParentFound(targetPlant)){
                            Plant parent2 = getParent(targetPlant);
                            targetPlant.reproduce(parent2);
                            contactedPlants.remove(targetPlant);
                            contactedPlants.remove(parent2);
                            cooldown = 25;
                        }
                        targetPlant = null;
                    }
                    energy--;
                    if (energy <= 0) {
                        Grid.killBee(this);
                        Grid.updateSpeciesList();
                        stopBehavior();
                        break;
                    }
                } catch (InterruptedException e) {
                    // Thread was interrupted, exit the loop gracefully
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error in move() for " + species + ": " + e.getMessage());
                    e.printStackTrace();
                    
                }
            }
        });
    }

    @Override
    /**
     * stops the animal's behavior thread
     */
    public void stopBehavior() {
        if (beeExecuter != null) {
        beeExecuter.shutdownNow();  // Interrupt the thread immediately
        try {
            if (!beeExecuter.awaitTermination(1, TimeUnit.SECONDS)) {
                beeExecuter.shutdownNow();
            }
        } catch (InterruptedException e) {
            beeExecuter.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    }


//#region MOVEMENT
    /**
     * returns the viewfield of the animal. The viewfield is a coordinate grid of hitboxes relative to the animal, with the animal at the center. Used for move() method.
     */
    private Hitbox[][] getViewField() {
        Hitbox[][] viewField = new Hitbox[51][51];
        synchronized (Grid.grid) {
            for (int i = 0; i < viewField.length; i++) {
                for (int j = 0; j < viewField[i].length; j++) {
                    int x = this.x - 17 + j;
                    int y = this.y - 17 + i;
                    // Grid is [height][width], so y is row index, x is column index
                    if (y >= 0 && y < Grid.grid.length && x >= 0 && x < Grid.grid[0].length) {
                        viewField[i][j] = Grid.grid[y][x];
                    } else {
                        viewField[i][j] = null;
                    }
                }
            }
        }
        return viewField;
    }


    
    private void move() {
        if (targetPlant == null){
            Hitbox[][] viewField = getViewField();
            plantWeights = new HashMap<Organism, Double>();
            // ArrayList<Organism> plantsSpotted = new ArrayList<Organism>();

            for (int i = 0; i < viewField.length; i++) {
                for (int j = 0; j < viewField[i].length; j++) {
                    Hitbox hitbox = viewField[i][j];
                    
                    if (hitbox != null
                    && hitbox.getOrganism() != null 
                    && !previousTargets.contains(hitbox.getOrganism()) //makes sure bee doesn't target the same plant after visiting it
                    && hitbox.getOrganism() instanceof Plant //makes sure its a plant
                    && isNewOrganism(new ArrayList<Organism>(plantWeights.keySet()), hitbox.getOrganism())) { //makes sure plant is not previously spotted
                        Plant plant = (Plant) hitbox.getOrganism();
                        int relativeX = j - 26;
                        int relativeY = i - 26;
                        double d = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
                        //formula: (produce on plant) * (half of max produce) / distance from plant. 
                        //this will make the bee prefer plants that can produce a lot and have a lot of produce
                        double weight = (plant.getProduce() * ((double) (plant.getMaxProduce()/2))) / Math.pow(d,1/4); //TODO tune if nessecary
                        plantWeights.put(plant, weight);
                    }
                    
                    
                }
            }
            if (!plantWeights.isEmpty()) {
                targetPlant = findTarget(plantWeights);
                
                move();
            }
        } else{ //if the plant has a target:
            moveVector = new WeightVector(targetPlant.getX() - this.x, targetPlant.getY() - this.y);
            Direction d = super.getDirection(moveVector);
            if (canMove(d) && moveVector.getWeight() != 0){
                safeMove(d);
            } else{
                d = Direction.values()[(int)(Math.random() * Direction.values().length)]; //get random direction
                ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values())); //create list of all directions
                //keep picking random directions until a valid one is found or if the bee can't move anywhere
                while (!canMove(d) && !directions.isEmpty()){
                    d = Direction.values()[(int)(Math.random() * Direction.values().length)];
                    directions.remove(d);
                    if (canMove()){
                        safeMove(d);
                        break;
                    }
                    if (directions.isEmpty()){
                        //if the bee can't move anywhere then it will stay in place
                        return;
                    }
                } 
                //if the first random direction works then it will move there
                safeMove(d);
            }
        }
        
        
                    
    }

    private Plant findTarget(HashMap<Organism, Double> plantWeights) {
        //list of the weights of each plant spotted
        ArrayList<Double> weights = new ArrayList<>(plantWeights.values());

        ArrayList<Organism> plants = new ArrayList<>(plantWeights.keySet());
        //indexes of these weights. This will store the indexes of the top 3 weights
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        //loop used to find the top 3 weights and store their indexes
        for (int i = 0; i < Math.min(3,plantWeights.size()); i++) {
            
            double max = weights.get(0);
            int maxIndex = 0;
            for (int j = 0; j < weights.size(); j++) {
                if (weights.get(j) > max) {
                    max = weights.get(j);
                    maxIndex = j;
                }
            }
            indexes.add(maxIndex);
            weights.set(maxIndex, Double.MIN_VALUE);
            
        }
        //pick target plant randomly out of the top 3 weights
        int targetIndex = indexes.get((int)(Math.random() * indexes.size()));
        Plant target = (Plant) plants.get(targetIndex);
        return target;
    }

    private boolean canMove(){
        // Hitbox[][] hitboxes = new Hitbox[3][3];
        
        synchronized (Grid.grid) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int x = this.x - 1 + j;
                    int y = this.y - 1 + i;
                    
                    for (Bee bee : Grid.bees) {
                        
                        if (bee.getX() == x && bee.getY() == y) {
                            return false;  // Can't move if another bee is there
                        
                        } 
                        
                    }
                }
            }
        }
        return true;  // Can move if no bees are blocking
    }

    private boolean canMove(Direction d){
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
        synchronized (Grid.grid) {
            for (Bee bee : Grid.bees) {
                        
                if (bee.getX() == newX && bee.getY() == newY) {
                    return false;  // Can't move - bee is there
                        
                } 
                        
            }
        }
        return true;  // Can move - space is clear

    }

    private void safeMove(Direction d){
        if (canMove(d)) {
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
            Hitbox hitbox = this.getHitbox();
            if (hitbox == null) {
                // Organism has been removed from grid or hitbox is null
                return;
            }
            x = newX;
            y = newY;
            hitbox.setX(newX);
            hitbox.setY(newY);
        }
    }

    private boolean touchingTarget(){
        if (targetPlant == null) {
            return false;
        }
        int targetX = targetPlant.getX();
        int targetY = targetPlant.getY();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = this.x - 1 + j;
                int y = this.y - 1 + i;
                if (x == targetX && y == targetY) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * This method checks to see if the bee has contacted another plant of the same species so it can help the plant reproduce
     * @param parent
     * @return
     */
    private boolean isParentFound(Plant parent){
        for (Plant plant : contactedPlants) {
            if (plant.getSpecies() == parent.getSpecies()) {
                return true;
            }
        }
        return false;
    }

    /**
     * retrieves a parent plant from the contacted plants for the given plant. 
     * NOTE: this method should only be called if isParentFound() returns true
     * @param parent1
     * @return
     */
    private Plant getParent(Plant parent1){
        Species parentSpecies = parent1.getSpecies();
        ArrayList<Plant> possibleParents = new ArrayList<>();
        for (Plant plant : contactedPlants) {
            if (plant.getSpecies() == parentSpecies) {
                possibleParents.add(plant);
            }
        }
        return possibleParents.get((int)(Math.random() * possibleParents.size()));
    }

    

}

        
        
    
