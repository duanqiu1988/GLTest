package com.duanqiu.gltest.activity.game;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.game.Game;
import com.duanqiu.gltest.game.GamePlayer;
import com.duanqiu.gltest.util.LogUtil;

public class GameActivity extends Activity {
    public static final String TAG = GameActivity.class.getSimpleName();
    Game game;
    GamePlayer player;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(flags);

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                });

        setContentView(R.layout.activity_game);
        game = new Game();
        player = (GamePlayer) findViewById(R.id.game_player);
        player.setGame(game);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        LogUtil.d(TAG, "x "+ point.x+" y "+point.y);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
}
