import greenfoot.*;

/**
 * Write a description of class Collectables here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
abstract class Collectables extends ScrollObjects
{
    /**
     * Act - do whatever the Collectables wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    abstract void checkHit();
}
