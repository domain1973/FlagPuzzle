package com.ads.puzzle.flag.listener;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.screen.GateScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Created by Administrator on 2014/8/2.
 */
public class LevelListener extends GestureDetector.GestureAdapter {
    private Stage stage;
    private Puzzle puzzle;
    private Vector3 touchPoint;
    //用来标记第一大关图片的位置，可以为负，无法显示的部分就不会被画出
    private float position;
    private float prex;
    private float currentx;

    public LevelListener(Stage s, Puzzle game) {
        stage = s;
        puzzle = game;
        touchPoint = new Vector3();
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        currentx = x;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        stage.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // 坐标转化
        int level = (int) Math.abs(position / 480);
        int tLevel = Settings.unlockGateNum / Answer.GATE_MAX;
        if (level <= tLevel && level != Assets.LEVEL_MAX) {
            float v = Assets.WIDTH - 2 * Assets.LEVEL_IMAGE_OFF_SIZE;
            Rectangle bounds = new Rectangle(Assets.LEVEL_IMAGE_OFF_SIZE, (Assets.HEIGHT - Assets.WIDTH) / 2 + Assets.LEVEL_IMAGE_OFF_SIZE, v, v);
            if (bounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.btnSound);
                puzzle.setScreen(new GateScreen(puzzle, level));
            }
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //position应该有一定的范围
        float m = -(Assets.levels.size() - 1) * Assets.LEVEL_IMAGE_SIZE;
        if (position <= 0 && position >= m) {
            prex = currentx;
            currentx = x;
            float n = position + currentx - prex;
            if (n <= 0 && n >= m)
                position += currentx - prex;
            else {
                if (n > 0)
                    position = 0;
                if (n < m)
                    position = m;
            }
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
