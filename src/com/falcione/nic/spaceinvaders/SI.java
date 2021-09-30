package com.falcione.nic.spaceinvaders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.falcione.nic.spaceinvaders.engine.GameTimer;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Space Invaders Main Class
 * 
 * @author Nic Falcione
 * @version 2021
 */
@SuppressWarnings("serial")
public class SI extends JFrame {

    private GameTimer timer = GameTimer.getInstance();
    
    private static SIpanel panel;

    /**
     * Constructor to create a Space Invaders Frame
     */
    public SI() {
        setTitle(Constants.SPACE_INVADERS);
        setSize(500, 500);

        JMenuBar menubar = new JMenuBar();
        JMenu game = new JMenu(Constants.GAME);
        JMenu help = new JMenu(Constants.HELP);
        JMenu stats = new JMenu(Constants.STATS);

        JMenuItem newGame = new JMenuItem(Constants.NEW_GAME);
        JMenuItem pause = new JMenuItem(Constants.PAUSE);
        JMenuItem resume = new JMenuItem(Constants.RESUME);
        JMenuItem exit = new JMenuItem(Constants.EXIT);

        JMenuItem about = new JMenuItem(Constants.ABOUT);
        
        JMenuItem topscore = new JMenuItem(Constants.HIGH_SCORE);

        menubar.add(game);
        menubar.add(help);
        menubar.add(stats);

        game.add(newGame);
        game.add(pause);
        game.add(resume);
        game.add(exit);

        help.add(about);
        
        stats.add(topscore);

        setJMenuBar(menubar);
        resume.setEnabled(false);

        panel = new SIpanel();

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                Object[] options = { Constants.YES, Constants.NO, Constants.CANCEL };
                int action = JOptionPane.showOptionDialog(null,
                        Constants.START_A_NEW_GAME, Constants.SELECT_AN_OPTION,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    remove(panel);
                    panel = new SIpanel();
                    add(panel);
                    revalidate();
                    timer.start();
                }

                else {
                    timer.start();
                }
            }
        });
        add(panel);

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                resume.setEnabled(true);
                pause.setEnabled(false);
            }

        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
                resume.setEnabled(false);
                pause.setEnabled(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                Object[] options = { Constants.YES, Constants.NO, Constants.CANCEL };
                int action = JOptionPane.showOptionDialog(null, Constants.DARE_TO_QUIT,
                        Constants.SELECT_AN_OPTION, JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }

                else {
                    timer.start();
                }
            }
        });
        
        topscore.addActionListener(panel.highscorelistener());
        
        about.addActionListener(panel.aboutListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                timer.stop();
                Object[] options = { Constants.YES, Constants.NO, Constants.CANCEL };
                int action = JOptionPane.showOptionDialog(null, Constants.DARE_TO_QUIT,
                        Constants.SELECT_AN_OPTION, JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }

                else {
                    timer.start();
                }
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * returns the panel
     */
    public static  SIpanel getPanel() {
        return panel;
    }

    /**
     * Main Method to Start Game
     * 
     * @param args
     *            Command Line Arguments
     */
    public static void main(String[] args) {
        JFrame f = new SI();
        f.setVisible(true);
    }
}
