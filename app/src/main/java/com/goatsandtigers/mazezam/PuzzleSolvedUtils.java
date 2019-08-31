package com.goatsandtigers.mazezam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public final class PuzzleSolvedUtils {

    private static final String KEY_DEFAULT_NICKNAME = "KEY_DEFAULT_NICKNAME";

    private PuzzleSolvedUtils() {
    }

    public static void askUserForNicknameForScoreboard(final Context context, final int numMoves, final String levelTitle) {
        if (Settings.isPuzzleSolved(context, levelTitle)) {
            return;
        }
        final EnterNicknameDialog enterNicknameDialog = new EnterNicknameDialog(context);

        new AlertDialog.Builder(context).setTitle("Congratulations!").setMessage("Please enter your nickname to see the best times for this puzzle.")
                .setView(enterNicknameDialog).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LoadWebPageASYNC task = new LoadWebPageASYNC(context, levelTitle);
                String nickname = enterNicknameDialog.getNickName();
                saveDefaultNickname(context, nickname);
                nickname = webifyString(nickname);
                String webifiedLevelTitle = webifyString(levelTitle);
                String country = enterNicknameDialog.getCountryCode();
                String url =
                        "http://goatsandtigers.com/mazezam/best_scores.php?name=" + nickname + "&puzzle=" + webifiedLevelTitle
                                + "&moves=" + numMoves + "&country=" + country + "&platform=android";
                task.execute(new String[] { url });
            }
        }).setNegativeButton(android.R.string.cancel, null).show();
    }

    public static void showBestTimes(Context context, String levelTitle) {
        LoadWebPageASYNC task = new LoadWebPageASYNC(context, levelTitle);
        String webifiedLevelTitle = webifyString(levelTitle);
        String url =
                "http://goatsandtigers.com/mazezam/return_best_scores.php?puzzle=" + webifiedLevelTitle;
        task.execute(new String[] { url });
    }

    private static class LoadWebPageASYNC extends AsyncTask<String, Void, String> {

        private Context context;
        private String levelTitle;

        public LoadWebPageASYNC(Context context, String levelTitle) {
            this.context = context;
            this.levelTitle = levelTitle;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                response = new Scanner(new URL(urls[0]).openStream(), "UTF-8").useDelimiter("\\A").next();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Settings.setPuzzleSolved(context, levelTitle);
            // TODO remove this hack
            //result = "Player 120138,25,GB,android,Player 120138,26,GB,android,";
            startScoreBoardActivity(context, result);
        }
    }

    public static void startScoreBoardActivity(Context context, String data) {
        if (data == null || data.length() == 0) {
            String msg = "Unable to retrieve score board data. Please check your internet connection.";
            new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(msg).setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        } else {
            Intent intent = new Intent(context, ScoreBoardActivity.class);
            intent.putExtra(ScoreBoardActivity.KEY_SCOREBOARD_DATA, data);
            context.startActivity(intent);
        }
    }

    private static void saveDefaultNickname(Context context, String nickname) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_DEFAULT_NICKNAME, nickname).commit();
    }

    public static String webifyString(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s.replace(" ", "%20").replace("=", "").replace("&", "").replace("\n", "").replace("\r", "");
        }
    }
}
