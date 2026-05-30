package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.imageio.ImageIO;

import gameobjects.Organism.Species;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import terrain.Map;
import utilities.Game;
import utilities.Grid;

import java.util.ArrayList;

import gui.Controls;
import gameobjects.*;

public class Menu {

    public JPanel gamePanel; //used for map
    public JPanel panel2;
    public JFrame mainFrame;
    public JPanel introPanel; //used for start screen
    JPanel shopPanel;
    JScrollPane scroll;
    int width = 1920;
    int height = 1080;
    int introButtonWidth = 600;
    int introButtonHeight = 100;
    int instrWidth = 1600;
    int instrHeight = 800;

    
    


    static Map m;
    private static JLabel moneyLabel = new JLabel("Money: " + String.valueOf(Game.money));
    
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
       

        mainFrame = new JFrame("EcoTree");
        mainFrame.setIconImage(new ImageIcon("src/images/main_tree_1.png").getImage());
        introPanel = createBackgroundPanel();
        introPanel.setLayout(null);
        
        mainFrame.add(introPanel);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.setSize(width + 500, height);
        Color backgroundColor = new Color(238, 238, 238); // Dark green
        mainFrame.getContentPane().setBackground(backgroundColor);
        mainFrame.setVisible(true);
        //mainframe.setLayout(null);
        // mainframe.add(frame);
        // mainframe.add(frame2);
        // frame2.setBounds(0,0, 500, 700);
        // frame.setBounds(500, 0, width, height);
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setFocusable(false);
        panel2 = new JPanel(null);
        panel2.setBackground(new Color(139, 69, 19));
        panel2.setOpaque(true);
        mainFrame.add(gamePanel);
        mainFrame.add(panel2);
        panel2.setBounds(0,0, 500, 900);
        
        moneyLabel.setForeground(Color.YELLOW);
        gamePanel.setBounds(500, 0, width, height);
        setButtons();


        // frame2 = new JFrame("Shop");
        // frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // frame2.setSize(500, 700);
        // frame2.setFocusable(false);
        // frame2.setFocusableWindowState(false);
        // frame2.setVisible(true);
        // frame2.setLayout(null);
        
        // initialize controls/ key listeners
        // focus screen - this is necessary in order for controls to work 
        mainFrame.setFocusable(true);
        gamePanel.requestFocusInWindow();
        Controls.initializeControls(mainFrame, gamePanel);

        // initialize movement
        Controls controls = new Controls();
        controls.initializeControlsB(mainFrame);

