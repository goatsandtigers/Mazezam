package com.goatsandtigers.mazezam;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

public class BoardTileView extends LinearLayout {

    private static final int PADDING = 8;
    public static final char SQUARE_EXIT = '*';
    public static final char SQUARE_EMPTY = ' ';
    public static final char SQUARE_ENTRACE = '+';
    public static final char SQUARE_OBSTACLE = '$';
    public static final char SQUARE_WALL = '#';

    private char background;
    private SquareTileView tileIcon;

    public BoardTileView(Context context, char value, int row) {
        super(context);
        tileIcon = new SquareTileView(context, row);
        addView(tileIcon);
        setBackgroundChar(value);
        if (value == SQUARE_ENTRACE) {
            setContainsPlayer(true);
        }
        if (value != SQUARE_WALL) {
            setPadding(PADDING, PADDING, PADDING, PADDING);
        }
    }

    public void setBackgroundChar(char c) {
        background = c;
        tileIcon.setBackgroundChar(c);
        if (c == SQUARE_OBSTACLE) {
            setBackground(new TileGradientBackground(getWidth(), getHeight()));
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public char getBackgroundChar() {
        return background;
    }

    public void setContainsPlayer(boolean containsPlayer) {
        if (containsPlayer) {
            tileIcon.setBackgroundResource(R.drawable.player);
        } else {
            tileIcon.setBackgroundChar(background);
        }
    }
}
