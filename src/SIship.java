/**
 * Abstract class for the Ship
 * 
 * @author Nic Falcione
 * @version 11/23/17
 */
public abstract class SIship extends SIthing {

    /**
     * Constructor for the Ship
     * 
     * @param x
     *            x pos of the ship
     * @param y
     *            y pos of the ship
     * @param width
     *            width of the ship
     * @param height
     *            height of the ship
     */
    protected SIship(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

}
