package com.goatsandtigers.mazezam;

import android.content.Context;
import android.content.SharedPreferences;

public final class Settings {

    private static final String KEY_PUZZLE_SOLVED = "KEY_PUZZLE_SOLVED_%s";
    private static final String KEY_SELECTED_LEVEL = "KEY_SELECTED_LEVEL";

    private Settings() {
    }

    public static int getSelectedLevelNumber(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_SELECTED_LEVEL, 0);
    }

    public static void setSelectedLevelNumber(Context context, int levelNumber) {
        context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(KEY_SELECTED_LEVEL, levelNumber).commit();
    }

    public static boolean isPuzzleSolved(Context context, String levelTitle) {
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(buildPuzzleSolvedKey(levelTitle), false);
    }

    public static void setPuzzleSolved(Context context, String levelTitle) {
        String key = buildPuzzleSolvedKey(levelTitle);
        context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, true).commit();
    }

    private static String buildPuzzleSolvedKey(String levelTitle) {
        return String.format(KEY_PUZZLE_SOLVED, levelTitle);
    }
}
