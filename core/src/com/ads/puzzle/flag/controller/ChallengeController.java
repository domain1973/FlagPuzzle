package com.ads.puzzle.flag.controller;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.actors.Challenge;

/**
 * Created by Administrator on 2014/7/5.
 */
public class ChallengeController extends IController {
    private Challenge challenge;
    private int level;
    private int gateNum;

    public ChallengeController(int level, int gateNum, String name) {
        setName(name);
        this.level = level;
        this.gateNum = gateNum;
        challenge = new Challenge(level, gateNum);
        addActor(challenge);
    }

    @Override
    public void handler() {
        challenge.remove();
        gateNum++;
        if (Answer.isLasterSmallGate(gateNum)) {
            level++;
        }
        if (level < Assets.LEVEL_MAX) {
            challenge = new Challenge(level, gateNum);
            addActor(challenge);
        }
    }

    public int getGateNum() {
        return gateNum;
    }
}
