import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class Chasm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Chasm extends Actor
{
    private GreenfootImage chasm;
    private int sizeCount = 0;       
    private Mario target;
    private boolean grown = false;

    public Chasm(Mario target)
    {
        this.target = target;
        getImage().setTransparency(0);        
    }

    private void followActor()
    {
        setLocation(target.getX(), target.getY() - 110);
    }

    /**
     * Act - do whatever the Chasm wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        turn(5);
        followActor();
        swirlAll();
        grow();            
        if(getImage().getTransparency() < 255)
        {
            getImage().setTransparency(getImage().getTransparency()+3);
        }
    }

    public void removeChasm()
    {
        World backworld = (World) getWorld();
        List<Enemies> ActorList = this.getObjectsInRange(300,Enemies.class);
        for (Enemies AL: ActorList)
        {
            if (AL != null)
            {
                AL.getWorld().removeObject(AL);
            }             
        }
        getWorld().removeObject(this);
    }

    /**
     * here in the grow method Ramy, I have removed the section where it scales the image
     * compare it to your previous code to see, the code below is working 
     */
    private void grow()
    {
        if (this.isTouching(Enemies.class))
        {            
            sizeCount++;
        }        
    }

    private void swirlAll()
    {
        World backworld = (World) getWorld();
        List<Enemies> ActorList = this.getObjectsInRange(300, Enemies.class);
        for (Enemies AL: ActorList)
        {
            if (AL != null)
            {                
                AL.turnTowards(this.getX(),this.getY());
                AL.move(7);
                AL.getImage().scale(150,7);                
            }
        }
    }
}
