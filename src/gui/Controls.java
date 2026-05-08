package gui;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import terrain.Map;
import java.util.ArrayList;

public class Controls {
    // boolean used later on for tracking if the mouse is pressed
    static boolean pressed;

    // boolean array to check if each arrow key is pressed
    // indexes of boolean array
    // 0 corresponds to left
    // 1 corresponds to right
    // 2 corresponds to up
    // 3 corresponds to down
    private static boolean[] keysPressed = {false, false, false, false};

    // each keyMovement corresponds to a translation
    // ie index 0 being pressed will signify keycode 37 (left arrow) is being pressed, translating x to the left
    private static final int[] keyMovements = {20,-20,20,-20};

    public static void initializeControls(JFrame frame)
    {
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                frame.requestFocusInWindow();
                // focus window on left click
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
                            Map.setDeltaX(Map.getDeltaX()+keyMovements[key]);
                        }
                        if (key == 2 || key == 3)
                        {
                            Map.setDeltaY(Map.getDeltaY()+keyMovements[key]);
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
    }
}
