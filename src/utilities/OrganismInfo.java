package utilities;
import gameobjects.Organism.Species;

import java.util.ArrayList;
import java.util.Arrays;

import gameobjects.Animal;
import gameobjects.FlyingAnimal;


public class OrganismInfo {

    private int energy;

    private int x;
    private int y;
    private int width = 1;
    private int height = 1;
    private int trophicLevel;

    private ArrayList<Species> predators;
    private ArrayList<Species> prey;

    public OrganismInfo(Species species){


        switch(species){
            case ANT:
                trophicLevel = 1;
                energy = 5;
                // foodCapacity = 10;
                // rftr = 20.0;
                // speed = 1.0;
                
                // thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.FROG, Species.BEAR, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.FERN, Species.GRASS));
                break;
            case SPIDER:
                trophicLevel = 2;
                energy = 15;
                // foodCapacity = 20;
                // rftr = 40.0;
                // speed = 2;
                // thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.FROG, Species.SCORPION, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.WORM, Species.GRASSHOPPER));
                prey.add(Species.ANT);
                break;
            case FROG:
                trophicLevel = 3;
                energy = 30;
                // foodCapacity = 55;
                // rftr = 50.0;
                width = 2;
                height = 2;
                // speed = 2;
                
                // thirstCapacity = 20.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT, Species.BEAR, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.ANT, Species.SPIDER, Species.BEETLE));
                break;
            case SNAKE:
                trophicLevel = 4;
                energy = 40;
                // foodCapacity = 75;
                // rftr = 50.0;
                width = 3;
                height = 2;
                // speed = 2.5;
                // thirstCapacity = 25.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.BOBCAT, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.MOUSE, Species.SCORPION, Species.HUMMINGBIRD));
                break;
            case WORM:
                trophicLevel = 1;
                energy = 20;
                // foodCapacity = 25;
                // rftr = 40;
                width = 1;
                height = 1;
                // speed = 0.75;
                // thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.SCORPION, Species.BEETLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.APPLE_TREE, Species.BERRY_BUSH));
                break;
            case MOUSE:
                trophicLevel = 1;
                energy = 30;
                // foodCapacity = 50;
                // rftr = 50;
                width = 1;
                height = 1;
                //speed = 2;
                //thirstCapacity = 15.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT, Species.BEAR, Species.BALD_EAGLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.BERRY_BUSH, Species.FERN, Species.APPLE_TREE, Species.SCORPION));
                break;
            case GRASSHOPPER:
                trophicLevel = 1;
                energy = 15;
                // foodCapacity = 25;
                // rftr = 20;
                width = 1;
                height = 1;
                //speed = 2;
                //thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.SPIDER, Species.SCORPION, Species.BEETLE, Species.BEAR));
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.FERN, Species.GRASS,Species.APPLE_TREE,Species.BERRY_BUSH));
                break;
            case SCORPION:
                trophicLevel = 3;
                energy = 20;
                // foodCapacity = 30;
                // mass = 2;
                // rftr = 50;
                width = 1;
                height = 1;
                //speed = 2.0;
                //thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.SNAKE, Species.BEETLE));
                prey = new ArrayList<Species>(Arrays.asList(Species.SPIDER, Species.GRASSHOPPER,Species.WORM));
                break;
            case BEETLE:
                trophicLevel = 3;
                energy = 20;
                // mass = 2;
                // foodCapacity = 30;
                // rftr = 40;
                width = 1;
                height = 1;
                //speed = 2.5;
                //thirstCapacity = 10.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.FROG, Species.BEAR, Species.HUMMINGBIRD));
                prey = new ArrayList<Species>(Arrays.asList(Species.GRASSHOPPER, Species.MOONFLOWER, Species.MOSS, Species.FERN, Species.GRASS, Species.SCORPION, Species.DRAGONFRUIT_CACTUS, Species.WORM, Species.FLOWER));
                break;
            case BEE:
                trophicLevel = 1;
                energy = 300; //energy for bees are used as its lifespan since it will have no predators
                //foodCapacity = 25;
                //rftr = 0; //bees cannot reproduce
                width = 1;
                height = 1;
                //speed = 10;
                //thirstCapacity = 5.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.DRAGONFRUIT_CACTUS, Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE));
                break;
            case BOBCAT:
                trophicLevel = 4;
                energy = 50;
                //foodCapacity = 100;
                //rftr = 60.0;
                width = 4;
                height = 2;
                //speed = 3.0;
                //thirstCapacity = 30.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.DEER, Species.MOUSE, Species.SNAKE, Species.FROG, Species.HUMMINGBIRD));
                break;
            case BEAR:
                trophicLevel = 4;
                energy = 100;
                //foodCapacity = 200;
                //rftr = 70.0;
                width = 4;
                height = 4;
                //speed = 3;
                //thirstCapacity = 50.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.BERRY_BUSH, Species.ANT, Species.GRASSHOPPER, Species.BEETLE, Species.FROG));
                break;
            case DEER:
                trophicLevel = 3;
                energy = 50;
                //foodCapacity = 75;
                //rftr = 70.0;
                width = 3;
                height = 3;
                //speed = 2.5;
                //thirstCapacity = 30.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.SNAKE, Species.BOBCAT));
                prey = new ArrayList<Species>(Arrays.asList(Species.GRASS, Species.FERN, Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE, Species.MOSS, Species.BEETLE, Species.GRASSHOPPER, Species.DRAGONFRUIT_CACTUS));
                break;
            case HUMMINGBIRD:
                trophicLevel = 1;
                energy = 25; 
                //foodCapacity = 50;
                //rftr = 120.0; //TODO: change to 120
                width = 1;
                height = 1;
                //speed = 10;
                //thirstCapacity = 5.0;
                predators = new ArrayList<Species>(Arrays.asList(Species.BOBCAT, Species.SNAKE));
                prey = new ArrayList<Species>(Arrays.asList(Species.FLOWER, Species.BERRY_BUSH, Species.APPLE_TREE, Species.ANT, Species.SPIDER, Species.DRAGONFRUIT_CACTUS, Species.MOONFLOWER, Species.BEETLE));
                break;
            case BALD_EAGLE:
                trophicLevel = 4;
                energy = 50;
                //foodCapacity = 150;
                //rftr = 300;
                width = 1;
                height = 1;
                //speed = 12;
                //thirstCapacity = 40.0;
                predators = new ArrayList<Species>();
                prey = new ArrayList<Species>(Arrays.asList(Species.MOUSE, Species.SNAKE, Species.FROG));
                break;
            case FERN:
                energy = 5;
                width = 2;
                height = 2;
                // photosynthesisEfficiency = 0.25;
                // maxProduce = 3;
                // productionRarity = 50;
                // SPRITE_0 = Sprite.FERN_SPRITE_0;
                // SPRITE_1 = Sprite.FERN_SPRITE_1;
                break;
            
