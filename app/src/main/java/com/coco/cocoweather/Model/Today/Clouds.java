package com.coco.cocoweather.Model.Today;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Singh on 23/4/19.
 */
public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer all;

    public Clouds() {
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }


}