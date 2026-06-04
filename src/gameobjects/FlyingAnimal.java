package gameobjects;
import gameobjects.Organism.Species;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import utilities.Grid;
import utilities.Grid.Direction;
import utilities.Hitbox;
import utilities.WeightVector;

public class FlyingAnimal extends Animal{

    private ExecutorService beeExecuter;
    private Plant targetPlant;
    private ArrayList<Plant> previousTargets;
    private ArrayList<Plant> contactedPlants;
    WeightVector moveVector;
    HashMap<Organism, Double> plantWeights;
    private double cooldown;
    private Species species;
    private double totalSpeed;
    double kP;
    double maxSpeedBonus;
    double kPDistance; // used for acceleration mechanism

    private static final double groundedRadius = 5.7;

    //FOR TESTING PURPOSES
    private static int id = 1;
    private int uniqueID;

    //for other flying animals other than bees
    int[] targetLocation;
    Organism targetPrey;

    FlyingAnimal targetMate;
    int[] groundedLocation; //location of prey where flying animal is trying to go to or just left from




    public FlyingAnimal(Species species){
        uniqueID = id;
        id++;
        
        this.species = species;
        super(species);
        totalSpeed = speed;
        //super.stopBehavior();
        if (species == Species.BEE){
            contactedPlants = new ArrayList<Plant>();
            targetPlant = null;
            cooldown = 35;
            previousTargets = new ArrayList<Plant>();
            previousTargets.add(null);
        } else{
            targetLocation = determineTargetLocation();
            targetMate = null;
            targetPrey = null;
            if (species == Species.HUMMINGBIRD){
                kP = 0.4;
                maxSpeedBonus = 1.5;
                kPDistance = 12;
            }
            if (species == Species.BALD_EAGLE){
                kP = 0.5;
                maxSpeedBonus = 7.5;
                kPDistance = -10;
            }
            
        }
        fertility = 0;
        //satiety = 0.5 * foodCapacity; //TODO: fix when done testing
        //initializeBehavior();
        
        
    }

