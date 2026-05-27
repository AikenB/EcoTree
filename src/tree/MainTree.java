package tree;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import gameobjects.Organism;
import gameobjects.Plant;
import utilities.Game;

public class MainTree extends Plant {
    // default health
    
    
    
    public MainTree()
    {

        super(Organism.Species.MAINTREE);

    }
    private ExecutorService executor;  
    public void initializeTree(JFrame frame)
    {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // calculate diversity index
                    double diversityIndex = 0;
                    double sum = 0;

                    

                    // every second
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
