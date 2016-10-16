package main;

import input.DialogReader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public class GameStart {

    protected static JFrame root;
    protected static MyPanel panel;

    public static void start() {
        if (MyPanel.isRenderingText()) {
            return;
        }
        root = new JFrame();
        root.setPreferredSize(new Dimension(400, 300));
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setUndecorated(true);
        panel = new MyPanel();
        panel.setLayout(new GridLayout(4, 1, 0, 0));
        panel.add(createPlayButton());
        panel.add(createOptionButton());
        panel.add(createCreditButton());
        panel.add(createExitButton());
        root.add(panel);
        root.pack();
        root.createBufferStrategy(3);
        root.setLocationRelativeTo(null);
        root.setIconImage(new ImageIcon("res/kgmo.png").getImage());
        root.setVisible(true);
    }

    private static void run() {
        if (MyPanel.isRenderingText()) {
            return;
        }
        TimerMaster.LEVEL.deactivateClock();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MyPanel.rollCredits(input.DialogReader.getDialog("res/intro.txt"), 22, 14);
                while (MyPanel.isRenderingText()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
                root.setVisible(false);
                root.removeAll();
                root = null;
                TimerMaster.LEVEL.resetAll();
                Main.start();
                int i = JOptionPane.showConfirmDialog(null, DialogReader.getDialog("res/guide.txt"), Main.NAME, JOptionPane.YES_NO_OPTION);
                if (i != 0) {
                    JOptionPane.showMessageDialog(null, DialogReader.getDialog("res/controls.txt"), Main.NAME, JOptionPane.PLAIN_MESSAGE);
                }
                TimerMaster.LEVEL.activateClock();
            }
        });
        t.start();
    }

    private static void option() {
        if (MyPanel.isRenderingText()) {
            return;
        }
        JOptionPane.showMessageDialog(null, "Sorry. The options settings have yet\nto be migrated over from a text file"
                + "\nin the res folder named screenSize.txt", Main.NAME, JOptionPane.PLAIN_MESSAGE);
    }

    private static void credit() {
        if (MyPanel.isRenderingText()) {
            return;
        }
        MyPanel.rollCredits(input.DialogReader.getDialog("res/credits.txt"), 24, 12);
    }

    private static void exit() {
        if (MyPanel.isRenderingText()) {
            return;
        }
        System.exit(0);
    }

    private static JButton createPlayButton() {
        MyButton b = new MyButton("Play", Color.blue);
        b.setPreferredSize(new Dimension(400, 75));
        b.setVisible(true);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                run();
            }
        });
        return b;
    }

    private static JButton createOptionButton() {
        MyButton b = new MyButton("Option", Color.yellow);
        b.setPreferredSize(new Dimension(400, 75));
        b.setVisible(true);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                option();
            }
        });
        return b;
    }

    private static JButton createCreditButton() {
        MyButton b = new MyButton("Credits", Color.green);
        b.setPreferredSize(new Dimension(400, 75));
        b.setVisible(true);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                credit();
            }
        });
        return b;
    }

    private static JButton createExitButton() {
        MyButton b = new MyButton("Exit", Color.red);
        b.setPreferredSize(new Dimension(400, 75));
        b.setVisible(true);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                exit();
            }
        });
        return b;
    }
}
