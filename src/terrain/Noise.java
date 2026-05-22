package terrain;
import java.util.ArrayList;

import utilities.WeightVector;
public class Noise {
    // p = partition size, generally is a power of 2
    public static double[][] noiseLayer(int p, int w, int h)
    {
        double[][] output = new double[h][w];
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
                    // TEST WITH RANDOM VECTORS
                    /* 
                    double randomAngle = Math.random() * Math.PI * 2;
                    vectorArr[i][j] = new WeightVector(Math.cos(randomAngle), Math.sin(randomAngle));
                    */

                }
            }
            // loop through each cell in the grid
            // remember that the array is formatted arr[height][width]
            for (int i = 0; i < h; i ++ )
            {
                for (int j = 0; j < w; j ++)
                {

                    double x = (j % p) / (double)p;
                    double y = (i % p) / (double)p;
                    // create 4 vectors, one from each corner to the selected cell
                    // top left
                    WeightVector vTL = new WeightVector(x, y);
                    // top right
                    WeightVector vTR = new WeightVector(x-1, y);
                    // bottom left
                    WeightVector vBL = new WeightVector(x, y-1);
                    // bottom right
                    WeightVector vBR = new WeightVector(x-1, y-1);

                    // find the dot product of these vectors and the 4 corner vectors
                    WeightVector bottomLeftVector = vectorArr[(i/p+1)][(j/p)];
                    WeightVector bottomRightVector = vectorArr[(i/p+1)][(j/p+1)];
                    WeightVector topLeftVector = vectorArr[(i/p)][(j/p)];
                    WeightVector topRightVector = vectorArr[(i/p)][(j/p)+1];

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

                    output[i][j] = output3;
                }
            }
            return output;
        }
    // this function layers with 64x64 partitions, 32x32 partitions, 16x16 partitions, and then 8x8 partitions
    public static int[][] octaveNoiseLayer(int w,int h)
    {
        double[][] noise64 = noiseLayer(64,w,h);
        double[][] noise32 = noiseLayer(32,w,h);
        double[][] noise16 = noiseLayer(16,w,h);
        double[][] noise8 = noiseLayer(8,w,h);
        int[][] output = new int[h][w];


        // blend noises together, giving more weight to the larger partition sizes
        // noise64 gets 1 weight, noise32 gets 1/2 weight, noise16 gets 1/4 weight, and noise8 gets 1/8 weight
        // 8/8+4/8+2/8+1/8= 15/8
        for (int i = 0; i < h; i ++)
        {
            for (int j = 0; j < w; j ++)
            {
                double weightedNoise = (noise64[i][j]*1.0)+(noise32[i][j]*1.0/2)+(noise16[i][j]*1.0/4)+(noise8[i][j]*1.0/8);
                //normalize weightedNoise 
                // consider the range of each noise should be [-1,1]
                weightedNoise /= (15.0/8);
                
                // now convert weightedNoise so that the range is [0,1]
                weightedNoise +=1;
                weightedNoise *= 0.5;

                output[i][j] = (int)(255*weightedNoise);

                if (output[i][j] > 255)
                {
                    output[i][j] = 255;
                }
                if (output[i][j] < 0)
                {
                    output[i][j] = 0;
                }
            }
        }

        //TESTING
        int max = output[0][0];
        int min = output[0][0];
        for (int a = 0; a < output.length; a++ )
        {
            for (int b = 0; b < output[0].length; b++)
            {
                if (output[a][b] > max)
                    max = output[a][b];
                if (output[a][b] < min)
                    min = output[a][b];
            }
        }
        System.out.println(max);
        System.out.println(min);
        // normalize/rescale based on max and min
        for (int a = 0; a < output.length; a++ )
        {
            for (int b = 0; b < output[0].length; b++)
            {
                output[a][b] = (output[a][b]-min) / (max-min);
            }
        }



        // print statements for TESTING

        ArrayList<Integer> noiseValues = new ArrayList<Integer>();
        for (int i =0; i < 255; i++)
        {
            noiseValues.add(0);
        }
        // count frequency for each noise value from 0 to 255
        for (int i = 0; i < output.length; i++)
        {
            for (int j = 0; j < output[0].length; j++)
            {
                int index = output[i][j];
                noiseValues.set(index,noiseValues.get(index)+1);
            }
        }
        // print all noise frequencies

        for (int i =0; i < noiseValues.size(); i++)
        {
            System.out.println(i + " freq: " + noiseValues.get(i) );
        }

        // TESTING

        return output;
    }
}
