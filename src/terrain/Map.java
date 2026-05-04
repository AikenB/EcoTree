package terrain;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        // draw rectangles for terrain
        g.setColor(Color.BLUE);
        for (int i = 0; i < 48; i ++)
        {
            for (int j= 0; j < 27; j ++)
            {
                g.drawRect(0+(40*i), 0+(40*j), 40, 40);       
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
        //

        frame.setVisible(true);
    }

}
