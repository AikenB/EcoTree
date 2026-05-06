
    package terrain;

import java.awt.Color;
public class Generation {
    public static Color[][] land;
    public static void initialize()
    {
        land = new Color[terrain.Map.getMapHeight()][terrain.Map.getMapWidth()];
        // test
        for (int i = 0; i < land.length; i++)
        {
            for (int j = 0; j < land[0].length; j++)
            {
                land[i][j] = Color.BLUE;

                // random gen test
                int r = (int)(Math.random()*255);
                int g = (int)(Math.random()*255);
                int b = (int)(Math.random()*255);

                land[i][j]= new Color(r,g,b);
            }
        }
    }
    public static Color[][] terrainValues()
    {
        return land;
    }
}

