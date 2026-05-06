package gui;
import gameobjects.Animal;
import gameobjects.Organism;
import java.util.concurrent.CopyOnWriteArrayList;
import utilities.Hitbox;

public class Grid {

    public static Hitbox[][] grid = new Hitbox[15][15];
    public static CopyOnWriteArrayList<Hitbox> hitboxes = new CopyOnWriteArrayList<>();

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
        Organism organism = hitbox.getOrganism();
        organism.setHitbox(null);  // Clear the cache
        
        // Stop the organism's background movement if it's an Animal
        if (organism instanceof Animal) {
            ((Animal) organism).stopMovement();
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

    public static void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == null) {
                    System.out.print("_ ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
                
            }
            System.out.println();
        }
    }

    
}
