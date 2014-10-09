package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Series;
import com.ads.puzzle.flag.window.AdGameWin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.List;


/**
 * Created by Administrator on 2014/8/10.
 */
public class AdGameScreen extends BaseScreen {
    private float margin = Assets.WIDTH / 15;
    private float seriesW = margin * 2.5f;
    private float seriesH = seriesW;

    private BaseScreen baseScreen;

    public AdGameScreen(Puzzle game, BaseScreen bs) {
        super(game);
        baseScreen = bs;
    }

    @Override
    public void show() {
        super.show();
        createBtns();
        createAdLabel();
        createBackground();
        List<Series> seriesList = Assets.seriesList;
        for (int i = 0; i < seriesList.size(); i++) {
            final Series series = seriesList.get(i);
            Image image = series.getImage();
            ImageButton seriesBtn = new ImageButton(image.getDrawable(), image.getDrawable(), image.getDrawable());
            seriesBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {
                    Assets.playSound(Assets.btnSound);
                    addActor(new AdGameWin(getOtherFont(), AdGameScreen.this, series.getReadmeImage(), series.getUrl()));
                    return true;
                }
            });
            float x = i % 4 * seriesW + (i % 4 + 1) * margin;
            float y = getY_bar() - (i / 4 + 1) * seriesH - (i / 4 + 2) * margin;
            seriesBtn.setBounds(x, y, seriesW, seriesH);
            addActor(seriesBtn);
        }

        setListens();
        Gdx.input.setInputProcessor(getStage());
    }

    private void createBackground() {
        Image layerBg = new Image(Assets.layerBg);
        layerBg.setBounds(0, 0, Assets.WIDTH, Assets.HEIGHT - Assets.TOPBAR_HEIGHT);
        addActor(layerBg);
    }

    private void createAdLabel() {
        String str = "爱迪出品";
        BitmapFont font = getOtherFont();
        Label l = new Label(str, new Label.LabelStyle(font, Color.WHITE));
        float w = font.getBounds(str).width;
        l.setBounds((Assets.WIDTH - w) / 2, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        addActor(l);
    }

    private void setListens() {
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().setScreen(baseScreen);
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getBatch().begin();
        getBatch().end();
    }
}
