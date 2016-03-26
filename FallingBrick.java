import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;
/**
 * Write a description of class FallingBrick here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FallingBrick extends Block
{
    private boolean startTime=false; // variable to start the time to when to start falling
    private int time; // keeps track of time to when start falling
    private int velocity=2; // vertical velocity of the brick
    private int acceleration =1;  // acceleration of the brick (gravity)
    /**
     * Act - do whatever the FallingBrick wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        checkHit();
        time();

    }    

    Mario mario; // variable to store mario
    /**
     * Checks if Mario stepped on it.
     * If he did, start the time.
     */
    public void checkHit(){
        
        if (getOneIntersectingObject (Mario.class)!=null){
            startTime =true;

        }

    }

    /**
     * Checks to see if it is time to start falling.
     * If it is time, the brick will start falling.
     */
    public void time(){
        if (startTime ==true){
            time++;
            if (time >=50){
                velocity = velocity + acceleration;
                setLocation (getX(), getY() + velocity);
            }
        }
    }
}