    public void initializeBehavior() {
        beeExecuter = Executors.newSingleThreadExecutor();
        beeExecuter.submit(() -> {
            while (energy > 0) {
                try {
                    int dt = (int)(4000/totalSpeed);
                    Thread.sleep(dt); 
                    if (species == Species.BEE){
                        beeBehavior(dt);
                        satiety = energy;
                        if (energy <= 0) {
                            Grid.killFlyingAnimal(this);
                            Grid.updateSpeciesList();
                            stopBehavior();
                            break;
                        }
                    } else {
                        flyingAnimalBehavior();
                        if (energy <= 0) {
                            Grid.killFlyingAnimal(this);
                            Grid.updateSpeciesList();
                            stopBehavior();
                            break;
                        }
                        if (health <= 0) {
                            Grid.killFlyingAnimal(this);
                            Grid.updateSpeciesList();
                            stopBehavior();
                            break;
                        }
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

    private void beeBehavior(int dt){
        cooldown -= dt/1000.0;
        //System.out.println(dt/4000.0);
        if (cooldown < 0){
            cooldown = 0;
        }
        //System.out.println(cooldown);
        //System.out.println(contactedPlants);
                    
        move();
        if (touchingTarget()) {
            //System.out.println("Cooldown: " + cooldown + ", ContactedPlants: " + contactedPlants.size() + ", TargetSpecies: " + targetPlant.getSpecies());
            //boolean hasParent = isParentFound(targetPlant);
            //System.out.println("isParentFound: " + hasParent);
            contactedPlants.add(targetPlant);
            //makes sure bee doesn't go to the last 3 plants it followed
            previousTargets.add(targetPlant);
            if (previousTargets.size() > 3) {
                previousTargets.remove(0);
            }
                        
            if (isHungry() && targetPlant.hasProduce()){
                satiety += targetPlant.energy;
                targetPlant.updateProduce(targetPlant.getProduce() - 1);
                if (targetPlant.species == Species.FLOWER){ // flowers help plants live longer by giving an energy boost
                    energy += targetPlant.getEnergy();
                    cooldown -= 5;
                }
            }
            if (cooldown <= 0 && isParentFound(targetPlant) && targetPlant.canReproduce()){
                //System.out.println("found parent" + targetPlant.getSpecies());
                int chance = (int) (Math.random() * 3);
                if (chance == 0){
                    Plant parent2 = getParent(targetPlant);
                    try {
                        targetPlant.reproduce(parent2);
                    } catch (IOException eee) {
                        Thread.currentThread().interrupt();
                    }
                    contactedPlants.remove(targetPlant);
                    contactedPlants.remove(parent2);
                    //System.out.println(contactedPlants);
                    cooldown = 25;
                }
                            
            }
            targetPlant = null;
        }
        energy-= dt/1000.0;
                    
    }

    

    @Override
    /**
     * stops the animal's behavior thread
     */
    public void stopBehavior() {
        if (beeExecuter != null) {
        beeExecuter.shutdownNow();  // Interrupt the thread immediately
        // try {
        //     if (!beeExecuter.awaitTermination(1, TimeUnit.SECONDS)) {
        //         beeExecuter.shutdownNow();
        //     }
        // } catch (InterruptedException e) {
        //     beeExecuter.shutdownNow();
        //     Thread.currentThread().interrupt();
        // }
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
                    && hitbox.getOrganism().species != Species.MAINTREE
                    && !previousTargets.contains(hitbox.getOrganism()) //makes sure bee doesn't target the same plant after visiting it
                    && hitbox.getOrganism() instanceof Plant //makes sure its a plant
                    && isNewOrganism(new ArrayList<Organism>(plantWeights.keySet()), hitbox.getOrganism())) { //makes sure plant is not previously spotted
                        Plant plant = (Plant) hitbox.getOrganism();
                        int relativeX = j - 26;
                        int relativeY = i - 26;
                        double d = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
                        //formula: (produce on plant) * (half of max produce) / distance from plant. 
                        //this will make the bee prefer plants that can produce a lot and have a lot of produce
                        double weight = ((plant.getProduce() + 1) * ((double) (plant.getMaxProduce()/2))) / Math.pow(d,1/4); //TODO tune if nessecary
                        plantWeights.put(plant, weight);
                    }
                    
                    
                }
            }
            if (!plantWeights.isEmpty()) {
                targetPlant = findPlantTarget(plantWeights);
                
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

    private Plant findPlantTarget(HashMap<Organism, Double> plantWeights) {
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
        
        // synchronized (Grid.grid) {
        //     for (int i = 0; i < 3; i++) {
        //         for (int j = 0; j < 3; j++) {
        //             int x = this.x - 1 + j;
        //             int y = this.y - 1 + i;
        //             boolean flyingAnimalThere = false;
        //             for (FlyingAnimal flyingAnimal : Grid.flyingAnimals) {
                        
        //                 if (flyingAnimal != this &&flyingAnimal.getX() == x && flyingAnimal.getY() == y) {
        //                     flyingAnimalThere = true;
                        
        //                 } 
                        
        //             }
        //             if (!flyingAnimalThere) {
        //                 return true;
        //             }
        //         }
        //     }
        // }
        for (int i = 0; i < Grid.Direction.values().length; i++) {
            Grid.Direction d = Grid.Direction.values()[i];
            if (canMove(d)) {
                return true;
            }
        }
        return false;  
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
            for (FlyingAnimal animal : Grid.flyingAnimals) {
                        
                if (animal.getX() == newX && animal.getY() == newY) {
                    return false;  
                        
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
            x = newX;
            y = newY;
            Hitbox hitbox = this.getHitbox();
            if (hitbox == null) {
                // Organism has been removed from grid or hitbox is null
                return;
            }
            
            hitbox.setX(newX);
            hitbox.setY(newY);
        } else {
            if (canMove()) {
                // If the intended direction is blocked, try other directions
                //this algorithm makes it so that if the organism can't move its original intended direction it will check the closest directions around it until it finds one it can move in
                int i = d.ordinal();
                //System.out.println("intended direction: " + d);
                int k = 1;
                for (int j = 0; j < 7; j++){
                    i += k * (int) Math.pow(-1, j);
                    //System.out.println("i: " + i + " k: " + k * (int) Math.pow(-1, j));
                    k++;
                    //makes i go in the order of 1, -2, 3, -4
                    //this is so that it checks the next closest directions around the initial direction
                    
                    int index = (i) % 8;
                    //System.out.println("index" + index);
                    if (index < 0) {
                        index = 8 + index;
                    }
                    Grid.Direction newDirection = Grid.Direction.values()[index];
                    //System.out.println(newDirection + " " + j);
                    if (canMove(newDirection)) {
                        //System.out.println("moved in direction: " + newDirection);
                        //if it can move in this direction, then call the method again. This time it should not run through this loop again
                        safeMove(newDirection);
                        return;
                    }
                }
            }
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

    //#region nonBee methods

    public void flyingAnimalBehavior(){
        //xi and yi are just for debugging
        int xi = this.x;
        int yi = this.y;
        move_nonBee();
        if (xi == this.x && yi == this.y){
           System.out.println("froze - id: " + uniqueID);
        }
        // if (isGrounded()){
        //     System.out.println("grounded - id: " + uniqueID);
        // }
        totalSpeed = calculateTotalSpeed();


        if (targetPrey != null && targetPrey instanceof Plant && ((Plant) targetPrey).getProduce() == 0){ //if the prey is a plant and has no produce left then stop targeting it
            targetPrey = findPrey(); //find new prey if plant has no produce anymore
            //System.out.println("target has no produce");
        }
        if (isHungry() && touchingPrey()){
            if (targetPrey instanceof Plant){ 
                Plant plantPrey = (Plant) targetPrey;
                plantPrey.updateProduce(-1);
                            
                if (targetPrey.species == Species.GRASS){
                    ((Plant) targetPrey).updateHealth(-(Math.random()*10 + 30));
                } else if (targetPrey.species == Species.FERN){
                    ((Plant) targetPrey).updateHealth(-(Math.random()*10 + 15));
                }
            } else {
                System.out.println("ate prey " + targetPrey.getSpecies());
                kill(targetPrey);
                Grid.updateSpeciesList();
            }
            satiety += targetPrey.getEnergy();
            fertility += targetPrey.getEnergy() * 0.8;
            targetPrey = null;
            if (canReproduce_nonbee() && potentialMateExists() && targetMate == null){
                int chance = (int)(Math.random() * 2);
                if (chance == 0){
                    FlyingAnimal mate = findMate();
                    targetMate = mate; // Set this animal's targetMate to the found mate
                    mate.targetMate = this; // Set the targetMate of the found mate to this animal     
                            
                }
            }
        }
        if (targetMate != null && withinProximityOfMate()){
            FlyingAnimal mate = targetMate;  // Cache reference to avoid race condition
            System.out.println("ready to mate - id: " + uniqueID);
            try {
                reproduce_nonbee(mate);
            
            } catch (IOException e) {
                Thread.currentThread().interrupt();
            }
            fertility = 0;
            if (mate != null) {  // Verify mate hasn't been nulled by another thread
                mate.fertility = 0;
                mate.targetMate = null;
            }
            targetMate = null;
        }


        satiety -= 0.1 * mass;
        satiety = Math.min(foodCapacity, Math.max(satiety, 0)); //clamp satiety between 0 and its max capacity
        fertility += 0.5;
        fertility = Math.min(fertility, rftr);
        if (satiety == 0) {
            //animal dies if it reaches 0 satiety
            kill(this);
            Grid.updateSpeciesList();
            stopBehavior();
        } else if (health <= 0) {
            //animal dies if it reaches 0 health
            kill(this);
            Grid.updateSpeciesList();
            stopBehavior();
        }
    }

    private boolean hasPreyOnMap(){
        for (Hitbox h : Grid.hitboxes) {
            if (h.getOrganism() != null && prey.contains(h.getOrganism().getSpecies())){
                if (h.getOrganism() instanceof Plant && ((Plant) h.getOrganism()).getProduce() == 0){ 
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    private int[] determineTargetLocation(){
        int[] location = new int[2];
        boolean isValidLocation = false;
        
        // if (canReproduce() && potentialMateExists()){
        //     int chance = (int)(Math.random() * 2);
        //     if (chance == 0){
        //         FlyingAnimal mate = findMate();
        //         targetMate = mate; // Set this animal's targetMate to the found mate
        //         mate.targetMate = this; // Set the targetMate of the found mate to this animal
                
        //         location[0] = mate.getX();
        //         location[1] = mate.getY();
        //         return location;
                
        //     }
        // }
        while (!isValidLocation) {
            location[0] = (int)(Math.random() * Grid.grid[0].length);
            location[1] = (int)(Math.random() * Grid.grid.length);
            if (Math.abs(this.x - location[0]) > 20 && Math.abs(this.y - location[1]) > 20){ //makes sure target location is not too close to current location
                isValidLocation = true;
            }
        }
        return location;
    }

    private Organism findPrey(){
        double biggestPreyWeight = 0;
        Organism target = null;
        for (Hitbox h : Grid.hitboxes) {
            if (h.getOrganism() != null && prey.contains(h.getOrganism().getSpecies())){
                Organism o = h.getOrganism();
                if (o instanceof Plant && ((Plant) o).getProduce() == 0){ //ignore plants with no produce
                    continue;
                    
                }
                double d = Math.hypot(o.getX()- this.x, o.getY() - this.y); 
                double weight = o.getEnergy() / d; //TODO tune if necessary
                if (weight > biggestPreyWeight) {
                    biggestPreyWeight = weight;
                    target = o;
                }
            }
        }
        //System.out.println("target prey: " + target);
        return target;
            
    }

    /**returns whether the animal is grounded. When flying animals are close to their prey they will become vulnerable to predators */
    public boolean isGrounded(){
        if (groundedLocation == null){
            return false;
        } else{
            double d = Math.hypot(groundedLocation[0] - this.x, groundedLocation[1] - this.y);
            return d < groundedRadius;
        }
    }

    private double calculateTotalSpeed(){
        if (groundedLocation == null){
            return speed;
        } else {
            double d = Math.hypot(groundedLocation[0] - this.x, groundedLocation[1] - this.y);
            //use concept of PID from robotics to make flying animls speed up and then slow down as they approach their prey
            //System.out.println("Total speed: " + Math.min(speed * maxSpeedBonus, speed + kP * (d-12)));
            return Math.min(speed * maxSpeedBonus, speed + kP * (d-kPDistance));
        }
    }

    private boolean isAtTargetLocation(){
        return this.x == targetLocation[0] && this.y == targetLocation[1];
    }

    private boolean touchingPrey(){
        if (targetPrey == null) {
            return false;
        }
        //int targetX = targetPrey.getX();
        //int targetY = targetPrey.getY();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = this.x - 1 + j;
                int y = this.y - 1 + i;
                x = Math.max(0, Math.min(x, Grid.grid[0].length - targetPrey.getWidth()));
                y = Math.max(0, Math.min(y, Grid.grid.length - targetPrey.getHeight()));
                if (Grid.grid[y][x] != null && Grid.grid[y][x].getOrganism() == targetPrey) {
                    return true;
                }
            }
        }
        return false;
    }

    //region #nonbee movement
    private void move_nonBee(){
            //System.out.println("target prey: " + targetPrey + "target mate: " + targetMate + " - id: " + uniqueID);
            //satiety = 0.95 * foodCapacity;
            //System.out.println("is hungry: " + isHungry());
            //(!isHungry() && targetPrey == null || targetMate != null) || (isHungry() && !hasPreyOnMap())
        if (isHungry() && hasPreyOnMap()){
                //System.out.println("case2: hunting");
                //System.out.println("hunting - id: " + uniqueID);
                targetPrey = findPrey();
                groundedLocation = new int[]{targetPrey.getX(), targetPrey.getY()};
                targetLocation = groundedLocation;
               //System.out.println("has target prey: " + (targetPrey != null) + " - id: " + uniqueID);
                //System.out.println("moving towards prey - id: " + uniqueID);
                WeightVector w = new WeightVector(targetPrey.getX() - this.x, targetPrey.getY() - this.y);
                Direction d = super.getDirection(w);
                safeMove(d);
        } else{
            
            
            //System.out.println("case1 - id: " + uniqueID);
            //System.out.println("has prey on map: " + hasPreyOnMap());
            if (targetPrey != null){
                groundedLocation = new int[]{targetPrey.getX(), targetPrey.getY()};
                targetLocation = groundedLocation;
            } else if (groundedLocation != null && Math.hypot(groundedLocation[0] - this.x, groundedLocation[1] - this.y) > groundedRadius){
                groundedLocation = null;
                //System.out.println("not grounded anymore - id: " + uniqueID);

            }
            targetPrey = null;
            //System.out.println("is at target location: " + isAtTargetLocation() + " - id: " + uniqueID);
            if (!isAtTargetLocation()){
                //System.out.println("moving towards target location - id: " + uniqueID);
                if (targetMate != null){ //find coordinates of mate
                    targetLocation[0] = targetMate.getX();
                    targetLocation[1] = targetMate.getY();

                }
                WeightVector w = new WeightVector(targetLocation[0] - this.x, targetLocation[1] - this.y);
                Direction d = super.getDirection(w);
                // Always try to move (safeMove will find alternative directions if blocked)
                safeMove(d);
                //System.out.println("can move at all:" + canMove());
                //System.out.println("can move in intended direction: " + canMove(d));
                
                
            }
            if (isAtTargetLocation()){
                    // System.out.println("can reproduce: " + canReproduce_nonbee() + ", potential mate exists: " + potentialMateExists());
                    if (targetMate == null && canReproduce_nonbee() && potentialMateExists()){
                        // System.out.println("attempting to find mate - id: " + uniqueID);
                        int chance = (int)(Math.random() * 2);
                        if (chance == 0){
                            FlyingAnimal mate = findMate();
                            targetMate = mate; // Set this animal's targetMate to the found mate
                            mate.targetMate = this; // Set the targetMate of the found mate to this animal
                            targetLocation[0] = targetMate.getX();
                            targetLocation[1] = targetMate.getY();
                            //System.out.println("found mate - id: " + uniqueID);
                            
                        } else {
                            //System.out.println("failed to find mate - id: " + uniqueID);
                            targetLocation = determineTargetLocation();
                        }
                    } else {
                        //System.out.println("finding new target location - id: " + uniqueID);
                        targetLocation = determineTargetLocation();
                    }
                }
            
            
        }
    }

    /**
     * This method checks to see if the bee has contacted another plant of the same species so it can help the plant reproduce
     * @param parent
     * @return
     */
    private boolean isParentFound(Plant parent){
        for (Plant plant : contactedPlants) {
            if (plant != parent && plant.getSpecies() == parent.getSpecies()) {
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

    private boolean potentialMateExists(){
        for (FlyingAnimal animal : Grid.flyingAnimals) {
            if (animal.getSpecies() == this.species  
            && animal != this
            && animal.canReproduce_nonbee()){
                return true;
            }
        }
        return false;
    }

    public boolean canReproduce_nonbee() {

        if (diseases.keySet().contains(Outbreak.type.FUNGUS_INFECTION)) {
            return false;
        } else {
            return fertility >= rftr 
            && satiety >= 0.9 * foodCapacity 
            && health >= 0.5 * maxHealth;
        }
        
    }

    private FlyingAnimal findMate(){
        if (targetMate != null)
            return targetMate;

        ArrayList<FlyingAnimal> possibleMates = new ArrayList<>();
        for (FlyingAnimal animal : Grid.flyingAnimals) {
            if (animal.getSpecies() == this.species  
            && animal != this
            && animal.canReproduce_nonbee()){
                possibleMates.add(animal);
            }
        }
        if (!possibleMates.isEmpty()) {
            return possibleMates.get((int)(Math.random() * possibleMates.size()));
        }
        return null;
    }

    // public void reproduce_nonbee(FlyingAnimal parent) throws IOException {
    //     //System.out.println("reproducing - id: " + uniqueID);
    //     // Create offspring of the same species as parent1
    //     FlyingAnimal offspring = new FlyingAnimal(this.species);
    //     offspring.stopBehavior();
        
    //     // Combine mutations from both parents into a gene pool
    //     ArrayList<Mutation> genePool = new ArrayList<>();
    //     genePool.addAll(this.getMutations());
    //     genePool.addAll(parent.getMutations());
        
    //     // Randomly inherit half of the mutations from the gene pool
    //     int inheritCount = genePool.size() / 2;
    //     for (int i = 0; i < inheritCount; i++) {
    //         int randomIndex = (int) (Math.random() * genePool.size());
    //         Mutation inheritedMutation = genePool.get(randomIndex);
    //         offspring.addMutation(inheritedMutation);
    //         genePool.remove(randomIndex);  // Remove to avoid inheriting twice
    //     }

    //     Hitbox hitbox = new Hitbox(offspring, this.x, this.y);
    //     if (offspring.canMove()){
    //         //System.out.println("offspring can be placed - id: " + uniqueID);
    //         Direction d = Grid.Direction.values()[(int) (Math.random() * Grid.Direction.values().length)];
    //         offspring.safeMove(d);
    //         Grid.createFlyingAnimal(offspring.species, offspring.x, offspring.y);
            

    //     }
        
    // }

    public void reproduce_nonbee(FlyingAnimal parent) throws IOException {
        // build and shuffle direction list
        Direction[] dirs = Grid.Direction.values().clone();
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            Direction tmp = dirs[i]; dirs[i] = dirs[j]; dirs[j] = tmp;
        }

        for (Direction d : dirs) {
            int newX = this.x, newY = this.y;
            switch (d) {
                case UP:         
                    newY--; break;
                case DOWN:       
                    newY++; break;
                case LEFT:       
                    newX--; break;
                case RIGHT:      
                    newX++; break;
                case UP_LEFT:    
                    newX--; 
                    newY--; 
                    break;
                case UP_RIGHT:   
                    newX++; 
                    newY--; 
                    break;
                case DOWN_LEFT:  
                    newX--; 
                    newY++; 
                    break;
                case DOWN_RIGHT: 
                    newX++; 
                    newY++; 
                    break;
            }
        FlyingAnimal offspring = Grid.createFlyingAnimal(this.species, newX, newY);
        if (offspring != null) {
            // inherit mutations
            ArrayList<Mutation> genePool = new ArrayList<>();
            genePool.addAll(this.getMutations());
            genePool.addAll(parent.getMutations());
            int inheritCount = genePool.size() / 2;
            for (int i = 0; i < inheritCount; i++) {
                int idx = (int)(Math.random() * genePool.size());
                offspring.addMutation(genePool.get(idx));
                genePool.remove(idx);
            }
            return;
        }
        
    }
    // no free adjacent space, reproduction fails
    }

    public boolean withinProximityOfMate(){
        for (int i = this.y - 2; i <= this.y + this.height + 2; i++) {
            for (int j = this.x - 2; j <= this.x + this.width + 2; j++) {
                if (i >= 0 && i < Grid.grid.length && j >= 0 && j < Grid.grid[0].length) {
                    for (FlyingAnimal animal : Grid.flyingAnimals) {
                        if (animal == targetMate && animal.getX() == j && animal.getY() == i) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    

    

}

        
        
    
