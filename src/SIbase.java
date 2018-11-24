import java.applet.AudioClip;
import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * SIbase class for ship class to inherit
 * 
 * @author Nic Falcione
 * @version 11/23/17
 */
public class SIbase {

    private Image base;
    private Image hit;
    private AudioClip shoot;
    private AudioClip die;
    private int x, y;
    private boolean left, right;
    private SImissile missile;
    private boolean dead;

    /**
     * Enumeration for directions
     * 
     * @author Nic Falcione
     * @version 1.0
     */
    enum Direction {
        LEFT, RIGHT
    }

    /**
     * SIbase constructor to add images and sound for the base
     */
    public SIbase() {
        dead = false;
        hit = getImage("SIbaseBlast.gif");
        base = getImage("SIbase.gif");
        shoot = getSound("SIbaseshoot.wav");
        die = getSound("SIshipHit.wav");
        y = 375;
        x = 225;
    }

    /**
     * Getter for x position
     * 
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y position
     * 
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the image for the base
     * 
     * @param filename
     *            Name of the file associated with the image
     * @return Image associated with the base
     */
    private Image getImage(String filename) {
        URL url = getClass().getResource(filename);
        ImageIcon icon = new ImageIcon(url);
        return icon.getImage();
    }

    /**
     * Gets the sound for the base
     * 
     * @param filename
     *            Name of the file associated with the Base's sound
     * @return The Sound of the Base
     */
    private AudioClip getSound(String filename) {
        URL url = getClass().getResource(filename);
        return Applet.newAudioClip(url);
    }

    /**
     * Method that moves the base around
     * 
     * @param b
     *            used to keep moving while key is pressed
     * @param d
     *            direction of movement
     */
    public void move(boolean b, Direction d) {
        switch (d) {
        case LEFT:
            left = b;
            break;
        case RIGHT:
            right = b;
            break;
        }
        if (missile != null) {
            missile.move();
        }
    }

    /**
     * Overloaded method for actual movement
     */
    public void move() {
        if (x < 0) {
            x = 0;
        }

        else if (x > 460) {
            x = 460;
        }
        if (left)
            x -= 5;
        if (right)
            x += 5;
        if (missile != null) {
            missile.move();
            if (missile.getY() < 0) {
                missile = null;
            }
        }
    }

    /**
     * Returns the missile object
     * 
     * @return the missile object
     */
    public SImissile getMissile() {
        if (missile != null) {
            return missile;
        }
        return null;
    }

    /**
     * Deletes the missile upon hit detection
     */
    public void deleteMissile() {
        missile = null;
    }

    /**
     * Method to draw the base
     * 
     * @param g2
     *            Graphics object
     */
    public void draw(Graphics2D g2) {
        if (missile != null) {
            missile.draw(g2);
        }

        if (dead) {
            g2.drawImage(hit, x, y, null);
        }

        else {
            g2.drawImage(base, x, y, null);
        }
    }

    /**
     * Keeps track of the bases life
     */
    public void die() {
        dead = true;
    }

    /**
     * Plays death sound
     */
    public void death() {
        die.play();
    }

    /**
     * Plays the shooting sound for the base
     */
    public void shoot() {
        if (missile == null) {
            missile = new SImissile(x + 12, y + base.getHeight(null) / 3, 2,
                    10);
        }
        shoot.play();
    }
}
