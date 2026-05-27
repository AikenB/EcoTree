package tree;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import gameobjects.Organism;
import gameobjects.Plant;
import utilities.Game;
import utilities.Grid;
import utilities.Hitbox;

public class MainTree extends Plant {
    // default health
    
    public MainTree()
    {

        super(Organism.Species.MAINTREE);

    }

    private ExecutorService executor;

    public void initializeTree()
    {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // print all organisms
                    ArrayList<Hitbox> newList = Grid.getHitboxes();
                    for (int i =0; i < newList.size(); i++)
                    {
                        Organism h = newList.get(i).getOrganism();
                        System.out.println(h.getSpecies() + "X: " + h.getX() + " Y: " + h.getY());
                    }

                    Thread.sleep(10000);
                }
                 catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            } 
        });
    }
}
