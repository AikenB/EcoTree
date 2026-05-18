package gameobjects;

import gameobjects.Organism.Species;
import java.util.ArrayList;
import javax.swing.Timer;
import utilities.Grid;
import utilities.Hitbox;

public class Outbreak {


    private int x;
    private int y;
    private double infectivity;
    private type diseaseType;
    private int INFECTION_RADIUS = 3;
    private int time;
    private int duration;
    private double lethality;
    private ArrayList<Species> targetedSpecies = new ArrayList<>();
    // private ArrayList<Organism> infectedOrganisms = new ArrayList<>();

    public static int antcount = 0;
    public static int mousecount = 0;
    public static int wormcount = 0;
    public static int andDeaths = 0;
    public static int mouseDeaths = 0;
    public static int wormDeaths = 0;
    private int infectedCount = 0;
    public static enum type {

        CORONAVIRUS,
        FLU,
        FUNGUS_INFECTION,
        BACTERIAL_INFECTION
    }
    /**
     * Creates an outbreak starting from a specific organism that will be infected with the specified disease
     * @param startingHost the organism that will be the initial host of the outbreak
     * @param diseaseType the type of disease that will be spreading in the outbreak
     * @param targetedSpecies the list of species that the outbreak can spread to
     */
    public Outbreak(Organism startingHost, type diseaseType, ArrayList<Species> targetedSpecies) {
        time = 0;
        this.x = startingHost.getX();
        this.y = startingHost.getY();
        this.diseaseType = diseaseType;
        this.targetedSpecies = targetedSpecies;
        duration = (int) (Math.random() * 250) + 250;
        //this.infectedOrganisms = new ArrayList<>();

        switch (diseaseType) {
            case CORONAVIRUS:
                infectivity = 0.04;
                lethality = 4.0;
                break;
            case FLU:
                infectivity = 0.035;
                lethality = 4.5;
                break;
            case FUNGUS_INFECTION: //This virus is tuned
                infectivity = 0.035;
                lethality = 6.0;
                INFECTION_RADIUS = 4;
                break;
            case BACTERIAL_INFECTION:
                infectivity = 0.04;
                lethality = 3.25;
                break;
        }
            
        startingHost.addInfection(this, 0.02); 
        infectedCount++;

        //causes all organisms with the infection to spread the infection every second
        Timer t = new Timer(1000, e -> {
            if (time <= duration) {
            System.out.println(diseaseType + " time left of outbreak: " + (duration - time));
            
            ArrayList<Organism> organisms = new ArrayList<>();
            for (int i = 0; i < Grid.grid.length; i++) {
                for (int j = 0; j < Grid.grid[0].length; j++) {
                    Hitbox hitbox = Grid.grid[i][j];
                    if (hitbox != null) {
                        Organism organism = hitbox.getOrganism();
                        if (organism != null && !organisms.contains(organism)) {
                            organisms.add(organism);
                            if (organism.isInfectedWith(this)) {
                                infect(organism.getX(), organism.getY());
                                
                                if (organism instanceof Plant) {
                                    organism.addInfection(this, 0.001);
                                } else {
                                    organism.addInfection(this, 0.005);
                                }
                                if (organism.diseases.get(this) >= 0.35) { // If severity is 100% or more, the organism dies
                                    organism.applyDiseaseEffect(this);  // Apply damage each tick
                                }
                                
                            }
                        }
                    }
                }
            }
            //System.out.println(diseaseType + " number of infected:" + infectedCount);
            
            time++;
            } else {
                
                ((Timer) e.getSource()).stop();
                for (Hitbox hitbox : Grid.hitboxes) {
                    Organism organism = hitbox.getOrganism();
                    if (organism != null && organism.isInfectedWith(this)) {
                        organism.diseases.remove(this); // Remove disease from organism after outbreak ends
                    }
                }
            }
        });

        t.start();

    }

    

    public void infect(int x, int y) {

        ArrayList<Organism> organisms = new ArrayList<>();
        for (int i = y - INFECTION_RADIUS; i <= y + INFECTION_RADIUS; i++) {
            for (int j = x - INFECTION_RADIUS; j <= x + INFECTION_RADIUS; j++) {
                if (i == x && j == y) continue; 
                
                if (i >= 0 && i < Grid.grid.length && j >= 0 && j < Grid.grid[0].length) {
                    Hitbox hitbox = Grid.grid[i][j];
                    if (hitbox != null) {
                        Organism organism = hitbox.getOrganism();
                        
                        if (organism != null 
                            //&& !(organism instanceof Plant)
                            && !organism.isInfectedWith(this) 
                            && !organisms.contains(organism)
                            && targetedSpecies.contains(organism.getSpecies())) {
                                
                            organisms.add(organism);

                            double resistance = organism.getInfectionResistance();
                            double infectionChance = infectivity * (1/resistance); // Higher resistance reduces chance
                            if (Math.random() < infectionChance) {
                                organism.addInfection(this, 0.02);
                                infectedCount++;
                                //organism.applyDiseaseEffect(this);
                            }
                        }
                    }
                }
            }
        }
    }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public double getLethality() {
        return lethality;
    }
    

    public type getDiseaseType() {
        return diseaseType;
    }

    public String toString() {
        return diseaseType.toString();
    }
    
}
