package com.example.hoang.fitness.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Target implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("workout")
    @Expose
    private Workout workout;
    @SerializedName("numDay")
    @Expose
    private int numDay;
    @SerializedName("state")
    @Expose
    private int state;
    @SerializedName("hour")
    @Expose
    private String hour;
    @SerializedName("minute")
    @Expose
    private String minute;
    @SerializedName("am_pm")
    @Expose
    private String am_pm;
    public Target() {
    }

    public Target(String name, Workout workout, int numDay, String hour, String minute, String am_pm, int state) {
        this.name = name;
        this.workout = workout;
        this.numDay = numDay;
        this.state = state;
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public int getNumDay() {
        return numDay;
    }

    public void setNumDay(int numDay) {
        this.numDay = numDay;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
