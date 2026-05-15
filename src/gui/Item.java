package gui;

import gameobjects.Organism.Species;
import java.awt.Image;
import java.io.File;
import javax.swing.*;
public class Item {
    // public double price;
    // public String name;
    public int number; // represents GUI location

    public JPanel panel;
    public JLabel name;
    public JLabel imageLabel;
    public JLabel price;
    public JButton buyButton;
    public int x;
    public int y;
    public double priceNumber;
    private Species species;

    public int quantity;
    public boolean isShop; // tells if we are in shop or inventory state

    public Item (String name, int number, double price) {
        panel = new JPanel();
        panel.setLayout(null);
        this.name = new JLabel(name);
        this.name.setHorizontalAlignment(JLabel.CENTER);
        this.price = new JLabel("Price: " + String.valueOf(price));
        this.priceNumber = price;
        this.buyButton = new JButton("buy");
        panel.add(this.name);
        
        this.imageLabel = new JLabel();
        try {
            String imagePath = null;
            switch (name) {
                case "Grass":
                    imagePath = "src/images/grass_1.png";
                    break;
                case "Apple Tree":
                    imagePath = "src/images/apple_tree_1.png";
                    break;
                case "Fern":
                    imagePath = "src/images/fern_1.png";
                    break;
                case "Ant":
                    imagePath = "src/images/ant.png";
                    break;
                case "Spider":
                    imagePath = "src/images/spider.png";
                    break;
                case "Frog":
                    imagePath = "src/images/frog.png";
                    break;
                case "Snake":
                    imagePath = "src/images/snake.png";
                    break;
                case "Worm":
                    imagePath = "src/images/worm.png";
                    break;
                case "Berry Bush":
                    imagePath = "src/images/berry_bush_1.png";
                    break;
                case "Moss":
                    imagePath = "src/images/moss_1.png";
                    break;
                case "Mouse":
                    imagePath = "src/images/mouse.png";
                    break;
            }
            
            if (imagePath != null && new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                this.imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            this.imageLabel.setText("No image");
        }
        panel.add(this.imageLabel);
        
        panel.add(this.price);
        panel.add(this.buyButton);
        this.name.setBounds(50, 0, 150, 50);
        this.imageLabel.setBounds(90, 60, 70, 70);
        this.price.setBounds(50, 150, 150, 50);
        this.buyButton.setBounds(50, 200, 150, 50);
        this.y = 200 +  280 * (number / 2); // does the integer division to determine which row. 
        this.x = (number % 2) * 250;
        isShop = true;
        
        switch (name) {
            case "Grass":
                this.species = Species.GRASS;
                break;
            case "Apple Tree":
                this.species = Species.APPLE_TREE;
                break;
            case "Fern":
                this.species = Species.FERN;
                break;
            case "Flower":
                this.species = Species.FLOWER;
                break;
            case "Oak Tree":
                this.species = Species.OAK_TREE;
                break;
            case "Pine Tree":
                this.species = Species.PINE_TREE;
                break;
            case "Cactus":
                this.species = Species.CACTUS;
                break;
            case "Berry Bush":
                this.species = Species.BERRY_BUSH;
                break;
            case "Moss":
                this.species = Species.MOSS;
                break;
            case "Bush":
                this.species = Species.BUSH;
                break;
            case "Rabbit":
                this.species = Species.RABBIT;
                break;
            case "Deer":
                this.species = Species.DEER;
                break;
            case "Wolf":
                this.species = Species.WOLF;
                break;
            case "Bear":
                this.species = Species.BEAR;
                break;
            case "Cow":
                this.species = Species.COW;
                break;
            case "Snake":
                this.species = Species.SNAKE;
                break;
            case "Frog":
                this.species = Species.FROG;
                break;
            case "Bobcat":
                this.species = Species.BOBCAT;
                break;
            case "Ant":
                this.species = Species.ANT;
                break;
            case "Worm":
                this.species = Species.WORM;
                break;
            case "Mouse":
                this.species = Species.MOUSE;
                break;
            case "Spider":
                this.species = Species.SPIDER;
                break;
            case "Grasshopper":
                this.species = Species.GRASSHOPPER;
                break;
            default:
                this.species = null;
                break;
        }
       


    }

    public void setupForShop () {
        isShop = true;
        //this.name.setVisible(true);
        this.price.setText("Price: " + this.priceNumber);
        this.buyButton.setText("buy");
    }

    public void setupForInventory () {
        isShop = false;
        //this.name.setVisible(false);
        this.price.setText("Quantity: " + this.quantity);
        this.buyButton.setText("select");
    }

    public Species getSpecies() {
        return species;
    }
}
