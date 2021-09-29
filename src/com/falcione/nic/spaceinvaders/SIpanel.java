package com.falcione.nic.spaceinvaders;

import java.awt.Color;
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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.falcione.nic.spaceinvaders.data.Direction;
import com.falcione.nic.spaceinvaders.model.SIBase;
import com.falcione.nic.spaceinvaders.model.SIBomb;
import com.falcione.nic.spaceinvaders.model.SIMissile;
import com.falcione.nic.spaceinvaders.services.GameStateService;
import com.falcione.nic.spaceinvaders.services.HudService;
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

    private static InvaderService invaderService = InvaderService.getInstance();
    private static GameStateService gameStateService = GameStateService.getInstance();
    private static HudService hudService = HudService.getInstance();

    private File highscore;
    private SIBase base;
    
    private Timer timer;
    private int pulseCount = 0;
    private List<SIBomb> bombs;

    /**
     * Constructor to create the panel for the game
     */
    public SIpanel() {
        base = new SIBase();

        invaderService.makeInvaders();

        bombs = new ArrayList<>();

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStateService.hasLost()) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        base.setDirection(true, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        base.setDirection(true, Direction.RIGHT);
                        break;
                    case KeyEvent.VK_SPACE:
                        base.shoot(Constants.MAX_MISSILES);
                    case KeyEvent.VK_P:
                        stopTimer();
                    case KeyEvent.VK_R:
                        startTimer();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!gameStateService.hasLost()) {
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
        timer = new Timer(Constants.TIMER_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pulseCount++;

                // Speeds up invaders if bounds are hit
                gameStateService.speedUpInvaders();

                base.move();

                // Find left and right most invaders that are alive
                int leftMostAliveInvader = invaderService.getInvaders().get(0).size() - 1;
                int rightMostAliveInvader = 0;
                
                for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                    for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                        if (!invaderService.getInvaders().get(i).get(j).isDead()) {
                            leftMostAliveInvader = leftMostAliveInvader > j ? j : leftMostAliveInvader;
                            rightMostAliveInvader = rightMostAliveInvader < j ? j : rightMostAliveInvader;
                        }
                    }
                }

                // Moves Invaders Down and changes direction to right based on farthest left alive invader
                if (invaderService.getInvaders().get(0).get(leftMostAliveInvader).getX() == 0
                        && invaderService.getInvaders().get(0).get(0).getDirec().equals("left")) {
                    gameStateService.setNeedToSpeedUpInvaders(true);
                    invaderService.getInvaders().forEach(row -> row.forEach(
                        invader -> {
                            invader.setDirec("right");
                            invader.setY(invader.getY() + 12);
                        }
                    ));
                }
                
                // Moves Invaders Down and changes direction to left based on farthest right alive invader
                if (invaderService.getInvaders().get(0).get(rightMostAliveInvader)
                        .getX() == 460
                        && invaderService.getInvaders().get(0).get(0).getDirec().equals("right")) {
                    gameStateService.setNeedToSpeedUpInvaders(true);
                    invaderService.getInvaders().forEach(row -> row.forEach(
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
                if (pulseCount % gameStateService.getPulseSpeedInvaders() == 0) {
                    invaderService.getInvaders().forEach(row -> row.forEach(invader -> invader.move()));
                }

                // Deletes Mystery when out of bounds
                if (invaderService.getMystery() != null
                        && (invaderService.getMystery().getX() < -10 || invaderService.getMystery().getX() > 499)) {
                    invaderService.removeMystery();
                }
                
                int ran = (int) (Math.random() * 1000);

                // Declares mystery if there isn't one and in a .3% chance every
                // 10ms
                if (ran > 996 && invaderService.getMystery() == null) {
                    invaderService.makeMystery();
                }

                // Moves Mystery
                if (invaderService.getMystery() != null && pulseCount % 2 == 0) {
                    invaderService.getMystery().move();
                }
                
                // Moves Boss
                if (invaderService.getBoss() != null && pulseCount % 6 == 0) {
                    invaderService.getBoss().move();
                    if (invaderService.getBoss().getX() > 295) {
                        invaderService.getBoss().setDirec("left");
                    } else if (invaderService.getBoss().getX() < -60) {
                        invaderService.getBoss().setDirec("right");
                    }
                }

                // Checks for bottom of invaders. Loops backwards 
                int max = 0;
                A: for (int i = invaderService.getInvaders().size() - 1; i >= 0; i--) {
                    for (int j = invaderService.getInvaders().get(0).size() - 1; j >= 0; j--) {
                        if (!invaderService.getInvaders().get(i).get(j).isDead()) {
                            max = i;
                            break A; // Breaks out of the nested loop completely
                        }
                    }
                }

                // Ends Game if invaders hit base
                if (invaderService.getInvaders().get(max).get(0).getY() > Constants.BASE_Y) {
                    gameStateService.setLost(true);
                }

                // Checks to see if all aliens are killed
                gameStateService.checkIfLevelHasBeenBeaten();

                repaint();
            }

        });
        timer.start();
    }
    
    public void increaseLevel(Graphics g, Graphics2D g2) {
        gameStateService.setWon(false);
        gameStateService.setNeedToSpeedUpInvaders(true);
        gameStateService.increaseLevel();
        
        // Trigger Boss round every defined interval of waves
        if (gameStateService.getCurrentLevel().getScoreFactor() % Constants.BOSS_WAVE_INTERVAL == 0) {
            invaderService.makeBoss();
            invaderService.drawBossHealthBar(g2);
            
            gameStateService.setAlienCount(1);
        } else {
            gameStateService.setAlienCount(50);
            gameStateService.assignLevelInitialInvaderSpeed();

            bombs = new ArrayList<>();
            
            invaderService.makeInvaders();
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
        if (bombs.size() < gameStateService.getCurrentLevel().getMaxBombs()) {
            for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                    int rand = (int) (Math.random() * 10000);
                    
                    if (rand > 10000 - gameStateService.getCurrentLevel().getBombRandomFactor()
                            && !invaderService.getInvaders().get(i).get(j).isDead()
                            && bombs.size() < gameStateService.getCurrentLevel().getMaxBombs()) {
                        invaderService.getInvaders().get(i).get(j).shoot();
                        bombs.add(invaderService.getInvaders().get(i).get(j).getBomb());
                    }
                }
            }
        }

        // Draws Bombs
        for (int i = 0; i < invaderService.getInvaders().size(); i++) {
            for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                if (invaderService.getInvaders().get(i).get(j).getBomb() != null) {
                    invaderService.getInvaders().get(i).get(j).drawBomb(g2);
                    
                    if (invaderService.getInvaders().get(i).get(j).getBomb() != null
                            && invaderService.getInvaders().get(i).get(j).getBomb().getY() > 500) {
                        for (int k = 0; k < bombs.size(); k++) {
                            if (bombs.get(k)
                                    .equals(invaderService.getInvaders().get(i).get(j).getBomb())) {
                                invaderService.getInvaders().get(i).get(j).deleteBomb();
                                bombs.remove(k);
                            }
                        }
                    }
                }
            }
        }
        
        // Draws Boss
        if (invaderService.getBoss() != null) {
            invaderService.getBoss().draw(g2);
        }

        // Checks to see if invaders are hit, uses iterator to avoid a concurrent modification exception
        for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
            SIMissile missile = iter.next();
            A: for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                    if (missile == null) {
                        break A;
                    }

                    boolean dead = invaderService.getInvaders().get(i).get(j).isDead();
                    int missX = missile.getX();
                    int missY = missile.getY();

                    int invX = invaderService.getInvaders().get(i).get(j).getX();
                    int invY = invaderService.getInvaders().get(i).get(j).getY();
                    int height = invaderService.getInvaders().get(i).get(j).getHeight();
                    int width = invaderService.getInvaders().get(i).get(j).getWidth();

                    if (!dead && missY > invY && missY < invY + height
                            && missX > invX && missX < invX + width) {
                        invaderService.getInvaders().get(i).get(j).dying();
                        invaderService.getInvaders().get(i).get(j).draw(g2);
                        invaderService.getInvaders().get(i).get(j).dead();

                        iter.remove();
                        gameStateService.increaseScoreBy(invaderService.getInvaders().get(i).get(j).getPoints()
                                * gameStateService.getCurrentLevel().getScoreFactor());

                        gameStateService.setAlienCount(gameStateService.getAlienCount() - 1);
                        break A;
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
                    && bombX < baseX + 25 && !gameStateService.hasLost()) {
                
                for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                    for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                        if (invaderService.getInvaders().get(i).get(j).getBomb() != null) {
                            for (int k = 0; k < bombs.size(); k++) {
                                if (bomb.equals(invaderService.getInvaders().get(i).get(j).getBomb())) {
                                    invaderService.getInvaders().get(i).get(j).deleteBomb();
                                }
                            }
                        }
                    }
                }
                iter.remove();
                
                base.setHealth(base.getHealth() - 1);
                if (base.getHealth() <= 0) {
                    gameStateService.setLost(true);
                    base.death();
                }
            }
        }

        // Check to see if mystery is hit
        if (invaderService.getMystery() != null) {
            for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
                SIMissile missile = iter.next();
                if (missile == null || invaderService.getMystery() == null) {
                    continue;
                }
                int missX = missile.getX();
                int missY = missile.getY();

                int invX = invaderService.getMystery().getX();
                int invY = invaderService.getMystery().getY();
                int height = invaderService.getMystery().getHeight();
                int width = invaderService.getMystery().getWidth();

                if (missY > invY && missY < invY + height && missX > invX
                        && missX < invX + width) {
                    invaderService.getMystery().dying();
                    invaderService.getMystery().draw(g2);
                    invaderService.getMystery().dead();

                    iter.remove();
                    
                    gameStateService.increaseScoreBy(invaderService.getMystery().getPoints());
                    invaderService.removeMystery();
                }
            }
        }
        
     // Check to see if boss is hit
        if (invaderService.getBoss() != null) {
            invaderService.drawBossHealthBar(g2);
            
            for (Iterator<SIMissile> iter = base.getMissiles().iterator(); iter.hasNext();) {
                SIMissile missile = iter.next();
                if (missile == null || invaderService.getBoss()  == null) {
                    continue;
                }
                int missX = missile.getX();
                int missY = missile.getY();

                int invX = invaderService.getBoss() .getX();
                int invY = invaderService.getBoss() .getY();
                int height = invaderService.getBoss() .getHeight();
                int width = invaderService.getBoss() .getWidth();

                if (missY > invY && missY < invY + height && missX > invX + 58
                        && missX < invX + width) {

                    iter.remove();
                    
                    invaderService.getBoss() .setHealth(invaderService.getBoss() .getHealth() - 1);
                    
                    if (invaderService.getBoss().getHealth() <= 0) {
                        invaderService.getBoss().dying();
                        invaderService.getBoss().draw(g2);
                        invaderService.getBoss().dead();
                        gameStateService.setAlienCount(gameStateService.getAlienCount() - 1);
                        gameStateService.increaseScoreBy(invaderService.getBoss() .getPoints());
                        invaderService.removeBoss();
                    }
                }
            }
        }

        // Draws all remaining invaders
        for (int i = 0; i < invaderService.getInvaders().size(); i++) {
            for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {

                // Animates invaders to have differing graphics
                if (invaderService.getInvaders().get(i).get(j).getX() % 10 == 0) {
                    invaderService.getInvaders().get(i).get(j).draw(g2);
                } else {
                    invaderService.getInvaders().get(i).get(j).draw2(g2);
                }
            }
        }

        // Displays score
        hudService.displayScore(g);
        
        // Displays score
        hudService.displayLevel(g, invaderService.getBoss());
        
        // Displays the amount of lives left
        hudService.displayLivesCount(g, base);

        // Draws mystery if declared
        if (invaderService.getMystery() != null) {
            invaderService.getMystery().draw(g2);
        }

        // Displays winning message
        if (gameStateService.hasWon()) {
//            hudService.displayWon(g, timer);
            increaseLevel(g, g2);
        }

        // Displays losing Message
        if (gameStateService.hasLost()) {
            base.death();
            base.die();
            base.draw(g2);
            timer.stop();
            updateHighscore();
            hudService.displayLost(g);
        }
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

            if (gameStateService.getScore() > currentHighScore) {
                writer.write(System.lineSeparator() + Integer.toString(gameStateService.getScore()));
                gameStateService.setAchievedNewHighScore(true);
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
