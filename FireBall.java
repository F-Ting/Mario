import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FireBall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FireBall extends Actor
{
    private boolean jumping=false;
    private int vSpeed = 0;
    private int acceleration = 1;    
    private int jumpStrength = 6;
    private boolean direction; //true is right, false is left
    private int xSpeed = 4;

    private String [] picNames = {"fire1.png","fire2.png","fire3.png","fire4.png"};
    private GreenfootImage character [];
    private boolean animation=true;
    private int delay;
    private int counter;

    public FireBall(boolean direction)
    {
        this.direction = direction;
    }

    public void addedToWorld (World world){
        character = new GreenfootImage [picNames.length];
        Greenfoot.playSound("fireball.wav");
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);
        }
    }       

    public void animation(){
        if (animation ==true){
            delay++;
            if (delay%8==0){

                if (direction == true)
                {
                    setImage (character[counter]);
                }                
                else
                {
                    GreenfootImage temp= new GreenfootImage (character[counter]);
                    temp.mirrorHorizontally();
                    setImage (temp);
                }
                counter++;
            }
            if (counter == character.length){
                counter =0;
            }
        }
    }

    /**
     * Act - do whatever the FireBall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        checkFall();
        if (direction == true)
        {
            move(xSpeed);
        } 
        else if (direction == false)
        {
            move(-xSpeed);
        }
        if (onGround() == true)
        {
            jump();
        }       

        animation();   
        checkRightWalls();
        checkLeftWalls();
    }    

    public void jump()
    {
        vSpeed = vSpeed - jumpStrength;
        jumping = true;
        fall();
    }

    public void fall()
    {
        setLocation(getX(), getY() + vSpeed);
        if(vSpeed <=9)
        {
            vSpeed = vSpeed + acceleration;
        }
        jumping = true;
    }

    public void checkFall()
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

    public boolean onGround()
    {
        int spriteHeight = getImage().getHeight();
        int yDistance = (int)(spriteHeight/2) + 5;
        Actor ground = getOneObjectAtOffset(0, getImage().getHeight()/2, Block.class);
        if(ground == null)
        {
            jumping = true;
            return false;
        }
        else
        {
            moveToGround(ground);
            return true;
        }
    }

    public void moveToGround(Actor ground)
    {
        int groundHeight = ground.getImage().getHeight();
        int newY = ground.getY() - (groundHeight + getImage().getHeight())/2;
        setLocation(getX(), newY);
        jumping = false;
    }

    public void checkRightWalls()
    {        
        int spriteWidth = getImage().getWidth();
        int xDistance = (int)(spriteWidth/2);
        Actor rightWall = getOneObjectAtOffset(xDistance, 0, Block.class);
        if(rightWall == null)
        {

        }
        else
        {
            getWorld().removeObject (this);

        }
    }

    public void checkLeftWalls()
    {
        if (getWorld()!=null)
        {
            int spriteWidth = getImage().getWidth();
            int xDistance = (int)(spriteWidth/-2);
            Actor leftWall = getOneObjectAtOffset(xDistance, 0, Block.class);
            if(leftWall == null)
            {

            }
            else
            {
                getWorld().removeObject (this);

            }
        }
    }
}
