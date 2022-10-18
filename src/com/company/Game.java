package com.company;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

public class Game {
    public static final int ROWS = 4;
    public static final int COLUMNS = ROWS;

    private static Condition current;
    private static Condition target;
    private static  Queue<Condition> O;
    private static HashSet<Condition> C;
    private static Condition last;

    private static  Queue<Condition> O2;
    private static HashSet<Condition> C2;
    private static Condition last2;

    public static Condition moveRight(int row, Condition condition) {
        Condition c = new Condition();
        c.copyCondition(condition);
        int tmp = c.field[row][COLUMNS - 1];
        for (int i = COLUMNS - 1; i > 0; i--) {
            c.field[row][i] = c.field[row][i - 1];
        }
        c.field[row][0] = tmp;
        return c;
    }

    public static Condition moveLeft(int row, Condition condition) {
        Condition c = new Condition();
        c.copyCondition(condition);
        int tmp = c.field[row][0];
        for (int i = 0; i < COLUMNS - 1; i++) {
            c.field[row][i] = c.field[row][i + 1];
        }
        c.field[row][COLUMNS - 1] = tmp;
        return c;
    }

    public static Condition moveDown(int column, Condition condition) {
        Condition c = new Condition();
        c.copyCondition(condition);
        int tmp = c.field[ROWS - 1][column];
        for (int i = ROWS - 1; i > 0; i--) {
            c.field[i][column] = c.field[i - 1][column];
        }
        c.field[0][column] = tmp;
        return c;
    }

    public static Condition moveUp(int column, Condition condition) {
        Condition c = new Condition();
        c.copyCondition(condition);
        int tmp = c.field[0][column];
        for (int i = 0; i < ROWS - 1; i++) {
            c.field[i][column] = c.field[i + 1][column];
        }
        c.field[ROWS - 1][column] = tmp;
        return c;
    }

    public static void solve() {
        System.out.println("---------------------------");
        O = new ArrayDeque<>();
        O.add(current);
        C = new HashSet<>();
        while (!O.isEmpty()) {
            System.out.println("O: " + O.size());
            System.out.println("C: " + C.size());
            Condition tmp = O.poll();
            if (tmp.equals(Game.getTarget())) {
                last = tmp;
                return;
            }
            C.add(tmp);
            p(tmp);
        }
    }

    private static void p(Condition parent) {
        for (int i = 0; i < COLUMNS; i++) {
            Condition tmp = moveDown(i, parent);
            tmp.setParent(parent);
            if (!C.contains(tmp) && !O.contains(tmp)) {
                O.add(tmp);
            }
        }
        for (int i = 0; i < ROWS; i++) {
            Condition tmp = moveRight(i, parent);
            tmp.setParent(parent);
            if (!C.contains(tmp) && !O.contains(tmp)) {
                O.add(tmp);
            }
        }
    }

    public static void solveSmart() {
        System.out.println("---------------------------");
        O = new ArrayDeque<>();
        O.add(current);
        C = new HashSet<>();
        O2 = new ArrayDeque<>();
        O2.add(target);
        C2 = new HashSet<>();
        while (!O.isEmpty()) {
            System.out.println("O: " + O.size());
            System.out.println("C: " + C.size());
            System.out.println("O2: " + O2.size());
            System.out.println("C2: " + C2.size());
            Condition tmp = O.poll();
            if(O2.contains(tmp)) {
                last = tmp;
                while (!O2.peek().equals(tmp)) {
                    O2.poll();
                }
                last2 = O2.poll();
                return;
            }
            C.add(tmp);
            Condition tmp2 = O2.poll();
            C2.add(tmp2);
            p(tmp);
            p2(tmp2);
        }
    }

    private static void p2(Condition parent) {
        for (int i = 0; i < COLUMNS; i++) {
            Condition tmp = moveUp(i, parent);
            tmp.setParent(parent);
            if (!C2.contains(tmp) && !O2.contains(tmp)) {
                O2.add(tmp);
            }
        }
        for (int i = 0; i < ROWS; i++) {
            Condition tmp = moveLeft(i, parent);
            tmp.setParent(parent);
            if (!C2.contains(tmp) && !O2.contains(tmp)) {
                O2.add(tmp);
            }
        }
    }

    public static void solveEvristic(int evristicN) {
        System.out.println("---------------------------");
        Comparator<Condition> comparator = Comparator.comparingInt(Condition::getF);
        O = new PriorityQueue<>(comparator);
        C = new HashSet<>();
        current.setF(0);
        O.add(current);
        while (!O.isEmpty()) {
            System.out.println("O: " + O.size());
            System.out.println("C: " + C.size());
            Condition tmp = O.poll();
            if (tmp.equals(Game.getTarget())) {
                last = tmp;
                return;
            }
            C.add(tmp);
            p3(tmp, evristicN);
        }
    }

    private static void p3(Condition parent, int evristicN) {
        for (int i = 0; i < COLUMNS; i++) {
            Condition tmp = moveDown(i, parent);
            tmp.setParent(parent);
            updateChild(tmp, evristicN);
        }
        for (int i = 0; i < ROWS; i++) {
            Condition tmp = moveRight(i, parent);
            tmp.setParent(parent);
            updateChild(tmp, evristicN);
        }
    }

    private static void updateChild(Condition condition, int evristicN) {
        int f = 0;
        if (evristicN == 1) {
            f = condition.g() + condition.h1();
        }
        else {
            f = condition.g() + condition.h2();
        }
        condition.setF(f);
        if (!C.contains(condition) && !O.contains(condition)) {
            O.add(condition);
        }
        else if(O.contains(condition)) {
            List<Condition> conditions = new LinkedList<>();
            Condition search = O.poll();
            while (!search.equals(condition)) {
                conditions.add(search);
                search = O.poll();
            }
            if (search.getF() > condition.getF()) {
                O.add(condition);
            }
            else {
                conditions.add(search);
            }

            for (Condition e: conditions) {
                O.add(e);
            }
        }
        else if (C.contains(condition)) {
            Optional<Condition> opt = C.stream().filter(c -> c.equals(condition)).findFirst();
            Condition search = opt.get();
            if(search.getF() > condition.getF()) {
                C.remove(search);
                O.add(condition);
            }
        }
    }

    public static void setCurrent(Condition c) {
        current = c;
    }

    public static Condition getCurrent() {
        return current;
    }

    public static void setTarget(Condition c) {
        target = c;
    }

    public static Condition getTarget() {
        return target;
    }

    public static Condition getLast() {
        return last;
    }

    public static Condition getLast2() {
        return last2;
    }
}