package tree;

import java.io.IOException;

import gameobjects.Organism.Species;
import gameobjects.Plant;
import gameobjects.Organism;
import utilities.Grid;
import utilities.Hitbox;

public class MainTree extends Plant {
    // default health

    public MainTree()
    {
        super(Organism.Species.MAINTREE);
    }
}
