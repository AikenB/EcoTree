package gui;

import gameobjects.Animal;
import gameobjects.FlyingAnimal;
import gameobjects.Mutation;
import gameobjects.Organism;
import gameobjects.Plant;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import terrain.Map;
import utilities.Grid;
import utilities.Hitbox;

public class StatsScreen extends JPopupMenu {
    
    private JLabel speciesLabel;
    private JLabel healthLabel;
    private JLabel energyLabel;
    private JLabel trophicLevelLabel;
    private ArrayList<Mutation> mutationsList;
    private ArrayList<JLabel> mutationLabels;
    private ArrayList<JLabel> diseaseLabels;
    private ArrayList<JLabel> plantStats;
    private ArrayList<JLabel> animalStats;
    JLabel produceLabel;
    JLabel maxProduceLabel;
    JLabel photosynthesisEfficiencyLabel;
    JLabel foodCapacityLabel;
    JLabel speedLabel;
    JLabel satietyLabel;
    JLabel fertilityLabel;
    private Organism currentOrganism;
    private Organism previousOrganism;
    private Timer updateTimer;
    public boolean canOpen = true;

    
    
    public StatsScreen(Map mapPanel) {
        speciesLabel = new JLabel("Species: ");
        healthLabel = new JLabel("Health: ");
        energyLabel = new JLabel("Energy: ");
        trophicLevelLabel = new JLabel("Trophic Level: ");
        mutationsList = new ArrayList<>();
        mutationLabels = new ArrayList<>();
        diseaseLabels = new ArrayList<>();

        add(speciesLabel);
        add(healthLabel);
        add(energyLabel);
        add(trophicLevelLabel);
        add(new JLabel("MUTATIONS:"));
        
        
        // Adjust width and height here
        setPreferredSize(new Dimension(200, 250));
        
        initialize(mapPanel);
    }
    
    /**
     * Updates the popup with stats for the given organism
     */
    public void updateStats(Organism organism) {
        if (organism == null) {
            this.setVisible(false);
            return;
        }
        
        removeAll();
        
        add(speciesLabel);
        add(healthLabel);
        add(energyLabel);
        add(trophicLevelLabel);
        
        speciesLabel.setText("Species: " + organism.getSpecies());
        healthLabel.setText("Health: " + (int) organism.getHealth());
        energyLabel.setText("Energy: " + (int) organism.getEnergy());
        trophicLevelLabel.setText("Trophic Level: " + (organism.getTrophicLevel() + 1));

        if (organism.getClass() == Plant.class) {
            Plant plant = (Plant) organism;
            produceLabel = new JLabel("Produce: " + plant.getProduce());
            maxProduceLabel = new JLabel("Max Produce: " + plant.getMaxProduce());
            photosynthesisEfficiencyLabel = new JLabel("Photosynthesis Rate: " + plant.getPhotosynthesisEfficiency());
            add(produceLabel);
            add(maxProduceLabel);
            add(photosynthesisEfficiencyLabel);
        } else if (organism instanceof Animal) {
            Animal animal;
            if (organism.getClass() == Animal.class){
                animal = (Animal) organism;
            }
            else{
                animal = (FlyingAnimal) organism;
                add(new JLabel("id: " + ((FlyingAnimal) organism).getUniqueID()));
            }
                
            foodCapacityLabel = new JLabel("Food Capacity: " + animal.getFoodCapacity());
            speedLabel = new JLabel("Speed: " + animal.getSpeed());
            satietyLabel = new JLabel("Satiety: " + (int) animal.getSatietyPercentage() + "%");
            fertilityLabel = new JLabel("Fertility: " + (int) animal.getFertilityPercentage() + "%");
            add(foodCapacityLabel);
            add(speedLabel);
            add(satietyLabel);
            add(fertilityLabel);
        }

        add(new JLabel("MUTATIONS:"));
        // Clear old mutation labels
        for (JLabel label : mutationLabels) {
            remove(label);
        }
        mutationLabels.clear();
        mutationsList = organism.getMutations();
        for (Mutation mutation : mutationsList) {
            JLabel mutationLabel = new JLabel(mutation.toString());
            mutationLabels.add(mutationLabel);
            add(mutationLabel);
        }

        add(new JLabel("DISEASES:"));
        // Clear old disease labels
        for (JLabel label : diseaseLabels) {
            remove(label);
        }
        diseaseLabels.clear();
        HashMap diseases = organism.getDiseases();
        for (Object disease : diseases.keySet()) {
            double infectionLevel = (double) diseases.get(disease);
            if (infectionLevel >= 0.35) {
                JLabel diseaseLabel = new JLabel(disease.toString() + ": " + String.format("%.1f", infectionLevel * 100) + "%");
                diseaseLabels.add(diseaseLabel);
                add(diseaseLabel);
            
            }
        }
    }
    
    /**
     * Checks if a new organism is being highlighted
     */
    private boolean isNewOrganism() {
        if (currentOrganism != previousOrganism && currentOrganism != null) {
            previousOrganism = currentOrganism;
            return true;
        }
        return false;
    }
    
    /**
     * Registers this stats screen to display on hover over the given Map component
     */
    private void initialize(Map mapPanel) {
        final StatsScreen statsScreen = this;
        currentOrganism = null;
        previousOrganism = null;
        
        // Timer to update stats only when organism changes
        updateTimer = new Timer(50, e -> {
            if (currentOrganism != null && statsScreen.isVisible()) {
                if (isNewOrganism()) {
                    updateStats(currentOrganism);
                }
            }
        });
        updateTimer.start();
        
        mapPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Convert screen coordinates to grid coordinates
                
                //NEW: rescale screen coords based on zoom
                double scale = Map.getScale();

                int screenX = (int) ((e.getX() - Map.getDeltaX()) / scale);
                int screenY = (int) ((e.getY() - Map.getDeltaY()) / scale);
                
                // Each grid square is 20 pixels
                int gridX = screenX / 20;
                int gridY = screenY / 20;
                
                // Check if coordinates are within grid bounds
                if (gridX >= 0 && gridX < Map.getMapWidth() && gridY >= 0 && gridY < Map.getMapHeight()) {
                    Hitbox hitbox = Grid.grid[gridY][gridX];
                    
                    
                    if (hoveringOverFlyingOrganism(gridX, gridY)) {
                        currentOrganism = getFlyingOrganismAt(gridX, gridY);
                        updateStats(currentOrganism);
                        if (canOpen)
                            statsScreen.show(mapPanel, e.getX(), e.getY());
                    } 
                    else if (hitbox != null) {
                        currentOrganism = hitbox.getOrganism();
                        updateStats(currentOrganism);
                        if (canOpen)
                            statsScreen.show(mapPanel, e.getX(), e.getY());
                    } else {
                        currentOrganism = null;
                        statsScreen.setVisible(false);
                    }
                } else {
                    currentOrganism = null;
                    statsScreen.setVisible(false);
                }
            }
        });
    }

    private boolean hoveringOverFlyingOrganism(int x, int y) {
        for (FlyingAnimal animal : Grid.flyingAnimals) {
            if (animal.getX() == x && animal.getY() == y) {
                return true;
            }
        }
        return false;

    }

    private Organism getFlyingOrganismAt(int x, int y) {
        for (FlyingAnimal animal : Grid.flyingAnimals) {
            if (animal.getX() == x && animal.getY() == y) {
                return animal;
            }
        }
        return null;
    }
}
