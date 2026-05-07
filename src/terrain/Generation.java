
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

            // set each vector to 1 of 8 directions randomly
            // arrays are in the form of [x,y]


            //rt = root two over two, basically so the vectors pointing diagonally have the same magnitude as the ones pointing horizontally or vertically
            final double rt = Math.sqrt(2)/2;

            double[][] randomVectors = {{0,1}, {1*rt,1*rt}, {1,0}, {rt,-1*rt}, {0,-1}, {-1*rt,-1*rt}, {-1,0}, {-1*rt,1*rt}};
            for (int i = 0; i < vectorArr.length; i++)
            {
                for (int j = 0; j < vectorArr[0].length;j++)
                {
                    // each vector in vectorArr gets a random x and y value
                    int randInt = (int)(Math.random()*8);
                    // TEMP 
                    //vectorArr[i][j] = new WeightVector(randomVectors[randInt][0],randomVectors[randInt][1]);
                    // TEST WITH RANDOM VECTORS
                    double randomAngle = Math.random() * Math.PI * 2;
                    vectorArr[i][j] = new WeightVector(Math.cos(randomAngle), Math.sin(randomAngle));

                }
            }

            // loop through each cell in the grid
            // remember that the array is formatted arr[height][width]

            int[][] output = Noise.noiseLayer(16,land.length,land[0].length);
            for (int i = 0; i < land.length; i ++ )
            {
                for (int j = 0; j < land[0].length; j ++)
                {
                    // set color based on output3
                    int r = 50;
                    int b = 50;
                    int g = output[i][j];
                    land[i][j]= new Color(r,g,b);
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

