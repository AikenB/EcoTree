package gui;
import java.util.ArrayList;
import utilities.Hitbox;

public class Grid {

    public static Hitbox[][] grid = new Hitbox[15][15];
    public static ArrayList<Hitbox> hitboxes = new ArrayList<>();

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
        hitboxes.add(hitbox);
        int x = hitbox.getX();
        int y = hitbox.getY();
        int width = hitbox.getWidth();
        int height = hitbox.getHeight();

        

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                grid[i][j] = hitbox;
            }
        }
    }

    public static void removeOrganism(Hitbox hitbox) {
        hitboxes.remove(hitbox);
        hitbox.getOrganism().setCachedHitbox(null);  // Clear the cache
        int x = hitbox.getX();
        int y = hitbox.getY();
        int width = hitbox.getWidth();
        int height = hitbox.getHeight();

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                grid[i][j] = null;
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
