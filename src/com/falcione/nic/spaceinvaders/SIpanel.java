package com.falcione.nic.spaceinvaders;

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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.falcione.nic.spaceinvaders.data.Direction;
import com.falcione.nic.spaceinvaders.data.Level;
import com.falcione.nic.spaceinvaders.model.SIBase;
import com.falcione.nic.spaceinvaders.model.SIBomb;
import com.falcione.nic.spaceinvaders.model.SIBoss;
import com.falcione.nic.spaceinvaders.model.SIInvader;
import com.falcione.nic.spaceinvaders.model.SIMissile;
import com.falcione.nic.spaceinvaders.model.SIMystery;
import com.falcione.nic.spaceinvaders.services.InvaderService;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Space Invaders panel class
 * 
 * @author Nic Falcione
 * @version 2021
 */
@SuppressWarnings("serial")
public class SIpanel extends JPanel {

    private static final int TIMER_SPEED = 10;
    private static final int MAX_MISSILES = 4;

    private static InvaderService invaderService = InvaderService.getInstance();

    private Level currentLevel;

    private File highscore;
    private boolean won, lost;
    private int alienCount;
    private int score;
    private SIBase base;
    private SIBoss boss;
    private SIMystery mystery;
    private ArrayList<ArrayList<SIInvader>> invaders;
    private Timer timer;
    private int pulseCount = 0;
    private int pulseSpeedInvaders;
    private List<SIBomb> bombs;
    private boolean achievedNewHighScore;

    private boolean speedUpInvaders;

