package com.company;

public class Condition {
    public int[][] field = new int[Game.ROWS][Game.COLUMNS];
    private Condition parent;

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
