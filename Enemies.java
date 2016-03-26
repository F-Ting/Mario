import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemies here.
 * 
 * @author (Nicholos Chand and Hansen Ngai) 
 * @version (June 10, 2015)
 */
abstract class Enemies extends ScrollObjects
{
    public GreenfootImage [] character ;
    protected boolean isAirborn = false;  //Determines if HammerBro is in the air
    protected int vSpeed = 0; //Vertical speed of the HammerBro
    protected int acceleration = 1;   //Rate at which HammerBro accelerates downwards per act (when falling)
    
    protected int startSpeed;     //Starting speed of the koopa
    protected int frame = 1;  //Animation frame number
    protected boolean movingLeft = true;  //Determmines if the HammerBro is moving left
    protected boolean deathAnimation = false; //Determines whether or not to run the death animation
    protected int count =0; //Count variable to "delay" movement animation
    protected Mario mario;    //Creating an instance of Mario
    protected boolean isAlive = true; //Determines if koopa is alive
     /**
     * Ramy, this method right here, has to be in the enemies super class 
     * it also must be put in the act class of each enemy individually at the 
     * END OF THE ACT method, NOT THE BEGINNING, I have written it already so it should work right now 
     */
    protected void checkTouchChasm()
    {
        if (this.getWorld() != null)
        {
            if (this.isTouching(Chasm.class))
            {
                getWorld().removeObject(this);
            }
        }
    }
    
    protected void checkFall()
    {
        if(onGround())
        {
            vSpeed = 0;
        }
        else
        {
            fall();
        }
    }

    protected boolean onGround()
    {
        int spriteHeight = getImage().getHeight();
        int yDistance = (int)(spriteHeight/2) + 5;
        Actor ground = getOneObjectAtOffset(0, getImage().getHeight()/2, Block.class);
        if(ground == null)
        {
            this.isAirborn = true;
            return false;
        }
        else
        {
            moveToGround(ground);
            return true;
        }
    }

    protected void moveToGround(Actor ground)
    {
        int groundHeight = ground.getImage().getHeight();
        int newY = ground.getY() - (groundHeight + getImage().getHeight())/2;
        setLocation(getX(), newY);
        this.isAirborn = false;
    }

    protected void fall()
    {
        setLocation(getX(), getY() + vSpeed);
        if(vSpeed <= 9)
        {
            vSpeed = vSpeed + acceleration;
        }
        this.isAirborn = true;
    }
    
    /**
     * Determines which koopa sprites to display based on the direction it is moving.
     */
    protected void checkMovement()
    {
        //Setting up a delay

        //If the count variable is equal to zero then animate the koopa
        if (count == 0)
        {
            //If the koopa is moving left, then run the moving left animation
            if (this.movingLeft == true)
            {
                animateLeft();
            }
            //Otherwise, run the moving right animation
            else 
            {
                animateRight();
            }
            count++;    //Increment the count variable
        }
        //If count reaches 6, reset its value to 0
        else if (count == 6)
        {
            count = 0;
        }
        //If the aforementioned conditions have not been met, increment the count variable
        else {
            count++;
        }
    }
    protected void animateLeft(){}
    protected void animateRight(){}

}
