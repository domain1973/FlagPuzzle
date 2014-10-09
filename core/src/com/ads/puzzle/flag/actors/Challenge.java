package com.ads.puzzle.flag.actors;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.List;

/**
 * Created by Administrator on 2014/7/5.
 */
public class Challenge extends Group {
    private final float imageSize = Assets.PIECE_SIZE / 3;

    public Challenge(int level, int gateNum) {
        Integer[] bmpIds = Answer.CHALLENGES[gateNum];
        List<Sprite> sprites = Assets.levelSpriteMap.get(level);
        float x_off = Assets.H_SPACE;
        float y_off = Assets.HEIGHT - (Assets.TOPBAR_HEIGHT + Assets.PIECE_SIZE * 3 + Assets.SPACE * 2);
        int off =  bmpIds.length - 1;
        for (int i = off; i >= 0; i--) {
            Image image = new Image(sprites.get(bmpIds[i]));
            float y = y_off + (- i / 3 + 2) * Assets.SPRITE_SIZE;
            float x = x_off + i % 3 * Assets.SPRITE_SIZE;
            image.setBounds(x, y, imageSize, imageSize);
            addActor(image);
        }
    }
}
