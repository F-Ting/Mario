import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class coinBox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class coinBox extends Block
{
    private String [] picNames = {"coinBlock1.png","coinBlock2.png","coinBlock3.png","coinBlock4.png"};
    private GreenfootImage character [];
    private int delay;
    private int counter;
    private boolean addCoin =true;
    private boolean animation =true;
    private boolean resize;

    public coinBox(boolean resize)
    {
        this.resize = resize;
    }

    /**
     * Act - do whatever the coinBox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        animation();
        checkHit();
    }   

    public void addedToWorld (World world){

        character = new GreenfootImage [picNames.length];
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);
        }
        if(resize)
        {
            for (GreenfootImage s : character)
                s.scale(20,20);
        }

    }

    public void animation(){
        if (animation ==true){
            delay++;
            if (delay%12==0){

                setImage (character[counter]);
                counter++;
            }
            if (counter == character.length){
                counter =0;
            }
        }

    }

    Mario mario;
    public void checkHit(){
        mario = (Mario) getOneObjectAtOffset (0, getImage().getHeight()/2, Mario.class);
        if (mario != null && addCoin ==true){
            Greenfoot.playSound("coin.wav");
            getWorld().addObject (new Coin (true), getX(), getY()-getImage().getHeight()-5);
            GreenfootImage block = new GreenfootImage ("coinBox2.png");
            if (resize ==true){
                block.scale(20,20);
            }
            this.setImage (block);
            addCoin =false;
            animation =false;
        }
    }
}
