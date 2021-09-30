package com.falcione.nic.spaceinvaders.services;

import java.awt.Graphics2D;
import java.util.Iterator;

import com.falcione.nic.spaceinvaders.model.SIBomb;
import com.falcione.nic.spaceinvaders.model.SIMissile;

/**
 * Singleton class to handle collision detection
 * 
 * @author Nic Falcione
 * @version 2021
 */
public class CollisionService {

    private static CollisionService instance = new CollisionService();
    private static InvaderService invaderService = InvaderService.getInstance();
    private static GameStateService gameStateService = GameStateService.getInstance();
    
    protected CollisionService() {}
    
    public static CollisionService getInstance() {
        return instance;
    }

    public void handleBossCollision(Graphics2D g2) {
        // Check to see if boss is hit
        if (invaderService.getBoss() != null) {
            invaderService.drawBossHealthBar(g2);
            
            for (Iterator<SIMissile> iter = gameStateService.getBase().getMissiles().iterator(); iter.hasNext();) {
                SIMissile missile = iter.next();
                if (missile == null || invaderService.getBoss()  == null) {
                    continue;
                }
                int missX = missile.getX();
                int missY = missile.getY();
    
                int invX = invaderService.getBoss() .getX();
                int invY = invaderService.getBoss() .getY();
                int height = invaderService.getBoss() .getHeight();
                int width =invaderService.getBoss() .getWidth();
    
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
    }

    public void handleMysteryCollision(Graphics2D g2) {
        // Check to see if mystery is hit
        if (invaderService.getMystery() != null) {
            for (Iterator<SIMissile> iter = gameStateService.getBase().getMissiles().iterator(); iter.hasNext();) {
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
    }

    public void handleBaseCollision() {
        for (Iterator<SIBomb> iter = invaderService.getBombs().iterator(); iter.hasNext();) {
            SIBomb bomb = iter.next();
            int bombY = bomb.getY();
            int bombX = bomb.getX();
    
            int baseX = gameStateService.getBase().getX();
            int baseY = gameStateService.getBase().getY();
    
            if (bombY < baseY && bombY > baseY - 10 && bombX > baseX
                    && bombX < baseX + 25 && !gameStateService.hasLost()) {
                
                for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                    for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                        if (invaderService.getInvaders().get(i).get(j).getBomb() != null) {
                            for (int k = 0; k < invaderService.getBombs().size(); k++) {
                                if (bomb.equals(invaderService.getInvaders().get(i).get(j).getBomb())) {
                                    invaderService.getInvaders().get(i).get(j).deleteBomb();
                                }
                            }
                        }
                    }
                }
                iter.remove();
                
                gameStateService.getBase().setHealth(gameStateService.getBase().getHealth() - 1);
                if (gameStateService.getBase().getHealth() <= 0) {
                    gameStateService.setLost(true);
                    gameStateService.getBase().death();
                }
            }
        }
    }

    public void handleInvaderCollision(Graphics2D g2) {
        // Checks to see if invaders are hit, uses iterator to avoid a concurrent modification exception
        for (Iterator<SIMissile> iter = gameStateService.getBase().getMissiles().iterator(); iter.hasNext();) {
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
    }
    
}