    /**
     * Constructor to create the panel for the game
     */
    public SIpanel() {
        base = new SIBase();
        boss = null;

        invaders = invaderService.makeInvaders();

        won = lost = achievedNewHighScore = false;
        currentLevel = Level.ONE;
        pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
        score = 0;
        alienCount = 50;
        speedUpInvaders = false;

        bombs = new ArrayList<>();

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!lost) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        base.setDirection(true, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        base.setDirection(true, Direction.RIGHT);
                        break;
                    case KeyEvent.VK_SPACE:
                        base.shoot(MAX_MISSILES);
                    case KeyEvent.VK_P:
                        stopTimer();
                    case KeyEvent.VK_R:
                        startTimer();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!lost) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        base.setDirection(false, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        base.setDirection(false, Direction.RIGHT);
                        break;
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });

        // Sets timer update
        timer = new Timer(TIMER_SPEED, new ActionListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void actionPerformed(ActionEvent e) {
                pulseCount++;

                // Speeds up invaders if bounds are hit
                if (speedUpInvaders && pulseSpeedInvaders >= currentLevel
                        .getSpeedFactor()) {
                    pulseSpeedInvaders *= currentLevel.getPulseSpeedFactor();
                    speedUpInvaders = false;
                }

                base.move();

                // Find left and right most invaders that are alive
                int leftMostAliveInvader = invaders.get(0).size() - 1;
                int rightMostAliveInvader = 0;
                
                for (int i = 0; i < invaders.size(); i++) {
                    for (int j = 0; j < invaders.get(0).size(); j++) {
                        if (!invaders.get(i).get(j).isDead()) {
                            leftMostAliveInvader = leftMostAliveInvader > j ? j : leftMostAliveInvader;
                            rightMostAliveInvader = rightMostAliveInvader < j ? j : rightMostAliveInvader;
                        }
                    }
                }

                // Moves Invaders Down and changes direction to right based on farthest left alive invader
                if (invaders.get(0).get(leftMostAliveInvader).getX() == 0
                        && invaders.get(0).get(0).getDirec().equals("left")) {
                    speedUpInvaders = true;
                    invaders.forEach(row -> row.forEach(
                        invader -> {
                            invader.setDirec("right");
                            invader.setY(invader.getY() + 12);
                        }
                    ));
                }
                
                // Moves Invaders Down and changes direction to left based on farthest right alive invader
                if (invaders.get(0).get(rightMostAliveInvader)
                        .getX() == 460
                        && invaders.get(0).get(0).getDirec().equals("right")) {
                    speedUpInvaders = true;
                    invaders.forEach(row -> row.forEach(
                        invader -> {
                            invader.setDirec("left");
                            invader.setY(invader.getY() + 12);
                        }
                    ));
                }

                // Moves bombs
                for (SIBomb s : bombs) {
                    s.move();
                }

                // Moves Invaders
                if (pulseCount % pulseSpeedInvaders == 0) {
                    invaders.forEach(row -> row.forEach(invader -> invader.move()));
                }

                // Deletes Mystery when out of bounds
                if (mystery != null
                        && (mystery.getX() < -10 || mystery.getX() > 499)) {
                    mystery = null;
                }
                
                int ran = (int) (Math.random() * 1000);

                // Declares mystery if there isn't one and in a .3% chance every
                // 10ms
                if (ran > 996 && mystery == null) {
                    ArrayList<String> dir = new ArrayList<String>(
                            Arrays.asList("left", "right"));
                    Collections.shuffle(dir);
                    String direc = dir.get(0);
                    if (direc.equals("left")) {
                        mystery = new SIMystery(499, 50, 35, 8);
                        mystery.setPoints(mystery.getPoints() * currentLevel.getScoreFactor());
                        mystery.setDirection(direc);
                        mystery.getSound().play();
                    } else if (direc.equals("right")) {
                        mystery = new SIMystery(-10, 50, 35, 8);
                        mystery.setPoints(mystery.getPoints() * currentLevel.getScoreFactor());
                        mystery.setDirection(direc);
                        mystery.getSound().play();
                    }
                }

                // Moves Mystery
                if (mystery != null && pulseCount % 2 == 0) {
                    mystery.move();
                }
                
                // Moves Boss
                if (boss != null && pulseCount % 6 == 0) {
                    boss.move();
                    if (boss.getX() > 295) {
                        boss.setDirec("left");
                    } else if (boss.getX() < -60) {
                        boss.setDirec("right");
                    }
                }

                // Checks for bottom of invaders. Loops backwards 
                int max = 0;
                A: for (int i = invaders.size() - 1; i >= 0; i--) {
                    for (int j = invaders.get(0).size() - 1; j >= 0; j--) {
                        if (!invaders.get(i).get(j).isDead()) {
                            max = i;
                            break A; // Breaks out of the nested loop completely
                        }
                    }
                }

                // Ends Game if invaders hit base
                if (invaders.get(max).get(0).getY() > Constants.BASE_Y) {
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
    
    public void increaseLevel(Graphics g, Graphics2D g2) {
        won = false;
        speedUpInvaders = false;
        currentLevel = currentLevel.next();
        
        if (currentLevel.getScoreFactor() % 4 == 0) {
            boss = invaderService.makeBoss();
            boss.setHealth(10 * (currentLevel.getScoreFactor()));
            boss.setDirec("right");
            boss.setPoints(500);
            
            invaderService.drawBossHealth(g2, boss.getHealth(), boss.getMaxHealth());
            
            alienCount = 1;
        } else {
            alienCount = 50;
            pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();

            bombs = new ArrayList<>();
            
            invaders = invaderService.makeInvaders();
        }
        
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
     * @param g graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draws the base
        base.draw(g2);

        // Creates bombs
        if (bombs.size() < currentLevel.getMaxBombs()) {
            for (int i = 0; i < invaders.size(); i++) {
                for (int j = 0; j < invaders.get(0).size(); j++) {
                    int rand = (int) (Math.random() * 10000);
                    
                    if (rand > 10000 - currentLevel.getBombRandomFactor()
                            && !invaders.get(i).get(j).isDead()
                            && bombs.size() < currentLevel.getMaxBombs()) {
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
                                invaders.get(i).get(j).deleteBomb();
                                bombs.remove(k);
                            }
                        }
                    }
                }
            }
        }
        
        // Draws Boss
        if (boss != null) {
            boss.draw(g2);
        }

        // Checks to see if invaders are hit, uses iterator to avoid a concurrent modification exception
        for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
            SIMissile missile = iter.next();
            A: for (int i = 0; i < invaders.size(); i++) {
                for (int j = 0; j < invaders.get(0).size(); j++) {
                    if (missile == null) {
                        break A;
                    }

                    boolean dead = invaders.get(i).get(j).isDead();
                    int missX = missile.getX();
                    int missY = missile.getY();

                    int invX = invaders.get(i).get(j).getX();
                    int invY = invaders.get(i).get(j).getY();
                    int height = invaders.get(i).get(j).getHeight();
                    int width = invaders.get(i).get(j).getWidth();

                    if (!dead && missY > invY && missY < invY + height
                            && missX > invX && missX < invX + width) {
                        invaders.get(i).get(j).dying();
                        invaders.get(i).get(j).draw(g2);
                        invaders.get(i).get(j).dead();

                        iter.remove();
                        score += invaders.get(i).get(j).getPoints()
                                * currentLevel.getScoreFactor();

                        alienCount--;
                        break;
                    }
                }
            }
        }

        // Checks to see if base is hit
        for (Iterator<SIBomb> iter = bombs.iterator(); iter.hasNext();) {
            SIBomb bomb = iter.next();
            int bombY = bomb.getY();
            int bombX = bomb.getX();

            int baseX = base.getX();
            int baseY = base.getY();

            if (bombY < baseY && bombY > baseY - 10 && bombX > baseX
                    && bombX < baseX + 25 && !lost) {
                
                for (int i = 0; i < invaders.size(); i++) {
                    for (int j = 0; j < invaders.get(0).size(); j++) {
                        if (invaders.get(i).get(j).getBomb() != null) {
                            for (int k = 0; k < bombs.size(); k++) {
                                if (bomb.equals(invaders.get(i).get(j).getBomb())) {
                                    invaders.get(i).get(j).deleteBomb();
                                }
                            }
                        }
                    }
                }
                iter.remove();
                
                base.setHealth(base.getHealth() - 1);
                if (base.getHealth() <= 0) {
                    lost = true;
                    base.death();
                }
            }
        }

        // Check to see if mystery is hit
        if (mystery != null) {
            for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
                SIMissile missile = iter.next();
                if (missile == null || mystery == null) {
                    continue;
                }
                int missX = missile.getX();
                int missY = missile.getY();

                int invX = mystery.getX();
                int invY = mystery.getY();
                int height = mystery.getHeight();
                int width = mystery.getWidth();

                if (missY > invY && missY < invY + height && missX > invX
                        && missX < invX + width) {
                    mystery.dying();
                    mystery.draw(g2);
                    mystery.dead();

                    iter.remove();
                    
                    score += mystery.getPoints();
                    mystery = null;
                }
            }
        }
        
     // Check to see if boss is hit
        if (boss != null) {
            invaderService.drawBossHealth(g2, boss.getHealth(), boss.getMaxHealth());
            
            for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
                SIMissile missile = iter.next();
                if (missile == null || boss == null) {
                    continue;
                }
                int missX = missile.getX();
                int missY = missile.getY();

                int invX = boss.getX();
                int invY = boss.getY();
                int height = boss.getHeight();
                int width = boss.getWidth();

                if (missY > invY && missY < invY + height && missX > invX + 58
                        && missX < invX + width) {

                    iter.remove();
                    
                    boss.setHealth(boss.getHealth() - 1);
                    
                    if (boss.getHealth() <= 0) {
                        boss.dying();
                        boss.draw(g2);
                        boss.dead();
                        alienCount--;
                        score += boss.getPoints();
                        boss = null;
                    }
                }
            }
        }

        // Moves all remaining invaders
        for (int i = 0; i < invaders.size(); i++) {
            for (int j = 0; j < invaders.get(0).size(); j++) {

                // Animates invaders to have differing graphics
                if (invaders.get(i).get(j).getX() % 10 == 0) {
                    invaders.get(i).get(j).draw(g2);
                } else {
                    invaders.get(i).get(j).draw2(g2);
                }
            }
        }

        // Displays score
        displayScore(g);
        
        // Displays score
        displayLevel(g);

        // Draws mystery if declared
        if (mystery != null) {
            mystery.draw(g2);
        }
        
        if (boss != null) {
            
        }

        // Displays winning message
        if (won) {
            displayWon(g);
            increaseLevel(g, g2);
        }

        // Displays losing Message
        if (lost) {
            base.death();
            base.die();
            base.draw(g2);
            timer.stop();
            updateHighscore();
            displayLost(g);
        }
    }

    /**
     * Displays the score on the panel
     * 
     * @param g Graphics object
     */
    public void displayScore(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth("Score: " + score) + 30;

        g.drawString("Score: " + Integer.toString(score), (500 - width),
                20);
    }
    
    /**
     * Displays the level on the panel
     * 
     * @param g Graphics object
     */
    public void displayLevel(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans", Font.BOLD, 16));
        if (boss == null) {
            g.drawString("Wave " + Integer.toString(currentLevel.getScoreFactor()), 20, 20);
        } else {
            g.drawString("Boss " + Integer.toString(currentLevel.getScoreFactor() / 4), 20, 20);
        }
    }

    /**
     * Displays message that says user won
     */
    public void displayWon(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString("You beat Level " + Integer.toString(currentLevel
                .getScoreFactor()), 75, 200);
        timer.stop();
    }

    /**
     * Displays message that says user lost
     */
    public void displayLost(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString("Game Over", 100, 200);
        if (achievedNewHighScore) {
            g.setFont(new Font("Helvetica", Font.BOLD, 25));
            g.drawString("New High Score!", 130, 250);
            achievedNewHighScore = false;
        }
        timer.stop();
    }

    /**
     * Updates high score if need be
     */
    public void updateHighscore() {
        try {
            highscore = new File(Constants.SAVE_FILE);
            Scanner scan = new Scanner(highscore);
            FileWriter writer = new FileWriter(highscore, true);
            int currentHighScore = 0;

            while (scan.hasNextLine()) {
                currentHighScore = Math.max(currentHighScore,
                        Integer.parseInt(scan.nextLine()));
            }

            if (score > currentHighScore) {
                writer.write(System.lineSeparator() + Integer.toString(score));
                achievedNewHighScore = true;
            }
            scan.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                    highscore = new File(Constants.SAVE_FILE);
                    Scanner scan = new Scanner(highscore);
                    while (scan.hasNextLine()) {
                        high = Math.max(high,
                                Integer.parseInt(scan.nextLine()));
                    }
                    scan.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f, "High Score: " + high);
            }
        });
    }
}
