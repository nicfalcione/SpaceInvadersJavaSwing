import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Space Invaders panel class
 * 
 * @author Nic Falcione
 * @version 11/23/17
 */
@SuppressWarnings("serial")
public class SIpanel extends JPanel {
    private static final int TIMER_SPEED = 10;

    private File highscore;
    private boolean won, lost;
    private int alienCount;
    private int score;
    private SIbase base;
    private SImystery mystery;
    private boolean halfPulse;
    private ArrayList<ArrayList<SIinvader>> invaders = new ArrayList<ArrayList<SIinvader>>();
    private Timer timer;
    private int pulseCount = 0;
    private int pulseSpeedInvaders = 40;
    private ArrayList<SIbomb> bombs;

    private boolean speedUpInvaders;

    /**
     * Constructor to create the panel for the game
     */
    public SIpanel() {
        base = new SIbase();
        makeInvaders();

        won = lost = false;
        score = 0;
        alienCount = 50;
        halfPulse = true;
        speedUpInvaders = false;

        bombs = new ArrayList<>();

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    base.move(true, SIbase.Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    base.move(true, SIbase.Direction.RIGHT);
                    break;
                case KeyEvent.VK_SPACE:
                    base.shoot();
                case KeyEvent.VK_P:
                    stopTimer();
                case KeyEvent.VK_R:
                    startTimer();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    base.move(false, SIbase.Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    base.move(false, SIbase.Direction.RIGHT);
                    break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });

        // Sets timer update
        timer = new Timer(TIMER_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pulseCount++;

                // Speeds up invaders if bounds are hit
                if (speedUpInvaders) {
                    pulseSpeedInvaders *= 0.8;
                    if (pulseSpeedInvaders <= 5) {
                        pulseSpeedInvaders = 5;
                    }
                    speedUpInvaders = false;
                }

                base.move();

                // Moves Invaders Down and changes direction to left
                if (invaders.get(0).get(invaders.get(0).size() - 1)
                        .getX() == 450
                        && invaders.get(0).get(0).getDirec().equals("right")) {
                    speedUpInvaders = true;
                    for (int k = 0; k < invaders.size(); k++) {
                        for (int l = 0; l < invaders.get(0).size(); l++) {
                            invaders.get(k).get(l).setDirec("left");
                            invaders.get(k).get(l)
                                    .setY(invaders.get(k).get(l).getY() + 12);
                        }
                    }
                }

                // Moves Invaders Down and changes direction to right
                if (invaders.get(0).get(0).getX() == 0
                        && invaders.get(0).get(0).getDirec().equals("left")) {
                    speedUpInvaders = true;
                    for (int k = 0; k < invaders.size(); k++) {
                        for (int l = 0; l < invaders.get(0).size(); l++) {
                            invaders.get(k).get(l).setDirec("right");
                            invaders.get(k).get(l)
                                    .setY(invaders.get(k).get(l).getY() + 12);
                        }
                    }
                }

                // Moves bombs
                for (SIbomb s : bombs) {
                    s.move();
                }

                // Moves Invaders
                if (pulseCount % pulseSpeedInvaders == 0) {
                    for (int i = 0; i < invaders.size(); i++) {
                        for (int j = 0; j < invaders.get(0).size(); j++) {
                            invaders.get(i).get(j).move();
                        }
                    }
                }
                // Deletes Mystery when out of bounds
                if (mystery != null
                        && (mystery.getX() < -10 || mystery.getX() > 499)) {
                    mystery = null;
                }
                int ran = (int) (Math.random() * 1000);

                // Declares mystery if there isn't one and in a .3% chance every
                // pulse
                if (ran > 996 && mystery == null) {
                    ArrayList<String> dir = new ArrayList<String>(
                            Arrays.asList("left", "right"));
                    Collections.shuffle(dir);
                    String direc = dir.get(0);
                    if (direc.equals("left")) {
                        mystery = new SImystery(499, 50, 35, 8);
                        mystery.setDirection(direc);
                        mystery.getSound().play();
                    } else
                        if (direc.equals("right")) {
                            mystery = new SImystery(-10, 50, 35, 8);
                            mystery.setDirection(direc);
                            mystery.getSound().play();
                        }
                }
                halfPulse = !halfPulse;

                // Moves Mystery
                if (mystery != null && halfPulse) {
                    mystery.move();
                }

                // Checks for bottom of invaders
                int max = 0;
                for (int i = 0; i < invaders.size(); i++) {
                    for (int j = 0; j < invaders.get(0).size(); j++) {
                        if (!invaders.get(i).get(j).isDead()) {
                            if (i > max) {
                                max = i;
                            }
                        }
                    }
                }

                // Ends Game if invaders hit base
                if (invaders.get(max).get(0).getY() > 350) {
                    lost = true;
                }

                // Checks to see if all aliens all killed
                if (alienCount == 0) {
                    won = true;
                }

                repaint();
            }

        });
        timer.start();

    }

    /**
     * Used to stop timer in other classes
     */
    public void stopTimer() {
        timer.stop();
    }

    /**
     * Used to start timer in other classes
     */
    public void startTimer() {
        timer.start();
    }

    /**
     * Method to paint components on panel
     * 
     * @param g
     *            graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draws the base
        base.draw(g2);

        // Creates bombs
        if (bombs.size() < 4) {
            for (int i = 0; i < invaders.size(); i++) {
                for (int j = 0; j < invaders.get(0).size(); j++) {
                    int rand = (int) (Math.random() * 10000);
                    if (rand > 9992 && !invaders.get(i).get(j).isDead()
                            && bombs.size() < 4) {
                        invaders.get(i).get(j).shoot();
                        bombs.add(invaders.get(i).get(j).getBomb());
                    }
                }
            }
        }

        // Draws Bombs
        for (int i = 0; i < invaders.size(); i++) {
            for (int j = 0; j < invaders.get(0).size(); j++) {
                if (invaders.get(i).get(j).getBomb() != null) {
                    invaders.get(i).get(j).drawBomb(g2);
                    if (invaders.get(i).get(j).getBomb() != null
                            && invaders.get(i).get(j).getBomb().getY() > 500) {
                        for (int k = 0; k < bombs.size(); k++) {
                            if (bombs.get(k)
                                    .equals(invaders.get(i).get(j).getBomb())) {
                                bombs.remove(k);
                            }
                        }
                    }
                }
            }
        }

        // Checks to see if invaders are hit
        if (base.getMissile() != null) {
            for (int i = 0; i < invaders.size(); i++) {
                for (int j = 0; j < invaders.get(0).size(); j++) {
                    if (base.getMissile() == null) {
                        break;
                    }

                    boolean dead = invaders.get(i).get(j).isDead();
                    int missX = base.getMissile().getX();
                    int missY = base.getMissile().getY();

                    int invX = invaders.get(i).get(j).getX();
                    int invY = invaders.get(i).get(j).getY();
                    int height = invaders.get(i).get(j).getHeight();
                    int width = invaders.get(i).get(j).getWidth();

                    if (!dead && missY > invY && missY < invY + height
                            && missX > invX && missX < invX + width) {
                        invaders.get(i).get(j).dying();
                        invaders.get(i).get(j).draw(g2);
                        invaders.get(i).get(j).dead();
                        base.deleteMissile();
                        score += invaders.get(i).get(j).getPoints();
                        alienCount--;
                        break;
                    }
                }
            }
        }

        // Checks to see if base is hit
        for (SIbomb bomb : bombs) {
            int bombY = bomb.getY();
            int bombX = bomb.getX();

            int baseX = base.getX();
            int baseY = base.getY();

            if (bombY < baseY && bombY > baseY - 10 && bombX > baseX
                    && bombX < baseX + 25) {
                lost = true;
                base.death();
            }
        }

        // Check to see if mystery is hit
        if (mystery != null && base.getMissile() != null) {
            int missX = base.getMissile().getX();
            int missY = base.getMissile().getY();

            int invX = mystery.getX();
            int invY = mystery.getY();
            int height = mystery.getHeight();
            int width = mystery.getWidth();

            if (missY > invY && missY < invY + height && missX > invX
                    && missX < invX + width) {
                mystery.dying();
                mystery.draw(g2);
                mystery.dead();
                base.deleteMissile();
                score += mystery.getPoints();
                mystery = null;
            }
        }

        // Moves all remaining invaders
        for (int i = 0; i < invaders.size(); i++) {
            for (int j = 0; j < invaders.get(0).size(); j++) {
                if (invaders.get(i).get(j).getX() % 10 == 0) {
                    invaders.get(i).get(j).draw(g2);
                }

                else {
                    invaders.get(i).get(j).draw2(g2);
                }
            }
        }

        // Displays score
        displayScore(g);

        // Draws mystery if declared
        if (mystery != null) {
            mystery.draw(g2);
        }

        // Displays winning message
        if (won) {
            displayWon(g);
            updateHighscore();
        }

        // Displays losing Message
        if (lost) {
            base.die();
            base.draw(g2);
            timer.stop();
            displayLost(g);
            updateHighscore();
        }
    }

    /**
     * Used to create the 2d list of invaders
     * 
     * @return 2d arraylist of invaders
     */
    public ArrayList<ArrayList<SIinvader>> makeInvaders() {
        for (int i = 0; i < 5; i++) {
            invaders.add(new ArrayList<SIinvader>());
        }

        int y = 85;
        int x = 70;
        for (int i = 0; i < 50; i++) {
            if (i < 10) {
                invaders.get(0).add(new SItop(x, y, 30, 15));
                x += 35;
                if (i == 9) {
                    y += 25;
                    x = 70;
                }
            } else
                if (i < 20) {
                    invaders.get(1).add(new SImiddle(x, y, 30, 15));
                    x += 35;
                    if (i == 19) {
                        y += 25;
                        x = 70;
                    }
                } else
                    if (i < 30) {
                        invaders.get(2).add(new SImiddle(x, y, 30, 15));
                        x += 35;
                        if (i == 29) {
                            y += 25;
                            x = 70;
                        }
                    } else
                        if (i < 40) {
                            invaders.get(3).add(new SIbottom(x, y, 30, 15));
                            x += 35;
                            if (i == 39) {
                                y += 25;
                                x = 70;
                            }
                        } else {
                            invaders.get(4).add(new SIbottom(x, y, 30, 15));
                            x += 35;
                        }
        }

        // Initializes the invaders to start moving right
        for (int i = 0; i < invaders.size(); i++) {
            for (int j = 0; j < invaders.get(0).size(); j++) {
                invaders.get(i).get(j).setDirec("right");
            }
        }
        return invaders;
    }

    /**
     * Displays the score on the panel
     * 
     * @param g
     *            Graphics object
     */
    public void displayScore(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth("Score: " + score) + 46;

        g.drawString("Score: " + String.format("%04d", score), (500 - width),
                20);
    }

    /**
     * Displays message that says user won
     */
    public void displayWon(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString("You Win!", 115, 200);
        timer.stop();
    }

    /**
     * Displays message that says user lost
     */
    public void displayLost(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString("Game Over", 100, 200);
        timer.stop();
    }

    /**
     * Updates high score if need be
     */
    public void updateHighscore() {
        try {
            highscore = new File("Untitled 1");
            Scanner scan = new Scanner(highscore);
            PrintWriter print = new PrintWriter(highscore);
            int currentHighScore = 0;

            while (scan.hasNextLine()) {
                currentHighScore = Math.max(currentHighScore,
                        Integer.parseInt(scan.nextLine()));
            }

            if (score > currentHighScore) {
                print.println(score);
            }
            scan.close();
            print.close();
        } catch (IOException e) {
        }
    }

    /**
     * ActionListener for the About Menu Option
     * 
     * @return ActionListener in association to this menu button
     */
    public ActionListener aboutListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f,
                        "-------------------------\nSpace Invaders\n"
                                + "by Nic O. Falcione\n-------------------------");
            }
        });
    }

    /**
     * ActionListener for the Stats Menu Option
     * 
     * @return ActionListener in association to this menu button
     */
    public ActionListener highscorelistener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                stopTimer();
                int high = 0;
                try {
                    highscore = new File("Untitled 1");
                    Scanner scan = new Scanner(highscore);
                    while (scan.hasNextLine()) {
                        high = Math.max(high,
                                Integer.parseInt(scan.nextLine()));
                    }
                    scan.close();
                } catch (IOException e) {
                }

                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, "High Score: " + high);
            }
        });
    }

}
