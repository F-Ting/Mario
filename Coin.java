import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class coinBox here.
 * 
 * @author (Ramy Elbakari) 
 * @version (June 11, 2015)
 */
public class Coin extends Collectables
{
    private String [] picNames = {"coin1.png","coin2.png","coin3.png","coin4.png"};
    private GreenfootImage character [];
    private int delay;
    private int counter;
    private boolean animation=true;
    private int time;
    private boolean beginTime =false;

    private static int coinNumber=0;
    public Coin(){}

    public Coin (boolean beginTime){
        this.beginTime= beginTime;
    }

    /**
     * Act - do whatever the coinBox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        checkHit();
        animation();

    }   

    public void addedToWorld (World world){

        character = new GreenfootImage [picNames.length];
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);
        }

    }

    /**
     * Performs the animation of the coin
     */
    public void animation(){
        time ++;
        if (animation ==true){
            delay++;
            if (delay%8==0){  // each frame is changed every 8 acts

                setImage (character[counter]);
                counter++;
            }
            if (counter == character.length){
                counter =0;
            }
        }
        if (time % 50==0 && this.beginTime==true){
            coinNumber ++;
            getWorld().removeObject (this);
        }
    }

    Mario mario;
    /**
     * Check if Mario collected the coin
     */
    protected void checkHit(){
        mario = (Mario) getOneObjectAtOffset (0,0, Mario.class);

        if (mario != null){
            Greenfoot.playSound("coin.wav");
            animation =false; // stop animation
            coinNumber ++; // increase coin numbers
            getWorld().removeObject (this); // remove the coin from the world
        }
    }

    public static void increaseCoinNumber(){
        coinNumber ++;
    }

    public static int getCoinNumber(){
        return coinNumber;

    }

    public static void setCoinNumber(int newNumber){
        coinNumber=newNumber;

    }
}
