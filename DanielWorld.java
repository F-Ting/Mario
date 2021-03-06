// Imports
import greenfoot.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * This is the main World where the maps of the games are created, there is however 2 ways of generating the map. One uses a 2-D array to load a text file and generate a map, this method breaks the world into 20 by 20 blocks
 * and inserting actors into the location based on their location within the textfile, this method allows a more visual based way of creating the map just by opening a textfile and type in symbols that represent different
 * objects and putting them in the order of how you want the map to be created. The downside to this method is that everything will be sized to be 20 by 20 or a multiple of 20 in either width or length, this limits the 
 * ability to have smaller actors as well as making unique placement of objects. The other method of creating the world is using 3 lines in a text document to store the name, x and y coordinate of the object, so the scanner would read
 * 3 lines of the text document and adds an object int othe world, by using this method, a save method must be created to save the objects that is within the world onto the textfile again. This allows the ability to save the game
 * and coming back to it later.
 * 
 * @author Daniel Chen & Ramy & Isaac (music and sound)
 * @version 1.2
 * -1.0 loadMap Method for loading a map from a text file and put it into a 2d array
 * -1.1 generateMap method for using the 2d array from load map method to create the map by adding the actors to the world
 * -1.2 Saves the map by detecting every location of array
 * -1.3 Method in 1.2 update removed, added in methods to find the 3 boundary blocks to form untimate boundaries that the actors cannot pass and 1 boundary the actor dies in.
 * -1.4 Merged with Ramy's code with his ways of generating the map and saving it, Along with 2 of Ramy's World subclasses
 */
public class DanielWorld extends World
{
    //size of each array
    private final int widthOfEachArray = 20;
    private final int heightOfEachArray = 20;

    private int maxMapWidth;
    private int maxMapHeight;

    protected boolean scrolling;
    protected int currLevel;

    private Scanner scan;
    private String [][] map;
    private String [] savedMap = new String[10];
    private CheckMap checkMap = new CheckMap();
    private CheckMap checkMO = new CheckMap();

    //Objects used to stop scrolling at the 4 edge of entire map
    private Block up;
    private Block down;
    private Block left;
    private Block right;

    //Different BackGrounds for each Level put in order from 1 - 7, but if you don't need then take out or switch around for preference
    private String [] backGround = {"Outside1.jpg","Outside2.jpg" ,"Outside4.png","Outside3.png","Outside5.png","Black.jpg","Outside6.png"};
    private GreenfootImage [] cameraView;

    //for music
    GreenfootSound music = new GreenfootSound("themeMusic.mp3");
    protected boolean canPlay = true;

    /**
     * Creates the world starting from Start Screen.
     */
    public DanielWorld()
    {
        super(800, 460, 1,false); 
        generateMap(loadMap("StartGame"));         //Generate the start game
        currLevel = 0;                          // set current level to level 0
        setPaintOrder(Enemies.class);           // prioritize the pain order of enemies over other actors
        scrolling = true;                        // the world is able to scroll
        GreenfootImage bg = new GreenfootImage(backGround[currLevel]);      // select a background based on the current level
        bg.scale(getWidth(),getHeight());                                       // scale the background to the size of the world
        setBackground(bg);                                                      // set the background

        Title title = new Title();
        addObject(title,getWidth()/2,(getHeight()/2)-75);
    }

    protected Author a = new Author ("Coins: ", 20);
    /**
     * Creates the world with the level inputted
     * @param level The level selected
     */
    public DanielWorld(int level)
    {
        super(800, 450, 1,false); 
        setPaintOrder(Enemies.class);
        this.currLevel = level;
        scrolling = true;
        GreenfootImage bg = new GreenfootImage(backGround[currLevel - 1]);
        bg.scale(getWidth(),getHeight());
        setBackground(bg);
        addObject (a, getWidth()-100, 100);
        if(level %2 != 0)  // if the world is generated by a 2-D array, which are the odd levels
        {
            generateMap(loadMap("level" + Integer.toString(level))); // generate the level useing the method that reads the textfile and loads it into a 2-D array then turns it into a map
        }

        Greenfoot.playSound("enternewlevel.wav");
    }

