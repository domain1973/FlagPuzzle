package com.ads.puzzle.flag.window;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.screen.AboutScreen;
import com.ads.puzzle.flag.screen.GameScreen;
import com.ads.puzzle.flag.screen.GateScreen;
import com.ads.puzzle.flag.screen.HelpScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


/**
 * Created by Administrator on 2014/7/20.
 */
public class SupsendWin extends BaseWin {
    private Puzzle puzzle;
    private GameScreen gameScreen;
    private float win_H;
    private float win_Y;
    private int level;
    private Image noMusic;
    private Image noSound;

    public SupsendWin(Puzzle p, BitmapFont ft, GameScreen gs, int lv) {
        super("", new Window.WindowStyle(ft, Color.WHITE, new TextureRegionDrawable(
                Assets.gameBg)));
        puzzle = p;
        gameScreen = gs;
        level = lv;
        create();
    }

    public void create() {
        final float btnSize = Assets.WIDTH / 6;
        gameScreen.getStage().addActor(layerBg);
        win_H = btnSize * 4;
        win_Y = (Assets.HEIGHT - win_H) / 2;
        setBounds(0, win_Y, Assets.WIDTH, win_H);
        float y2 = win_H / 5;
        float y1 = win_H / 5 * 3;
        float x = Assets.WIDTH / 6;

        noSound = new Image(Assets.forbid);
        ImageButton gate = new ImageButton(new TextureRegionDrawable(Assets.gate), new TextureRegionDrawable(Assets.gate));
        gate.setBounds(x, y1, btnSize, btnSize);
        ImageButton share = new ImageButton(new TextureRegionDrawable(Assets.share), new TextureRegionDrawable(Assets.share));
        share.setBounds(x * 2, y1, btnSize, btnSize);
        ImageButton buy = new ImageButton(new TextureRegionDrawable(Assets.buy), new TextureRegionDrawable(Assets.buy));
        buy.setBounds(x * 3, y1, btnSize, btnSize);
        ImageButton continues = new ImageButton(new TextureRegionDrawable(Assets.continueTr), new TextureRegionDrawable(Assets.continueTr));
        continues.setBounds(x * 4, y1, btnSize, btnSize);

        final ImageButton music = new ImageButton(new TextureRegionDrawable(Assets.music), new TextureRegionDrawable(Assets.music));
        music.setBounds(x, y2, btnSize, btnSize);
        noMusic = new Image(Assets.forbid);
        noMusic.setBounds(music.getX(), music.getY(), btnSize, btnSize);
        ImageButton sound = new ImageButton(new TextureRegionDrawable(Assets.sound), new TextureRegionDrawable(Assets.sound));
        sound.setBounds(x * 2, y2, btnSize, btnSize);
        noSound = new Image(Assets.forbid);
        noSound.setBounds(sound.getX(), sound.getY(), btnSize, btnSize);
        ImageButton about = new ImageButton(new TextureRegionDrawable(Assets.about), new TextureRegionDrawable(Assets.about));
        about.setBounds(x * 3, y2, btnSize, btnSize);
        ImageButton help = new ImageButton(new TextureRegionDrawable(Assets.help), new TextureRegionDrawable(Assets.help));
        help.setBounds(x * 4, y2, btnSize, btnSize);
        Gdx.input.setInputProcessor(gameScreen.getStage());

        share.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.getPEvent().share();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        buy.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.getPEvent().pay();
                super.touchUp(event, x, y, pointer, button);
            }
        });
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
                puzzle.setScreen(new AboutScreen(puzzle, gameScreen));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        help.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.setScreen(new HelpScreen(puzzle, gameScreen));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        gate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                puzzle.setScreen(new GateScreen(puzzle, level));
                super.touchUp(event, x, y, pointer, button);
            }
        });
        continues.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.resumeTimer();
                layerBg.remove();
                SupsendWin.this.remove();
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
        addActor(about);
        addActor(help);
        addActor(gate);
        addActor(share);
        addActor(buy);
        addActor(continues);
    }
}
