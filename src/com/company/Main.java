package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    //comment
    public static void main(String[] args) {
        try {
            Game.setCurrent(readCondition("initial.txt"));
            Game.setTarget(readCondition("target.txt"));

            EventQueue.invokeLater(() -> {
                        if (UIManager.getLookAndFeel().isSupportedLookAndFeel()) {
                            final String platform = UIManager.getSystemLookAndFeelClassName();
                            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                                try {
                                    UIManager.setLookAndFeel(platform);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }

                        MainFrame frame = new MainFrame();
                    }
            );

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Condition readCondition(String filename) throws Exception{
        try (FileReader fr = new FileReader(filename)) {
            Scanner scanner = new Scanner(fr);
            int i = 0;
            Condition condition = new Condition();
            while (scanner.hasNextLine() && i < Game.ROWS) {
                String line = scanner.nextLine();
                String[] numbers = line.split(" ");
                for (int j = 0; j < Game.COLUMNS; j++) {
                    condition.field[i][j] = Integer.parseInt(numbers[j]);
                    if (condition.field[i][j] > Game.COLUMNS || condition.field[i][j] < 0)
                        throw new Exception();
                }
                i++;
            }
            return condition;
        }
        catch (Exception ex) {
            throw new Exception();
        }
    }
}