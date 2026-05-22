package gui;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import terrain.Map;
import utilities.Game;
import utilities.Sprite;

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
    // 4 corresponds to the NUMBER 1
    // 5 corresponds to the NUMBER 2
    // 6 corresponds to the letter O (for outbreak)
    private static boolean[] keysPressed = {false, false, false, false, false, false, false};

    // each keyMovement corresponds to a translation
    // ie index 0 being pressed will signify keycode 37 (left arrow) is being pressed, translating x to the left
    private static final int[] keyMovements = {20,-20,20,-20};

    private ExecutorService executor;

    ArrayList<Integer> movements = new ArrayList<Integer>();

    static Sprite placementGhost = null;

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

                    // Calculate bounds based on actual map size and viewport size
                    int mapPixelWidth = Map.getMapWidth() * 20;
                    int mapPixelHeight = Map.getMapHeight() * 20;
                    int viewportWidth = 1080;
                    int viewportHeight = 540;
                    
                    //int deltaXMin = -(mapPixelWidth - viewportWidth);
                    //int deltaYMin = -(mapPixelHeight - viewportHeight);
                    int deltaYMin = -(2560-1140);    
                    int deltaXMin = -(2560-1420);

                    if (keysPressed[0] == true)
                    {
                        if (Map.getDeltaX()+keyMovements[0] <= 0)
                        {
                            Map.setDeltaX(Map.getDeltaX()+keyMovements[0]);
                            System.out.println("deltaX: " + Map.getDeltaX());
                        }
                    }
                    if (keysPressed[1] == true)
                    {
                        if (Map.getDeltaX()+keyMovements[1] >= deltaXMin)
                        {
                            Map.setDeltaX(Map.getDeltaX()+keyMovements[1]);
                            System.out.println("deltaX: " + Map.getDeltaX());
                        }
                    }
                    if (keysPressed[2] == true)
                    {
                        if (Map.getDeltaY()+keyMovements[2] <= 0)
                        {
                            Map.setDeltaY(Map.getDeltaY()+keyMovements[2]);
                            System.out.println("deltaY: " + Map.getDeltaY());
                        }
                    }
                    if (keysPressed[3] == true)
                    {
                        if (Map.getDeltaY()+keyMovements[3] >= deltaYMin)
                        {
                            Map.setDeltaY(Map.getDeltaY()+keyMovements[3]);
                            System.out.println("deltaY: " + Map.getDeltaY());
                        }
                    }


                    // zoom in and out with keys "1" and "2"
                    if (keysPressed[4] == true)
                    {
                        // max scale is 2
                        if (Map.getScale() < 2)
                        {
                            Map.setScale(Map.getScale() + 0.05);
                        }
                    }

                    // minimum scale is 1
                    if (keysPressed[5] == true)
                    {
                        if (Map.getScale()-0.05 >= 1)
                        {
                            Map.setScale(Map.getScale() - 0.05);
                        }
                    }

                    // create outbreak with O key
                    if (keysPressed[6] == true)
                    {
                        Game.createOutbreak();
                        keysPressed[6] = false;
                    }
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
    
    public static void initializeControls(JFrame frame, JPanel panel)
    {
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                frame.requestFocusInWindow();
                //System.out.println("requesting");
                panel.requestFocusInWindow();
                // focus window on left click
                pressed = true;
                // x = e.getX();
                // y = e.getY();
                //placementGhost = new Sprite(e.getX(), e.getY(), );
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

        // detect mouse all the time, to place sprite
        frame.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (true) {
                    System.out.println("bob");
                }
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

                // update keysPressed for keys "1" and "2"

                if (e.getKeyCode() == 49)
                {
                    keysPressed[4] = true;
                }
                if (e.getKeyCode() == 50)
                {
                    keysPressed[5] = true;
                }

                // create outbreak with O key
                if (e.getKeyCode() == 79)
                {
                    keysPressed[6] = true;
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
                if (e.getKeyCode() == 49)
                {
                    keysPressed[4] = false;
                }
                if (e.getKeyCode() == 50)
                {
                    keysPressed[5] = false;
                }
                if (e.getKeyCode() == 79)
                {
                    keysPressed[6] = false;
                }
            }
            // all 3 methods must be implemented even if they are not used, hence why keyTyped is here
            public void keyTyped(KeyEvent e)
            {
            }
        });
    }
}
