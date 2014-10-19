package com.ads.puzzle.flag.actors;

import com.ads.puzzle.flag.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Administrator on 2014/7/4.
 */
public class Piece extends Image {
    private int area = -1;
    private int orientation = 0;
    private int id;

    public Piece(int pieceId) {
        super(Assets.pieces[pieceId]);
        id = pieceId;
        setOrigin(Assets.PIECE_SIZE / 2, Assets.PIECE_SIZE / 2);
        return2BeginArea();
    }

    public void return2BeginArea() {
        float x_off = Assets.WIDTH / 2;
        float y_off = Assets.HEIGHT - (Assets.TOPBAR_HEIGHT + Assets.PIECE_SIZE * 2 + Assets.V_SPACE * 2);
        float x = 0;
        float y = 0;
        float size = Assets.SMALL_PIECE_SIZE;
        float space = size / 20;
        switch (id) {
            case 0:
                x = x_off;
                y = y_off - size;
                break;
            case 1:
                x = x_off + size + space;
                y = y_off - size;
                break;
            case 2:
                x = x_off;
                y = y_off - 2 * size - space;
                break;
            case 3:
                x = x_off + size + space;
                y = y_off - 2 * size - space;
        }
        setBounds(x, y, size, size);
        setArea(-1);
        setRotation(0);
        orientation = 0;
    }

    public void changeOrientation() {
        if (orientation == 3) {
            orientation = 0;
        } else {
            orientation++;
        }
        setRotation(orientation * 90);
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        setRotation(orientation * 90);
    }

    public int getId() {
        return id;
    }
}
