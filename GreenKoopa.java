import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class GreenKoopa here.
 * 
 * @author Nicholas Chan
 * @version June 2015
 */
public class GreenKoopa extends Enemies
{

    private int rotationValue = 2;  //Rate at which koopa rotates at upon death

    private int delay = 0;  //Delay variable for dealing with lives

    private boolean marioAlive = true;  //Determines if mario is alive
    private boolean shellAnimation = false; //Determines if the shell animation should be run (if mario jumps on the koopa)
    private String[] koopaSprites = {"Koopa3.png", "Koopa4.png", "Koopa5.png", "Koopa6.png", "Shell1.png", "Shell2.png", "Shell3.png", "Shell4.png", "Shell5.png"}; //Loading an array with koopa sprites
    /**
     * Creates an instance of a Green Koopa
     */
    public GreenKoopa() 
    {
        this.startSpeed = -1;   //Setting the initial speed of the koopa
    }

    /**
     * Act - do whatever the GreenKoopa wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    { 
        //Runs methods if the Green Koopa is still alive
        if (this.isAlive == true) 
        {
            checkHitItems();    //Checks if the koopa comes into contact with an item (Ex. shell, fireball)

            checkFall();    //Checks if the koopa is falling (if it is in midair, the koopa will fall. If the koopa is not in midair, it does not need to fall)
            move(this.startSpeed);  //Koopa moves at a preset speed

            checkMovement();    //Responsible for the movement animation. If it is moving left, koopa sprites will make the koopa look like it's moving left, and vice versa for moving towards the right

            checkLeftWalls();   //Checks for contact with walls to the left
            checkRightWalls();  //Checks for contact with walls to the right

            checkHitFireBall();
            //If mario is still alive, check to see if the koopa comes into contact with him
            if (this.marioAlive == true)
            {
                checkHitMario(); 
            }
        }

        shellAnimation();   //Runs shell animation if shellAnimation boolean is true
        deathAnimation();   //Runs death animation if deathAnimation boolean is true
        checkTouchChasm();
    }  

    /**
     * Loads and adds the koopa sprites to the world
     */
    public void addedToWorld (World world)
    {
        character = new GreenfootImage [koopaSprites.length];   //Creates an array, character, with a size equal to the amount of koopa sprites

        //Loading the sprites into the character array
        for (int i = 0; i < character.length; i++)
        {
            character[i] = new GreenfootImage(koopaSprites[i]);
        }      
    }

