package com.goatsandtigers.mazezam;

import static com.goatsandtigers.mazezam.ScoreBoardActivity.isTablet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.widget.GridView;

public class LevelView extends GridView {

    private Level level;
    private LevelAdapter levelAdapter;
    private GestureDetectorCompat gestureDetectorCompat;
    private Point playerPos;

    public LevelView(Context context, Level level) {
        super(context);
        this.level = level;
        playerPos = level.getPlayerStartPoint();
        setBackgroundColor(Color.argb(100, 255, 188, 121));
        setNumColumns(level.getNumCols());
        levelAdapter = new LevelAdapter(getContext(), level);
        setAdapter(levelAdapter);
        createSwipeGestureListener();
        updateTitle();
        refreshFromSavedState();
    }

    private void createSwipeGestureListener() {
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener(this);
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    public void moveLeft() {
        char squareToLeft = level.getCellContents(playerPos.x - 1, playerPos.y);
        if (squareToLeft == BoardTileView.SQUARE_EMPTY || squareToLeft == BoardTileView.SQUARE_EXIT) {
            unpaintPlayer();
            playerPos.x--;
            repaintPlayer();
            updateStateAfterMove();
        } else if (squareToLeft == BoardTileView.SQUARE_OBSTACLE && level.isLeftmostSquareEmptyInRow(playerPos.y)) {
            unpaintPlayer();
            level.shiftObstaclesLeftInRow(playerPos.y);
            repaintRow(playerPos.y);
            playerPos.x--;
            repaintPlayer();
            updateStateAfterMove();
        }
    }

    public void moveRight() {
        char squareToRight = level.getCellContents(playerPos.x + 1, playerPos.y);
        if (squareToRight == BoardTileView.SQUARE_EMPTY || squareToRight == BoardTileView.SQUARE_EXIT) {
            unpaintPlayer();
            playerPos.x++;
            repaintPlayer();
            updateStateAfterMove();
        } else if (squareToRight == BoardTileView.SQUARE_OBSTACLE && level.isRightmostSquareEmptyInRow(playerPos.y)) {
            unpaintPlayer();
            level.shiftObstaclesRightInRow(playerPos.y);
            repaintRow(playerPos.y);
            playerPos.x++;
            repaintPlayer();
            updateStateAfterMove();
        }
    }

    public void moveUp() {
        char squareAbovePlayer = level.getCellContents(playerPos.x, playerPos.y - 1);
        if (squareAbovePlayer == BoardTileView.SQUARE_EMPTY || squareAbovePlayer == BoardTileView.SQUARE_EXIT) {
            unpaintPlayer();
            playerPos.y--;
            repaintPlayer();
            updateStateAfterMove();
        }
    }

    public void moveDown() {
        char squareBelowPlayer = level.getCellContents(playerPos.x, playerPos.y + 1);
        if (squareBelowPlayer == BoardTileView.SQUARE_EMPTY || squareBelowPlayer == BoardTileView.SQUARE_EXIT) {
            unpaintPlayer();
            playerPos.y++;
            repaintPlayer();
            updateStateAfterMove();
        }
    }

    private void repaintRow(int rowIndex) {
        for (int i = 0; i < level.getNumCols(); i++) {
            char c = level.getCellContents(i, rowIndex);
            if (c == BoardTileView.SQUARE_EMPTY || c == BoardTileView.SQUARE_OBSTACLE || c == BoardTileView.SQUARE_EXIT) {
                BoardTileView squareView = levelAdapter.getSquareView(i, rowIndex);
                squareView.setBackgroundChar(c);
            }
        }
    }

    private void unpaintPlayer() {
        levelAdapter.getSquareView(playerPos).setContainsPlayer(false);
    }

    private void repaintPlayer() {
        levelAdapter.getSquareView(playerPos).setContainsPlayer(true);
    }

    private void updateStateAfterMove() {
        incrementNumMovesForLevel();
        updateTitle();
        checkForVictory();
        saveState();
    }

    private void checkForVictory() {
        if (level.getCellContents(playerPos.x, playerPos.y) == BoardTileView.SQUARE_EXIT) {
            int numMoves = loadNumMovesForLevel();
            String levelTitle = level.getTitle();
            PuzzleSolvedUtils.askUserForNicknameForScoreboard(getContext(), numMoves, levelTitle);
        }
    }

    private int loadNumMovesForLevel() {
        SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(buildNumMovesForLevelKey(), 0);
    }

    private void incrementNumMovesForLevel() {
        String key = buildNumMovesForLevelKey();
        int moves = loadNumMovesForLevel() + 1;
        getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(key, moves).commit();
    }

    private void updateTitle() {
        String title = level.getTitle();
        int numMoves = loadNumMovesForLevel();
        if (numMoves > 0) {
            if (ScoreBoardActivity.isTablet(getContext())) {
                title += " - Moves so far: " + loadNumMovesForLevel();
            } else {
                title += " - " + loadNumMovesForLevel() + " Moves";
            }
        }
        ((Activity) getContext()).setTitle(title);
    }

    private String buildNumMovesForLevelKey() {
        return "numMovesForLevel_" + level.getTitle();
    }

    private void refreshFromSavedState() {
        SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        for (int rowIndex = 0; rowIndex < level.getNumRows(); rowIndex++) {
            String key = buildSavedRowKeyForRow(rowIndex);
            String row = prefs.getString(key, null);
            if (row == null) {
                continue;
            }
            for (int charIndex = 0; charIndex < row.length(); charIndex++) {
                char c = row.charAt(charIndex);
                BoardTileView squareView = levelAdapter.getSquareView(charIndex, rowIndex);
                if (squareView.getBackgroundChar() != c) {
                    squareView.setBackgroundChar(c);
                }
            }
        }
        int playerX = prefs.getInt(buildPlayerPosXKey(), -1);
        int playerY = prefs.getInt(buildPlayerPosYKey(), -1);
        if (playerX != -1) {
            unpaintPlayer();
            playerPos.x = playerX;
            playerPos.y = playerY;
            repaintPlayer();
        }
    }

    private void saveState() {
        SharedPreferences.Editor prefs = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit();
        for (int rowIndex = 0; rowIndex < level.getNumRows(); rowIndex++) {
            String key = buildSavedRowKeyForRow(rowIndex);
            String row = level.getRow(rowIndex);
            prefs.putString(key, row);
        }
        prefs.putInt(buildPlayerPosXKey(), playerPos.x);
        prefs.putInt(buildPlayerPosYKey(), playerPos.y);
        prefs.commit();
    }

    public void restart() {
        SharedPreferences.Editor prefs = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit();
        for (int rowIndex = 0; rowIndex < level.getNumRows(); rowIndex++) {
            String key = buildSavedRowKeyForRow(rowIndex);
            prefs.remove(key);
        }
        prefs.remove(buildPlayerPosXKey());
        prefs.remove(buildPlayerPosYKey());
        playerPos = level.getPlayerStartPoint();
        prefs.putInt(buildPlayerPosXKey(), playerPos.x);
        prefs.putInt(buildPlayerPosYKey(), playerPos.y);
        prefs.commit();
        level = LevelFileParser.getLevel(getContext(), Settings.getSelectedLevelNumber(getContext()));
        repaintAllRows();
        repaintPlayer();
    }

    private void repaintAllRows() {
        for (int rowIndex = 0; rowIndex < level.getNumRows(); rowIndex++) {
            repaintRow(rowIndex);
        }
    }

    private String buildSavedRowKeyForRow(int rowIndex) {
        return level.getTitle() + "_row_" + rowIndex;
    }

    private String buildPlayerPosXKey() {
        return level.getTitle() + "_playerX";
    }

    private String buildPlayerPosYKey() {
        return level.getTitle() + "_playerY";
    }

}
