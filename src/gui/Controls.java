package gui;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import terrain.Map;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controls {
    // boolean used later on for tracking if the mouse is pressed
    static boolean pressed;

    static public int x;
    static public int y;

    static Menu menu;

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

    private ExecutorService executor;

    ArrayList<Integer> movements = new ArrayList<Integer>();

    public void initializeControlsB(JFrame frame)
    {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                            // detect arrow key presses for screen translation
                    // 0 = left arrow
                    // 1 = up arrow
                    // 2 = right arrow
                    // 3 = down arrow
                    if (keysPressed[0] == true)
                    {
                        if (Map.getDeltaX()+keyMovements[0] <= 0)
                        {
                            Map.setDeltaX(Map.getDeltaX()+keyMovements[0]);
                        }
                    }
                    if (keysPressed[1] == true)
                    {
                        if (Map.getDeltaX()+keyMovements[1] >= -620)
                        {
                            Map.setDeltaX(Map.getDeltaX()+keyMovements[1]);
                        }
                    }
                    if (keysPressed[2] == true)
                    {
                        if (Map.getDeltaY()+keyMovements[2] <= 0)
                        {
                            Map.setDeltaY(Map.getDeltaY()+keyMovements[2]);
                        }
                    }
                    if (keysPressed[3] == true)
                    {
                        if (Map.getDeltaY()+keyMovements[3] >= -620)
                        {
                            Map.setDeltaY(Map.getDeltaY()+keyMovements[3]);
                        }
                    }

                    //System.out.println("y: " + Map.getDeltaY());
                    System.out.println("x: " + Map.getDeltaX());
                    frame.repaint();
                    Thread.sleep(25);
                }
                 catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                    
                }
            } 
        });
    }
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
                // x = e.getX();
                // y = e.getY();
                menu.recieveClick(e.getX(), e.getY());
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
                    // repaint canvas with translations
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
