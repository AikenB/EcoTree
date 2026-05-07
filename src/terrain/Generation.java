
package terrain;

import java.awt.Color;
import utilities.WeightVector;
import terrain.Noise;
public class Generation {
    public static Color[][] land;

    public static boolean initialized = false;
    public static boolean terrainGenerated = false;

    
    public static void initialize()
    {

        if (!initialized)
        {
            initialized = true;

            land = new Color[terrain.Map.getMapHeight()][terrain.Map.getMapWidth()];
        // test
        for (int i = 0; i < land.length; i++)
        {
            for (int j = 0; j < land[0].length; j++)
            {
                land[i][j] = Color.BLUE;

                // random gen test
                int r = (int)(Math.random()*100);
                int g = (int)(Math.random()*255);
                //int b = (int)(Math.random()*255);

                land[i][j]= new Color(r,g,0);
            }
        }
        }
        

        // NEW noise gen

        if (terrainGenerated == false)
        {
            terrainGenerated = true;
            int h = Map.getMapHeight();
            int w = Map.getMapWidth();
            // p = partition size
            int p = 32;

            // break up into pxp squares
            WeightVector[][] vectorArr = new WeightVector[(h/p)+2][(w/p)+2];
            //

            // loop through each cell in the grid
            // remember that the array is formatted arr[height][width]

            int[][] output = Noise.octaveNoiseLayer(land.length,land[0].length);
            for (int i = 0; i < land.length; i ++ )
            {
                for (int j = 0; j < land[0].length; j ++)
                {
                    // set color based on output3
                    int r = 50;
                    int b = 50;
                    int g = output[i][j];
                    land[i][j]= new Color(r,g,b);

                    // lakes attempt
                    if (output[i][j] > 100 && output[i][j] < 130)
                    {
                        b = 100;
                        land[i][j] = new Color(r,g,b);
                    }
                    System.out.println(output[i][j]);
                }
            }
        }
        
        
        // NEW

    }
    public static Color[][] terrainValues()
    {
        return land;
    }
}

