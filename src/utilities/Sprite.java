package utilities;

import gameobjects.Organism;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

    public static int SQUARE_WIDTH = 20;
    private int x;
    private int y;
    private int width;
    private int height;

    private int gridX;
    private int gridY;
    private int gridWidth;
    private int gridHeight;
    private String imagePath;

    //constants for sprite image paths
    public static final String FROG_SPRITE = "EcoTree/src/images/frog.png";
    public static final String SPIDER_SPRITE = "EcoTree/src/images/spider.png";
    public static final String ANT_SPRITE = "EcoTree/src/images/ant.png";
    public static final String FERN_SPRITE_0 = "EcoTree/src/images/fern_0.png";
    public static final String FERN_SPRITE_1 = "EcoTree/src/images/fern_1.png";
    public static final String GRASS_SPRITE_0 = "EcoTree/src/images/grass_0.png";
    public static final String GRASS_SPRITE_1 = "EcoTree/src/images/grass_1.png";
    public static final String APPLE_TREE_SPRITE_0 = "EcoTree/src/images/apple_tree_0.png";
    public static final String APPLE_TREE_SPRITE_1 = "EcoTree/src/images/apple_tree_1.png";
    public static final String WORM_SPRITE = "EcoTree/src/images/worm.png";
    public static final String SNAKE_SPRITE = "EcoTree/src/images/snake.png";
    public static final String MOUSE_SPRITE = "EcoTree/src/images/mouse.png";
    public static final String BERRY_BUSH_SPRITE_0 = "EcoTree/src/images/berry_bush_0.png";
    public static final String BERRY_BUSH_SPRITE_1 = "EcoTree/src/images/berry_bush_1.png";
    public static final String MOSS_SPRITE_0 = "EcoTree/src/images/moss_0.png";
    public static final String MOSS_SPRITE_1 = "EcoTree/src/images/moss_1.png";
    public static final String GRASSHOPPER_SPRITE = "EcoTree/src/images/grasshopper.png";
    public static final String SCORPION_SPRITE = "EcoTree/src/images/scorpion.png";
    public static final String BEETLE_SPRITE = "EcoTree/src/images/beetle.png";
    public static final String BEE_SPRITE = "EcoTree/src/images/bee.png";
    public static final String FLOWER_SPRITE_0 = "EcoTree/src/images/flower_0.png";
    public static final String FLOWER_SPRITE_1 = "EcoTree/src/images/flower_1.png";
    public static final String BOBCAT_SPRITE = "src/images/bobcat.png";
    public static final String BEAR_SPRITE = "src/images/bear.png";
    public static final String DEER_SPRITE = "src/images/deer.png";
    public static final String MAINTREE_SPRITE_1 = "src/images/main_tree_1.png";
    public static final String MAINTREE_SPRITE_2 = "src/images/main_tree_2.png";
    public static final String MAINTREE_SPRITE_3 = "src/images/main_tree_3.png";
    public static final String MAINTREE_SPRITE_4 = "src/images/main_tree_4.png";
    public static final String MAINTREE_SPRITE_5 = "src/images/main_tree_5.png";
    public static final String DRAGONFRUIT_CACTUS_SPRITE_0 = "src/images/dragonfruit_cactus_0.png";
    public static final String DRAGONFRUIT_CACTUS_SPRITE_1 = "src/images/dragonfruit_cactus_1.png";
    public static final String MOONFLOWER_SPRITE_0 = "src/images/moonflower_0.png";
    public static final String MOONFLOWER_SPRITE_1 = "src/images/moonflower_1.png";
    public static final String HUMMINGBIRD_SPRITE = "src/images/hummingbird.png";
    public static final String BALD_EAGLE_SPRITE = "src/images/bald_eagle.png";
    

    private BufferedImage image;
    public Sprite(int x, int y, int width, int height, String imagePath) throws IOException {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;

        this.gridX = x * SQUARE_WIDTH;
        this.gridY = y * SQUARE_WIDTH;
        this.gridWidth = width * SQUARE_WIDTH;
        this.gridHeight = height * SQUARE_WIDTH;

        this.image = ImageIO.read(new File(imagePath));
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String getImagePath() {
        return imagePath;
    }
    public int getGridX() {
        return gridX;
    }
    public int getGridY() {
        return gridY;
    }
    public int getGridWidth() {
        return gridWidth;
    }
    public int getGridHeight() {
        return gridHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
        this.gridX = x * SQUARE_WIDTH;
    }
    public void setY(int y) {
        this.y = y;
        this.gridY = y * SQUARE_WIDTH;
    }
    public void setWidth(int width) {
        this.width = width;
        this.gridWidth = width * SQUARE_WIDTH;
    }
    public void setHeight(int height) {
        this.height = height;
        this.gridHeight = height * SQUARE_WIDTH;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
        this.x = gridX / SQUARE_WIDTH;
    }
    public void setGridY(int gridY) {
        this.gridY = gridY;
        this.y = gridY / SQUARE_WIDTH;
    }
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        this.width = gridWidth / SQUARE_WIDTH;
    }
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        this.height = gridHeight / SQUARE_WIDTH;
    }
    
    /**
     * this does the same thing as updateSprite in Hitbox but can also be used by the Sprite class for code readibility
     */
    public static void updateSprite(Organism o, String newImagePath) throws IOException {
        Hitbox.updateSprite(o, newImagePath);
    }

    
}
