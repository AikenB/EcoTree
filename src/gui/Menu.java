// package gui;

// import javax.swing.*;
// import java.awt.*;
// import terrain.Map;
// import java.util.ArrayList;

// import gui.Controls;

// public class Menu {

//     private JFrame frame;
//     int width = 1920;
//     int height = 1080;
//     int introButtonWidth = 600;
//     int introButtonHeight = 100;
//     int instrWidth = 1600;
//     int instrHeight = 800;
//     private Map m;
//     ArrayList<JComponent> components = new ArrayList<JComponent>();
//     // the idea of the ArrayList is that references to different buttons and whatnot can be accessed...
//     //... from the place where the buttons were initialized. This enables more modular programming, ...
//     //... rather than putting everything in the setup() method. However, it requires careful documentation...
//     //... of what diferent indices represent.
//     // current: 0 - start button, 1 - instructions button, 2 - instructions panel, 3 - close instructions button
//     ArrayList<Boolean> componentsVisible = new ArrayList<Boolean>();
//     // this is a parallel array to manage whether items are hidden or not.

//     public Menu () {
//         frame = new JFrame("EcoTree");
//         frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//         frame.setSize(width, height);
//         Color backgroundColor = new Color(170, 240, 130); // Dark green
//         frame.getContentPane().setBackground(backgroundColor);
//         frame.setVisible(true);
//         frame.setLayout(null);
//         setButtons();

//         // initialize controls/ key listeners
//         // focus screen - this is necessary in order for controls to work 
//         frame.setFocusable(true);
//         frame.requestFocusInWindow();
//         Controls.initializeControls(frame);
//     }
//     public void setButtons() {

//         JButton start = new JButton("Start");
//         start.setBounds(width/2 - introButtonWidth/2, 50, introButtonWidth, introButtonHeight);
//         components.add(start);
//         componentsVisible.add(true);
//         frame.add(start);
        
//         JButton instrButton = new JButton("Instructions");
//         instrButton.setBounds(width/2 - introButtonWidth/2, 50*2 + introButtonHeight, introButtonWidth, introButtonHeight);
//         components.add(instrButton);
//         componentsVisible.add(true);
//         frame.add(instrButton);

//         JLabel instr = new JLabel("This is a \n description of instructions\nto this game");
//         instr.setBounds(width/2 - instrWidth/2, 150, instrWidth, instrHeight);
//         instr.setOpaque(true);
//         instr.setBackground(new Color(200,220,225));
//         // instr.setEditable(false);
//         // instr.setOpaque(false);
//         // instr.setFocusable(false);
//         // instr.setLineWrap(true);
//         // instr.setWrapStyleWord(true);
//         //instr.setComponentZOrder(instr, 0);
//         components.add(instr);
//         componentsVisible.add(false);
//         frame.add(instr);
//         frame.getContentPane().setComponentZOrder(instr,0);

//         JButton closeInstr = new JButton("X");
//         closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);
//         //closeInstr.setBounds(0, 150, 50, 50);
//         // so I literally can't see the close button if I put it on the traditional top right corner...
//         //..., so I will put it top-left for testing and change later.
//         //closeInstr.setComponentZOrder(instr, 0);
//         components.add(closeInstr);
//         componentsVisible.add(false);
//         frame.add(closeInstr);
//         frame.getContentPane().setComponentZOrder(closeInstr,0); //note: this is the way to reorder components
//         //closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);


//         refresh();

//         System.out.println(components.size());
//         System.out.println(componentsVisible);

//         start.addActionListener(e -> {
//             if (!componentsVisible.get(2)) {
//                 start.setVisible(false);
//                 instrButton.setVisible(false);
//                 m = new Map();
//                 m.setBounds(0,0,1920, 1080);
//                 frame.add(m);
//                 System.out.println("something");
//                 frame.repaint();
//                 //Map.loadMap();
//             }

//         });

//         instrButton.addActionListener(e -> {
//             setupInstructions();
//             System.out.println(componentsVisible);
//         });

//         closeInstr.addActionListener(e -> {
//             componentsVisible.set(2,false);
//             componentsVisible.set(3, false);
//             refresh();
//         });
//     }

//     public void refresh () {
//         for (int i = 0; i < components.size(); i++) {
//             components.get(i).setVisible(componentsVisible.get(i));
//             //frame.add(components.get()
//         }
//     }
//     public void setupInstructions() {
//         componentsVisible.set(2, true);
//         componentsVisible.set(3, true);
//         refresh();
//     }

