package terrain;

import gui.Menu;
import gui.StatsScreen;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utilities.Grid;
import utilities.Sprite;
import utilities.Game;

public class Map extends JPanel {

    // numbers to track screen translation
    // -320/-320 centers screen on start
    private static int deltaX = -440;
    private static int deltaY = -900;
    private Menu m;
    private StatsScreen statsScreen;

    private int tick = 0;
    int windVectorX = 5;
    int windVectorY = 5;
    


    // numbers to track zoom in
    private static double scale = 1;
    
    public static double getScale()
    {
        return scale;
    }
    public static void setScale(double s)
    {
        scale = s;
    }

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
    private static int width = 128;
    private static int height = 128;

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
        // System.out.println(Game.timeElapsed);
        if (Game.timeElapsed != tick) {
            tick = Game.timeElapsed;
            System.out.println(Game.timeElapsed);
            Generation.changeSkyRow(windVectorY);
            Generation.changeSkyColumn(windVectorX);
            if (Game.timeElapsed % 24 == 0) {
                windVectorX = -10 + (int) (21 *Math.random());
                windVectorY = -10 + (int) (21 *Math.random());

            }

        }
        // clear the screen
        super.paintComponent(g);
        //use graphics 2d ( so we can translate things during screen scrolling)
        Graphics2D g2d = (Graphics2D) g;

        // zoom in/out
        g2d.scale(scale, scale);

        // move screen
        //g2d.translate(deltaX,deltaY);

        // adjust in order for scale to zoom in on the center
        double d = scale/Map.getMapHeight();
        int adjust = (int)((Map.getMapHeight()/2)-(d/2)); 
        g2d.translate(deltaX,deltaY);
 
        // rectangle outline color
        g2d.setColor(Color.BLACK);

        int squareWidth = 20;
        int squareHeight = 20;
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
                int r = Math.min(255, terrain.Generation.terrainValues()[j][i].getRed() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                int gre = Math.min(255, terrain.Generation.terrainValues()[j][i].getGreen() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                int b = Math.min(255, terrain.Generation.terrainValues()[j][i].getRed() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                Color c;
                if ((int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]) != 0) {
                    c = new Color(gre - 50, gre, gre - 50);
                } else {
                    c = new Color(r, gre, b);
                }
                
                g2d.setColor(c);
                g2d.fillRect(0+(squareWidth*i), 0+(squareHeight*j), squareWidth, squareHeight); 

            }
        }

        
        
            if (Grid.sprites.size() > 0){

                for (int i = 0; i < Grid.sprites.size(); i++){
                    Sprite s = Grid.sprites.get(i);
                    if (s != null) {
                        g2d.drawImage(s.getImage(), s.getGridX(), s.getGridY(), s.getGridWidth(), s.getGridHeight(), null);
                    }
                }
            }
            if (Grid.beeSprites.size() > 0){

                for (int i = 0; i < Grid.beeSprites.size(); i++){
                    Sprite s = Grid.beeSprites.get(i);
                    if (s != null) {
                        g2d.drawImage(s.getImage(), s.getGridX(), s.getGridY(), s.getGridWidth(), s.getGridHeight(), null);
                    }
                }
             }
            //m.refresh();
        

    }
    // testing this by changing it to a constructor
    public Map(Menu m)
    {
        this.m = m;
        this.statsScreen = new StatsScreen(this);
        statsScreen.setFocusable(false);
        
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                m.recieveClick(e.getX(), e.getY());
            }
        });
        
        Timer timer = new Timer(100, e -> {
            repaint();
            revalidate();
            //m.refresh();
            //m.frame.setComponentZOrder(m.components.get(4),0);
        });

        timer.start();
    }

    /**
     * Controls the visibility of the stats screen
     * @param visible true to show stats screen, false to hide it
     */
    public void setStatsScreenVisible(boolean visible) {
        //statsScreen.setVisible(visible);
        statsScreen.canOpen = visible;
    }
}
//BUG: holding a key and then switching directions halts movement
//how to reproduce: 
// 1. hold any arrow key
// 2. hold a second arrow key 
// 3. release the second arrow key
