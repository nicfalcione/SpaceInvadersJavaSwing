import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.net.URL;

/**
 * Class to inherit all types of invaders
 * 
 * @author Nic Falcione
 * @version 11/27/27
 */
public abstract class SIinvader extends SIship {

    private int points;
    private String direc;
    private SIbomb missile;
    private AudioClip shoot;

    /**
     * Constructor to create an invader
     * 
     * @param x
     *            x position
     * @param y
     *            y position
     * @param width
     *            width of invader
     * @param height
     *            height of invader
     */
    protected SIinvader(int x, int y, int width, int height) {
        super(x, y, width, height);
        shoot = getSound("SIbaseShoot.wav");
    }

    /**
     * Method to set the amount of points an invader is worth
     * 
     * @param points
     *            number of points an invader is worth
     */
    protected void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the sound for the invader
     * 
     * @param filename
     *            Name of the file associated with the invader's sound
     * @return The Sound of the invader
     */
    private AudioClip getSound(String filename) {
        URL url = getClass().getResource(filename);
        return Applet.newAudioClip(url);
    }

    /**
     * Method to set the amount of points an invader is worth
     * 
     * @param points
     *            number of points an invader is worth
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the direction of the invader
     * 
     * @param s
     *            the direction
     */
    public void setDirec(String s) {
        direc = s;
    }

    /**
     * Getter for the direction of the invader
     * 
     * @return the direction
     */
    public String getDirec() {
        return direc;
    }

    /**
     * Plays the shooting sound for the Invader
     */
    public void shoot() {
        if (missile == null) {
            missile = new SIbomb(getX() + 12, getY() - getHeight() / 3, 2, 10);
        }
        shoot.play();
    }

    /**
     * Returns the bomb of the invader
     * 
     * @return the bomb object
     */
    public SIbomb getBomb() {
        if (missile != null) {
            return missile;
        }
        return null;
    }

    /**
     * Deletes the bomb upon hit detection
     */
    public void deleteBomb() {
        missile = null;
    }

    /**
     * Draws Bombs
     * 
     * @param g2
     *            Graphics object
     */
    public void drawBomb(Graphics2D g2) {
        if (missile != null) {
            missile.draw(g2);
        }
    }

    /**
     * Draw method for alternating images
     * 
     * @param g2
     *            Graphics Object
     */
    public abstract void draw2(Graphics2D g2);

    /**
     * Invader is dying
     */
    public abstract void dying();

    /**
     * Invader is dead
     */
    public abstract void dead();

    /**
     * Checks to see if the alien is dead
     * 
     * @return true or false
     */
    public abstract boolean isDead();

}
