package terrain;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        System.out.println("doing thing");
        // draw rectangles for terrain
        g.setColor(Color.BLUE);
        for (int i = 0; i < 96; i ++)
        {
            for (int j= 0; j < 54; j ++)
            {
                g.drawRect(0+(20*i), 0+(20*j), 40, 40);       
            }
        }

    }
    public static void loadMap()
    {
        // open terrain window
        JFrame frame = new JFrame("EcoTree");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1920,1080);
        
        // add rectangles
        frame.add(new Map());
        //

        frame.setVisible(true);
    }

}
