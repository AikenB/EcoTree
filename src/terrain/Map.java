package terrain;

import terrain.Generation;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import gui.Sprite;

import gui.Grid;

public class Map extends JPanel {

    // numbers to track screen translation
    private static int deltaX = 0;
    private static int deltaY = 0;
    
    public static int getDeltaX()
    {
        return deltaX;
    }
    public static void setDeltaX(int i)
    {
        deltaX = i;
    }
    public static int getDeltaY()
    {
        return deltaY;
    }
    public static void setDeltaY(int i)
    {
        deltaY = i;
    }

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
        // clear the screen
        super.paintComponent(g);
        //use graphics 2d ( so we can translate things during screen scrolling)
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(deltaX,deltaY);

        // rectangle outline color
        g2d.setColor(Color.BLACK);

        // IF width = 96, squareWidth = 20
        // IF height = 54, squareHeight = 20
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

        
        
            if (Grid.sprites.size() > 0){

                for (int i = 0; i < Grid.sprites.size(); i++){
                    Sprite s = Grid.sprites.get(i);
                    if (s != null) {
                        g2d.drawImage(s.getImage(), s.getGridX() + deltaX, s.getGridY() + deltaY, s.getGridWidth(), s.getGridHeight(), null);
                        s.setGridX(-1*deltaX + s.getY());
                        s.setGridY(-1*deltaY + s.getX());
                    }
                    
                }
            }
            
        

    }
    // testing this by changing it to a constructor
    public Map()
    {
        // open terrain window
        /* 
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1920,1080);
        Color backgroundColor = new Color(170, 240, 130); // Dark green
        // background 
        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
        */
    }
}
//BUG: holding a key and then switching directions halts movement
//how to reproduce: 
// 1. hold any arrow key
// 2. hold a second arrow key 
// 3. release the second arrow key