    public  void act(){
        int coinNumber =Coin.getCoinNumber();
        a.update ("Coins: " + String.valueOf(coinNumber));
        if (canPlay == true)
        {
            music.playLoop();  // play music
        }
    }

    /**
     * @param Level the level from a textfile that your want to load
     * @param scroll true if you want the world to scroll
     * 
     */
    public DanielWorld(int width, int height, String Level, boolean scroll)
    {
        super(width, height, 1,false);
        generateMap(loadMap(Level));
        scrolling = scroll;

    }

    /**
     * This method moves the screen around the world by moving all scrolling actors around
     * 
     * @param x The amount horizontal pixels you want to move the screen
     * @param y The amount vertical pixels you want to move the screen
     * 
     */
    public void cameraMove(int x, int y)
    {
        if(scrolling)  // is the world was meant to scroll
        {
            List<ScrollObjects> S = getObjects(ScrollObjects.class); // get all the scrolling objects in a list
            for (ScrollObjects s : S)
            {
                s.setLocation(s.getX() + x,s.getY() + y); // moves the actors in the opposite direction as the player is moving to create a scroll effect
            }
        }
    }

    /**
     * This method reads a text file that is designed to create a map and load the content in the text file into a 2-D array
     * 
     * @param fileName Name of the text file to be loaded
     */
    protected String [][] loadMap(String fileName)
    {
        // Initialize objects
        ArrayList<String> textFileContents = new ArrayList<String>();
        int lines = 0;
        try {
            scan = new Scanner (new File (fileName + ".txt"));
            // Make use of two interesting new methods found on the Scanner API
            while (scan.hasNext())
            {
                // Use the ArrayList's add() method and the Scanner's nextLine() method
                textFileContents.add(scan.nextLine()); 
                lines ++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch (IOException e)
        {
            System.out.println("Error reading data");
        }
        finally
        {
            if(scan != null)
                scan.close();
        }

        for (String line : textFileContents)
            if (line.length() > maxMapWidth)
                maxMapWidth = line.length(); // to determine the maximum width of the level to prevent index out of bound exception
        map = new String[lines][maxMapWidth]; // generates the 2-D array
        // chops the world into 20 by 20 squares
        maxMapWidth *= 20;
        maxMapHeight = lines*20;
        int i = 0;
        for (String line : textFileContents)    //for each element in textFileContents arraylist
        {
            for (int j = 0; j < line.length(); j++)
            {
                map[i][j] = line.substring(j,j+1);  //Break the row of string into individual element in an array.
            }
            i++;
        }
        return map;
    }

    /**
     * This method uses the map created from the loadMap method and adds the Object into the world based on each element in the 2-D array
     * 
     * @param map The 2-D array of the map
     */
    protected void generateMap (String [][] map)
    {
        int actorWidth;
        int actorHeight;
        for (int i = 0; i < map.length;i++) // Labling this loop as the outer loop
        {
            int x = 0;
            for (int j = 0; j < map[i].length;j++)
            {
                // each index of the string represents an actor to be added to the world
                int kind = "M456789BCcTQWtNGbjnswSIi".indexOf(""+map[i][j]);
                if (kind < 0) 
                    continue;
                Actor actor = null;
                if(kind == 0)
                    actor = new Mario();
                if(kind == 1)
                    actor = new leftSideStriped();
                if(kind == 2)
                    actor = new centreMiddle();
                if(kind == 3)
                    actor = new rightSideStriped();
                if(kind == 4)
                    actor = new leftTopStriped();
                if(kind == 5)
                    actor = new centreTop();
                if(kind == 6)
                    actor = new rightTopStriped();
                if(kind == 7)
                    actor = new BigBlock();
                if(kind == 8)
                    actor = new coinBox(true);
                if(kind == 9)
                    actor = new Coin();
                if(kind == 10)
                    actor = new Teleporter();
                if(kind == 11)
                    actor = new Block1();
                if(kind == 12)
                    actor = new Base2();
                if(kind == 13)
                    actor = new Tree();
                if(kind == 14)
                    actor = new Transporter();
                if(kind == 15)
                    actor = new GreenKoopa();
                if(kind == 16)
                    actor = new Brick();
                if(kind == 17)
                    actor = new Jumper();
                if(kind == 18)
                    actor = new MiniWorld();
                if(kind == 19)
                    actor = new Stand();
                if(kind == 20)
                    actor = new Spikes();
                if(kind == 21)
                    actor = new StartGame();
                if(kind == 22)
                    actor = new Instructions();
                if(kind == 23)
                    actor = new Instruction();

                // gets Actor Width and Height
                actorWidth = actor.getImage().getWidth();
                actorHeight = actor.getImage().getHeight();

                // round the width down to the closest amount of array width needed
                int w = 1;
                for (; w*20 < actorWidth; w++){}
                if (w > 1)
                    w--;
                int h = 1;
                for (; h*20 < actorHeight; h++){}
                if (h > 1)
                    h--;

                // scales the actor to the amount of array needed to fit
                if(kind != 0 && kind != 14 && kind != 17 && kind != 18)
                    actor.getImage().scale(w*20,h*20);

                if (w > 1)
                {
                    if (w % 2 == 0)
                    {
                        // adds the actor to the designed spot for that particular center array.
                        addObject(actor,  widthOfEachArray + widthOfEachArray*j, heightOfEachArray + heightOfEachArray*i);
                    }
                    else
                    {
                        addObject(actor,  widthOfEachArray/2 + widthOfEachArray*j, heightOfEachArray/2 + heightOfEachArray*i);
                    }
                }
                else 
                {
                    addObject(actor,  widthOfEachArray/2 + widthOfEachArray*j, heightOfEachArray/2 + heightOfEachArray*i);
                }
            }
        }
        this.map = map;        
        // stores the 4 extrem block into the world to form untimate boundaries that cannot be passed when the actor reaches the end of the world
        this.left = leftMostObject();
        this.right = rightMostObject();
        this.up = upperMostObject();
        this.down = bottomMostObject();
    }

    /**
     * get the one object with the lowest x coordinate
     */
    private Block leftMostObject()
    {
        List<Block> S = getObjects(Block.class);
        int x = getWidth()/2;
        Block a = null;
        for (Block s : S)
        {
            // find the block with the lowest block value among all blocks in the world
            if(s.getX() < x)
            {
                x = s.getX();
                a = s;
            }
        }
        if(a != null)
            return a;
        return null;
    }

    /**
     * get the one object with the lowest y coordinate
     */
    private Block upperMostObject()
    {
        List<Block> S = getObjects(Block.class);
        int y = getHeight()/2;
        Block a = null;
        for (Block s : S)
        {
            // find the block with the lowest y value among all blocks in the world
            if(s.getY() < y)
            {
                y = s.getY();
                a = s;
            }
        }
        if(a != null)
            return a;
        return null;
    }

    /**
     * get the one object with the highest y coordinate
     */
    private Block bottomMostObject()
    {
        List<Block> S = getObjects(Block.class);
        int y = getHeight()/2;
        Block a = null;
        for (Block s : S)
        {
            // find the block with the highest y value among all blocks in the world
            if(s.getY() > y)
            {
                y = s.getY();
                a = s;
            }
        }
        if(a != null)
            return a;
        return null;
    }

    /**
     * get the one object with the highest x coordinate
     */
    private Block rightMostObject()
    {
        List<Block> S = getObjects(Block.class);
        int x = getWidth()/2;
        Block a = null;
        for (Block s : S)
        {
            // find the block with the highest x value among all blocks in the world
            if(s.getX() > x)
            {
                x = s.getX();
                a = s;
            }
        }
        if(a != null)
            return a;
        return null;
    }

    /**
     * @return true if the world is able to scroll
     */
    protected boolean getScroll()
    {
        return scrolling;
    }

    /**
     * @return the current level
     */
    public int getLevel()
    {
        return currLevel;
    }

    /**
     * @return the object with the highest x coordinate
     */
    public Block getRightMostObject()
    {
        return this.right;
    }

    /**
     * @return the object with the lowest y coordinate
     */
    public Block getHighestObject()
    {
        return this.up;
    }

    /**
     * @return the object with the highest y coordinate
     */
    public Block getLowestObject()
    {
        return this.down;
    }

    /**
     * @return the object with the lowest x coordinate
     */
    public Block getLeftMostObject()
    {
        return this.left;
    }

    protected String fileName;
    protected List actors; // a list of all the actors in the world

    /**
     * Writes a text file to save the information.
     * Whenever the user writes in the text file, the data is stored on a seperate line each time.
     * The user names the file along with the option of modifing an exsisting file with the same name or creatin
     * a new one.
     * @param path - the name of the file
     * @param textLine - text
     * @param  appentToFile - true - saves the text on an already exisisting file (continues on the last line), false - make a new file and save the text in it
     */
    protected void writeToFile (String path, String textLine, boolean appendToFile) throws IOException{
        FileWriter write = new FileWriter (path, appendToFile); // create an instance of a class that can make a text file
        PrintWriter print_line = new PrintWriter (write); // create an instance of a class that can write in a text file

        print_line.printf ("%s" +"%n",textLine); // saves the text on a seperate line each time

        print_line.close(); // closes the PrintWriter (that wrote in the text file)
    }

    /**
     * Adds any actor in the world except the basis blocks.
     * The user has to specify the type of actor and its coordinates on the screen
     * @param type - the type of actor (name of its class)
     * @param x - the x-coordinate
     * @param y - the y-coordinate
     */
    protected void addElements(String type, int x, int y){
        // adds each actor in the world based on its nature (the location is specified in the parameters)
        if (type.equals("Stand")){  // if the actor is a stand, add a stand in the world with the specified x and y coordinates
            addObject (new Stand(), x, y);
        } else if (type.equals("GreenKoopa")){
            addObject (new GreenKoopa(), x, y);
        } else if (type.equals("Tree")){
            addObject (new Tree(), x, y);
        } else if (type.equals("Mario")){
            addObject (new Mario(), x, y);
        } else if (type.equals("Stand2")){
            addObject (new Stand2(), x, y);
        } else if (type.equals("Spikes")){
            addObject (new Spikes(), x, y);
        } else if (type.equals("coinBox")){
            addObject (new coinBox(false), x, y);
        } else if (type.equals("Door")){
            addObject (new Door(), x, y);
        }  else if (type.equals("Jumper")){
            addObject (new Jumper(), x, y);
        } else if (type.equals("Key")){
            addObject (new Key(), x, y);
        } else if (type.equals("FallingBrick")){
            addObject (new FallingBrick(), x, y);
        } else if (type.equals("Teleporter")){
            addObject (new Teleporter(x2, y2), x, y); // the teleporter has a constructor asking for the x and y distance it will move
        }else if (type.equals("Transporter")){
            addObject (new Transporter(), x, y);
        } else if (type.equals("Flight")){
            addObject (new Flight(), x, y);
        }else if (type.equals("FireFlower")){
            addObject (new FireFlower(), x, y);
        }else if (type.equals("InvincibleStar")){
            addObject (new InvincibleStar(), x, y);
        }else if (type.equals("BlackHole")){
            addObject (new BlackHole(), x, y);
        }else if (type.equals("Wiggler")){
            addObject (new Wiggler(), x, y);
        }else if (type.equals("HammerBros")){
            addObject (new HammerBros(), x, y);
        }else if (type.equals("Goomba")){
            addObject (new Goomba(), x, y);
        }
    }

    protected ArrayList <String> world= new ArrayList <String>(); // an arraylist to store each each line in the text file
    protected int x2,y2; // saves the x and y distances each telporter will cover
    /**
     * Constructs the world using information from a textfile.
     * All the actors and their x and y location are add in the world.
     * Uses addBlock and addElements methods
     */
    protected void loadWorld (String fileName){
        // the try-catch-finally block saves all the contents of the textfile onto an array list
        try{
            scan = new Scanner (new File (fileName));  // open the scanner in the text file
            while (scan.hasNext()){ // if the textfile has one more line, then
                world.add(scan.nextLine());  // save that line in the array list

            }
        }
        catch (FileNotFoundException e){System.out.println ("File not found");}  // if file not found, output the result to the user
        finally{
            if (scan!= null){  // if the scanner is open,
                scan.close();  // close the scanner
            }
        }

        // the world is made of blocks that make up the basis of the world and other actors like mario, enemies, and stands (where mario can jump on)
        // the textfile saved the 17 basis blocks first then saves the other actors
        // adding the 17 basis blocks to the world has a method of its own
        // adding the other elements has a different method
        int blockNumber=1; // keeps tracks of the number of the current basis block
        for (int i=1; i< world.size(); i+=3){ // loop through the elements of the array list (the loop goes every three because the 2 that it skips are the x and y-coordinates 
            // the loop starts at one because the first line is blank (when you create the file, initially there is a blank line)
            if (world.get(i).equals ("level2Block")){ // if the actor is one of the 17 blocks that makes up the world, then
                // call the method responsible for constructing the basis blocks of the world
                // the method needs - the number of the block (goes in chronological order from 1-17)
                // the method needs - the x-coordinate of the block
                // the method needs - the y-coordinate of the block
                addBlock (blockNumber, Integer.parseInt (world.get(i+1)), Integer.parseInt (world.get(i+2)));  // i+1 & i+2 - each data is on a seperate line            
                blockNumber++;  // increase the block number 
            }else if (world.get(i).equals ("Teleporter")){  // if the actor is a teleporter, then
                // the first two lines after the name will be the x and y distance the teleporter moves
                // save the 2 data in their allocated variables
                x2 = Integer.parseInt (world.get(i+1));
                y2 = Integer.parseInt (world.get(i+2));
                i+=2; // skip 2 elements in the loop (the loop loops every 3 elements but this one has 5, therefore increase the i value to account for the additional lines)
                addElements (world.get(i-2), Integer.parseInt (world.get(i+1)), Integer.parseInt (world.get(i+2))); // call the method responsible for adding the actor on the screen

            } else {  // if the actor is not a basis block or a teleporter, then
                // call the method responsible for adding the actor on the screen
                addElements (world.get(i), Integer.parseInt (world.get(i+1)), Integer.parseInt (world.get(i+2)));
            }

        }
        this.left = leftMostObject();
        this.right = rightMostObject();
        this.up = upperMostObject();
        this.down = bottomMostObject();
    }

    /**
     * Saves the current world and the location of all the objects.
     * Saves the teleporter's movement back and forth.
     * Saves the data in a new text file every time (automatically deletes the old one).
     * Uses writeToFile method
     */
    public void saveWorld () throws IOException{
        actors = getObjects (null);  // save all the actors in the world
        writeToFile (fileName,"", false);  // make a new text file named level1 (first line blank)
        for (int i=0; i< actors.size();i++){  // loop through all the actors in the world
            Actor actor =  (Actor) actors.get(i); // save the current actor in the loop in a variable

            // each actor will have three properties in the textfile: class name, x-coordinate, y-coordinate (each on seperate line)
            // the teleporter will have an additional property : the x and y distance it will move from its current location

            writeToFile (fileName, actors.get(i).getClass().getName(), true); // save the the name of the actor's class 
            if (actors.get(i).getClass().getName().equals("Teleporter")){  // if the actor is a teleporter
                Teleporter t= (Teleporter) actor; // save the actor as a teleporter (to access API)
                writeToFile (fileName, Integer.toString(t.getX2()), true); // save the x-distance the actor is suppose to move from its current location
                writeToFile (fileName, Integer.toString(t.getY2()), true); // save the x-distance the actor is suppose to move from its current location
            }
            writeToFile (fileName, Integer.toString(actor.getX()), true);  // save the actor's x-coordinate
            writeToFile (fileName, Integer.toString(actor.getY()), true);  // save the actor's x-coordinate
        }

    }

    protected void addBlock (int number, int x, int y)
    {
    }

    //for sound from here

    /**
     *Plays the background music. Gets called to continue playing the music when it stops by the stopped method
     */
    public void started() {
        music.playLoop();  // play music
    }

    /**
     * Pauses the background music
     */
    public void stopped() {
        music.pause();  // pause music
    }

    public void setCanPlay()
    {
        canPlay = false;
    }

    public void stopMusic()
    {
        music.stop(); 
        canPlay = false;
    }
}

