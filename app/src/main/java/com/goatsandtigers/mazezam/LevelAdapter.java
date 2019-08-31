package com.goatsandtigers.mazezam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LevelAdapter extends BaseAdapter {

    private Context context;
    private Level level;
    private BoardTileView[][] squareView;

    public LevelAdapter(Context context, Level level) {
        this.context = context;
        this.level = level;
        createSquareViews();
    }

    private void createSquareViews() {
        squareView = new BoardTileView[level.getNumCols()][level.getNumRows()];
        for (int x = 0; x < level.getNumCols(); x++) {
            for (int y = 0; y < level.getNumRows(); y++) {
                char squareContent = level.getCellContents(x, y);
                int rowNumber = y - level.getFirstRowContainingObstacle();
                squareView[x][y] = new BoardTileView(context, squareContent, rowNumber);
            }
        }
    }

    @Override
    public int getCount() {
        return level.getNumCols() * level.getNumRows();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public BoardTileView getView(int position, View convertView, ViewGroup parent) {
        int x = position % level.getNumCols();
        int y = position / level.getNumCols();
        return squareView[x][y];
    }

    public BoardTileView getSquareView(int x, int y) {
        return squareView[x][y];
    }

    public BoardTileView getSquareView(Point p) {
        return squareView[p.x][p.y];
    }
}
