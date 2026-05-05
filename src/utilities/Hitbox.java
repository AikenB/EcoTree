package utilities;
import gameobjects.Organism;
import gui.Grid;

public class Hitbox{

    private int x, y, width, height;
    private Organism organism;
    

    public Hitbox(Organism organism, int x, int y) {
        this.x = x;
        this.y = y;
        this.width = organism.getWidth();
        this.height = organism.getHeight();
        this.organism = organism;
        organism.setX(x);
        organism.setY(y);
        organism.setCachedHitbox(this);  // Cache the hitbox reference
        Grid.hitboxes.add(this);

        
    }

    // Getters and setters
    public int getX() {
         return x;
        }
    public void setX(int x) { 
        this.x = x;
    }
    public int getY() {
        return y; 
        }
    public void setY(int y) { 
        this.y = y;
    }
    public int getWidth() {
         return width;
        }
    // public void setWidth(int width) {
    //      this.width = width;
    //     }
    public int getHeight() {
         return height;
        }
    // public void setHeight(int height) { 
    //     this.height = height;
    // }

    public Organism getOrganism() {
        if (organism != null) {
            return organism;
        } else {
            return null;
        }
        
    }

    public String toString() {
        return organism.toString();
    }

}
