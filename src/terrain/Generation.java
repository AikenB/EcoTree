
package terrain;

import java.awt.Color;
import utilities.WeightVector;

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
                    vectorArr[i][j] = new WeightVector(randomVectors[randInt][0],randomVectors[randInt][1]);
                }
            }

            // loop through each cell in the grid
            // remember that the array is formatted arr[height][width]
            for (int i = 0; i < land.length; i ++ )
            {
                for (int j = 0; j < land[0].length; j ++)
                {

                    // because each partition is 8x8, scale down
                    // local coordinates of corners are 
                    // top left: (0,1)
                    // top right: (1,1)
                    // bottom left: (0,0)
                    // bottom right: (1,0)


                    double x = (j % p) / (double)p;
                    double y = (i % p) / (double)p;
                    // create 4 vectors, one from each corner to the selected cell
                    // top left
                    WeightVector vTL = new WeightVector(x, y-1);
                    // top right
                    WeightVector vTR = new WeightVector(x-1, y-1);
                    // bottom left
                    WeightVector vBL = new WeightVector(x, y);
                    // bottom right
                    WeightVector vBR = new WeightVector(x-1, y);

                    // find the dot product of these vectors and the 4 corner vectors
                    WeightVector bottomLeftVector = vectorArr[(i/p)][(j/p)];
                    WeightVector bottomRightVector = vectorArr[(i/p)][(j/p)+1];
                    WeightVector topLeftVector = vectorArr[(i/p)+1][(j/p)];
                    WeightVector topRightVector = vectorArr[(i/p)+1][(j/p)+1];

                    double topLeftDP = WeightVector.dotProduct(vTL, topLeftVector);
                    double topRightDP = WeightVector.dotProduct(vTR, topRightVector);
                    double bottomLeftDP = WeightVector.dotProduct(vBL, bottomLeftVector);
                    double bottomRightDP = WeightVector.dotProduct(vBR, bottomRightVector);

                    // interpolate horizontally between topleft & topright dot product
                    // equation for variable t is 6x^5-15x^4+10x^3
                    double t = x*x*x*(x*(x*6 - 15) + 10);

                    // output equation is (a*t) + (b*(1-t))
                    // as t (the output of the function / y value) increases, a*t increases, a being the top right dot product in this case

                    double output1 = (topRightDP*t)+(topLeftDP*(1-t));
                    // interpolate horizontally between bottomleft & bottomright dot product
                    double output2 = (bottomRightDP*t)+(bottomLeftDP*(1-t));

                    // interpolate vertically using output1 and output2
                    // this time we input the localY coordinate of the cell into the equation for t
                    t = y*y*y*(y*(y*6 - 15) + 10);
                    double output3 = (output2*t)+(output1*(1-t));

                    // output 3 should be a double between -1 and 1

                    // normalize output3
                    // first make it [0,2]
                    output3 += 1;
                    // divide it by 2 to make it [0,1]
                    output3 /= 2;

                    // set color based on output3
                    int r = 50;
                    int g = (int)(output3 * 255);
                    if (g > 255)
                    {
                        g = 255;
                    }
                    if (g < 0)
                    {
                        g = 0;
                    }
                    int b = 50;
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

