import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class HammerBros here.
 * 
 * @author Nicholas Chan
 * @version June 2015
 */
public class HammerBros extends Enemies
{

    private int rotationValue = 2;  //Rate at which HammerBro rotates upon death
    private boolean isThrowing = false;  //Determines if the HammerBro is throwing a hammer
    private boolean throwingLeft = true; //Determines if the HammerBro is throwing left
    private boolean marioDied = false;  //Determines if Mario is dead
    private boolean animationComplete = true;   

    //HammerBro sprites
    private String[] hammerBrosSprites = {"HammerBrosWalking1.png","HammerBrosWalking2.png","HammerBrosWalking3.png","HammerBrosWalking4.png","HammerBrosThrowing1.png","HammerBrosThrowing2.png","HammerBrosThrowing3.png","HammerBrosThrowing4.png","HammerBrosThrowing5.png","HammerBrosThrowing6.png","HammerBrosThrowing7.png","HammerBrosThrowing8.png","HammerBrosThrowing9.png","HammerBrosThrowing10.png"};

    private Hammer hammer;  //Instance of Hammer

    /**
     * Creates an instance of HammerBros
     */
    public HammerBros() 
    {
        this.startSpeed = -2;   //Setting the intial speed of the HammerBro
    }

    /**
     * Act - do whatever the HammerBros wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {     
        //Runs methods if the HammerBro is still alive
        if (this.isAlive == true)
        {
            checkHitItems();     //Checks if the Hammer Bro comes into contact with an item (Ex. shell, fireball)
            checkFall();    //Checks if the HammerBro is falling (if it is in midair, it will fall. If it koopa is not in midair, it does not need to fall)

            //If the HammerBro is not throwing, it moves at its preset speed, otherwise, it stands still
            if (this.isThrowing == false)
            {
                move(this.startSpeed);
            }
            else {
                move(0);
            }

            checkMovement();    //Responsible for the movement animation
            checkLeftWalls();   //Checks for contact with walls to the left
            checkRightWalls();  //Checks for contact with walls to the right

            checkHitMario();    //Checks for contact with an instance of Mario
            checkHitFireBall();

            //If the HammerBro is throwing:
            if (this.isThrowing == true)
            {
                //If the HammerBro is throwing while it is walking (in otherwords, walking animation was interrupted)
                if (this.animationComplete == false)
                {
                    count = 0;  //Count is reset
                    this.animationComplete = true;  //Animation is complete (fixes bugs)
                }     
                //If count is equal to 0
                if (count == 0)
                {    
                    throwingLeft(); //
                    count++;
                }
                else if (count == 2)
                {
                    count = 0;
                }
                else 
                {
                    count++;
                }
            }
        }

        deathAnimation();   //Runs death animation if deathAnimation boolean is true
        checkTouchChasm();
    } 

    public void addedToWorld (World world)
    {
        character = new GreenfootImage [hammerBrosSprites.length];

        for (int i = 0; i < character.length; i++)
        {
            character[i] = new GreenfootImage(hammerBrosSprites[i]);
        } 
    }

    /**
     * Determines which HammerBros sprites to display based on the direction it is moving.
     */
    protected void checkMovement()
    {
        //If the HammerBro is moving and it is not currently throwing:
        if (getSpeed() != 0 && this.isThrowing == false)
        { 
            this.animationComplete = false; //Variable that determines whether or not the animation was run without interruption

            //Setting up delay. If count is equal to zero:
            if (count == 0)
            {              
                //If it is moving left, then run the moving left animation
                if (this.movingLeft == true)
                {
                    animateLeft();
                }
                //Otherwise, run the moving right animation
                else 
                {
                    animateRight();
                }
                this.animationComplete = true;  //Animation is complete
                count++;    //Increment count
            }
            //If count reaches 6, it resets back to zero
            else if (count == 6)
            {
                count = 0;
            }
            //If the aforementioned conditions have not been met, increment the count variable
            else {
                count++;    //Increment count
            }
        }   
    }

