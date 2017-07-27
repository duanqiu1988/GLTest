package com.duanqiu.gltest.game;

import android.content.Context;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.LogUtil;
import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by duanjunjie on 17-7-27.
 */

public class GameLevel {
    public static final String TAG = GameLevel.class.getSimpleName();
    List<Block> blocks;
    private int texBlock;
    private int texBlockSolid;

    public GameLevel(Context context, int levelRes) {
        blocks = new ArrayList<>();
        texBlock = GLUtil.bindTexture2D(context, R.raw.block);
        texBlockSolid = GLUtil.bindTexture2D(context, R.raw.block_solid);
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(levelRes)));
        String line;
        List<List<String>> levels = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                levels.add(Arrays.asList(line.split(" ")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (levels.isEmpty()) {
            return;
        }
        int height = levels.size();
        int width = levels.get(0).size();

        for (int j = 0; j < height; j++) {
            List<String> list = levels.get(j);
            for (int i = 0; i < width; i++) {
                float x = (float) i / width;
                float y = (float) j / height / 2;
                float w = 1f / width;
                float h = 1f / height / 2;

                Vector3 color = null;
                int sprite = texBlock;
                boolean isSolid = false;
                int value = Integer.valueOf(list.get(i));
                switch (value) {
                    case 0:
                        continue;
                    case 1:
                        color = new Vector3(0.8f, 0.8f, 0.7f);
                        sprite = texBlockSolid;
                        isSolid = true;
                        break;
                    case 2:
                        color = new Vector3(0.2f, 0.6f, 1.0f);
                        break;
                    case 3:
                        color = new Vector3(0.0f, 0.7f, 0.0f);
                        break;
                    case 4:
                        color = new Vector3(0.8f, 0.8f, 0.4f);
                        break;
                    case 5:
                        color = new Vector3(1.0f, 0.5f, 0.0f);
                        break;
                }

                LogUtil.d(TAG, String.format("x: %.2f, y: %.2f, w: %.2f, h %.2f, value: %d",
                        x, y, w, h, value));
                blocks.add(new Block(new Vector2(x, y), new Vector2(w, h), sprite, color, isSolid));
            }
        }
    }

    public void draw(SpriteRenderer renderer) {
        for (Block block : blocks) {
            block.draw(renderer);
        }
    }
}
