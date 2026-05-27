package tree;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
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
    
    private static boolean initialized;
    
    public static boolean treeInitialized()
    {
        return initialized;
    }
    
    public MainTree()
    {

        super(Organism.Species.MAINTREE);
        initialized = false;

    }
    private ExecutorService executor;  

    public void initializeTree()
    {
        initialized = true;
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int totalOrganisms = 0;
                    double diversityIndex = 0;
                    
                    ArrayList<Hitbox> newList = Grid.getHitboxes();
                    ArrayList<Species> speciesList = Grid.getSpeciesList();
                    // create parallel list with the count of each species
                    ArrayList<Integer> speciesCounts = new ArrayList<Integer>();
                    for (int i = 0; i < speciesList.size(); i++)
                    {
                        speciesCounts.add(0);
                    }

                    // loop through each hitbox and get the organism
                    for (int i = 0; i < newList.size(); i++)
                    {
                        totalOrganisms++;
                        Organism h = newList.get(i).getOrganism();

                        // find the index of the species in the species list
                        int ind = speciesList.indexOf(h.getSpecies());
                        speciesCounts.set(ind,speciesCounts.get(ind)+1);
                    }

                    // print to test
                    for (int i = 0; i < speciesCounts.size(); i++)
                    {
                        System.out.println(speciesList.get(i) + ":" + speciesCounts.get(i));
                    }

                    Thread.sleep(1000);
                }
                 catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            } 
        });
    }
}