    /**
     * Animates the HammerBro to make it appear as if it were walking to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateRight()
    {
        //If you are currently on frame 1, the HammerBro's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            setImage(character[0]); //Sets HammerBro's image to the image with an index of 0
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
            frame = 1;  //Frame is rest to 1
            return; //Exits the method
        }        
        frame++;      //Increment frame
    }

    /**
     * Animates the HammerBro to make it appear as if it were walking to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    protected void animateLeft()
    {
        //If you are currently on frame 1, the HammerBro's image is set to the image located in the character array with an index of 0
        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[0]); //Stores the sprite with an index of 0 in the character array into a temp Greenfoot Image
            temp.mirrorHorizontally();  //Flip the temp image horizontally
            this.setImage (temp); //Set the HammerBro's image as that of the temp image
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
     * Animates the HammerBro to make it appear as it were throwing to the right. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void throwingRight()
    {
        //If frame is equal to 1:
        if(frame == 1)
        {
            setImage(character[4]); //Set the image of the HammerBro to the image stored in index 0 in the array, character
        }
        //Code repeats for the rest of the frames
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
        else if(frame == 5)
        {
            setImage(character[8]);
        }
        else if(frame == 6)
        {
            setImage(character[9]);
        }
        else if(frame == 7)
        {
            setImage(character[10]);
        }
        else if(frame == 8)
        {
            setImage(character[11]);
        }
        else if(frame == 9)
        {
            setImage(character[12]);
        }
        else if(frame == 10)
        {
            setImage(character[13]);

            count = 0;  //Resetting the count variable
            throwHammer("right");   //Throws a hammer to the right (Hammer is actually created)
            frame = 0;  //Resetting frame

            this.isThrowing = false;    //Once it throws the hammer, it is no longer throwing
        }
        frame++;  //Increment frame
    }

    /**
     * Animates the HammerBro to make it appear as it were throwing to the left. Everytime this method is run, the next sprite in the sequence is displayed
     */
    private void throwingLeft()
    {                 
        //If frame is equal to 1:
        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[4]); //Stores the sprite with an index of 4 in the character array into a temp Greenfoot Image
            temp.mirrorHorizontally();  //Flip the temp image horizontally
            this.setImage (temp);   //Set the image of the HammerBro to the temp image
        }
        //Code repeats for the rest of the frames
        else if(frame == 2)
        {
            GreenfootImage temp = new GreenfootImage (character[5]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 3)
        {
            GreenfootImage temp = new GreenfootImage (character[6]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 4)
        {
            GreenfootImage temp = new GreenfootImage (character[7]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 5)
        {
            GreenfootImage temp = new GreenfootImage (character[8]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 6)
        {
            GreenfootImage temp = new GreenfootImage (character[9]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 7)
        {
            GreenfootImage temp = new GreenfootImage (character[10]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 8)
        {
            GreenfootImage temp = new GreenfootImage (character[11]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 9)
        {
            GreenfootImage temp = new GreenfootImage (character[12]);
            temp.mirrorHorizontally();
            this.setImage (temp);
        }
        else if(frame == 10)
        {
            GreenfootImage temp = new GreenfootImage (character[13]);
            temp.mirrorHorizontally();
            this.setImage (temp);

            count = 0;  //Resets count
            throwHammer("left");    //Throws a hammer to the left
            frame = 0;  //Resets frame

            this.isThrowing = false;    //Once it throws the hammer, it is no longer throwing
        }
        frame++;   //Increment frame
    }

    /**
     * Creates an instance of Hammer and sends it in the direction the HammerBro is throwing.
     * 
     * @param direction The direction at which the hammer is being thrown.
     */
    private void throwHammer(String direction)
    {      
        //If direction is "left"
        Greenfoot.playSound("hammerthrow.wav");
        if (direction == "left")
        {
            hammer = new Hammer("left");    //Create a new instance of Hammer that will move left once brought into the world
            getWorld().addObject(hammer, getX() - 10, getY() - 5);  //Add the Hammer to the world
        }
        //If direction is "right"
        else if (direction == "right")
        {
            hammer = new Hammer("right");   //Create a new instance of Hammer that will move right once brought into the world
            getWorld().addObject(hammer, getX() + 10, getY() - 5);   //Add the Hammer to the world
        }    
    }

    private int range=200;
    private int delay=50;
    private void checkHitMario()
    {       
        if (delay%50==0){
            scanMario();

            if (getOneObjectAtOffset(-5, 5, Mario.class) != null || getOneObjectAtOffset(5, 5, Mario.class) != null){
                if (mario.isInvincible()){
                    this.isAlive = false; //die 
                    deathAnimation = true;//squish
                } else{
                    mario.loseALife();
                }
            }
            if (mario.getX()> this.getX() && mario.getX() <= this.getX()+range && this.getSpeed() >0){
                throwHammer("right");
            }

            if (mario.getX()< this.getX() && mario.getX() >= this.getX()-range && this.getSpeed() <0){
                throwHammer("left");
            }

        }
        delay++;
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
     * Determines if the HammerBro comes into contact with an item
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
     * Checks if a fire ball hits the hammerbro.
     */
    private void checkHitFireBall(){
        if (getOneIntersectingObject (FireBall.class)!= null){
            this.isAlive = false; //die 
            this.deathAnimation = true;//kill
        }

    }

    /**
     * Animates the HammerBro to make it appear as if it were "falling" off the screen
     */
    private void deathAnimation()
    {
        //If deathAnimation is true
        if (deathAnimation == true)
        {
            setLocation(getX(), getY() + 5);    //Set koopa's location to 5 pixels lower (vertically) than its original position
            setRotation(rotationValue * count); //Set koopa's rotation to the rotatinValue mutlipied by the count variable
            count++;    //Increment count variable

            //If the HammerBro reaches the edge of the world, remove it from the world
            if (getY() == 399)
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

        HammerBros h = new HammerBros();    //Creates a new instance of HammerBro
        getWorld().addObject(h, getX(), getY());    //Adds a HammeRBro to the world at the location of this current HammerBro
        h.move(-1); //New HammerBro moves to the left
        h.checkFall();  //Check to see if the new HammerBro falls

        //If the HammerBro comes into contact with a wall to its left, or if the new HammerBro falls off the platform then:
        if(leftWall != null || h.checkFalling() == true)
        {
            if (this.getSpeed()>0){
                setLocation (leftWall.getX() +(leftWall.getImage().getWidth()/2) + (getImage().getWidth()/2)+1, getY());
            } else {
                setSpeed(-(getSpeed()));    //Reverse the speed of the current HammerBro
                frame = 1;  //Set frame to 1
                this.movingLeft = false;    //HammerBro's direction is flipped
            }
        }
        getWorld().removeObject(h); //Removes the new HammerBro from the world
    }

    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);

        HammerBros h = new HammerBros();    //Creates a new instance of HammerBro
        getWorld().addObject(h, getX(), getY());    //Adds a HammeRBro to the world at the location of this current HammerBro
        h.move(1); //New HammerBro moves to the left
        h.checkFall();  //Check to see if the new HammerBro falls      

        //If the HammerBro comes into contact with a wall to its right, or if the new HammerBro falls off the platform then:
        if(rightWall != null || h.checkFalling() == true)
        {
            if (this.getSpeed()<0){
                setLocation (rightWall.getX() -(rightWall.getImage().getWidth()/2) - (getImage().getWidth()/2)-1, getY());
            } else {
                setSpeed(-(getSpeed()));    //Reverse the speed of the current HammerBro
                frame = 1;  //Set frame to 1
                this.movingLeft = true; //Reverse current HammerBro's direction
            }
        }
        getWorld().removeObject(h); //Removes the new HammerBro from the world
    }

    /**
     * Sets the speed of the HammerBro
     * 
     * @param speed The speed at which the koopa moves at
     */
    public void setSpeed(int speed)
    {
        this.startSpeed = speed;  //Sets the current speed of the HammerBro
    }

    /**
     * Returns the speed at which the HammerBro is currently moving at
     * 
     * @return int Speed that the HammerBro is moving at.
     */
    public int getSpeed()
    {
        return this.startSpeed; //returns start speed
    }

    /**
     * 
     */
    public void isThrowing()
    {
        this.isThrowing = true;
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

