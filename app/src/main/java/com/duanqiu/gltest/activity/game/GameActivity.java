package com.duanqiu.gltest.activity.game;

import android.app.Activity;
import android.os.Bundle;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.game.Game;
import com.duanqiu.gltest.game.GamePlayer;

public class GameActivity extends Activity {
    Game game;
    GamePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = new Game();
        player = (GamePlayer) findViewById(R.id.game_player);
        player.setGame(game);
    }
}
