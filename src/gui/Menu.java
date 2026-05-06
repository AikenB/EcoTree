package gui;

import javax.swing.*;
import java.awt.*;
import terrain.Map;
import java.util.ArrayList;

import gui.Controls;

public class Menu {

    JFrame frame;
    int width = 1920;
    int height = 1080;
    int introButtonWidth = 600;
    int introButtonHeight = 100;
    int instrWidth = 1600;
    int instrHeight = 800;
    ArrayList<JComponent> components = new ArrayList<JComponent>();
    // the idea of the ArrayList is that references to different buttons and whatnot can be accessed...
    //... from the place where the buttons were initialized. This enables more modular programming, ...
    //... rather than putting everything in the setup() method. However, it requires careful documentation...
    //... of what diferent indices represent.
    // current: 0 - start button, 1 - instructions button, 2 - instructions panel, 3 - close instructions button
    ArrayList<Boolean> componentsVisible = new ArrayList<Boolean>();
    // this is a parallel array to manage whether items are hidden or not.

    public Menu () {
        frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(width, height);
        Color backgroundColor = new Color(170, 240, 130); // Dark green
        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
        frame.setLayout(null);
        setButtons();

        // initialize controls/ key listeners
        // focus screen - this is necessary in order for controls to work 
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        Controls.initializeControls(frame);
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
                start.setVisible(false);
                instrButton.setVisible(false);
                Map m = new Map();
                m.setBounds(0,0,1920, 1080);
                frame.add(m);
                System.out.println("something");
                frame.repaint();
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
    }
    public void setupInstructions() {
        componentsVisible.set(2, true);
        componentsVisible.set(3, true);
        refresh();
    }
}