            case GRASS:
                energy = 5;
                width = 1;
                height = 1;
                // photosynthesisEfficiency = 0.1;
                // maxProduce = 1;
                // productionRarity = 50;
                // SPRITE_0 = Sprite.GRASS_SPRITE_0;
                // SPRITE_1 = Sprite.GRASS_SPRITE_1;
                break;
            case APPLE_TREE:
                energy = 20;
                width = 3;
                height = 3;
                // photosynthesisEfficiency = 2;
                // maxProduce = 8;
                // productionRarity = 100;
                // SPRITE_0 = Sprite.APPLE_TREE_SPRITE_0;
                // SPRITE_1 = Sprite.APPLE_TREE_SPRITE_1;
                break;
            case BERRY_BUSH:
                energy = 10;
                width = 2;
                height = 1;
                // photosynthesisEfficiency = 2;
                // maxProduce = 12;
                // productionRarity = 50;
                // SPRITE_0 = Sprite.BERRY_BUSH_SPRITE_0;
                // SPRITE_1 = Sprite.BERRY_BUSH_SPRITE_1;
                break;
            case MOSS:
                energy = 3;
                width = 1;
                height = 1;
                // photosynthesisEfficiency = 0.05;
                // maxProduce = 1;
                // productionRarity = 25;
                // SPRITE_0 = Sprite.MOSS_SPRITE_0;
                // SPRITE_1 = Sprite.MOSS_SPRITE_1;
                break;
            case FLOWER:
                energy = 10;
                width = 1;
                height = 1;
                // photosynthesisEfficiency = 1;
                // maxProduce = 2;
                // productionRarity = 50;
                // SPRITE_0 = Sprite.FLOWER_SPRITE_0;
                // SPRITE_1 = Sprite.FLOWER_SPRITE_1;
                break;
            case DRAGONFRUIT_CACTUS:
                energy = 20;
                width = 3;
                height = 4;
                // photosynthesisEfficiency = 1.5;
                // maxProduce = 5;
                // productionRarity = 75;
                // SPRITE_0 = Sprite.DRAGONFRUIT_CACTUS_SPRITE_0;
                // SPRITE_1 = Sprite.DRAGONFRUIT_CACTUS_SPRITE_1;
                break;
            case MOONFLOWER:
                energy = 10;
                width = 2;
                height = 1;
                // photosynthesisEfficiency = 0.75;
                // maxProduce = 6;
                // productionRarity = 75;
                // SPRITE_0 = Sprite.MOONFLOWER_SPRITE_0;
                // SPRITE_1 = Sprite.MOONFLOWER_SPRITE_1;
                break;
            case MAINTREE:
                // health = 1000;
                energy = 0;
                width = 10;
                height = 10;
                // photosynthesisEfficiency = 1;
                // maxProduce = 0;
                // productionRarity = 0;
                // SPRITE_0 = Sprite.APPLE_TREE_SPRITE_1;
                // SPRITE_1 = Sprite.APPLE_TREE_SPRITE_1;
                break; 
        
             default:
                 trophicLevel = 1;
                 energy = 20;
                 //foodCapacity = 30;
                 //rftr = 40;
                 width = 1;
                 height = 1;
                 //speed = 1.5;
                 //thirstCapacity = 10.0;
                 predators = new ArrayList<Species>();
                 prey = new ArrayList<Species>();

        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public ArrayList<Species> getPredators() {
        return predators;
    }
    public ArrayList<Species> getPrey() {
        return prey;
    }
    
}
