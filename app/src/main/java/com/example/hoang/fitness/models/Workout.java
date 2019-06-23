package com.example.hoang.fitness.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workout implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("pic_pad")
    @Expose
    private String picPad;
    @SerializedName("pic_phone")
    @Expose
    private String picPhone;
    @SerializedName("brief")
    @Expose
    private String brief;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("exercises")
    @Expose
    private List<WorkoutExercise> exercises = null;
    @SerializedName("calorie")
    @Expose
    private Integer calorie;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPicPad() {
        return picPad;
    }

    public void setPicPad(String picPad) {
        this.picPad = picPad;
    }

    public String getPicPhone() {
        return picPhone;
    }

    public void setPicPhone(String picPhone) {
        this.picPhone = picPhone;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
