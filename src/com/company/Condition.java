package com.company;

import java.util.Arrays;

public class Condition {
    public int[][] field = new int[Game.ROWS][Game.COLUMNS];
    private Condition parent;
    private int f;

    public void copyCondition(Condition other) {
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                this.field[i][j] = other.field[i][j];
            }
        }
    }

    public void setParent(Condition parent) {
        this.parent = parent;
    }

    public Condition getParent() {
        return parent;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int g() {
        int n = 0;
        Condition current = this;
        while (current.parent != null) {
            n++;
            current = current.parent;
        }
        return n;
    }

    public int h1() {
        int[] nColorsRowsTarget = new int[Game.ROWS];
        int[] nColorsColumnsTarget = new int[Game.COLUMNS];
        int[] nColorsRowsReal = new int[Game.ROWS];
        int[] nColorsColumnsReal = new int[Game.COLUMNS];
        //получаем целевое и реальное количество цветов в строках, ...
        for (int i = 0; i < Game.ROWS; i++) {
            nColorsRowsTarget[i] = (int)Arrays.stream(Game.getTarget().field[i]).distinct().count();
            nColorsRowsReal[i] = (int)Arrays.stream(this.field[i]).distinct().count();
        }
        //...в столбцах
        for (int i = 0; i < Game.COLUMNS; i++) {
            int[] tmpColumnTarget = new int[Game.ROWS];
            int[] tmpColumnReal = new int[Game.ROWS];
            for (int j = 0; j < Game.ROWS; j++) {
                tmpColumnTarget[j] = Game.getTarget().field[j][i];
                tmpColumnReal[j] = this.field[j][i];
            }
            nColorsColumnsTarget[i] = (int)Arrays.stream(tmpColumnTarget).distinct().count();
            nColorsColumnsReal[i] = (int)Arrays.stream(tmpColumnReal).distinct().count();
        }

        //высчитываем разницу
        int difference = 0;
        for (int i = 0; i < Game.ROWS; i++) {
            difference += Math.abs(nColorsRowsTarget[i] - nColorsRowsReal[i]);
            difference += Math.abs(nColorsColumnsTarget[i] - nColorsColumnsReal[i]);
        }

        int n = difference / Game.ROWS;
        if (difference % Game.ROWS != 0) {
            n++;
        }
        return n;
    }

    public int h2() {
        int distance = 0;
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                distance += countBallDistance(i, j);
            }
        }
        return distance / 4;
    }

    private int countBallDistance(int row, int column) {
        int color = field[row][column];
        int min = 100;
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                if (Game.getTarget().field[i][j] == color) {
                    int distance = distanceBetween(row, column, i, j);
                    if (distance < min) {
                        min = distance;
                    }
                }
            }
        }
        return min;
    }

    private int distanceBetween(int fromRow, int fromColumn, int toRow, int toColumn) {
        int rowDistance = 0;
        int columnDistance = 0;
        if (fromRow <= toRow) {
            rowDistance = toRow - fromRow;
        }
        else {
            rowDistance = Game.ROWS - fromRow + toRow;
        }

        if (fromColumn <= toColumn) {
            columnDistance = toColumn - fromColumn;
        }
        else {
            columnDistance = Game.COLUMNS - fromColumn + toColumn;
        }
        return rowDistance + columnDistance;
    }

    @Override
    public boolean equals (Object o) {
        if(this == o)
            return true;
        if(o == null)
            return false;

        Condition other = (Condition) o;
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                if(this.field[i][j] != other.field[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLUMNS; j++) {
                hash += this.field[i][j] * (i + 1) * (j + 1);
            }
        }
        return hash;
    }
}
