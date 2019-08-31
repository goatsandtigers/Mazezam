package com.goatsandtigers.mazezam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreBoardActivity extends Activity {

    public static final String KEY_SCOREBOARD_DATA = "KEY_SCOREBOARD_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildGrid(orderData(parseCsv(loadData()))));
    }

    private List<ScoreBoardRow> orderData(List<ScoreBoardRow> parseCsv) {
        Collections.sort(parseCsv, new Comparator<ScoreBoardRow>() {

            @Override
            public int compare(ScoreBoardRow lhs, ScoreBoardRow rhs) {
                if (lhs.moves < rhs.moves) {
                    return -1;
                } else if (lhs.moves == rhs.moves) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return parseCsv;
    }

    private GridLayout buildGrid(List<ScoreBoardRow> data) {
        GridLayout grid = new GridLayout(this) {
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                int quarterOfWidth = w / 4;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.getLayoutParams().width = quarterOfWidth;
                }
            }
        };
        grid.setPadding(10, 0, 0, 0);
        grid.setColumnCount(4);
        boolean isTablet = isTablet(this);
        grid.addView(buildHeaderTextView(isTablet ? "Position " : "Pos. "));
        grid.addView(buildHeaderTextView("Name "));
        grid.addView(buildHeaderTextView("Move "));
        grid.addView(buildHeaderTextView("Country"));
        int pos = 1;
        for (ScoreBoardRow row : data) {
            grid.addView(buildTextView("" + pos + ". "));
            grid.addView(buildTextView(row.name + " "));
            grid.addView(buildTextView("" + row.moves + " "));
            String countryEmoji = CountryCodeToEmojiConverter.getEmojiForCountryCode(row.country);
            grid.addView(buildTextView(countryEmoji));
            pos++;
        }
        return grid;
    }

    private TextView buildHeaderTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTypeface(null, Typeface.BOLD);
        if (isTablet(this)) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
        return tv;
    }

    private TextView buildTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return tv;
    }

    private String loadData() {
        return getIntent().getStringExtra(KEY_SCOREBOARD_DATA);
    }

    private class ScoreBoardRow {
        String name;
        int moves;
        String country;
        String platform;

        public ScoreBoardRow(String name, int moves, String country, String platform) {
            this.name = name;
            this.moves = moves;
            this.country = country;
            this.platform = platform;
        }

    }

    private List<ScoreBoardRow> parseCsv(String csv) {
        List<ScoreBoardRow> scoreBoardRows = new ArrayList<ScoreBoardRow>();
        String split[] = csv.split(",");
        for (int i = 0; i < split.length; i += 4) {
            String name = split[i];
            int moves;
            try {
                moves = Integer.valueOf(split[i + 1]);
            } catch (Exception e) {
                moves = 0;
            }
            String country = split[i + 2];
            String platform = split[i + 3];
            scoreBoardRows.add(new ScoreBoardRow(name, moves, country, platform));
        }
        return scoreBoardRows;
    }

    public static boolean isTablet(Context context) {
        try {
            // Compute screen size
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));

            // Tablet devices should have a screen size greater than 6 inches
            return size >= 6;
        } catch (Throwable t) {
            return false;
        }
    }
}