        Controls.menu = this;


    }
    public void setButtons() {

        // width adjusts based on screen size  
        width = mainFrame.getContentPane().getWidth();

        JButton start = new JButton("Start");
        start.setBounds(width/2 - introButtonWidth/2, 50, introButtonWidth, introButtonHeight);
        start.setBackground(new Color(34, 102, 34));
        start.setForeground(Color.WHITE);
        // start.setOpaque(true);
        // start.setFocusPainted(false);
        components.add(start);
        componentsVisible.add(true);
        introPanel.add(start);
        
        JButton instrButton = new JButton("Instructions");
        instrButton.setBounds(width/2 - introButtonWidth/2, 50*2 + introButtonHeight, introButtonWidth, introButtonHeight);
        instrButton.setBackground(new Color(34, 102, 34));
        instrButton.setForeground(Color.WHITE);
        // instrButton.setOpaque(true);
        // instrButton.setFocusPainted(false);
        components.add(instrButton);
        componentsVisible.add(true);
        introPanel.add(instrButton);

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
        introPanel.add(instr);
        introPanel.setComponentZOrder(instr,0);

        JButton closeInstr = new JButton("X");
        closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);
        //closeInstr.setBounds(0, 150, 50, 50);
        // so I literally can't see the close button if I put it on the traditional top right corner...
        //..., so I will put it top-left for testing and change later.
        //closeInstr.setComponentZOrder(instr, 0);
        components.add(closeInstr);
        componentsVisible.add(false);
        introPanel.add(closeInstr);
        introPanel.setComponentZOrder(closeInstr,0); //note: this is the way to reorder components
        //closeInstr.setBounds(width/2 - instrWidth/2, 150, 50, 50);


        refresh();

        System.out.println(components.size());
        System.out.println(componentsVisible);

        start.addActionListener(e -> {
            if (!componentsVisible.get(2)) {
                componentsVisible.set(0, false);
                componentsVisible.set(1,false);
                mainFrame.remove(introPanel);
                m = new Map(this);
                m.setBounds(0,000,1920, 1080);
                gamePanel.add(m);
                //System.out.println("something");
                gamePanel.repaint();
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
            if (components.get(i) != null) {
                components.get(i).setVisible(componentsVisible.get(i));
            }
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
            //waiting = false;
            m.setStatsScreenVisible(true);  // Show stats screen when placement completes
            int xPos = (x - Map.getDeltaX()) / 20;
            int yPos = (y - Map.getDeltaY()) / 20;
            //System.out.println("(" + xPos + ", " + yPos + ")");
            try {
                Organism o = Grid.createOrganism(waitingItem.getSpecies(), xPos, yPos);
                if (o != null) {
                    waitingItem.quantity--;
                    if (waitingItem.quantity < 1) {
                        waiting = false;
                    }
                    waitingItem.price.setText("Quantity: " + waitingItem.quantity);
                }
                
            } catch (Exception e) {
                //System.out.println("thing");
                
                waitingItem.quantity++;
                waitingItem.price.setText("Quantity: " + waitingItem.quantity);
            }
        }
    }
    public void setupGame() {

        shopPanel = new JPanel();
        scroll = new JScrollPane(shopPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBackground(new Color(139, 69, 19));
        scroll.getViewport().setBackground(new Color(139, 69, 19));
            
        //JScrollBar bar = shopPanel.createVerticalScrollBar();
       // shopPanel.setVerticalScrollBar(bar);
        //bar.setVisible(true);
        shopPanel.setLayout(null);
        shopPanel.setPreferredSize(new Dimension(480, 3000));
        shopPanel.setBackground(new Color(139, 69, 19));
        shopPanel.setOpaque(true);
        components.add(shopPanel);
        componentsVisible.add(true);
        //frame2.add(shopPanel);
        
        panel2.add(scroll,BorderLayout.CENTER);
        panel2.setLayout(null);
        scroll.setBounds(0, 150, 500, 900 - 150);
        
        // Increase scroll sensitivity and make scroll bar bigger
        JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20); // Increase scroll sensitivity
        verticalScrollBar.setBlockIncrement(100); // Increase block scroll sensitivity
        verticalScrollBar.setPreferredSize(new Dimension(20, 0)); // Make scroll bar wider
        // verticalScrollBar.setBackground(new Color(101, 50, 15));
        // verticalScrollBar.setForeground(new Color(80, 40, 10));
        verticalScrollBar.setBorder(null);
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 40, 10);
                this.trackColor = new Color(101, 50, 15);
            }
        });
        JPanel nonscroll = new JPanel(null);
        panel2.add(nonscroll, BorderLayout.CENTER);
        nonscroll.setBounds(0,0,500, 150);
        nonscroll.setVisible(true);
        nonscroll.setBackground(new Color(139, 69, 19));
        nonscroll.setOpaque(true);
        
        // JButton shop = new JButton("shop");
        // nonscroll.add(shop);
        // shop.setFocusable(false);
        // shop.setVisible(true);
        // //nonscroll.setComponentZOrder(shop,0);
        // //frame2.setComponentZOrder(nonscroll,0);
        components.add(null);
        componentsVisible.add(true);
        // //components.get(5).setBounds(50, 50, 100, 50);
        // shop.setBounds(100, 0, 100, 100);
        
        
        JButton inventory = new JButton("Inventory");
        inventory.setFocusable(false);
        components.add(null);
        nonscroll.add(inventory);
        inventory.setBackground(new Color(100, 150, 0));
        inventory.setForeground(Color.WHITE);
        
        //shopPanel.setComponentZOrder(shop,0);
        componentsVisible.add(true);
        inventory.setBounds(200, 50, 100, 50);

        

        JLabel catalogLabel = new JLabel("Shop");
        catalogLabel.setForeground(Color.WHITE);
        // label.setForeground(new Color(100, 150, 0));
        catalogLabel.setFocusable(false);
        components.add(catalogLabel);
        componentsVisible.add(true);
        nonscroll.add(catalogLabel);
        components.get(7).setBounds(0, 100, 500, 50);   

        JButton shop = new JButton("Shop");
        shop.setBackground(new Color(100, 150, 0));
        shop.setForeground(Color.WHITE);
        nonscroll.add(shop);
        shop.setFocusable(false);
        shop.setBounds(50,50,100,50);
        shop.setVisible(true);
        
        //nonscroll.setComponentZOrder(test, 0);
        // test.setLayout(null);
        // nonscroll.setLayout(null);
        // frame2.setLayout(null);
        // mainframe.setLayout(null);
        //frame2.setComponentZOrder(nonscroll, 0);
        //mainframe.setComponentZOrder(frame2, 0);
        
        
        components.add(moneyLabel);
        componentsVisible.add(true);
        nonscroll.add(moneyLabel);
        moneyLabel.setBounds(310, 50, 180, 50);
        moneyLabel.setFocusable(false);

        Item[] items = {
            new Item("Grass", 0, 10.0),
            new Item("Moss", 1, 10.0),
            new Item("Worm", 2, 20.0),
            new Item("Fern", 3, 25.0),
            new Item("Ant", 4, 25.0),
            new Item("Spider", 5, 50.0),
            new Item("Flower", 6, 50.0),
            new Item("Grasshopper", 7, 75.0),
            new Item("Apple Tree", 8, 100.0),
            new Item("Mouse", 9, 100.0),
            new Item("Berry Bush", 10, 125.0),
            new Item("Scorpion", 11, 125.0),
            new Item("Beetle", 12, 150.0),
            new Item("Frog", 13, 150.0),
            new Item("Deer", 14, 175.0),
            new Item("Snake", 15, 200.0),
            new Item("Bee", 16, 200.0),
            new Item("Bobcat", 17, 200.0),
            new Item("Bear", 18, 250.0)
        };
        


        for (int i = 0; i < items.length; i++) {
            final int index = i;
            shopPanel.add(items[i].panel);
            items[i].panel.setBounds(items[i].x, items[i].y, 250, 280);
        

            //for (int i = 0; i < items.length; i++) { // for now, this loop demands i be final for some reason
            // we should fix it later to make it less inconvinient (though a ton of copy-pasting is possible)

             items[i].buyButton.addActionListener(e -> {
                //System.out.println("is shop- " + items[index].isShop);
                if (items[index].isShop) {
                  if (Game.money - (items[index]).priceNumber >= 0) {
                      Game.money -= items[index].priceNumber;
                      items[index].quantity++;
                      String moneyText = String.valueOf((int) Math.floor(Game.money));
                      moneyLabel.setText("Money: " + moneyText);
                  }
                } else if (items[index].quantity > 0) {
                    waiting = true;
                    waitingItem = items[index];
                    m.setStatsScreenVisible(false);  // Hide stats screen when item is selected for placement
                }
             });
        }
         //}
        

        shop.addActionListener(e -> {
            waiting = false;
            catalogLabel.setText("Shop");
            for (int i = 0; i < items.length; i++) {
                items[i].setupForShop();
            }
            gamePanel.requestFocus();
        });

        inventory.addActionListener(e -> {
            waiting = false;
            catalogLabel.setText("Inventory");
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
        Game.change += amount;
        if (!Game.atMaxRate){
            Game.money += amount;
            
        } 
        String moneyText = String.valueOf((int) Math.floor(Game.money));
        moneyLabel.setText("Money: " + moneyText);
        
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            private BufferedImage backgroundImage;
            private BufferedImage backgroundLogo;

            {
                try {
                    backgroundImage = ImageIO.read(new File("src/images/background.png"));
                    backgroundLogo = ImageIO.read(new File("src/images/introLogo.png"));
                } catch (IOException e) {
                    System.err.println("Error loading background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.drawImage(backgroundLogo, getWidth() / 2 - backgroundLogo.getWidth() / 2, 0, this);
                }
            }
        };
    }
}