//     public JFrame getFrame() {
//         return frame;
//     }

//     public Map getMap() {
//         return m;
//     }
// }

package gui;

import javax.swing.*;

import gameobjects.Organism.Species;

import java.awt.*;
import terrain.Map;
import utilities.Grid;

import java.util.ArrayList;

import gui.Controls;
import gameobjects.*;

public class Menu {

    public JFrame frame;
    public JFrame frame2;
    int width = 1920;
    int height = 1080;
    int introButtonWidth = 600;
    int introButtonHeight = 100;
    int instrWidth = 1600;
    int instrHeight = 800;
    public static double money = 0;
    static Map m;
    private static JLabel moneyLabel = new JLabel("Money: " + String.valueOf(money));
    public ArrayList<JComponent> components = new ArrayList<JComponent>();
    // the idea of the ArrayList is that references to different buttons and whatnot can be accessed...
    //... from the place where the buttons were initialized. This enables more modular programming, ...
    //... rather than putting everything in the setup() method. However, it requires careful documentation...
    //... of what diferent indices represent.
    // current: 0 - start button, 1 - instructions button, 2 - instructions panel,
    //  3 - close instructions button, 4 - panel for shop & inventory, 5 - shop button,
    //  6 - inventory button, 7 - label telling whether shop or inventory is open, 8 - money label
    public ArrayList<Boolean> componentsVisible = new ArrayList<Boolean>();
    // this is a parallel array to manage whether items are hidden or not.

    boolean waiting;
    Item waitingItem; // the idea here is that this checks if we are waiting to recieve a click to place something.

    public Menu () {
        frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(width, height);
        Color backgroundColor = new Color(170, 240, 130); // Dark green
        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
        frame.setLayout(null);
        setButtons();

        frame2 = new JFrame("Shop");
        frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame2.setSize(500, 700);
        frame2.setVisible(true);
        frame2.setLayout(null);

        // initialize controls/ key listeners
        // focus screen - this is necessary in order for controls to work 
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        Controls.initializeControls(frame);
        Controls.menu = this;


    }
    public void setButtons() {

        JButton start = new JButton("Start");
        start.setBounds(width/2 - introButtonWidth/2, 50, introButtonWidth, introButtonHeight);
        components.add(start);
        componentsVisible.add(true);
        frame.add(start);
        
        JButton instrButton = new JButton("Instructions");
        instrButton.setBounds(width/2 - introButtonWidth/2, 50*2 + introButtonHeight, introButtonWidth, introButtonHeight);
        components.add(instrButton);
        componentsVisible.add(true);
        frame.add(instrButton);

        JLabel instr = new JLabel("This is a \n description of instructions\nto this game");
        instr.setBounds(width/2 - instrWidth/2, 150, instrWidth, instrHeight);
        instr.setOpaque(true);
        instr.setBackground(new Color(200,220,225));
        // instr.setEditable(false);
        // instr.setOpaque(false);
        // instr.setFocusable(false);
        // instr.setLineWrap(true);
        // instr.setWrapStyleWord(true);
        //instr.setComponentZOrder(instr, 0);
        components.add(instr);
        componentsVisible.add(false);
        frame.add(instr);
        frame.getContentPane().setComponentZOrder(instr,0);

        JButton closeInstr = new JButton("X");
        closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);
        //closeInstr.setBounds(0, 150, 50, 50);
        // so I literally can't see the close button if I put it on the traditional top right corner...
        //..., so I will put it top-left for testing and change later.
        //closeInstr.setComponentZOrder(instr, 0);
        components.add(closeInstr);
        componentsVisible.add(false);
        frame.add(closeInstr);
        frame.getContentPane().setComponentZOrder(closeInstr,0); //note: this is the way to reorder components
        //closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);


        refresh();

        System.out.println(components.size());
        System.out.println(componentsVisible);

        start.addActionListener(e -> {
            if (!componentsVisible.get(2)) {
                componentsVisible.set(0, false);
                componentsVisible.set(1,false);
                m = new Map(this);
                m.setBounds(0,0,1920, 1080);
                frame.add(m);
                //System.out.println("something");
                frame.repaint();
                refresh();
                setupGame();
                //Map.loadMap();
            }

        });

