package com.falcione.nic.spaceinvaders.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.Timer;

import com.falcione.nic.spaceinvaders.model.SIBase;
import com.falcione.nic.spaceinvaders.model.SIBoss;

/**
 * Singleton Class to handle drawing the HUD
 * 
 * @author Nic
 * @version 2021
 */
public class HudService {
    
    private static HudService instance = new HudService();
    private static GameStateService gameStateService = GameStateService.getInstance();
    
    protected HudService() {}

    public static HudService getInstance() {
        return instance;
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
        int width = fm.stringWidth("Score: " + gameStateService.getScore()) + 30;

        g.drawString("Score: " + Integer.toString(gameStateService.getScore()), (500 - width),
                20);
    }
    
    /**
     * Displays the level on the panel
     * 
     * @param g Graphics object
     */
    public void displayLevel(Graphics g, SIBoss boss) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans", Font.BOLD, 16));
        if (boss == null) {
            g.drawString("Wave " + Integer.toString(gameStateService.getCurrentLevel().getScoreFactor()), 20, 20);
        } else {
            g.drawString("Boss " + Integer.toString(gameStateService.getCurrentLevel().getScoreFactor() / 4), 20, 20);
        }
    }
    
    /**
     * Displays the lives count on the panel
     * 
     * @param g Graphics object
     */
    public void displayLivesCount(Graphics g, SIBase base) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans", Font.BOLD, 16));
        g.drawString("Lives: " + Integer.toString(base.getHealth()), 400, 435);
    }
    
    /**
     * Displays message that says user won
     */
    public void displayWon(Graphics g, Timer timer) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString("You beat Level " + Integer.toString(gameStateService.getCurrentLevel()
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
        if (gameStateService.hasAchievedNewHighScore()) {
            g.setFont(new Font("Helvetica", Font.BOLD, 25));
            g.drawString("New High Score!", 130, 250);
            gameStateService.setAchievedNewHighScore(false);
        }
    }
}
