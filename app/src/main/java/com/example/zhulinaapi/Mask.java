package com.example.zhulinaapi;

public class Mask {

    private int id;
    private String day;
    private String wotkout;
    private String trainer;
    private String image;

    public Mask(int id, String day, String wotkout, String trainer, String image) {
        this.id = id;
        this.day = day;
        this.wotkout = wotkout;
        this.trainer = trainer;
        this.image = image;
    }

    public Mask(String day, String wotkout, String trainer, String image) {
        this.day = day;
        this.wotkout = wotkout;
        this.trainer = trainer;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setday(String day) {
        this.day = day;
    }

    public void setwotkout(String wotkout) {
        this.wotkout = wotkout;
    }

    public void settrainer(String trainer) {
        this.trainer = trainer;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getday() {
        return day;
    }

    public String getwotkout() {
        return wotkout;
    }

    public String gettrainer() {
        return trainer;
    }

    public String getImage() {
        return image;
    }
}