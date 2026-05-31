package utilities;
import gameobjects.Animal;
import gameobjects.Bee;
import gameobjects.MainTree;
import gameobjects.Organism;
import gameobjects.Organism.Species;
import gameobjects.Plant;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class Grid {

    public static Hitbox[][] grid = new Hitbox[128][128];
    public static CopyOnWriteArrayList<Hitbox> hitboxes = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Bee> bees = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Sprite> beeSprites = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Sprite> sprites = new CopyOnWriteArrayList<>();
    /** A list of all different species present in the grid */
    public static CopyOnWriteArrayList<Species> speciesList = new CopyOnWriteArrayList<>();

    public static enum Direction{
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT
    }

    
    

    public static void addOrganism(Hitbox hitbox) {
        if (!hitboxes.contains(hitbox)) {
            hitboxes.add(hitbox);
            
            //sprites.add(hitbox.getSprite());
            addSprite(hitbox.getSprite());
        }
        synchronized (grid) {
            int x = hitbox.getX();
            int y = hitbox.getY();
            int width = hitbox.getWidth();
            int height = hitbox.getHeight();

            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    grid[i][j] = hitbox;
                }
            }
        }
    }

    public static void removeOrganism(Hitbox hitbox) {
        if (hitbox == null) {
            return;
        }
        
        synchronized (grid) {
            int x = hitbox.getX();
            int y = hitbox.getY();
            int width = hitbox.getWidth();
            int height = hitbox.getHeight();

            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    grid[i][j] = null;
                }
            }
        }
    }

    public static void killOrganism(Hitbox hitbox) {
        if (hitbox == null) {
            return;
        }
        
        hitboxes.remove(hitbox);
        sprites.remove(hitbox.getSprite());
        Organism organism = hitbox.getOrganism();
        organism.setHitbox(null);  // Clear the cache
        
        // Stop the organism's background movement if it's an Animal
        if (organism instanceof Animal) {
            ((Animal) organism).stopBehavior();
        }
        
        synchronized (grid) {
            int x = hitbox.getX();
            int y = hitbox.getY();
            int width = hitbox.getWidth();
            int height = hitbox.getHeight();

            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    grid[i][j] = null;
                }
            }
        }
    }

    /**
     * Creates an animal and automatically adds it to the grid
     * 
     */
    public static Animal createAnimal(Species species, int x, int y) throws IOException {
        
        Animal animal = new Animal(species);
        if (Grid.canFit(animal, x, y)){
            Hitbox hitbox = new Hitbox(animal, x, y);
            addOrganism(hitbox);
            updateSpeciesList();
            return animal;
        }
        return null;
        
    }

    public static Bee createBee(int x, int y) throws IOException {
        //don't create a bee if one already exists there
        for (Bee bee : bees) {
            if (bee.getX() == x && bee.getY() == y) {
                return null;
            }
        }
        Bee bee = new Bee();
        
        Hitbox hitbox = new Hitbox(bee, x, y);
        hitboxes.add(hitbox);
        bees.add(bee);
            
        //sprites.add(hitbox.getSprite());
        addSprite(hitbox.getSprite());
        updateSpeciesList();
        return bee;
        
        
    }

    public static void killBee(Bee bee) {
        if (bee.getHitbox() != null) {
            hitboxes.remove(bee.getHitbox());
            bees.remove(bee);
            beeSprites.remove(bee.getHitbox().getSprite());
            bee.getHitbox().getOrganism().setHitbox(null); 
        }
    }

    public static Organism createOrganism(Species species, int x, int y) throws IOException {
        switch (species) {
            case FERN:
                return createPlant(species, x, y);
            case GRASS:
                return createPlant(species, x, y);
            case APPLE_TREE:
                return createPlant(species, x, y);
            case MOSS:
                return createPlant(species, x, y);
            case FLOWER:
                return createPlant(species, x, y);
            case OAK_TREE:
                return createPlant(species, x, y);
            case PINE_TREE:
                return createPlant(species, x, y);
            case CACTUS:
                return createPlant(species, x, y);
            case BERRY_BUSH:
                return createPlant(species, x, y);
            case BUSH:
                return createPlant(species, x, y);
            case MAINTREE:
                return createPlant(species, x, y);
            case DEER:
                return createAnimal(species, x, y);
            case ANT:
                return createAnimal(species, x, y);
            case FROG:
                return createAnimal(species, x, y);
            case SNAKE:
                return createAnimal(species, x, y);
            case WORM:
                return createAnimal(species, x, y);
            case SPIDER:
                return createAnimal(species, x, y);
            case BEAR:
                return createAnimal(species, x, y);
            case BOBCAT:
                return createAnimal(species, x, y);
            case MOUSE:
                return createAnimal(species, x, y);
            case GRASSHOPPER:
                return createAnimal(species, x, y);
            case SCORPION:
                return createAnimal(species, x, y);
            case BEETLE:
                return createAnimal(species, x, y);
            case DRAGONFRUIT_CACTUS:
                return createPlant(species, x, y);
            case BEE:
                return createBee(x, y);
            default:
                return null;
        }
    }

    /**
     * Creates a plant and automatically adds it to the grid
     * 
     */
    public static Plant createPlant(Species species, int x, int y) throws IOException {
        
        Plant plant;
        if (species == Species.MAINTREE) {
            plant = new MainTree();
        } else {
            plant = new Plant(species);
        }
        
        if (Grid.canFit(plant, x, y)){
            Hitbox hitbox = new Hitbox(plant, x, y);
            addOrganism(hitbox);
            if (species != Species.MAINTREE){
                updateSpeciesList();
            }
            
            return plant;
        } 
        return null;
        
    }

    public static boolean canFit(Organism organism, int x, int y) {
        int width = organism.getWidth();
        int height = organism.getHeight();

        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                if (i >= grid.length || j >= grid[0].length || grid[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean canFit(int x, int y, int width, int height) {
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                if (i >= grid.length || j >= grid[0].length || grid[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    // public static void printGrid() {
    //     for (int i = 0; i < grid.length; i++) {
    //         for (int j = 0; j < grid[i].length; j++) {
    //             if (grid[i][j] == null) {
    //                 System.out.print("_ ");
    //             } else {
    //                 System.out.print(grid[i][j] + " ");
    //             }
                
    //         }
    //         System.out.println();
    //     }
    // }

    public static void addSprite(Sprite sprite) {
        if (sprite.getImagePath().equals(Sprite.BEE_SPRITE)) {
            if (!beeSprites.contains(sprite)) {
                beeSprites.add(sprite);
            }
        } else {
            if (!sprites.contains(sprite)) {
                sprites.add(sprite);
            }
        }
    }

    public static void updateSpeciesList() {
        // This method can be called to refresh the species list if needed
        speciesList.clear();
        for (Hitbox hitbox : hitboxes) {
            Organism organism = hitbox.getOrganism();
            if (organism != null && !speciesList.contains(organism.getSpecies())) {
                speciesList.add(organism.getSpecies());
            }
        }
    }

    // public static Direction[] getDirectionsList(){
    //     Direction[] directions = new Direction[8];
    //     int index = 0;
    //     for (int i = 0; i < Direction.values().length; i++){
    //         directions[index] = Direction.values()[i];
    //         index++;
    //     }
    //     return directions;
    // }
    

    
}
