package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/7/27.
 */
public class OtherScreen extends BaseScreen {
    private ImageButton shareBtn;
    private ImageButton adBtn;
    private Image star;
    private Label starLabel;

    public OtherScreen(Puzzle game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        createBtns();
    }

    protected void createBtns() {
        super.createBtns();
        shareBtn = new ImageButton(new TextureRegionDrawable(Assets.share));
        shareBtn.setBounds(Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        shareBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().getPEvent().share();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        adBtn = new ImageButton(new TextureRegionDrawable(Assets.recommend));
        adBtn.setBounds(Assets.TOPBAR_HEIGHT * 2, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        adBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (getPuzzle().getPEvent().isNetworkEnable()) {
                    getPuzzle().setScreen(new AdGameScreen(getPuzzle(), OtherScreen.this));
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        star = new Image(new TextureRegionDrawable(Assets.star));
        star.setBounds(Assets.WIDTH - Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);

        BitmapFont font = getOtherFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE); // 创建一个Label样式，使用默认白色字体
        String str = getStarNumInfo();
        BitmapFont.TextBounds bounds = font.getBounds(str);
        starLabel = new Label(str, labelStyle);
        starLabel.setPosition(Assets.WIDTH - bounds.width - Assets.TOPBAR_HEIGHT, getY_bar());

        addActor(starLabel);
        addActor(shareBtn);
        addActor(adBtn);
        addActor(star);
    }

    protected String getStarNumInfo() {
        return "总计:" + getStarNum();
    }

    protected int getStarNum() {
        int starNum = 0;
        for (int num : Answer.gateStars) {
            starNum = starNum + num;
        }
        return starNum;
    }

    protected void setStarNum() {
        starLabel.setText(getStarNumInfo());
    }
}
