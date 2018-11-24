import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Class for the Middle Row Invader
 * 
 * @author Nic Falcione
 * @version 11/23/17
 *
 */
public class SImiddle extends SIinvader {

    private Image image1, image2, hit;
    private boolean dying, dead;

    /**
     * Constructor for the Middle Row Invader
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
    protected SImiddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        super.setPoints(20);
        image1 = getImage("SImiddle0.gif");
        image2 = getImage("SImiddle1.gif");
        hit = getImage("SIinvaderBlast.gif");
        dying = dead = false;
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
            g2.drawImage(image1, getX(), getY(), null);
        }
    }

    /**
     * Graphics drawing method overridden
     * 
     * @param g2
     *            Graphics object
     */
    public void draw2(Graphics2D g2) {
        if (dying) {
            g2.drawImage(hit, getX(), getY(), null);
        }

        else if (dead) {
        }

        else {
            g2.drawImage(image2, getX(), getY(), null);
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
        if (getBomb() != null) {
            getBomb().move();
            if (getBomb().getY() < 0) {
                deleteBomb();
            }
        }
        if (getDirec().equals("left")) {
            setX(getX() - 5);
        } else if (getDirec().equals("right")) {
            setX(getX() + 5);
        }
    }

}
