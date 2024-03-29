package com.falcione.nic.spaceinvaders.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.falcione.nic.spaceinvaders.engine.GameTimer;
import com.falcione.nic.spaceinvaders.model.SIBomb;
import com.falcione.nic.spaceinvaders.model.SIBoss;
import com.falcione.nic.spaceinvaders.model.SIBottom;
import com.falcione.nic.spaceinvaders.model.SIInvader;
import com.falcione.nic.spaceinvaders.model.SIMiddle;
import com.falcione.nic.spaceinvaders.model.SIMystery;
import com.falcione.nic.spaceinvaders.model.SITop;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Singleton Class to handle invader actions for use in game loop
 * 
 * @author Nic
 * @version 2021
 */
public class InvaderService {

    private static InvaderService instance = new InvaderService();
    private static GameStateService gameStateService = GameStateService.getInstance();
    private static GameTimer timer = GameTimer.getInstance();
    
    private ArrayList<ArrayList<SIInvader>> invaders;
    private SIBoss boss = null;
    private SIMystery mystery = null;
    private List<SIBomb> bombs;
    
    private static Rectangle2D bossHealthBar = null;

    protected InvaderService() {
        bombs = new ArrayList<>();
    }

    public static InvaderService getInstance() {
        return instance;
    }

    /**
     * Used to create the 2d list of invaders
     */
    public void makeInvaders() {
        
        invaders = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            invaders.add(new ArrayList<SIInvader>());
        }

        // Start coordinates of invaders
        int y = 85;
        int x = 70;
        for (int i = 0; i < Constants.BASE_INVADER_COUNT; i++) {
            if (i < 10) {
                invaders.get(i/10).add(new SITop(x, y, 30, 15));
            } else if (i < 30) {
                invaders.get(i/10).add(new SIMiddle(x, y, 30, 15));
            } else {
                invaders.get(i/10).add(new SIBottom(x, y, 30, 15));
            }
            
            if (i % 10 == 9) {
                y += 25;
                x = 70;
            } else {
                x += 35;
            }
        }

        // Initializes the invaders to start moving right
        invaders.forEach(row -> row.forEach(invader -> invader.setDirec(
                "right")));
    }
    
    public ArrayList<ArrayList<SIInvader>> getInvaders() {
        return invaders;
    }

    public SIMystery getMystery() {
        return mystery;
    }
    
    public void removeMystery() {
        mystery = null;
    }

    public SIBoss getBoss() {
        return boss;
    }

    public List<SIBomb> getBombs() {
        return bombs;
    }

    /**
     * Makes the boss invader
     */
    public void makeBoss() {
        this.boss = new SIBoss(250, 0, 190, 150);
        boss.setHealth(10 * (gameStateService.getCurrentLevel().getScoreFactor()));
        boss.setDirec("right");
        boss.setPoints(500 * gameStateService.getCurrentLevel().getScoreFactor());
    }
    
    /**
     * Makes the mystery invader
     */
    @SuppressWarnings("deprecation")
    public void makeMystery() {
        List<String> dir = new ArrayList<>(Arrays.asList("left", "right"));
        
        Collections.shuffle(dir);
        String direc = dir.get(0);
        
        if (direc.equals("left")) {
            mystery = new SIMystery(499, 50, 35, 8);
            mystery.setPoints(mystery.getPoints() * gameStateService.getCurrentLevel().getScoreFactor());
            mystery.setDirection(direc);
            mystery.getSound().play();
        } else if (direc.equals("right")) {
            mystery = new SIMystery(-10, 50, 35, 8);
            mystery.setPoints(mystery.getPoints() * gameStateService.getCurrentLevel().getScoreFactor());
            mystery.setDirection(direc);
            mystery.getSound().play();
        }
    }
    
    /**
     * Removes boss on death
     */
    public void removeBoss() {
        boss = null;
    }
    
    /**
     * Draws the Boss's health bar
     * 
     * @param g2 {@link Graphics2D} 
     */
    public void drawBossHealthBar(Graphics2D g2) {
        if (bossHealthBar == null) {
            bossHealthBar = new Rectangle2D.Double(95, 30, 250, 5);
        }
        double width = 250 * ((double) boss.getHealth() / (double) boss.getMaxHealth());
        bossHealthBar.setRect(110, 16, width, 5);
        g2.setColor(Color.GREEN);
        g2.fill(bossHealthBar);
    }

    /**
     * Generates bombs from the invaders and boss
     */
    public void generateBombs() {
        if (bombs.size() < gameStateService.getCurrentLevel().getMaxBombs()) {
            for (int i = 0; i < invaders.size(); i++) {
                for (int j = 0; j < invaders.get(0).size(); j++) {
                    int rand = (int) (Math.random() * 10000);
                    
                    if (rand > 10000 - gameStateService.getCurrentLevel().getBombRandomFactor()
                            && !invaders.get(i).get(j).isDead()
                            && bombs.size() < gameStateService.getCurrentLevel().getMaxBombs()) {
                        bombs.add(invaders.get(i).get(j).shoot());
                    }
                }
            }
        }
        
        // if a boss round, shoot bombs at the rate for that boss wave
        if (boss != null & timer.getPulseCount() % Constants.BOSS_FIRE_DELAY == 0) {
            for (int i = 1; i <= 3; i++) {
                bombs.add(boss.shoot(i));
            }
        }
    }
}
