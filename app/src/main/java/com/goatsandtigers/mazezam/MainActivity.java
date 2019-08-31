package com.goatsandtigers.mazezam;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private static final String GITHUB_URL =
            "https://github.com/goatsandtigers/Mazezam";
    private static final String RULES = "Move the player by swiping up, down, left or right.\n\n" +
            "Obstacles may be pushed left or right.\n\n" +
            "Pushing an obstacle will move all obstacles of the same type.\n\n" +
            "Obstacles may not be pushed through walls.\n\n" +
            "The player may not move through walls.\n\n" +
            "The puzzle is solved when the player has reached the exit door.";
    private static final String SCOREBOARD_UNAVAILABLE =
            "The scoreboard for this level will be available once you have solved this puzzle.";
    private static final String SOURCE_CODE_HEADER_TEXT =
            "Source code for this app is available from the following URL:";

    public static final String PREFS_NAME = "MazezaM Prefs";

    private Level level;
    private LevelView levelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = loadLevel(Settings.getSelectedLevelNumber(this));
        levelView = new LevelView(this, level);
        RelativeLayout rootView = new RelativeLayout(this);
        rootView.addView(levelView);
        AdView adView = buildAdView();
        rootView.addView(adView);
        RelativeLayout.LayoutParams adViewLp = (RelativeLayout.LayoutParams) adView.getLayoutParams();
        adViewLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setContentView(rootView);
    }

    private Level loadLevel(int levelNumber) {
        return LevelFileParser.getLevel(this, levelNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mazezam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_restart_level) {
            confirmRestartLevel();
            return true;
        } else if (id == R.id.action_select_level) {
            showLevelSelect();
        } else if (id == R.id.action_rules) {
            showRules();
        } else if (id == R.id.action_show_scoreboard) {
            showScoreboard();
        } else if (id == R.id.action_show_github_url) {
            showGithubUrl();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmRestartLevel() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Really restart level?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        restartLevel();
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void restartLevel() {
        levelView.restart();
    }

    private void showLevelSelect() {
        Intent intent = new Intent(this, LevelSelectActivity.class);
        startActivity(intent);
    }

    private void showRules() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(RULES)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, null)
                .show();
    }

    private void showScoreboard() {
        if (Settings.isPuzzleSolved(this, level.getTitle())) {
            PuzzleSolvedUtils.showBestTimes(this, level.getTitle());
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(SCOREBOARD_UNAVAILABLE)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }
    }

    private void showGithubUrl() {
        TextView headerTextView = new TextView(this);
        headerTextView.setText(SOURCE_CODE_HEADER_TEXT);
        headerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        EditText urlTextView = new EditText(this);
        urlTextView.setText(GITHUB_URL);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(headerTextView);
        linearLayout.addView(urlTextView);
        new android.app.AlertDialog.Builder(this).setTitle(R.string.app_name)
                .setView(linearLayout).setPositiveButton(android.R.string.ok, null).show();
    }

    private AdView buildAdView() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-7942129400584060/2106159134");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        adView.setLayoutParams(params);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return adView;
    }
}
