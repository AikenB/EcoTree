package terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class Map extends JPanel {
    // NOTE TO SELF: add constructor?
    // boolean used later on for tracking if the mouse is pressed
    private static boolean pressed = false;
    // numbers to track screen translation
    private static int deltaX = 0;
    private static int deltaY = 0;

    // boolean array to check if each arrow key is pressed
    // indexes of boolean array
    // 0 corresponds to left
    // 1 corresponds to right
    // 2 corresponds to up
    // 3 corresponds to down
    private static boolean[] keysPressed = {false, false, false, false};

    // each keyMovement corresponds to a translation
    // ie index 0 being pressed will signify keycode 37 (left arrow) is being pressed, translating x to the left
    private static final int[] keyMovements = {10,-10,10,-10};

    //NOTE: 1920 must be divisible by width, 1080 must be divisible by height
    // CONFIG WIDTH AND HEIGHT HERE
    private static int width = 96;
    private static int height = 54;

    public static int getMapWidth()
    {
        return width;
    }
    public static int getMapHeight()
    {
        return height;
    }
    @Override
    protected void paintComponent(Graphics g)
    {
         // initialize terrain generation before drawing map
        terrain.Generation.initialize();
        // clear the screen
        super.paintComponent(g);
        //use graphics 2d ( so we can translate things during screen scrolling)
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(deltaX,deltaY);

        // rectangle outline color
        g2d.setColor(Color.BLUE);

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
                // setting colors using generation
                g2d.setColor(terrain.Generation.terrainValues()[j][i]);
                g2d.fillRect(0+(squareWidth*i), 0+(squareHeight*j), squareWidth, squareHeight); 

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
                pressed = true;
            }
        });
        // detect when mouse is released
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {       
                pressed = false;
            }
        });

        // detect arrow key presses for screen translation
        // 37 = left arrow
        // 38 = up arrow
        // 39 = right arrow
        // 40 = down arrow

        frame.addKeyListener(new KeyListener() 
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                // update keysPressed array to reflect which keys are currently pressed
                if (e.getKeyCode() == 37)
                {
                    keysPressed[0] = true;
                }
                if (e.getKeyCode() == 39)
                {
                    keysPressed[1] = true;
                }
                if (e.getKeyCode() == 38)
                {
                    keysPressed[2] = true;
                }
                if (e.getKeyCode() == 40)
                {
                    keysPressed[3] = true;
                }

                // movement: checks if any keys were pressed by referencing the keysPressed array
                ArrayList<Integer> movements = new ArrayList<Integer>();
                // loops through each key to see if it is pressed
                for (int i = 0; i < 4; i ++)
                {
                    if (keysPressed[i])
                    {
                        movements.add(i);
                    }
                }
                
                // if any movement keys are pressed
                if (movements.size() > 0)
                {
                    // loop through pressed keys
                    for (int i = 0; i < movements.size(); i++)
                    {
                        int key = movements.get(i);
                        if (key == 0 || key == 1)
                        {
                            deltaX += keyMovements[key];
                        }
                        if (key == 2 || key == 3)
                        {
                            deltaY += keyMovements[key];
                        }
                    }
                    // repaint canvas with translations

                    frame.repaint();
                }

            }
            public void keyReleased(KeyEvent e)
            {
                // update keysPressed arrays to reflect which keys are no longer pressed
                if (e.getKeyCode() == 37)
                {
                    keysPressed[0] = false;
                }
                if (e.getKeyCode() == 39)
                {
                    keysPressed[1] = false;
                }
                if (e.getKeyCode() == 38)
                {
                    keysPressed[2] = false;
                }
                if (e.getKeyCode() == 40)
                {
                    keysPressed[3] = false;
                }
            }
            // all 3 methods must be implemented even if they are not used, hence why keyTyped is here
            public void keyTyped(KeyEvent e)
            {
            }
        }
    );

        frame.setVisible(true);
    }

}
//BUG: holding a key and then switching directions halts movement
//how to reproduce: 
// 1. hold any arrow key
// 2. hold a second arrow key 
// 3. release the second arrow key
