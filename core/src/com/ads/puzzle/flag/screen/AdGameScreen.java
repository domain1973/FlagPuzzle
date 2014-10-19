package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Series;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2014/8/10.
 */
public class AdGameScreen extends BaseScreen {
    private List<Series> seriesList;
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
        if (!isShow()) {
            super.show();
            createBtns();
            createAdLabel();
            loadAd();
            for (int i = 0; i < seriesList.size(); i++) {
                final Series series = seriesList.get(i);
                Image image = series.getImage();
                ImageButton seriesBtn = new ImageButton(image.getDrawable(), image.getDrawable(), image.getDrawable());
                seriesBtn.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y,
                                             int pointer, int button) {
                        Assets.playSound(Assets.btnSound);
                        getPuzzle().getPEvent().install(series);
                        return true;
                    }
                });
                float x = i % 4 * seriesW + (i % 4 + 1) * margin;
                float y = getY_bar() - (i / 4 + 1) * seriesH - (i / 4 + 2) * margin;
                seriesBtn.setBounds(x, y, seriesW, seriesH);
                addActor(seriesBtn);
            }
            setListens();
            setShow(true);
        } else {
            Gdx.input.setInputProcessor(getStage());
        }
        if (seriesList.size() == 0) {
            getPuzzle().getPEvent().netSlowInfo();
        }
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
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            baseScreen.setBackFlag(true);
            getPuzzle().setScreen(baseScreen);
        }
        super.render(delta);
    }

    private void loadAd() {
        try {
            seriesList = new ArrayList<Series>();
            //动态获取系列、说明、图标
            FileHandle packFile = Gdx.files.external("ads/ad.atlas");
            TextureAtlas atlas = new TextureAtlas(packFile);
            List<Sprite> spriteNames = new ArrayList<Sprite>();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                Sprite s = atlas.createSprite("series" + i);
                if (s == null) {
                    break;
                }
                spriteNames.add(s);
            }
            FileHandle filehandle = Gdx.files.external("ads/url.txt");
            String[] urls = filehandle.readString("UTF-8").split("[#]");
            for (int i = 0; i < spriteNames.size(); i++) {
                String[] url = getUrl(urls, "series" + i).split("[|]");
                Series series = new Series().setImage(new Image(spriteNames.get(i)))
                        .setName(url[0])
                        .setDetail(url[1])
                        .setUrl(url[2]);
                seriesList.add(series);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrl(String[] urls, String name) {
        for (String url : urls) {
            String trim = url.trim();
            if (trim.split("[=]")[0].contains(name)) {
                return trim.split("=")[1];
            }
        }
        return null;
    }
}
