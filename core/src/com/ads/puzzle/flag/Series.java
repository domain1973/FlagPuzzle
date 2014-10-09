package com.ads.puzzle.flag;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/27.
 */
public class Series {

    private Image image;

    private Image readmeImage;

    private String url;

    public Image getImage() {
        return image;
    }

    public Series setImage(Image image) {
        this.image = image;
        return this;
    }

    public Image getReadmeImage() {
        return readmeImage;
    }

    public Series setReadmeImage(Image readmeImage) {
        this.readmeImage = readmeImage;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Series setUrl(String url) {
        this.url = url;
        return this;
    }
}
