package menu;

import javax.swing.*;
import java.awt.*;
import terrain.Map;

public class Menu {
    JFrame frame;
    public Menu () {
        frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1920,1080);
        Color backgroundColor = new Color(170, 240, 130); // Dark green
        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
        frame.setLayout(null);
        setButtons();
    }
    public void setButtons() {

        JButton start = new JButton("Start");
        start.setBounds(660, 50, 600, 100);
        frame.add(start);
        
        JButton instr = new JButton("Instructions");
        instr.setBounds(660, 200, 600, 100);
        frame.add(instr);

        start.addActionListener(e -> {
            start.setVisible(false);
            instr.setVisible(false);
            Map m = new Map();
            m.setBounds(0,0,1920, 1080);
            frame.add(m);
            System.out.println("something");
            frame.repaint();

        });
    }
}
