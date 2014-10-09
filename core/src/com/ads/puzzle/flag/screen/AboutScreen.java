package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Administrator on 2014/9/1.
 */
public class AboutScreen extends OtherScreen {
    private Image aboutImage;
    private BaseScreen gameScreen;

    private AboutScreen(Puzzle game) {
        super(game);
        aboutImage = new Image(Assets.aboutInfo);
        aboutImage.setPosition(0, (Assets.HEIGHT - aboutImage.getHeight()) / 2);
    }

    public AboutScreen(Puzzle game, BaseScreen gs) {
        this(game);
        gameScreen = gs;
    }

    @Override
    public void show() {
        super.show();
        addActor(aboutImage);
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
        getStarLabel().setText("总计:" + getStarNum());
    }
}
