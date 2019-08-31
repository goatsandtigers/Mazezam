package com.goatsandtigers.mazezam;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelFileParser {

    private static final String AUTHOR_PREFIX = ";;Author: ";
    private static final String COMMENT_PREFIX = "; ";
    private static final String LEVEL_FILE = "original.mzm";
    private static final String TITLE_PREFIX = ";;Title: ";
    private static final String VERSION_PREFIX = ";;Version: ";

    private static List<Level> levels;

    private static void parseLevelFile(Context context) {
        levels = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(LEVEL_FILE)));
            String title = null;
            String author = null;
            List<String> rows = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(TITLE_PREFIX)) {
                    if (title != null) {
                        rows = ensureHeightDoesNotExceedWidth(rows);
                        rows = ensureWidthDoesNotExceedHeight(rows);
                        levels.add(new Level(title, author, rows));
                    }
                    title = line.replace(TITLE_PREFIX, "");
                    rows = new ArrayList<>();
                } else if (line.startsWith(AUTHOR_PREFIX)) {
                    author = line.replace(AUTHOR_PREFIX, "");
                } else if (!line.isEmpty() && !line.startsWith(COMMENT_PREFIX) && !line.startsWith(VERSION_PREFIX)) {
                    rows.add(line);
                }
            }
        } catch (IOException ignore) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private static List<String> ensureHeightDoesNotExceedWidth(List<String> input) {
        int inputNumCols = input.get(0).length();
        if (inputNumCols >= input.size()) {
            return input;
        } else {
            boolean oddNumberOfCols = (inputNumCols & 1) == 1;
            int padding = (input.size() - inputNumCols) / 2;
            String leftPaddingString = "";
            for (int i = 0; i < padding; i++) {
                leftPaddingString += BoardTileView.SQUARE_WALL;
            }
            String rightPaddingString = leftPaddingString;
            if (oddNumberOfCols) {
                leftPaddingString += BoardTileView.SQUARE_WALL;
            }
            List<String> paddedLevel = new ArrayList<>(input.size());
            for (int i = 0; i < input.size(); i++) {
                String row = leftPaddingString + input.get(i) + rightPaddingString;
                paddedLevel.add(row);
            }
            return paddedLevel;
        }
    }

    private static List<String> ensureWidthDoesNotExceedHeight(List<String> input) {
        int inputNumCols = input.get(0).length();
        String walledRow = "";
        for (int i = 0; i < inputNumCols; i++) {
            walledRow += BoardTileView.SQUARE_WALL;
        }
        while (input.size() < inputNumCols) {
            input.add(walledRow);
            if (input.size() < inputNumCols) {
                input.add(0, walledRow);
            }
        }
        return input;
    }

    public static Level getLevel(Context context, int levelNumber) {
        if (levels == null) {
            parseLevelFile(context);
        }
        Level level = levels.get(levelNumber).clone();
        return level;
    }

    public static int getNumLevels(Context context) {
        if (levels == null) {
            parseLevelFile(context);
        }
        return levels.size();
    }
}
