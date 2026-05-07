package utilities;
import gameobjects.Organism;
import gameobjects.Organism.Species;
import gui.Sprite;
import java.io.IOException;

public class Hitbox{

    private int x, y, width, height;
    private Organism organism;
    private Sprite sprite;
    

    public Hitbox(Organism organism, int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        this.width = organism.getWidth();
        this.height = organism.getHeight();
        this.organism = organism;
        organism.setX(x);
        organism.setY(y);
        organism.setHitbox(this); 
        configureSprite(organism.getSpecies());
        
        
    }

    // Getters and setters
    public int getX() {
         return x;
        }
    public void setX(int x) { 
        this.x = x;
        sprite.setX(x);
    }
    public int getY() {
        return y; 
        }
    public void setY(int y) { 
        this.y = y;
        sprite.setY(y);
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

    public Sprite getSprite() {
        return sprite;
    }

    public String toString() {
        return organism.toString();
    }


    private void configureSprite(Species species) throws IOException {
        String imagePath;

        switch (species) {
            // case ANT:
            //     imagePath = "src/images/ant.png";
            //     break;
            // case SPIDER:
            //     imagePath = "src/images/spider.png";
            //     break;
            case FROG:
                imagePath = "src/images/frog.jpg";
                sprite = new Sprite(x, y, width, height, imagePath);
                break;
            case SPIDER:
                imagePath = "src/images/spider.jpg";
                sprite = new Sprite(x, y, width, height, imagePath);
                break;
            case ANT:
                imagePath = "src/images/ant.jpg";
                sprite = new Sprite(x, y, width, height, imagePath);
                break;
            // Add more cases for other species
        }

        // this.sprite = new Sprite(x, y, width, height, imagePath);

    }
}