        instrButton.addActionListener(e -> {
            setupInstructions();
            System.out.println(componentsVisible);
        });

        closeInstr.addActionListener(e -> {
            componentsVisible.set(2,false);
            componentsVisible.set(3, false);
            refresh();
        });
    }

    public void refresh () {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).setVisible(componentsVisible.get(i));
            //frame.add(components.get()
        }
        //components.get(8).setText("Money: " + String.getValueOf);
    }
    public void setupInstructions() {
        componentsVisible.set(2, true);
        componentsVisible.set(3, true);
        refresh();
    }
    public void recieveClick (int x, int y) {
        if (waiting) {
            waiting = false;
            int xPos = Map.getDeltaX() + x;
            int yPos = Map.getDeltaY()+ y;
            System.out.println("(" + x + ", " + y + ")");
            try {
                Grid.createAnimal(Species.GRASS, xPos, yPos);
            } catch (Exception e) {
                System.out.println("thing");
            }
        }
    }
    public void setupGame() {

        JPanel shopPanel = new JPanel();
        shopPanel.setLayout(null);
        components.add(shopPanel);
        componentsVisible.add(true);
        components.get(4).setOpaque(false);
        frame2.add(shopPanel);
        shopPanel.setBounds(0, 0, 500, 700);
        frame2.getContentPane().setComponentZOrder(shopPanel, 0);
        
        JButton shop = new JButton("shop");
        shopPanel.add(shop);
        shopPanel.setComponentZOrder(shop,0);
        components.add(shop);
        componentsVisible.add(true);
        components.get(5).setBounds(50, 50, 100, 50);
        
        JButton inventory = new JButton("inventory");
        components.add(inventory);
        shopPanel.add(inventory);
        shopPanel.setComponentZOrder(shop,0);
        componentsVisible.add(true);
        components.get(6).setBounds(200, 50, 100, 50);

        JLabel label = new JLabel("Shop");
        components.add(label);
        componentsVisible.add(true);
        shopPanel.add(label);
        components.get(7).setBounds(0, 100, 500, 50);   
        
        
        components.add(moneyLabel);
        componentsVisible.add(true);
        shopPanel.add(moneyLabel);
        components.get(8).setBounds(250, 0, 250, 50);

        Item[] items = {
            new Item("Grass", 0, 50.0),
            new Item("Apple Tree", 1, 100.0)};

        for (int i = 0; i < items.length; i++) {
            frame2.add(items[i].panel);
            items[i].panel.setBounds(items[i].x, items[i].y, 250, 400);
        }

        //for (int i = 0; i < items.length; i++) { // for now, this loop demands i be final for some reason
        // we should fix it later to make it less inconvinient (though a ton of copy-pasting is possible)
             items[0].buyButton.addActionListener(e -> {
                if (items[0].isShop) {
                  if (money - (items[0]).priceNumber >= 0) {
                      money -= items[0].priceNumber;
                      items[0].quantity++;
                      moneyLabel.setText(String.valueOf(money));
                  }
                } else if (items[0].quantity > 0) {
                    items[0].quantity--;
                    items[0].price.setText("Quantity: " + items[0].quantity);
                    waiting = true;
                    waitingItem = items[0];
                    // System.out.println("x: " + Controls.x);
                    // System.out.println("Y: " + Controls.y);
                }
             });
             items[1].buyButton.addActionListener(e -> {
                if(items[1].isShop) {
                  if (money - (items[1]).priceNumber >= 0) {
                      money -= items[1].priceNumber;
                      items[1].quantity++;
                      moneyLabel.setText(String.valueOf(money));
                  }
                } else if (items[1].quantity > 0) {
                    items[1].quantity--;
                    items[1].price.setText("Quantity: " + items[1].quantity);
                    waiting = true;
                    waitingItem = items[0];
                }
             });
         //}
        

        shop.addActionListener(e -> {
            label.setText("Shop");
            for (int i = 0; i < items.length; i++) {
                items[i].setupForShop();
            }
        });

        inventory.addActionListener(e -> {
            label.setText("Inventory");
            for (int i = 0; i < items.length; i++) {
                items[i].setupForInventory();
            }
        });



        refresh();

    }

    public static Map getMap() {
        return m;
    }

    public synchronized static void updateMoney(double amount) {
        money += amount;
        moneyLabel.setText(String.valueOf(Math.floor(money)));
    }
}
