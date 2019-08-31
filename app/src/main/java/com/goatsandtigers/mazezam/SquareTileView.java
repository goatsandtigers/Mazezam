package com.goatsandtigers.mazezam;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import static com.goatsandtigers.mazezam.BoardTileView.SQUARE_EMPTY;
import static com.goatsandtigers.mazezam.BoardTileView.SQUARE_ENTRACE;
import static com.goatsandtigers.mazezam.BoardTileView.SQUARE_EXIT;
import static com.goatsandtigers.mazezam.BoardTileView.SQUARE_OBSTACLE;
import static com.goatsandtigers.mazezam.BoardTileView.SQUARE_WALL;

public class SquareTileView extends View {

    private final int row;

    public SquareTileView(Context context, int row) {
        super(context);
        this.row = row;
    }

    public void setBackgroundChar(char c) {
        switch (c) {
            case SQUARE_EXIT: setBackgroundResource(R.drawable.exit); break;
            case SQUARE_EMPTY: setBackgroundResource(0); break;
            case SQUARE_ENTRACE: setBackgroundResource(R.drawable.entrance); break;
            case SQUARE_WALL: setBackgroundResource(R.drawable.wall); break;
            case SQUARE_OBSTACLE:
                switch (row) {
                    case 0: setBackgroundResource(R.drawable.i1_buckeye); break;
                    case 1: setBackgroundResource(R.drawable.i2_bunny); break;
                    case 2: setBackgroundResource(R.drawable.i3_crab); break;
                    case 3: setBackgroundResource(R.drawable.i4_snail); break;
                    case 4: setBackgroundResource(R.drawable.i5_angelfish); break;
                    case 5: setBackgroundResource(R.drawable.i6_fish_blue); break;
                    case 6: setBackgroundResource(R.drawable.i7_cool_shark); break;
                    case 7: setBackgroundResource(R.drawable.goat2); break;
                    case 8: setBackgroundResource(R.drawable.goat3); break;
                    case 9: setBackgroundResource(R.drawable.goat4); break;
                    case 10: setBackgroundResource(R.drawable.goat5); break;
                    case 11: setBackgroundResource(R.drawable.goat6); break;
                    default: setBackgroundResource(R.drawable.goat7); break;
                }
                break;
        }
    }

    /**
     * Ensures the height of the view matches its width.
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        int width = r - l;
        if (width != b - t) {
            setLayoutParams(new LinearLayout.LayoutParams(width, width));
        }
    }
}
