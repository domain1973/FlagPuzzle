package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.listener.LevelDetector;
import com.ads.puzzle.flag.listener.LevelListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2014/6/22.
 */
public class LevelScreen extends OtherScreen {
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private float lockX;
    private float lockY;
    private float lockSize;
    private float levelSize;
    private int level;
    private LevelListener levelListener;
    private float targetposition;
    private float initPosition = Integer.MAX_VALUE;
    private boolean isAddLock;

    public LevelScreen(Puzzle puzzle) {
        super(puzzle);
    }

    public LevelScreen(Puzzle puzzle, int lv) {
        super(puzzle);
        level = lv;
        initPosition = -Assets.WIDTH * level;
    }

    @Override
    public void show() {
        super.show();
        lockSize = Assets.WIDTH / 3;
        lockX = (Assets.WIDTH - lockSize) / 2;
        lockY = (Assets.HEIGHT - lockSize) / 2;

        levelSize = Assets.WIDTH;
        float btnSize = Assets.WIDTH / 5;
        leftBtn = new ImageButton(new TextureRegionDrawable(Assets.levelPreBtn));
        leftBtn.setBounds(0, (Assets.HEIGHT - btnSize) / 2, btnSize, btnSize);
        rightBtn = new ImageButton(new TextureRegionDrawable(Assets.levelNextBtn));
        rightBtn.setBounds(Assets.WIDTH - btnSize, (Assets.HEIGHT - btnSize) / 2, btnSize, btnSize);
        btnVisiableHandle();
        setListens();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(getStage());
        levelListener = new LevelListener(getStage(), getPuzzle());
        if (initPosition != Integer.MAX_VALUE) {
            levelListener.setPosition(initPosition);
        }
        multiplexer.addProcessor(new LevelDetector(getStage(), levelListener));
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void lock() {
        int tLevel = Settings.unlockGateNum / Answer.GATE_MAX;
        if (level > tLevel && level != Assets.LEVEL_MAX) {
            isAddLock = true;
        } else {
            isAddLock = false;
        }
    }

    private void setListens() {
        leftBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                if (level != 0) {
                    level--;
                    levelListener.setPosition(-Assets.WIDTH * level);
                    lock();
                }
                return true;
            }
        });
        rightBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                if (level != Assets.LEVEL_MAX) {
                    level++;
                    levelListener.setPosition(-Assets.WIDTH * level);
                    lock();
                }
                return true;
            }
        });
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
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getBatch().begin();
        float position = levelListener.getPosition();
        for (int i = 0; i < Assets.levels.size(); i++) {
            getBatch().draw(Assets.levels.get(i), position + i * levelSize, (Assets.HEIGHT - levelSize) / 2, levelSize, levelSize);
            if (isAddLock) {
                getBatch().draw(Assets.lock, lockX, lockY, lockSize, lockSize);
            }
        }
        getBatch().end();
        //用户放手时才进行回弹
        if (!Gdx.input.isTouched()) {
            //巧妙的用一个取整数的函数来表示一张图片翻过一半的时候就应该跳转到下一张图片的效果
            targetposition = (int) ((position - 0.5f * levelSize) / levelSize) * levelSize;
            float v = 20;
            if (targetposition < position) {
                //注意不能越界，min/Assets.WIDTH是一个因子，用于分辨率适配，让回弹速度和分辨率搭配得协调一些
                if (position - v < targetposition) {
                    position = targetposition;
                } else {
                    position -= v;
                }
            }
            if (targetposition > position) {
                //注意不能越界
                if (position + v > targetposition)
                    position = targetposition;
                else
                    position += v;
            }
            levelListener.setPosition(position);
            level = (int) Math.abs(position / Assets.WIDTH);
            lock();
        }
    }

    private void btnVisiableHandle() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            public void run() {
                computer();
            }

            private void computer() {
                if (level == 0) {
                    leftBtn.remove();
                    if (rightBtn.getStage() == null) {
                        addActor(rightBtn);
                    }
                } else if (level == Assets.LEVEL_MAX) {
                    rightBtn.remove();
                } else {
                    if (rightBtn.getStage() == null) {
                        addActor(rightBtn);
                    }
                    if (leftBtn.getStage() == null) {
                        addActor(leftBtn);
                    }
                }
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }
}
