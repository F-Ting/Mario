import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Goomba is based off the Goomba in the original Mario game. It walks around and changes direction after hitting a wall or reaching the edge of a platform. If Mario hits
 * the Goomba from the side, Mario will lose a life. However if he jumps on the Goomba, it will squish and then die. 
 * 
 * @author Hansen Ngai
 * @version June 2015
 */
public class Goomba extends Enemies
{

    private int count2 = 0; //delay counter 2
    private boolean squishAnimation=false; //squish animation toggle
    private String[] goombaSprites = {"Goomba1.png", "Goomba2.png", "Goomba3.png", "Goomba4.png", "Goomba5.png", "Goomba6.png", "Goomba7.png", "Goomba8.png"}; //sprites
    /**
     * Creates a Goomba.
     */
    public Goomba() 
    {
        this.startSpeed = -1; //speed

    }

    /**
     * Act - do whatever the Goomba wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (this.isAlive == true)
        {
            checkHitItems(); //check hit for items

            checkFall(); 
            move(this.startSpeed); //move

            checkMovement(); 
            checkLeftWalls(); //check sides
            checkRightWalls();  //check sides

            checkHitMario(); //check hit for mario
            checkHitFireBall(); // check if fire ball hit it

        }
        squishAnimation(); //squish if not alive
        checkTouchChasm();
    }  

    /**
     * Sets up the character array for sprites. 
     * 
     * @param world The world operated in.
     */
    public void addedToWorld (World world)
    {
        character = new GreenfootImage [goombaSprites.length]; //array for sprites

        for (int i = 0; i < character.length; i++)
        {
            character[i] = new GreenfootImage(goombaSprites[i]);
        }      
    }

    /**
     * Animates the Goomba to move left. Switches frames in the array, with a delay. 
     */
    protected void animateLeft()
    {

        if(frame == 1)
        {
            setImage(character[0]); //load image in array
        }
        else if(frame == 2)
        {
            setImage(character[1]);//load image in array
        }
        else if(frame == 3)
        {
            setImage(character[2]);//load image in array
        }
        else if(frame == 4)
        {
            setImage(character[3]);//load image in array
            frame = 1;
            return;
        }
        frame++;      

    }

    /**
     * Animates the Goomba to move left. Switches frames in the array, with a delay.
     */
    protected void animateRight()
    {

        if(frame == 1)
        {
            GreenfootImage temp = new GreenfootImage (character[0]); //load image in array
            temp.mirrorHorizontally(); //flip
            this.setImage (temp); //set new flipped pic
        }
        else if(frame == 2)
        {
            GreenfootImage temp = new GreenfootImage (character[1]);//load image in array
            temp.mirrorHorizontally();//flip
            this.setImage (temp);//set new flipped pic
        }
        else if(frame == 3)
        {
            GreenfootImage temp = new GreenfootImage (character[2]);//load image in array
            temp.mirrorHorizontally();//flip
            this.setImage (temp);//set new flipped pic
        }
        else if(frame == 4)
        {
            GreenfootImage temp = new GreenfootImage (character[3]);//load image in array
            temp.mirrorHorizontally();//flip
            this.setImage (temp);//set new flipped pic
            frame = 1;
            return;
        }
        frame++;

    }

    /**
     * Animation for squishing. Switches between frames in the array, with a delay.
     */
    private void squishAnimation()
    {

        if (squishAnimation == true){
            if (count2 == 0){
                if(frame == 1)
                {
                    setImage(character[4]); //load image in array                  
                }
                else if(frame == 2)
                {
                    setImage(character[5]);//load image in array
                }
                else if(frame == 3)
                {
                    setImage(character[6]);//load image in array
                }
                else if(frame == 4)
                {
                    setImage(character[7]);//load image in array
                    Greenfoot.playSound("enemygetshit.wav");
                    getWorld().removeObject(this); //Goomba dies
                }

                frame++; 
            }

            else if (count2 == 10){
                count2 = 0; //reset
            } else {
                count2++;
            }
        }
    }

    /**
     * Checks interaction with Mario. Acts different if interacting from side or from top.
     */
    private void checkHitMario()
    {
        if (getOneObjectAtOffset(-5, 5, Mario.class) != null || getOneObjectAtOffset(5, 5, Mario.class) != null) //side hit
        {  
            scanMario();
            if (mario.isInvincible()){
                this.isAlive = false; //die 
                squishAnimation = true;//squish
            } else{
                mario.loseALife();
            }
        }
        else if (getOneObjectAtOffset(-5, -25, Mario.class) != null || getOneObjectAtOffset(5, -25, Mario.class) != null) //top hit
        {
            this.isAlive = false; //die 
            squishAnimation = true;//squish

            setSpeed(0);//stop moving
            frame = 1;

            scanMario();
            mario.jump();         
        }            

    }

    /**
     * Checks if a fire ball hits the goomba.
     */
    private void checkHitFireBall(){
        if (getOneIntersectingObject (FireBall.class)!= null){
            this.isAlive = false; //die 
            squishAnimation = true;//squish
        }

    }

    /**
     * Scans for Mario. 
     */
    private void scanMario(){
        List marios= getWorld().getObjects(Mario.class);
        mario = (Mario) marios.get(0);
    }

    /**
     * Checks to see if interacted with items such as a Koopa shell.
     */
    private void checkHitItems()
    {
        if (getOneObjectAtOffset(0, 5, Shell.class) != null)
        {
            Shell shell= (Shell) getOneObjectAtOffset(0, 5, Shell.class);
            if (shell.getSpeed() != 0)
            {
                this.isAlive = false; //die
                squishAnimation = true; //squish
            }
        }
    }

    /**
     * Checks to see if Goomba is in freefall.
     */
    public void checkFall()
    {
        if(onGround())
        {
            vSpeed = 0; //can't fall while on ground
        }
        else
        {
            fall();
        }
    }

    /**
     * Checks for walls on the left side.
     */
    public void checkLeftWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/-2);
        Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);//wall on left

        if(leftWall != null)
        {
            if (this.getSpeed()>0){
                //setLocation (leftWall.getX() +(leftWall.getImage().getWidth()/2) + (getImage().getWidth()/2)+1, getY());
            } else {
                setSpeed(-(getSpeed())); // opposite direction
                frame = 1;
                this.movingLeft = false;  
            }
        }

    }

    /**
     * Checks for walls on the right side.
     */
    public void checkRightWalls()
    {
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);//right wall

        if(rightWall != null )
        {
            if (this.getSpeed()<0){
                //setLocation (rightWall.getX() -(rightWall.getImage().getWidth()/2) - (getImage().getWidth()/2)-1, getY());
            } else {
                setSpeed(-(getSpeed())); //opposite direction
                frame = 1;
                this.movingLeft = true;
            }
        }

    }

    /**
     * Sets the speed. Mutator. 
     * 
     * @param speed The speed Goomba has.
     */
    private void setSpeed(int speed)
    {
        this.startSpeed = speed; 
    }

    /**
     * Accessor for speed.
     * 
     * @return int The speed of Goomba.
     */
    private int getSpeed()
    {
        return this.startSpeed;
    }

    /**
     * Checks to see if Goomba is falling.
     * @return boolean True if it is airborne, otherwise false.
     */
    public boolean checkFalling(){
        if (isAirborn ==true) return true;
        return false;

    }
}
