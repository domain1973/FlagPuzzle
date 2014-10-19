package com.ads.puzzle.flag.actors;

import com.ads.puzzle.flag.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.List;

/**
 * Created by Administrator on 2014/7/6.
 */
public class Area extends Group {
    private int id;
    private int pieceId;
    private float pieceSize;
    private float x5;
    private float x4;
    private float x3;
    private float x2;
    private float x1;
    private float x0;
    private float y5;
    private float y4;
    private float y3;
    private float y2;
    private float y1;
    private float y0;
    private float spriteSize;
    private float x_bg;
    private float y_bg;
    private List<Sprite> sprites;

    /**
     * 0,1
     * 2,3
     *
     * @param areaId
     */
    public Area(int areaId, int lv) {
        id = areaId;
        pieceId = -1;
        pieceSize = Assets.PIECE_SIZE;
        spriteSize = Assets.SPRITE_SIZE;
        sprites = Assets.levelSpriteMap.get(lv);
        float top0 = Assets.HEIGHT - pieceSize - Assets.TOPBAR_HEIGHT;
        if (areaId == 0) {
            x_bg = Assets.H_SPACE;
            y_bg = top0;
        } else if (areaId == 1) {
            x_bg = 2 * Assets.H_SPACE + pieceSize;
            y_bg = top0;
        } else {
            float top1 = top0 - pieceSize - Assets.V_SPACE;
            if (areaId == 2) {
                x_bg = Assets.H_SPACE;
                y_bg = top1;
            } else {
                x_bg = 2 * Assets.H_SPACE + pieceSize;
                y_bg = top1;
            }
        }
        setBounds(x_bg, y_bg, pieceSize, pieceSize);

        x0 = Assets.H_SPACE;
        x1 = x0 + spriteSize;
        x2 = x1 + spriteSize;
        x3 = x2 + Assets.H_SPACE + spriteSize;
        x4 = x3 + spriteSize;
        x5 = x4 + spriteSize;
        y5 = Assets.HEIGHT - Assets.TOPBAR_HEIGHT - spriteSize;;
        y4 = y5 - spriteSize;
        y3 = y4 - spriteSize;
        y2 = y3 - Assets.V_SPACE - spriteSize;
        y1 = y2 - spriteSize;
        y0 = y1 - spriteSize;
    }

    public int getId() {
        return id;
    }

    public int getPieceId() {
        return pieceId;
    }

    public void setPieceId(int pieceId) {
        this.pieceId = pieceId;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(Assets.areaBg, x_bg, y_bg, pieceSize, pieceSize);
        drawFixSprites(batch);
    }

    private void drawFixSprites(Batch batch) {
        switch (id) {
            case 0:
                batch.draw(sprites.get(0), x0, y5, spriteSize, spriteSize);
                batch.draw(sprites.get(2), x0, y4, spriteSize, spriteSize);
                batch.draw(sprites.get(4), x0, y3, spriteSize, spriteSize);
                batch.draw(sprites.get(3), x1, y4, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x1, y3, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x2, y5, spriteSize, spriteSize);
                batch.draw(sprites.get(2), x2, y4, spriteSize, spriteSize);
                batch.draw(sprites.get(0), x2, y3, spriteSize, spriteSize);
                break;
            case 1:
                batch.draw(sprites.get(3), x3, y5, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x3, y3, spriteSize, spriteSize);
                batch.draw(sprites.get(2), x4, y4, spriteSize, spriteSize);
                batch.draw(sprites.get(4), x4, y3, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x5, y5, spriteSize, spriteSize);
                batch.draw(sprites.get(0), x5, y4, spriteSize, spriteSize);
                batch.draw(sprites.get(3), x5, y3, spriteSize, spriteSize);
                break;
            case 2:
                batch.draw(sprites.get(4), x0, y2, spriteSize, spriteSize);
                batch.draw(sprites.get(3), x0, y1, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x0, y0, spriteSize, spriteSize);
                batch.draw(sprites.get(0), x1, y2, spriteSize, spriteSize);
                batch.draw(sprites.get(2), x2, y2, spriteSize, spriteSize);
                batch.draw(sprites.get(3), x2, y1, spriteSize, spriteSize);
                batch.draw(sprites.get(4), x2, y0, spriteSize, spriteSize);
                break;
            case 3:
                batch.draw(sprites.get(3), x3, y0, spriteSize, spriteSize);
                batch.draw(sprites.get(4), x4, y1, spriteSize, spriteSize);
                batch.draw(sprites.get(0), x4, y0, spriteSize, spriteSize);
                batch.draw(sprites.get(1), x5, y1, spriteSize, spriteSize);
                batch.draw(sprites.get(2), x5, y0, spriteSize, spriteSize);
                break;
        }
    }
}
