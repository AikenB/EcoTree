package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

    private final int SQUARE_WIDTH = 20;
    private int x;
    private int y;
    private int width;
    private int height;

    private int gridX;
    private int gridY;
    private int gridWidth;
    private int gridHeight;
    private String imagePath;

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
}
