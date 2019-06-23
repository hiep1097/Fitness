package com.example.hoang.fitness.models;

import java.io.Serializable;
import java.util.List;

public class CustomWorkout implements Serializable {
    private String name;
    private String level;
    private String picPad;
    private String picPhone;
    private String brief;
    private Integer order;
    private Integer time;
    private List<WorkoutExercise> exercises = null;
    private Integer calorie;
    private String type;
    private Integer id;
    private int circuit;
    private int workoutRestTime;


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

    public int getCircuit() {
        return circuit;
    }

    public void setCircuit(int circuit) {
        this.circuit = circuit;
    }

    public int getWorkoutRestTime() {
        return workoutRestTime;
    }

    public void setWorkoutRestTime(int workoutRestTime) {
        this.workoutRestTime = workoutRestTime;
    }
}
