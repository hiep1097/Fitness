package com.example.hoang.fitness.utils;

import android.content.Context;

import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.models.WorkoutExercise;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    private static JsonUtil instance;
    private JsonUtil(){

    }

    public static JsonUtil getInstance(){
        if (instance==null){
            instance = new JsonUtil();
        }
        return instance;
    }


    public List<Workout> getListWorkout(Context context){
        String json=AssetsUtil.inputStreamToString(context,"jsondata/workout.json");
        Gson gson = new GsonBuilder().create();
        Workout[] list = gson.fromJson(json,Workout[].class);
        return Arrays.asList(list);
    }

    public Workout getWorkout(Context context, int id){
        List<Workout> list = getListWorkout(context);
        Workout workout = null;
        for (Workout w: list)
            if (w.getId()==id){
                workout = w;
            }
        return workout;
    }

    public List<Exercise> getListExercise(Context context,Workout workout){
        List<Exercise> list = new ArrayList<>();
        List<WorkoutExercise> workoutExerciseList = workout.getExercises();
        String json=AssetsUtil.inputStreamToString(context,"jsondata/exercise.json");
        Gson gson = new GsonBuilder().create();
        Exercise[] exercises = gson.fromJson(json,Exercise[].class);
        for (int i=0;i<workoutExerciseList.size();i++){
            for (int j=0;j<exercises.length;j++){
                if (workoutExerciseList.get(i).getId().equals(exercises[j].getId())){
                    list.add(exercises[j]);
                    break;
                }
            }
        }
        return list;
    }

    public List<Exercise> getListExercise(Context context, CustomWorkout workout){
        List<Exercise> list = new ArrayList<>();
        List<WorkoutExercise> workoutExerciseList = workout.getExercises();
        String json=AssetsUtil.inputStreamToString(context,"jsondata/exercise.json");
        Gson gson = new GsonBuilder().create();
        Exercise[] exercises = gson.fromJson(json,Exercise[].class);
        for (int i=0;i<workoutExerciseList.size();i++){
            for (int j=0;j<exercises.length;j++){
                if (workoutExerciseList.get(i).getId().equals(exercises[j].getId())){
                    list.add(exercises[j]);
                    break;
                }
            }
        }
        return list;
    }

    public List<Exercise> getListExercise(Context context){
        String json=AssetsUtil.inputStreamToString(context,"jsondata/exercise.json");
        Gson gson = new GsonBuilder().create();
        Exercise[] exercises = gson.fromJson(json,Exercise[].class);
        return Arrays.asList(exercises);
    }

    public Exercise getExercise(Context context, int id){
        List<Exercise> list = getListExercise(context);
        Exercise exercise = null;
        for (Exercise e: list)
            if (e.getId()==id){
                exercise = e;
            }
        return exercise;
    }

    public List<Exercise> getListExercise(Context context,String type){
        String json=AssetsUtil.inputStreamToString(context,"jsondata/exercise.json");
        Gson gson = new GsonBuilder().create();
        Exercise[] exercises = gson.fromJson(json,Exercise[].class);
        List<Exercise> list = new ArrayList<>();
        for (int i=0;i<exercises.length;i++)
            if (exercises[i].getBodyPart().equals(type)){
                list.add(exercises[i]);
            }
        return list;
    }


}
