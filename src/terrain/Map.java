package terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel {
    // boolean used later on for tracking if the mouse is pressed
    public static boolean pressed = false;
    // numbers to track screen translation
    public static int deltaX = 0;
    public static int deltaY = 0;

    @Override
    protected void paintComponent(Graphics g)
    {
        // clear the screen
        super.paintComponent(g);
        //use graphics 2d ( so we can translate things during screen scrolling)
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(deltaX,deltaY);

        // rectangle outline color
        g2d.setColor(Color.BLUE);


        //NOTE: 1920 must be divisible by width, 1080 must be divisible by height
        // CONFIG WIDTH AND HEIGHT HERE
        int width = 96;
        int height = 54;

        int squareWidth = 1920/width;
        int squareHeight = 1080/height;
        //

        // width
        
        for (int i = 0; i < width; i ++)
        {
            // height
            for (int j= 0; j < height; j ++)
            {
                // draw each square in the grid
                g2d.drawRect(0+(squareWidth*i), 0+(squareHeight*j), squareWidth, squareHeight);       
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
        // background 
        frame.getContentPane().setBackground(backgroundColor);

        // add rectangles
        frame.add(new Map());

        // detect mouse press

        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {       
                //TEST
                deltaY += 100;
                frame.repaint();
                
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

        // detect arrow key presses for screen translation
        frame.addKeyListener(new KeyListener() 
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                System.out.println(e);
            }
            public void keyReleased(KeyEvent e)
            {
            }
            public void keyTyped(KeyEvent e)
            {
            }
        }
    );

        frame.setVisible(true);
    }

}
