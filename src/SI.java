import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Space Invaders Main Class
 * 
 * @author Nic Falcione
 * @version 12/7/17
 */
@SuppressWarnings("serial")
public class SI extends JFrame {

    private SIpanel panel;

    /**
     * Constructor to create a Space Invaders Frame
     */
    public SI() {
        setTitle("Space Invaders");
        setSize(500, 450);

        JMenuBar menubar = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenu help = new JMenu("Help");
        JMenu stats = new JMenu("Stats");

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem resume = new JMenuItem("Resume");
        JMenuItem exit = new JMenuItem("Exit");

        JMenuItem about = new JMenuItem("About...");
        
        JMenuItem topscore = new JMenuItem("High Score");

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
                panel.stopTimer();
                Object[] options = { "Yes", "No", "Cancel" };
                int action = JOptionPane.showOptionDialog(null,
                        "Start a new game?", "Select an Option",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    remove(panel);
                    panel = new SIpanel();
                    add(panel);
                    revalidate();
                    panel.startTimer();
                }

                else {
                    panel.startTimer();
                }
            }
        });
        add(panel);

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.stopTimer();
                resume.setEnabled(true);
                pause.setEnabled(false);
            }

        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.startTimer();
                resume.setEnabled(false);
                pause.setEnabled(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.stopTimer();
                Object[] options = { "Yes", "No", "Cancel" };
                int action = JOptionPane.showOptionDialog(null, "Dare to Quit?",
                        "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    dispose();
                }

                else {
                    panel.startTimer();
                }
            }
        });
        
        topscore.addActionListener(panel.highscorelistener());
        
        about.addActionListener(panel.aboutListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                panel.stopTimer();
                Object[] options = { "Yes", "No", "Cancel" };
                int action = JOptionPane.showOptionDialog(null, "Dare to Quit?",
                        "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, null);
                if (action == JOptionPane.YES_OPTION) {
                    dispose();
                }

                else {
                    panel.startTimer();
                }
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Starts a new game
     */
    public SIpanel getPanel() {
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
