package com.goatsandtigers.mazezam;

import java.util.ArrayList;
import java.util.List;

public class Level implements Cloneable {

    private String title;
    private List<String> rows;

    public Level(String title, List<String> rows) {
        this.title = title;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public int getNumCols() {
        return rows.get(0).length();
    }

    public int getNumRows() {
        return rows.size();
    }

    public char getCellContents(int x, int y) {
        String s = rows.get(y);
        try {
            return s.charAt(x);
        } catch (ArrayIndexOutOfBoundsException e) {
            return BoardTileView.SQUARE_WALL;
        }
    }

    public int getFirstRowContainingObstacle() {
        String obstacleString = String.valueOf(BoardTileView.SQUARE_OBSTACLE);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).contains(obstacleString)) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    public boolean isLeftmostSquareEmptyInRow(int rowIndex) {
        String row = rows.get(rowIndex);
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (c != BoardTileView.SQUARE_WALL && c != BoardTileView.SQUARE_ENTRACE && c != BoardTileView.SQUARE_EXIT) {
                return c == BoardTileView.SQUARE_EMPTY;
            }
        }
        return false;
    }

    public boolean isRightmostSquareEmptyInRow(int rowIndex) {
        String row = rows.get(rowIndex);
        for (int i = row.length() - 1; i >= 0; i--) {
            char c = row.charAt(i);
            if (c != BoardTileView.SQUARE_WALL && c != BoardTileView.SQUARE_ENTRACE && c != BoardTileView.SQUARE_EXIT) {
                return c == BoardTileView.SQUARE_EMPTY;
            }
        }
        return false;
    }

    public void shiftObstaclesLeftInRow(int rowIndex) {
        String oldRow = rows.get(rowIndex);
        char[] newRow = oldRow.toCharArray();
        for (int i = 1; i < oldRow.length(); i++) {
            if (newRow[i] == BoardTileView.SQUARE_OBSTACLE) {
                newRow[i - 1] = BoardTileView.SQUARE_OBSTACLE;
                newRow[i] = BoardTileView.SQUARE_EMPTY;
            }
        }
        rows.remove(rowIndex);
        rows.add(rowIndex, new String(newRow));
    }

    public void shiftObstaclesRightInRow(int rowIndex) {
        String oldRow = rows.get(rowIndex);
        char[] newRow = oldRow.toCharArray();
        for (int i = newRow.length - 2; i >= 0; i--) {
            if (newRow[i] == BoardTileView.SQUARE_OBSTACLE) {
                newRow[i + 1] = BoardTileView.SQUARE_OBSTACLE;
                newRow[i] = BoardTileView.SQUARE_EMPTY;
            }
        }
        rows.remove(rowIndex);
        rows.add(rowIndex, new String(newRow));
    }

    public Point getPlayerStartPoint() {
        for (int y = 0; y < rows.size(); y++) {
            int x = rows.get(y).indexOf(BoardTileView.SQUARE_ENTRACE);
            if (x != -1) {
                return new Point(x, y);
            }
        }
        throw new IllegalStateException();
    }

    public String getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public Level clone() {
        return new Level(title, new ArrayList<>(rows));
    }
}
