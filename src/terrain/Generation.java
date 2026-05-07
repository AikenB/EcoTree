
package terrain;

import java.awt.Color;
import utilities.WeightVector;

public class Generation {
    public static Color[][] land;

    public static boolean initialized = false;

    
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

        int h = Map.getMapHeight();
        int w = Map.getMapWidth();

        // break up into 8x8 squares
        WeightVector[][] vectorArr = new WeightVector[(h/8)+1][(w/8)+1];
        //

        // set each vector to 1 of 8 directions randomly
        // arrays are in the form of [x,y]
        int[][] randomVectors = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};
        for (int i = 0; i < vectorArr.length; i++)
        {
            for (int j = 0; j < vectorArr[0].length;j++)
            {
                // each vector in vectorArr gets a random x and y value
                int randInt = (int)(Math.random()*8);
                vectorArr[i][j] = new WeightVector(randomVectors[randInt][0],randomVectors[randInt][1]);
            }
        }

        // loop through each cell in the grid
        // remember that the array is formatted arr[height][width]
        for (int i = 0; i < land.length; i ++ )
        {
            for (int j = 0; j < land[0].length; j ++)
            {
                // find the local coordinates of the cell within the 8x8 square
                // Example: cells in column 0 of the array are considered to have a localX of 1
                // Example II: cells in column 10 of the array are considered to have a localX of 3
                int localX = 1 + j % 8;
                int localY = 1 + i % 8;

                // because each partition is 8x8, scale down
                // local coordinates of corners are 
                // top left: (0,1)
                // top right: (1,1)
                // bottom left: (0,0)
                // bottom right: (1,0)
                double scaledLocalX = 0.125*localX;
                double scaledLocalY = 0.125*localY;

                // create 4 vectors, one from each corner to the selected cell
                // top left
                WeightVector v1 = new WeightVector(scaledLocalX-0, scaledLocalY-1);
                // top right
                WeightVector v2 = new WeightVector(scaledLocalX-1, scaledLocalY-1);
                // bottom left
                WeightVector v3 = new WeightVector(scaledLocalX-0, scaledLocalY-0);
                // bottom right
                WeightVector v4 = new WeightVector(scaledLocalX-1, scaledLocalY-0);

                // find the dot product of these vectors and the 4 corner vectors

                //double dp1 = 


            }
        }

        
        // NEW

    }
    public static Color[][] terrainValues()
    {
        return land;
    }
}

