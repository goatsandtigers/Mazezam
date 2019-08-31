package com.goatsandtigers.mazezam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LevelSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildLevelSelectRows();
    }

    private void buildLevelSelectRows() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (int levelNumber = 0; levelNumber < LevelFileParser.getNumLevels(this); levelNumber++) {
            View row = buildLevelSelectRow(levelNumber);
            layout.addView(row);
        }
        setContentView(layout);
    }

    private View buildLevelSelectRow(final int rowIndex) {
        TextView textView = new TextView(this);
        Level level = LevelFileParser.getLevel(this, rowIndex);
        textView.setText("Level " + (rowIndex + 1) + " - " + level.getTitle());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.setSelectedLevelNumber(LevelSelectActivity.this, rowIndex);
                Intent intent = new Intent(LevelSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        if (Settings.isPuzzleSolved(this, level.getTitle())) {
            ImageView tick = new ImageView(this);
            tick.setImageResource(R.drawable.tick);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.addView(textView);
            linearLayout.addView(tick);
            return linearLayout;
        } else {
            return textView;
        }
    }
}
