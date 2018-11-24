import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Class to create a missile object
 * 
 * @author Nic Falcione
 * @version 11/23/17
 */
public class SImissile extends SIthing {

    /**
     * Constructor for a missile
     * 
     * @param x
     *            x pos of the missile
     * @param y
     *            y pos of the missile
     * @param width
     *            width of the missile
     * @param height
     *            height of the missile
     */
    public SImissile(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Moves the missile
     */
    @Override
    public void move() {
        setY(getY() - 10);
    }

    /**
     * Graphics drawing method overriden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(getX(), getY(), 2f, 10f));
    }
}
