package terrain;
import utilities.WeightVector;
public class Noise {
    // p = partition size, generally is a power of 2
    public static int[][] noiseLayer(int p, int w, int h)
    {
        int[][] output = new int[h][w];
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
            for (int i = 0; i < h; i ++ )
            {
                for (int j = 0; j < w; j ++)
                {

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

                    int val = (int)(output3 * 255);
                    if (val > 255)
                    {
                        val = 255;
                    }
                    if (val < 0)
                    {
                        val = 0;
                    }
                    output[i][j]= val;
                }
            }
            return output;
    }
}
