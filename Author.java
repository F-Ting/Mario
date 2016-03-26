import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
/**
 * Just a class that can display whatever text is put in its constructor.
 * was made so multiple text displays cna easily be made.
 * 
 * @author Isaac Chang
 * @version 0.0.1
 */
public class Author extends Actor
{
    private GreenfootImage pic;  // the image of this class
    private String displayText;   // the text that this image will display
    private int fontSize;   // the font of the string
    private String color = "black";  // the color of the string  (can only be white/black)

    /**
     * Creates a text object. The string will be shown in black.
     * 
     * @param displayText   the text that will be displayed
     * @param fontSize      the font size
     */
    public Author(String displayText, int fontSize)
    {
        // store the string and the font the user would like to show
        this.displayText = displayText;
        this.fontSize = fontSize;
        // import the above aspects into a greenfoot image -default color is black
        pic = new GreenfootImage(displayText,fontSize,Color.BLACK,new Color(0,0,0,0));        
        
        // set this greenfoot image as the actor
        setImage(pic);  
                
    }
    
     /**
     * Creates a text object where the color of the text can be specified.
     * type as a string, "white" for white text
     * "black", for black text
     * 
     * @param displayText   the text that will be displayed
     * @param fontSize      the font size
     * @param color         the color of the text
     */
    public Author(String displayText, int fontSize, String color)
    {
        // store the color, string and font the user would like to display
        this.color = color;
        this.displayText = displayText;
        this.fontSize = fontSize;
        
        if (color.equals("black"))  // if the color is black, then
        {
            pic = new GreenfootImage(displayText,fontSize,Color.BLACK,new Color(0,0,0,0)); // set the greenfoot image to display the text in black, also sents the font size
        }
        else if (color.equals("white"))  // if the color is black, then
        {
            pic = new GreenfootImage(displayText,fontSize,Color.WHITE,new Color(0,0,0,0)); // set the greenfoot image to display the text in white, also sents the font size
        }
               
        pic.setFont(new Font("Apple Chancery", Font.PLAIN,10)); // set the font of the picture

        setImage(pic);  // set this greenfoot image as the actor
                
    }
    
    public void update(String text){
    setImage (new GreenfootImage (text, 20, Color.BLACK, null));
    }
    
    
}
