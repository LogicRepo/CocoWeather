package com.coco.cocoweather.Model.Today;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Singh on 23/4/19.
 */
public class Coord {

    @SerializedName("lon")
    @Expose
    private Float lon;
    @SerializedName("lat")
    @Expose
    private Float lat;

    public Coord(){

    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return new StringBuilder("[").append(this.lat).append(',').append(this.lon).append(']').toString();
    }

}