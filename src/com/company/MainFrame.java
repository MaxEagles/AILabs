package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class MainFrame extends JFrame {
    public static final Color[] colors = new Color[] {Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN};
    public static final int BALL_SIZE = 100;
    public static final int MINI_BALL_SIZE = 25;
    JPanel panel;
    public MainFrame() {
        panel = new JPanel();
        add(panel);
        setContentPane(panel);
        setSize((Game.COLUMNS + 1)  * 100 + 15,(Game.ROWS + 1)  * 100 + 60);
        setTitle("Двигаем шарики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        MiniDrawComponent mini = new MiniDrawComponent();
        mini.setSize(100,100);
        mini.setBackground(Color.WHITE);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 100;
        constraints.weighty = 100;
        panel.add(mini, constraints);

        DrawComponent field = new DrawComponent();

        Icon icon = new ImageIcon("arrowDown.jpg");
        for (int i = 0; i < Game.COLUMNS; i++) {
            JButton button = new JButton();
            button.setSize(BALL_SIZE, BALL_SIZE);
            button.setIcon(icon);
            constraints.gridx = i + 1;
            panel.add(button, constraints);
            setDownAction(i, button, field);
        }

        icon = new ImageIcon("arrowRight.jpg");
        constraints.gridx = 0;
        for (int i = 0; i < Game.ROWS; i++) {
            JButton button = new JButton();
            button.setSize(BALL_SIZE,BALL_SIZE);
            button.setIcon(icon);
            constraints.gridy = i + 1;
            panel.add(button, constraints);
            setRightAction(i, button, field);
        }

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.gridheight = 4;
        panel.add(field, constraints);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem solveSmart = new JMenuItem("Решить двунапр.");
        JMenuItem solve = new JMenuItem("Решить");
        JMenuItem next = new JMenuItem("->");
        next.setEnabled(false);
        menuBar.add(next);
        menuBar.add(solve);
        menuBar.add(solveSmart);
        setJMenuBar(menuBar);
        solve.addActionListener(event -> {
            next.setEnabled(false);
            solveSmart.setEnabled(false);
            Game.solve();
            Condition c = Game.getLast();
            Stack<Condition> conditions = new Stack<>();
            while (c != null) {
                conditions.add(c);
                c = c.getParent();
            }
            conditions.pop();

            next.setEnabled(true);
            solveSmart.setEnabled(true);
            next.addActionListener(event2 -> {
                if(!conditions.isEmpty()) {
                    Game.setCurrent(conditions.pop());
                    field.repaint();
                    if (Game.getCurrent().equals(Game.getTarget())) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Победа!!!");
                    }
                }
            });
            JOptionPane.showMessageDialog(MainFrame.this, "Решение готово");
        });

        solveSmart.addActionListener(event -> {
            next.setEnabled(false);
            solve.setEnabled(false);
            Game.solveSmart();
            Condition c = Game.getLast();
            Condition c2 = Game.getLast2();
            Stack<Condition> conditions = new Stack<>();
            while (c != null) {
                conditions.add(c);
                c = c.getParent();
            }
            conditions.pop();
            Queue<Condition> conditions2 = new ArrayDeque<>();
            while (c2 != null) {
                conditions2.add(c2);
                c2 = c2.getParent();
            }
            conditions2.poll();

            next.setEnabled(true);
            solve.setEnabled(true);
            next.addActionListener(event2 -> {
                if(!conditions.isEmpty()) {
                    Game.setCurrent(conditions.pop());
                    field.repaint();
                    if (Game.getCurrent().equals(Game.getTarget())) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Победа!!!");
                    }
                }
                else if(!conditions2.isEmpty()) {
                    Game.setCurrent(conditions2.poll());
                    field.repaint();
                    if (Game.getCurrent().equals(Game.getTarget())) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Победа!!!");
                    }
                }
            });
            JOptionPane.showMessageDialog(MainFrame.this, "Решение готово");
        });
    }

    private void setDownAction(int column, JButton button, DrawComponent field) {
        button.addActionListener(event -> {
            Game.setCurrent(Game.moveDown(column, Game.getCurrent()));
            field.repaint();
            if (Game.getCurrent().equals(Game.getTarget())) {
                JOptionPane.showMessageDialog(MainFrame.this, "Победа!!!");
            }
        });
    }

    private void setRightAction(int row, JButton button, DrawComponent field) {
        button.addActionListener(event -> {
            Game.setCurrent(Game.moveRight(row, Game.getCurrent()));
            field.repaint();
            if (Game.getCurrent().equals(Game.getTarget())) {
                JOptionPane.showMessageDialog(MainFrame.this, "Победа!!!");
            }
        });
    }
}

class DrawComponent extends JComponent {
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Condition condition = Game.getCurrent();
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                Ellipse2D ball = new Ellipse2D.Double(
                        j * MainFrame.BALL_SIZE,
                        i * MainFrame.BALL_SIZE,
                        MainFrame.BALL_SIZE,
                        MainFrame.BALL_SIZE
                );
                g2d.setColor(MainFrame.colors[condition.field[i][j]]);
                g2d.fill(ball);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(MainFrame.BALL_SIZE * Game.ROWS,MainFrame.BALL_SIZE * Game.COLUMNS);
    }
}
class MiniDrawComponent extends JComponent {
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Condition condition = Game.getTarget();
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                Ellipse2D ball = new Ellipse2D.Double(
                        j * MainFrame.MINI_BALL_SIZE,
                        i * MainFrame.MINI_BALL_SIZE,
                        MainFrame.MINI_BALL_SIZE,
                        MainFrame.MINI_BALL_SIZE
                );
                g2d.setColor(MainFrame.colors[condition.field[i][j]]);
                g2d.fill(ball);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(MainFrame.MINI_BALL_SIZE * Game.ROWS,MainFrame.MINI_BALL_SIZE * Game.COLUMNS);
    }
}