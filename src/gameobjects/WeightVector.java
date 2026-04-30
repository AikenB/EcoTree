package gameobjects;
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
    /**
     * sets the WeightVector's x,y,theta, and weight components based on the inputs
     * 
     */
    public void orient(double x, double y, double weight) {
        this.theta = Math.atan2(y, x);
        if (x < 0) {
            this.theta += Math.PI;
        }
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
        return new WeightVector(x + other.getX(), y + other.getY(), weight + other.getWeight());
    }
    
    /**
     * turns the vector 180 degrees
     */
    public void flip(){
        x *= -1;
        y *= -1;
        orient(x, y, weight);
    }

    
}
