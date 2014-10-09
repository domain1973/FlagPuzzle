package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by Ads on 2014/6/22.
 */
public class MainScreen extends BaseScreen {
    private Puzzle puzzle;

    public MainScreen(Puzzle p) {
        super(p);
        puzzle = p;
    }

    @Override
    public void show() {
        super.show();
        Image theme = new Image(Assets.theme);
        float themeSize = Assets.WIDTH * 3 / 4;
        theme.setBounds((Assets.WIDTH - themeSize) / 2, Assets.HEIGHT / 2, themeSize, themeSize);

        float playSize = Assets.WIDTH / 3;
        float otherSize = Assets.WIDTH / 7;
        float btnPlayX = (Assets.WIDTH - playSize) / 2;
        float otherX = otherSize;
        float btnPlayY = Assets.HEIGHT / 3;
        ImageButton playBtn = new ImageButton(new TextureRegionDrawable(Assets.playBtn), new TextureRegionDrawable(Assets.playBtnDown));
        playBtn.setBounds(btnPlayX, btnPlayY, playSize, playSize);
        ImageButton helpBtn = new ImageButton(new TextureRegionDrawable(Assets.help), new TextureRegionDrawable(Assets.help));
        float y = btnPlayY - 3 * otherSize / 2;
        helpBtn.setBounds(otherX, y, otherSize, otherSize);
        ImageButton settingBtn = new ImageButton(new TextureRegionDrawable(Assets.setting), new TextureRegionDrawable(Assets.setting));
        settingBtn.setBounds(3 * otherSize, y, otherSize, otherSize);
        ImageButton exitBtn = new ImageButton(new TextureRegionDrawable(Assets.exit), new TextureRegionDrawable(Assets.exit));
        exitBtn.setBounds(5 * otherSize, y, otherSize, otherSize);

        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.setScreen(new LevelScreen(puzzle));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        settingBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.setScreen(new SettingScreen(puzzle));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        helpBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.setScreen(new HelpScreen(puzzle, MainScreen.this));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.getPEvent().exit(MainScreen.this);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        addActor(theme);
        addActor(playBtn);
        addActor(settingBtn);
        addActor(helpBtn);
        addActor(exitBtn);
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
        getGameFont().dispose();
        getOtherFont().dispose();
        getStage().dispose();
    }
}
