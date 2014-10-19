package com.ads.puzzle.flag.controller;

import com.ads.puzzle.flag.Assets;
import com.ads.puzzle.flag.actors.Area;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Administrator on 2014/7/6.
 */
public class AreaController extends IController {
    private Area[] areas;
    private Rectangle bounds;

    public AreaController(int level, String name) {
        setName(name);
        areas = new Area[4];
        buildArea(level);
    }

    public void buildArea(int level) {
        for (Area area : areas) {
            if (area != null) {
                area.remove();
            }
        }
        for (int i = 0; i < 4; i++) {
            areas[i] = new Area(i, level);
            addActor(areas[i]);
        }
        //area有效区域
        bounds = new Rectangle(areas[2].getX(), areas[2].getY(), Assets.PIECE_SIZE * 2, Assets.PIECE_SIZE * 2);
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public Area[] getAreas() {
        return areas;
    }

    @Override
    public void handler() {
        SnapshotArray<Actor> actors = getChildren();
        for (Actor actor : actors) {
            Area area = (Area) actor;
            area.setPieceId(-1);
        }
    }
}
