package com.ads.puzzle.flag.screen;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.Puzzle;
import com.ads.puzzle.flag.Settings;
import com.ads.puzzle.flag.actors.Gate;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/6/22.
 */
public class GateScreen extends OtherScreen {
    private int level;
    private int starNum;

    public GateScreen(Puzzle puzzle, int lv) {
        super(puzzle);
        level = lv;
    }

    @Override
    public void show() {
        super.show();
        buildGateImage();
        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                Assets.playSound(Assets.btnSound);
                getPuzzle().setScreen(new LevelScreen(getPuzzle(), level));
                return true;
            }
        });
        String str = starNum + "/36";
        float w = getOtherFont().getBounds(str).width;
        getStarLabel().setBounds((Assets.WIDTH - Assets.TOPBAR_HEIGHT - w), getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        getStarLabel().setText(str);
    }

    private void buildGateImage() {
        float gateBtnSize = Assets.WIDTH / 5;
        for (int i = 0; i < Answer.GATE_MAX; i++) {
            int gateNum = level * Answer.GATE_MAX + i;
            TextureRegion gateTRegion = null;
            if (Settings.unlockGateNum >= gateNum || gateNum == 0) {
                int num = Answer.gateStars.get(gateNum);
                starNum = starNum + num;
                switch (num) {
                    case 0:
                        gateTRegion = Assets.gate_0star;
                        break;
                    case 1:
                        gateTRegion = Assets.gate_1star;
                        break;
                    case 2:
                        gateTRegion = Assets.gate_2star;
                        break;
                    case 3:
                        gateTRegion = Assets.gate_3star;
                        break;
                }
            } else {
                gateTRegion = Assets.gate_lock;
            }
            TextureRegionDrawable imageUp = new TextureRegionDrawable(gateTRegion);
            imageUp.setMinWidth(gateBtnSize);
            imageUp.setMinHeight(gateBtnSize);
            final Gate gate = new Gate(imageUp, i, gateNum);
            gate.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {
                    Assets.playSound(Assets.btnSound);
                    if (gate.getGateNum() <= Settings.unlockGateNum) {
                        getPuzzle().setScreen(new GameScreen(getPuzzle(), level, gate.getGateNum()));
                    }
                    return true;
                }
            });
            addActor(gate);
        }
    }
}
