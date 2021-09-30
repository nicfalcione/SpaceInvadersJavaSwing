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
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.falcione.nic.spaceinvaders.data.Direction;
import com.falcione.nic.spaceinvaders.engine.GameTimer;
import com.falcione.nic.spaceinvaders.services.CollisionService;
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
    private static CollisionService collisionService = CollisionService.getInstance();
    private static GameTimer timer = GameTimer.getInstance();

    private File highscore;
    /**
     * Constructor to create the panel for the game
     */
    public SIpanel() {

        invaderService.makeInvaders();

        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStateService.hasLost()) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        gameStateService.getBase().setDirection(true, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        gameStateService.getBase().setDirection(true, Direction.RIGHT);
                        break;
                    case KeyEvent.VK_SPACE:
                        gameStateService.getBase().shoot(Constants.MAX_MISSILES);
                    case KeyEvent.VK_P:
                        timer.stop();
                    case KeyEvent.VK_R:
                        timer.start();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!gameStateService.hasLost()) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        gameStateService.getBase().setDirection(false, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        gameStateService.getBase().setDirection(false, Direction.RIGHT);
                        break;
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

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
        gameStateService.getBase().draw(g2);

        // Creates bombs
        invaderService.generateBombs();

        // Draws Bombs
        for (int i = 0; i < invaderService.getInvaders().size(); i++) {
            for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                if (invaderService.getInvaders().get(i).get(j).getBomb() != null) {
                    invaderService.getInvaders().get(i).get(j).drawBomb(g2);
                    
                    if (invaderService.getInvaders().get(i).get(j).getBomb() != null
                            && invaderService.getInvaders().get(i).get(j).getBomb().getY() > 500) {
                        for (int k = 0; k < invaderService.getBombs().size(); k++) {
                            if (invaderService.getBombs().get(k)
                                    .equals(invaderService.getInvaders().get(i).get(j).getBomb())) {
                                invaderService.getInvaders().get(i).get(j).deleteBomb();
                                invaderService.getBombs().remove(k);
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

        collisionService.handleInvaderCollision(g2);

        // Checks to see if base is hit
        collisionService.handleBaseCollision();

        collisionService.handleMysteryCollision(g2);
        
        collisionService.handleBossCollision(g2);

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
        hudService.displayLevel(g);
        
        // Displays the amount of lives left
        hudService.displayLivesCount(g);

        // Draws mystery if declared
        if (invaderService.getMystery() != null) {
            invaderService.getMystery().draw(g2);
        }

        // Displays winning message
        if (gameStateService.hasWon()) {
//            hudService.displayWon(g, timer);
            gameStateService.increaseLevel(g, g2);
        }

        // Displays losing Message
        if (gameStateService.hasLost()) {
            gameStateService.getBase().death();
            gameStateService.getBase().die();
            gameStateService.getBase().draw(g2);
            
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
                timer.stop();
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
                timer.stop();
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
