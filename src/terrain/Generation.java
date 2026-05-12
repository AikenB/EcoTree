
package terrain;

import java.awt.Color;
import java.io.IOException;

import utilities.Grid;
import utilities.Sprite;
import utilities.WeightVector;
import terrain.Noise;

import java.util.ArrayList;
public class Generation {
    public static Color[][] land;
    public static boolean initialized = false;
    private static boolean terrainGenerated = false;

    private static ArrayList<int[]> treeLocations = new ArrayList<int[]>();
    
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
                    int r = 20;
                    int b = 0;
                    int g = output[i][j];
                    land[i][j]= new Color(r,g,b);

                    // lakes attempt
                    if (output[i][j] > 100 && output[i][j] < 130)
                    {
                        b = 0;
                        r = 15;
                        g = output[i][j]-20;
                        land[i][j] = new Color(r,g,b);
                    }
                    if (output[i][j] < 100 || output[i][j] == 140)
                    // tree gen
                    {
                        int randInt = (int)(Math.random()*15);
                        if (randInt == 1)
                        {
                            int[] loc = {i,j};
                            treeLocations.add(loc);
                        }
                        
                    }
                    
                    //System.out.println(output[i][j]);
                }
            }
        }
        


    }


    public static ArrayList<int[]> getTreeLocations()
    {
        return treeLocations;
    }
    
    public static Color[][] terrainValues()
    {
        return land;
    }
}

