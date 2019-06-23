package com.example.hoang.fitness.models;

public class ExercisePick {
    private Exercise exercise;
    private boolean isChoose;

    public ExercisePick(Exercise exercise, boolean isChoose) {
        this.exercise = exercise;
        this.isChoose = isChoose;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
