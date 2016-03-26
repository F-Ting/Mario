import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Hammer here.
 * 
 * @author Nicholas Chan
 * @version June 2015
 */
public class Hammer extends ScrollObjects
{
    private String direction;   //Direction at which the hammer is moving
    private Mario mario;    //Creating an instance of Mario

    private int startSpeed;     //Initial speed of the hammer
    private int rotationValue;  //Rate at which the hammer rotates
    private int count = 0;  //Count variable used for the rotation aspect of the hammer

    private int delay = 0;  //Delay variable used for managing Mario's lives

    private boolean hitObject = false;  //Determines if the Hammer has hit an object (ie. Mario, wall)
    private boolean marioAlive = true;  //Determines if Mario is alive

    /**
     * Creates an instance of Hammer
     * 
     * @param direction Direction at which the hammer will be moving at
     */
    public Hammer(String direction)
    {
        this.direction = direction; //Assigning the hammer a direction based on the parameter

        //If the direction is to the left
        if (this.direction == "left")
        {
            this.startSpeed = -6;   //Start speed will be 6 pixels to the left per act
            this.rotationValue = -20;   //Will rotate counter clockwise
        }
        //If the direction is to the right
        else if (this.direction == "right")
        {
            this.startSpeed = 6;    //Start speed will be 6 pixels to the right per act
            this.rotationValue = 20;    //Will rotate clockwise
        }       
    }

    /**
     * Act - do whatever the Hammer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        //If the hammer doesn't hit an object and if Mario is still alive
        if (this.hitObject == false && this.marioAlive == true)
        {
            setLocation(getX() + this.startSpeed, getY());  //Move the hammer to the left or right, depending on its associated direction
            setRotation(rotationValue * count); //Rotate the hammer in its correct direction
            count++;    //Increment the count variable

            checkLeftWalls();   //Check for contact with walls to the left
            checkRightWalls();  //Check for contact with walls to the right

            checkHitMario();    //Check for contact with Mario
        }
        else {
            getWorld().removeObject(this); //If Mario is no longer alive or if the hammer has hit an object, then remove the hammer
        }
    }  

    private void checkHitMario()
    {
        scanMario(); //Scans for an instance of Mario on the screen

        //crashes when mario dies, other enemies trying to detect mario, but he is removed from the world
        //too many enemies on screen to indicate that mario died

        //If the hammer hits Mario
        if (getOneObjectAtOffset(-5, 5, Mario.class) != null || getOneObjectAtOffset(5, 5, Mario.class) != null)
        {
            //Setting up a delay

            //If delay is equal to zero
            if (this.delay == 0)
            {
                // And if Mario's total lives is equal to 1
                if (mario.isInvincible()){
                } else if (mario.getLives() == 1)
                {
                    mario.loseALife();  //Mario loses a life
                    this.marioAlive = false;    //Mario dies
                }
                //Otherwise, if his total lives is above 1
                else 
                {
                    mario.loseALife();      //Mario loses a life
                    this.delay++;   //Delay is incremented
                }
            }
            //If delay is equal to 4
            else if (this.delay == 4)
            {
                this.delay = 0; //Set delay to zero
            }
            //If the aforementioned conditons are not met, increment delay
            else
            {
                this.delay++;
            }
        }    
    }

    /**
     * Scans for instances of Mario on the screen and adds it to a List of Marios. Mario can then be identified as there is only one instance of mario on the screen at all times
     */
    private void scanMario()
    {
        List marios = getWorld().getObjects(Mario.class);   //Loads a List with all instances of the Mario class
        mario = (Mario) marios.get(0);  //The private object mario becomes the mario located at an index of 0 in the List
    }

    public void checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/-2);
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        //If hammer hits a wall to its left, hitObject becomes true
        if(leftWall != null)
        {
            this.hitObject = true;
        }
    }

    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        //If hammer hits a wall to its right, hitObject becomes true
        if(rightWall != null)
        {
            this.hitObject = true;
        }
    }
}