    /**
     * Animates the koopa to make it appear as if it were walking to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateLeft()
    {
        //If you are currently on frame 1, the koopa's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            setImage(character[0]); //Sets koopa's image to the image with an index of 0
        }
        //Code repeats for the rest of the frames
        else if(frame == 2)
        {
            setImage(character[1]);
        }
        else if(frame == 3)
        {
            setImage(character[2]);
        }
        //If frame 4 is reached:
        else if(frame == 4)
        {
            setImage(character[3]);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;      //Frame is incremented
    }

    /**
     * Animates the koopa to make it appear as if it were walking to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateRight()
    {
        //If you are currently on frame 1, the koopa's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[0]);    //Stores the sprite with an index of 0 in the character array into a temp Greenfoot Image
            temp.mirrorHorizontally();  //Flip the temp image horizontally
            this.setImage (temp);   //Set the koopa's image as that of the temp image
        }
        //Code repeats through the rest of the frames
        else if(frame == 2)
        {
            GreenfootImage temp = new GreenfootImage (character[1]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 3)
        {
            GreenfootImage temp = new GreenfootImage (character[2]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 4)
        {
            GreenfootImage temp = new GreenfootImage (character[3]);
            temp.mirrorHorizontally();
            this.setImage (temp);
            frame = 1;  //Frame is reset to 1
            return; //Exits the method
        }
        frame++;    //Increment frame
    }

    /**
     * Animates the koopa to make it appear as if it were turning into a shell. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void shellAnimation()
    {
        //If the boolean variable shellAnimation is true

        if (shellAnimation == true)
        {

            //If the frame number is 1, set the image of the koopa to that of the image stored in the array, character, with an index of 4
            if(frame == 1)
            {
                setImage(character[4]);                   
            }
            //Code repeats for the remaining frames
            else if(frame == 2)
            {
                setImage(character[5]);
            }
            else if(frame == 3)
            {
                setImage(character[6]);
            }
            else if(frame == 4)
            {
                setImage(character[7]);
            }
            //For frame 5:
            else if(frame == 5)
            {
                setImage(character[8]);                    
                Greenfoot.playSound("enemygetshit.wav");
                getWorld().addObject(new Shell(), getX(), getY());  //Creates a new instance of Shell, and adds it to the world exactly where the koopa is
                getWorld().removeObject(this);  //Removes the koopa from the world
            }
            frame++; //Increments frame
        }
    }

    /**
     * Determines if the koopa comes into contact with Mario. The koopa behaves differently depending on where it comes into contact with him.
     */
    private void checkHitMario()
    {
        scanMario();    //Scans for an instance of mario on the screen

        //If Mario jumps on top of the Koopa:
        if (getOneObjectAtOffset(-5, -25, Mario.class) != null || getOneObjectAtOffset(5, -25, Mario.class) != null)
        {
            if (mario.isInvincible()){
                this.isAlive = false; //die 
                deathAnimation = true;//squish
            } else{
                this.isAlive = false;   //Koopa is no longer "alive", it becomes dormant as a shell
                this.shellAnimation = true;    //Set shellAnimation to true; this will run the shell animation

                setSpeed(0);    //Sets the speed of the Koopa to 0
                frame = 1;  //Frame is set to 1

                //scanMario();
                mario.jump();   //Calls upon the jump() method from the Mario class, which causes Mario to jump
            }
        }
        //If Mario runs into the koopa (from the sides):
        else if (getOneObjectAtOffset(-5, 5, Mario.class) != null || getOneObjectAtOffset(5, 5, Mario.class) != null)
        {  
            //If the delay variable is equal to 0
            if (this.delay == 0)
            {

                if (mario.isInvincible()){
                    this.isAlive = false; //die 
                    deathAnimation = true;//squish
                } else if (mario.getLives() == 1)
                {
                    mario.loseALife();  //Mario loses a life
                    this.marioAlive = false;    //Mario is no longer alive, ran out of lives
                }
                //Otherwise:
                else {
                    mario.loseALife();  //Mario loses a life
                    this.delay++;   //Increment the delay variable
                }

                //If the delay reaches 26, set the delay to -1
            } else if (this.delay == 26)
            {
                this.delay = -1;
            }
            //If the aforementioned conditions are not met, increment delay
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

    /**
     * Determines if the Koopa comes into contact with an item
     */
    private void checkHitItems()
    {
        //If a shell hits the Koopa from the side, the Koopa will be instantly killed and will "fall" off the screen
        if (getOneObjectAtOffset(0, 5, Shell.class) != null && this.isAlive == true)
        {
            Shell shell = (Shell) getOneObjectAtOffset(0, 5, Shell.class);  //Retrieve the shell that the koopa comes into contact with

            //If the shell's speed is not equal to zero, in other words, is moving:
            if (shell.getSpeed() != 0)
            {
                this.isAlive = false;    //Koopa is no longer alive
                this.deathAnimation = true; //deathAnimation is set to true
            }
        }

    }

    /**
     * Checks if a fire ball hits the koopa.
     */
    private void checkHitFireBall(){
        if (getOneIntersectingObject (FireBall.class)!= null){
            this.isAlive = false; //die 
            deathAnimation = true;//kill
        }

    }

    /**
     * Animates the koopa to make it appear as if it were "falling" off the screen
     */
    private void deathAnimation()
    {
        //If deathAnimation is true
        if (deathAnimation == true)
        {           
            setLocation(getX(), getY() + 5);    //Set koopa's location to 5 pixels lower (vertically) than its original position
            setRotation(rotationValue * count); //Set koopa's rotation to the rotatinValue mutlipied by the count variable
            count++;    //Increment count variable

            //If the Koopa reaches the edge of the world, remove it from the world
            if (getY() > getWorld().getHeight())
            {
                Greenfoot.playSound("enemygetshit.wav");
                getWorld().removeObject(this);
            }
        }
    }

    public void checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/-2);
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        GreenKoopa koopa = new GreenKoopa();    //Creates a new instance of GreenKoopa
        getWorld().addObject(koopa, getX(), getY());    //Adds a koopa to the world at the location of this koopa
        koopa.move(-1); //New koopa moves to the left
        koopa.checkFall();  //Check to see if the new koopa falls

        //If the koopa comes into contact with a wall to its left, or if the new koopa falls off the platform then:
        if(leftWall != null || koopa.checkFalling() == true)
        {
            setSpeed(-(getSpeed()));    //Reverse the speed of the current koopa
            frame = 1;  //Set frame to 1
            this.movingLeft = false;    //Koopa is no longer moving left; is moving right instead
        }
        getWorld().removeObject (koopa);    //Remove the new koopa from the world
    }

    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        GreenKoopa koopa = new GreenKoopa();    //Creates a new instance of GreenKoopa
        getWorld().addObject(koopa, getX(), getY());    //Adds a koopa to the world at the location of this koopa
        koopa.move(1);  //New koopa moves to the right
        koopa.checkFall();  //Check to see if the new koopa falls

        //If the koopa comes into contact with a wall to its right, or if the new koopa falls off the platform then:
        if(rightWall != null || koopa.checkFalling() == true)
        {
            setSpeed(-(getSpeed()));    //Reverse the speed of the current koopa
            frame = 1;  //Set frame to 1
            this.movingLeft = true; //Koopa is no longer moving right; is moving left instead
        }
        getWorld().removeObject (koopa);    //Remove the new koopa from the world
    }

    /**
     * Sets the speed of the Koopa
     * 
     * @param speed The speed at which the koopa moves at
     */
    public void setSpeed(int speed)
    {
        this.startSpeed = speed;     //Sets the current speed of the Koopa to the speed indicated in the parameter  
    }

    /**
     * Returns the speed at which the koopa is currently moving at
     * 
     * @return int Speed that the Koopa is moving at.
     */
    public int getSpeed()
    {
        return this.startSpeed; //returns start speed
    }

    public boolean checkFalling()
    {
        if (isAirborn == true)
        {
            return true;
        }
        return false;
    }
}

