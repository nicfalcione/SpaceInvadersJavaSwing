import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ImageIcon;

/**
 * Class for the mystery Invader
 * 
 * @author Nic Falcione
 * @version 11/23/17
 */
public class SImystery extends SIinvader {

    private ArrayList<Integer> pntVals;
    private String direc = "";
    private boolean dying, dead;

    private Image image, hit;
    private AudioClip sound;

    /**
     * Constructor for the Mystery Invader
     * 
     * @param x
     *            x pos of the invader
     * @param y
     *            y pos of the invader
     * @param width
     *            width of the invader
     * @param height
     *            height of the invader
     */
    protected SImystery(int x, int y, int width, int height) {
        super(x, y, width, height);

        pntVals = new ArrayList<Integer>(Arrays.asList(50, 100, 150, 300));
        Collections.shuffle(pntVals);
        super.setPoints(pntVals.get(0));

        image = getImage("SImystery.gif");
        hit = getImage("SIinvaderBlast.gif");
        sound = getSound("SImystery.wav");
    }

    /**
     * Gets the image for the base
     * 
     * @param filename
     *            Name of the file associated with the image
     * @return Image associated with the mystery
     */
    private Image getImage(String filename) {
        URL url = getClass().getResource(filename);
        ImageIcon icon = new ImageIcon(url);
        return icon.getImage();
    }

    /**
     * Gets the sound for the mystery
     * 
     * @param filename
     *            Name of the file associated with the mystery's sound
     * @return The Sound of the mystery
     */
    private AudioClip getSound(String filename) {
        URL url = getClass().getResource(filename);
        return Applet.newAudioClip(url);
    }

    /**
     * Graphics drawing method overriden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw(Graphics2D g2) {
        if (dying) {
            g2.drawImage(hit, getX(), getY(), null);
        }

        else if (dead) {
        }

        else {
            g2.drawImage(image, getX(), getY(), null);
        }
    }

    /**
     * Invader is dying
     */
    public void dying() {
        dying = true;
    }

    /**
     * Invader is dead
     */
    public void dead() {
        dying = false;
        dead = true;
    }

    /**
     * Checks to see if the alien is dead
     * 
     * @return true or false
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Moves the Space Invader
     */
    @Override
    public void move() {
        if (direc.equals("left")) {
            setX(getX() - 5);
        } else if (direc.equals("right")) {
            setX(getX() + 5);
        }
    }

    /**
     * Overloaded Method to get sound of mystery
     * 
     * @return the mystery sound
     */
    public AudioClip getSound() {
        return sound;
    }

    /**
     * Method to set the direction of the mystery ship
     * 
     * @param d
     *            direction to set
     */
    public void setDirection(String d) {
        direc = d;
    }

    /**
     * Graphics drawing method overriden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw2(Graphics2D g2) {
    }

}
