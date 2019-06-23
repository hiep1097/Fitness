package com.example.hoang.fitness.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkoutExercise implements Serializable {

    @SerializedName("get_ready")
    @Expose
    private Integer getReady;
    @SerializedName("switch_side")
    @Expose
    private Boolean switchSide;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("time_span")
    @Expose
    private Integer timeSpan;

    public Integer getGetReady() {
        return getReady;
    }

    public void setGetReady(Integer getReady) {
        this.getReady = getReady;
    }

    public Boolean getSwitchSide() {
        return switchSide;
    }

    public void setSwitchSide(Boolean switchSide) {
        this.switchSide = switchSide;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(Integer timeSpan) {
        this.timeSpan = timeSpan;
    }

}