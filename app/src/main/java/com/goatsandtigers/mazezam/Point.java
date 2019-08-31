package com.goatsandtigers.mazezam;

public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point p2 = (Point) obj;
        return p2.x == x && p2.y == y;
    }
}
