package gui;

import gameobjects.Organism.Species;
import javax.swing.*;
public class Item {
    // public double price;
    // public String name;
    public int number; // represents GUI location

    public JPanel panel;
    public JLabel name;
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
        this.price = new JLabel("Price: " + String.valueOf(price));
        this.priceNumber = price;
        this.buyButton = new JButton("buy");
        panel.add(this.name);
        panel.add(this.price);
        panel.add(this.buyButton);
        this.name.setBounds(50, 0, 150, 50);
        this.price.setBounds(50, 300, 150, 50);
        this.buyButton.setBounds(50, 350, 150, 50);
        this.y = 200 +  400 * (number / 2); // does the integer division to determine which row. 
        this.x = (number % 2) * 250;
        isShop = true;
        
        if (name.equals("Grass")){
            this.species = Species.GRASS;
        }
        else if (name.equals("Apple Tree")){
            this.species = Species.APPLE_TREE;
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
