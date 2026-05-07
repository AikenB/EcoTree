package utilities;
public class WeightVector {

    private double x;
    private double y;
    private double theta;
    private double weight;

    /**
     * Creates a WeightVector object.
     * A WeightVector is just a representation of a 2d vector, however, the x and y components are only used to determine the direction of the vector. The vector is then converted to a unit vector and the weight is used as its new magnitude
     * @param x
     * @param y
     * @param weight
     */

    

    public WeightVector(double x, double y, double weight) {
        this.weight = weight;
        orient(x, y, weight);


    }

    // NORMAL VECTORS NOT WEIRD ONES
    public WeightVector(double x, double y)
    {
        this.x = x;
        this.y = y;

        this.theta = Math.atan2(y, x);
        this.weight = Math.sqrt(x * x + y * y);
    }
    // DOT PRODUCT
    public static double dotProduct(WeightVector v1, WeightVector v2)
    {
        double dp = (v1.getX()*v2.getX()) + (v1.getY()*v2.getY());
        // note to self: COMPLETE
        return dp;
    }

    /**
     * sets the WeightVector's x,y,theta, and weight components based on the inputs
     * 
     */
    public void orient(double x, double y, double weight) {

        this.theta = Math.atan2(y, x);
        this.x = Math.cos(theta) * weight;
        this.y = Math.sin(theta) * weight;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWeight() {
        return weight;
    }
    public double getTheta() {
        return theta;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        orient(x, y, weight);
    }

    public WeightVector add(WeightVector other) {
        // Sum the weighted components directly
        WeightVector result = new WeightVector(0, 0, 0);  // Dummy creation
        result.x = this.x + other.getX();  // Sum weighted x components
        result.y = this.y + other.getY();  // Sum weighted y components
        result.weight = Math.sqrt(result.x * result.x + result.y * result.y);  
        result.theta = Math.atan2(result.y, result.x);  // Calculate new angle from summed components
        return result;
    }
    
    /**
     * turns the vector 180 degrees
     */
    public void doubleOrthogonalize(){
        x *= -1;
        y *= -1;
        theta = Math.atan2(y, x);  // Just recalculate theta, don't call orient()
    }

    
}
