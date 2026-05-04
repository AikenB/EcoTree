package terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel {

    public static boolean pressed = false;
    // boolean used later on for tracking if the mouse is pressed

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        // draw rectangles for terrain
        g.setColor(Color.BLUE);
        // width
        for (int i = 0; i < 96; i ++)
        {
            // height
            for (int j= 0; j < 54; j ++)
            {
                // draw each square in the grid
                g.drawRect(0+(20*i), 0+(20*j), 20, 20);       
            }
        }

    }
    public static void loadMap()
    {
        // open terrain window
        JFrame frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1920,1080);
        Color backgroundColor = new Color(170, 240, 130); // Dark green
        frame.getContentPane().setBackground(backgroundColor);
        // add rectangles
        frame.add(new Map());

        // detect mouse press for screen scrolling

        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {       
                pressed = true;
                System.out.println(pressed);
            }
        });
        // detect when mouse is released
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {       
                pressed = false;
                System.out.println(pressed);
            }
        });

        frame.setVisible(true);
    }

}
