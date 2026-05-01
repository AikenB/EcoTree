package gui;
import gameobjects.Organism;

public class Grid {

    public static Organism[][] grid = new Organism[250][250];

    public static enum Direction{
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT
    }

    public static void addOrganism(Organism organism) {
        int x = organism.getX();
        int y = organism.getY();
        grid[x][y] = organism;
    }

    public static void removeOrganism(int x, int y) {
        grid[x][y] = null;

    }
}
