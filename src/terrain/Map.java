package terrain;

import gui.DayNightCycle;
import gui.Screen;
import gui.StatsScreen;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.*;
import utilities.Game;
import utilities.Grid;
import utilities.Sprite;

public class Map extends JPanel {

    // numbers to track screen translation
    // -320/-320 centers screen on start
    private static int deltaX = -440;
    private static int deltaY = -900;
    private Screen m;
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
                Color c;
                if (Screen.cloudsVisible) {
                    int r = Math.min(255, terrain.Generation.terrainValues()[j][i].getRed() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                    int gre = Math.min(255, terrain.Generation.terrainValues()[j][i].getGreen() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                    int b = Math.min(255, terrain.Generation.terrainValues()[j][i].getRed() + (int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]));
                    
                    if ((int) (terrain.Generation.cloudValues()[(int) Generation.getSkyRow() + i][(int) Generation.getSkyColumn() + j]) != 0) {
                        c = new Color(gre - 50, gre, gre - 50);
                    } else {
                        c = new Color(r, gre, b);
                    }
                } else {
                    c = terrain.Generation.terrainValues()[j][i];
                }
                
                g2d.setColor(darken(c, (float) (DayNightCycle.dayTimeLevel)));
                g2d.fillRect(0+(squareWidth*i), 0+(squareHeight*j), squareWidth, squareHeight); 

            }
        }

            if (Screen.gameRunning){
            
                // Render sprites with thread-safe iteration
                for (Sprite s : Grid.sprites) {
                    if (s != null) {
                        g2d.drawImage(darken(s.getImage(), (float) (DayNightCycle.dayTimeLevel * 0.5)), s.getGridX(), s.getGridY(), s.getGridWidth(), s.getGridHeight(), null);
                    }
                
                }
            
                // Render bee sprites with thread-safe iteration
                for (Sprite s : Grid.flyingAnimalSprites) {
                    if (s != null) {
                        g2d.drawImage(darken(s.getImage(), (float) (DayNightCycle.dayTimeLevel * 0.5)), s.getGridX(), s.getGridY(), s.getGridWidth(), s.getGridHeight(), null);
                    }
                
                }
             //Menu.dayNightLayer.repaint();
            }
            //m.refresh();
        

    }
    // testing this by changing it to a constructor
    public Map(Screen m)
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
        
        // Render at 60 FPS for smooth animation (16.67ms per frame)
        Timer timer = new Timer(16, e -> {
            repaint();
            // Removed revalidate() to reduce layout thrashing
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

    public Color darken(Color color, float darknessFactor) {
        float factor = 1 - darknessFactor; 
        int r = Math.min(255, (int)(color.getRed()   * factor));
        int g = Math.min(255, (int)(color.getGreen() * factor));
        int b = Math.min(255, (int)(color.getBlue()  * factor));
        return new Color(r, g, b);
    }
    // public BufferedImage darken(BufferedImage image, float darknessFactor) {
    //     //float factor = 1 - darknessFactor; 
    
    //     BufferedImage darkened = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    //     for (int y = 0; y < image.getHeight(); y++) {
    //         for (int x = 0; x < image.getWidth(); x++) {
    //             Color c = new Color(image.getRGB(x, y));
                
    //             Color newColor = darken(c, darknessFactor);
    //             darkened.setRGB(x, y, newColor.getRGB());
    //         }
    //     }
    //     return darkened;
    // }
    public BufferedImage darken(BufferedImage image, float darknessfactor) {
        RescaleOp op = new RescaleOp(1 - darknessfactor, 0, null);
        return op.filter(image, null);
    }

    
}
//BUG: holding a key and then switching directions halts movement
//how to reproduce: 
// 1. hold any arrow key
// 2. hold a second arrow key 
// 3. release the second arrow key
