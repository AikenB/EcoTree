package utilities;
import gameobjects.Organism;
import gameobjects.Organism.Species;
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
                imagePath = Sprite.FROG_SPRITE;
                break;
            case SPIDER:
                imagePath = Sprite.SPIDER_SPRITE;
                break;
            case ANT:
                imagePath = Sprite.ANT_SPRITE;
                break;
            case FERN:
                imagePath = Sprite.FERN_SPRITE_0;
                break;
            case GRASS:
                imagePath = Sprite.GRASS_SPRITE_0;
                break;
            case APPLE_TREE:
                imagePath = Sprite.APPLE_TREE_SPRITE_0;
                break;
            case BERRY_BUSH:
                imagePath = Sprite.BERRY_BUSH_SPRITE_0;
                break;
            case MOSS:
                imagePath = Sprite.MOSS_SPRITE_0;
                break;
            case GRASSHOPPER:
                imagePath = Sprite.GRASSHOPPER_SPRITE;
                break;
            case SCORPION:
                imagePath = Sprite.SCORPION_SPRITE;
                break;
            // case FLOWER:
            //     imagePath = Sprite.APPLE_TREE_SPRITE_0;
            //     break;
            // case OAK_TREE:
            //     imagePath = Sprite.APPLE_TREE_SPRITE_0;
            //     break;
            // case PINE_TREE:
            //     imagePath = Sprite.APPLE_TREE_SPRITE_0;
            //     break;
            // case CACTUS:
            //     imagePath = Sprite.APPLE_TREE_SPRITE_0;
            //     break;
            // case BUSH:
            //     imagePath = Sprite.APPLE_TREE_SPRITE_0;
            //     break;
            case WORM:
                imagePath = Sprite.WORM_SPRITE;
                break;
            case SNAKE:
                imagePath = Sprite.SNAKE_SPRITE;
                break;
            case MOUSE:
                imagePath = Sprite.MOUSE_SPRITE;
                break;
            case BEETLE:
                imagePath = Sprite.BEETLE_SPRITE;
                break;
            case BEE:
                imagePath = Sprite.BEE_SPRITE;
                break;
            case FLOWER:
                imagePath = Sprite.FLOWER_SPRITE_0;
                break;


            case MAINTREE:
                imagePath = Sprite.APPLE_TREE_SPRITE_0;
                break;    
            default:
                imagePath = null;
            // Add more cases for other species
        }
        sprite = new Sprite(x, y, width, height, imagePath);

        // this.sprite = new Sprite(x, y, width, height, imagePath);
        
    }

    /**
     * updates the sprite of the organism
     */
    public static void updateSprite(Organism o, String newImagePath) throws IOException {
    Hitbox hitbox = o.getHitbox();
    if (hitbox != null) {
        Sprite newSprite = new Sprite(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight(), newImagePath);
        
        Grid.sprites.remove(hitbox.sprite);
        hitbox.sprite = newSprite;
        Grid.sprites.add(newSprite);
    }
}     

}
