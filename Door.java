import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Door here.
 * 
 * @author (Ramy Elbakari) 
 * @version (June 11, 2015)
 */
public class Door extends ScrollObjects
{
    private String [] picNames = {"door1.png","door2.png","door3.png","door4.png", "door5.png","door6.png","door7.png","door8.png"};  // names of images
    private GreenfootImage character [];  // store the image
    private int delay; //delay between animaion
    private int counter;  // frame/image number being shown
    private boolean animation=false;  // incidates animation is undergoing if true

    private boolean pass=false;  // indicates whether mario is allowed to pass, true =pass, false, can't pass

    private int xBarriar;  // x-loation mystical field mario can't enter
    private List actors; // actors who attempt to pass the door
    /**
     * Act - do whatever the Door wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        remove();
        stop();
        animation();
    }    

    /**
     * Stores the images of the animation in an array to be later used for animation.
     */
    public void addedToWorld (World world){

        character = new GreenfootImage [picNames.length]; // array that stores the names
        for (int i=0; i< character.length; i++){
            character [i]= new GreenfootImage (picNames [i]);  // array that stores the images (have the names above)
        }

    }

    /**
     * The actors who are originally after the barriar are allowed to exist after the barriar line.
     */
    public void remove(){

        actors = getWorld().getObjects(Mario.class);
        for (int i =0; i < actors.size();i++){
            Actor actor;
            actor = (Actor) actors.get(i);
            if (actor.getX()> xBarriar){
                actors.remove(i);
            }
        }

    }
    
    /**
     * Performs the animation of the opening of the door.
     */
    public void animation(){
        if (animation ==true){
            delay++;  // increase the delay
            if (delay%3==0){  // image switchs every 3 acts

                setImage (character[counter]);  // switch the frame
                counter++;  // move to the next frame
            }
            if (counter == character.length){  // if reach the last frame, move to the first, stop the animation
                counter =0;
                animation =false;
            }
        }
    }

    /**
     * Do not allow Mario to pass the door
     */
    public void stop (){
        xBarriar= getX()- getImage().getWidth()/2; // the x-barrior is the door's location with graphical extent
        if (actors!= null){

            if (!pass){  // if door is closed

                for (int i =0; i < actors.size();i++){ // loop through all the actors
                    Mario mario;
                    mario = (Mario) actors.get(i);
                    if (mario != null && mario.getX()+2 >= xBarriar){  // if mario ever attemts to reach beyond the door

                        mario.setLocation (xBarriar -5, mario.getY()); // push him 5 pixels away
                    } 

                }
            } 
        }
    }

    /**
     * Open the door and let Mario pass.
     */
    public void open(){
        animation =true;  // perform animation
        pass =true;  // do not restric movement

    }

    /**
     * Checks whether Mario can pass or not
     * @return true- door is open, false- door is closed
     */
    public boolean checkOpen(){
        if (pass ==true) return true;
        return false;
    }

}
