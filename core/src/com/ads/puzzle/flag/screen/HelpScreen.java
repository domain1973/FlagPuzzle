package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Administrator on 2014/9/1.
 */
public class HelpScreen extends OtherScreen {
    private Image readmeImage;
    private BaseScreen gameScreen;

    private HelpScreen(Puzzle game) {
        super(game);
        readmeImage = new Image(Assets.readme);
        float imageW = Assets.WIDTH * 20 / 21;
        readmeImage.setBounds((Assets.WIDTH - imageW) / 2, Assets.TOPBAR_HEIGHT, imageW, imageW * 1.6f);
    }

    public HelpScreen(Puzzle game, BaseScreen ms) {
        this(game);
        gameScreen = ms;
    }

    @Override
    public void show() {
        super.show();
        addActor(readmeImage);
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().setScreen(gameScreen);
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }
}
