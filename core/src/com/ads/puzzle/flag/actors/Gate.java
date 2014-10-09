package com.ads.puzzle.flag.actors;

import com.ads.puzzle.flag.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Administrator on 2014/7/5.
 */
public class Gate extends ImageButton {
    private int id;
    private int gateNum;

    public Gate(Drawable imageUp, int id, int gateNum) {
        super(imageUp);
        this.id = id;
        this.gateNum = gateNum;
        float gateBtnSize = Assets.WIDTH / 5;
        float gateBtnSpace = gateBtnSize / 5;
        float y_off = Gdx.graphics.getHeight() / 3;
        float hspace = y_off / 2;
        setPosition((id % 4 + 1) * gateBtnSpace + id % 4 * gateBtnSize, Assets.HEIGHT - y_off - id / 4 * hspace);
    }

    public int getId() {
        return id;
    }

    public int getGateNum() {
        return gateNum;
    }
}
