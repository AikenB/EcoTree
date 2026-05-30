package gui;

import gameobjects.Organism;
import gameobjects.Organism.Species;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
public class Item {
    // public double price;
    // public String name;
    public int number; // represents GUI location

    public JPanel panel;
    public JLabel name;
    public JLabel preyList;
    public JLabel predatorList;
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
        panel.setBackground(new Color(139, 69, 19));
        panel.setOpaque(true);
        this.name = new JLabel(name);
        
        this.name.setForeground(Color.BLACK);
        this.name.setHorizontalAlignment(JLabel.CENTER);
        this.price = new JLabel("Price: " + String.valueOf(price));
        this.price.setForeground(Color.BLACK);
        this.priceNumber = price;
        this.buyButton = new JButton("buy");
        this.buyButton.setFocusable(false);
        buyButton.setBackground(new Color(100, 150, 0));
        buyButton.setForeground(Color.WHITE);
        buyButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        buyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buyButton.setBorder(new LineBorder(Color.YELLOW, 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                buyButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }
        });
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
                case "Bear":
                    imagePath = "src/images/bear.png";
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
                case "Grasshopper":
                    imagePath = "src/images/grasshopper.png";
                    break;
                case "Scorpion":
                    imagePath = "src/images/scorpion.png";
                    break;
                case "Beetle":
                    imagePath = "src/images/beetle.png";
                    break;
                case "Bee":
                    imagePath = "src/images/bee.png";
                    break;
                case "Flower":
                    imagePath = "src/images/flower_1.png";
                    break;
                case "Bobcat":
                    imagePath = "src/images/bobcatshopicon.png";
                    break;
                case "Deer":
                    imagePath = "src/images/deer.png";
                    break;
            }
            
            if (imagePath != null && new File(imagePath).exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                int width = 70;
                // if (species == Species.BOBCAT){
                //     width = 100;
                // }
                Image scaledImage = icon.getImage().getScaledInstance(width, 70, Image.SCALE_SMOOTH);
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
        
        this.y = 280 * (number / 2); // does the integer division to determine which row. 
        // this.y = 200 +  280 * (number / 2); 
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
            case "Deer":
                this.species = Species.DEER;
                break;
            case "Bear":
                this.species = Species.BEAR;
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
            case "Scorpion":
                this.species = Species.SCORPION;
                break;
            case "Beetle":
                this.species = Species.BEETLE;
                break;
            case "Bee":
                this.species = Species.BEE;
                break;
            default:
                this.species = null;
                break;
        }
        preyList = new JLabel("<html>Prey: " + formatList(Organism.getPreyList(species)));
        predatorList = new JLabel("<html>Predators: " + formatList(Organism.getPredatorList(species)));
        this.preyList.setBounds(0, 50, 250, 20);
        this.predatorList.setBounds(0, 30, 250, 30);
        this.preyList.setFont(this.preyList.getFont().deriveFont(7f));
        this.predatorList.setFont(this.predatorList.getFont().deriveFont(7f));
        this.preyList.setHorizontalAlignment(JLabel.LEFT);
        this.predatorList.setHorizontalAlignment(JLabel.LEFT);
        preyList.setForeground(Color.BLACK);
        predatorList.setForeground(Color.BLACK);
        panel.add(this.preyList);
        panel.add(this.predatorList);


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

    private String formatList(ArrayList<Species> list) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        String result = "";
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            Species species = list.get(i);
            if (count >= 5){
                result += "<br>";
                count = 0;
            }
            if (i != list.size() -1){
                result += species.toString() + ", ";
            } else {
                result += species.toString();
            }
            count++;
        }
        return result + "<html>";
    }

    public void setListsVisible(boolean visible) {
        this.preyList.setVisible(visible);
        this.predatorList.setVisible(visible);
    }
}
