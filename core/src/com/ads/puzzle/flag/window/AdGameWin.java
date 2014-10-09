package com.ads.puzzle.flag.window;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.screen.AdGameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/9/30.
 */
public class AdGameWin extends BaseWin {
    private AdGameScreen gameScreen;
    private String apkUrl;

    public AdGameWin(BitmapFont ft, AdGameScreen gs, Image readme, String url) {
        super("", new Window.WindowStyle(ft, Color.WHITE, readme.getDrawable()));
        gameScreen = gs;
        apkUrl = url;
        create();
    }

    private void create() {
        gameScreen.getStage().addActor(layerBg);
        float win_H = Assets.WIDTH * 3 / 4;
        float win_Y = (Assets.HEIGHT - win_H) / 2;
        setBounds(0, win_Y, Assets.WIDTH, win_H);

        final float btnSize = Assets.WIDTH * 9 / 20;
        ImageButton install = new ImageButton(new TextureRegionDrawable(Assets.downloadBtn), new TextureRegionDrawable(Assets.downloadBtnDown));
        install.setBounds(Assets.WIDTH / 4, 0, btnSize, btnSize / 5);
        install.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                layerBg.remove();
                AdGameWin.this.remove();
                gameScreen.getPuzzle().getPEvent().install(apkUrl);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(install);

        float closeSize = 75 * Assets.WIDTH / 800;
        ImageButton close = new ImageButton(new TextureRegionDrawable(Assets.closeBtn), new TextureRegionDrawable(Assets.closeBtn));
        close.setBounds(Assets.WIDTH - closeSize, win_H - closeSize, closeSize, closeSize);
        close.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                layerBg.remove();
                AdGameWin.this.remove();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(close);
    }
}
