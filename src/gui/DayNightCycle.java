package gui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utilities.Game;

public class DayNightCycle{
    // private float transparency = 0f; // Alpha value from 0 (transparent) to 1 (opaque)
    //private Color dayColor = new Color(0,0,0,1);
    private ExecutorService executor;
    private double dt = 20;
    private double dayNightCycleDuration = 60; // Duration of a full day-night cycle in seconds
    private double time = 0; // Current time in the cycle (in seconds)
    public static boolean isDay = true;
    public static double dayTimeLevel = 0; // 0 is day, 1 is night

    public DayNightCycle() {
        // setFocusable(false);
        // setOpaque(true);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()){
                    Thread.sleep((long) dt);
                    time += dt/1000;
                    //System.out.println(time);
                    if (time >= dayNightCycleDuration) {
                        time = 0;
                        isDay = !isDay;
                    }
                    if (time <= 4){
                        if (isDay && Game.timeElapsed > 5){

                            setDayTimeLevel(Math.min(0.4 - time/10, 1));
                        } else if (!isDay){
                            setDayTimeLevel(Math.min(time/10, 1));
                        }
                    }
                    
                    
                    
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        
    }
    /**
     * determines the level of daylight. the level should be from 0 to 1, where 0 is daytime (original color) and 1 is nighttime (dark)
     * 
     */
    public void setDayTimeLevel(double level){
        //int transparency = (int)(255 *  Math.min(1, Math.max(0, 0.5 * level)));
        dayTimeLevel = level;
        //repaint();
    }
    
    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     Graphics2D g2d = (Graphics2D) g;
        
    //     //sets the transparency
    //     //setComposite determines how the drawing will be blended with what is below it
    //     //SRC_OVER means the drawing is drawn over the existing stuff below

    //     //g2d.setComposite(AlphaComposite.Clear);
    //     //g2d.fillRect(0, 0, getWidth(), getHeight());

    //     //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
    //     g2d.setColor(dayColor);
    //     g2d.fillRect(0, 0, getWidth(), getHeight());
        
    // }


}
