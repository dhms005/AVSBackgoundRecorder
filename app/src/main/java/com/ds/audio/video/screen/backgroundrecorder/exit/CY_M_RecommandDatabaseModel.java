package com.ds.audio.video.screen.backgroundrecorder.exit;

/**
 * Created by s4ittech on 01/12/17.
 */

public class CY_M_RecommandDatabaseModel {
    private int id;
    private String cur_date;
    private String data;
    private String name;
    private String icon,icon2,icon3;
    private String link;
    private String type;
    private float rating;

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getIcon3() {
        return icon3;
    }

    public void setIcon3(String icon3) {
        this.icon3 = icon3;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCur_date(String cur_date) {
        this.cur_date = cur_date;
    }

    public String getCur_date() {
        return cur_date;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }
}
