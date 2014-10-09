package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/6/22.
 */
public class SettingScreen extends OtherScreen {
    private Puzzle game;
    private Image noMusic;
    private Image noSound;

    public SettingScreen(Puzzle game) {
        super(game);
        this.game = game;
        noSound = new Image(Assets.forbid);
        noMusic = new Image(Assets.forbid);
    }

    @Override
    public void show() {
        super.show();
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().setScreen(getPuzzle().getMainScreen());
                super.touchUp(event, x, y, pointer, button);
            }
        });

        float otherSize = Assets.WIDTH / 5;
        ImageButton music = new ImageButton(new TextureRegionDrawable(Assets.music), new TextureRegionDrawable(Assets.music));
        float x = (Assets.WIDTH - otherSize) / 2;
        float btn_y = Assets.HEIGHT * 2 / 3;
        music.setBounds(x, btn_y, otherSize, otherSize);
        noMusic.setBounds(music.getX(), music.getY(), otherSize, otherSize);
        ImageButton sound = new ImageButton(new TextureRegionDrawable(Assets.sound), new TextureRegionDrawable(Assets.sound));
        float btn_y1 = btn_y - otherSize * 3 / 2;
        sound.setBounds(x, btn_y1, otherSize, otherSize);
        noSound.setBounds(sound.getX(), sound.getY(), otherSize, otherSize);
        ImageButton about = new ImageButton(new TextureRegionDrawable(Assets.about), new TextureRegionDrawable(Assets.about));
        float btn_y2 = btn_y - otherSize * 3;
        about.setBounds(x, btn_y2, otherSize, otherSize);
        ImageButton resetGameBtn = new ImageButton(new TextureRegionDrawable(Assets.resetGameBtn), new TextureRegionDrawable(Assets.resetGameBtn));
        float btn_y3 = btn_y - otherSize * 4.5f;
        resetGameBtn.setBounds(x, btn_y3, otherSize, otherSize);

        music.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addActor(noMusic);
                Settings.musicEnabled = false;
                Assets.musicbg.stop();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        noMusic.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                noMusic.remove();
                Settings.musicEnabled = true;
                Assets.musicbg.play();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        sound.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addActor(noSound);
                Settings.soundEnabled = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        noSound.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                noSound.remove();
                Settings.soundEnabled = true;
                super.touchUp(event, x, y, pointer, button);
            }
        });
        about.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new AboutScreen(game, SettingScreen.this));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        resetGameBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getPuzzle().getPEvent().resetGame();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(music);
        addActor(sound);
        if (!Settings.musicEnabled) {
            addActor(noMusic);
        }
        if (!Settings.soundEnabled) {
            addActor(noSound);
        }
        addActor(resetGameBtn);
        addActor(about);
    }
}
