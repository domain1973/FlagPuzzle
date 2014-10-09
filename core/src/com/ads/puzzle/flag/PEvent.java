package com.ads.puzzle.flag;

import com.ads.puzzle.flag.screen.GameScreen;
import com.ads.puzzle.flag.screen.MainScreen;

/**
 * Created by Administrator on 2014/9/10.
 */
public abstract class PEvent {

    public abstract void pay();

    public abstract void exit(MainScreen mainScreen);

    public abstract void sos(GameScreen gs);

    public abstract void invalidateSos();

    public abstract void resetGame();

    public abstract void share();

    public abstract void install(String url);

    public abstract boolean isNetworkEnable();

    public abstract void save();
}
