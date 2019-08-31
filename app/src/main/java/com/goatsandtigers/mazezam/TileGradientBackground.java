package com.goatsandtigers.mazezam;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class TileGradientBackground extends GradientDrawable {

    private static final int BORDER_THICKNESS = 1;

    public static final int GRADIENT_START_COLOR = Color.rgb(255, 188, 121);

    public TileGradientBackground(int width, int height) {
        setColors(new int[]{
                GRADIENT_START_COLOR,
                Color.WHITE
        });
        setGradientType(LINEAR_GRADIENT);
        setShape(RECTANGLE);
        setStroke(BORDER_THICKNESS, Color.BLACK);
        setSize(width, height);
    }
}
