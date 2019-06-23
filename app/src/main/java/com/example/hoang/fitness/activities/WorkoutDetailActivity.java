package com.example.hoang.fitness.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.ExerciseAdapter;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.AssetsUtil;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.img_workout)
    ImageView mImage;
    @BindView(R.id.tv_workout_name)
    TextView mName;
    @BindView(R.id.tv_workout_detail)
    TextView mDetail;
    @BindView(R.id.tv_workout_description)
    TextView mDes;
    @BindView(R.id.tv_workout_exercises_count)
    TextView mCount;
    @BindView(R.id.rcv_exercise)
    RecyclerView mExercises;
    @BindView(R.id.btn_start_workout)
    Button mStart;
    private ExerciseAdapter adapter;
    private List<Exercise> list;
    private Workout workout;
    private int WORKOUT_ID;
    private String WORKOUT_NAME;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);
        WORKOUT_ID = getIntent().getIntExtra("WORKOUT_ID",0);
        if (WORKOUT_ID==0) WORKOUT_NAME = getIntent().getStringExtra("WORKOUT_NAME");
        workout = JsonUtil.getInstance().getWorkout(this,WORKOUT_ID);
        mImage.setImageDrawable(AssetsUtil.getDrawable(this,"workout_pic/"+workout.getPicPhone()));
        mName.setText(workout.getName());
        if (WORKOUT_ID==0) {
            mDetail.setText(workout.getLevel()+" • "+ workout.getTime()+" mins • "+ workout.getCalorie()+" calories");
        } else {
            mDetail.setText(workout.getLevel()+" • "+ workout.getTime()+" mins • "+ workout.getCalorie()+" calories");
        }
        mDes.setText(workout.getBrief());
        mCount.setText(workout.getExercises().size()+" EXERCISES");
        list = JsonUtil.getInstance().getListExercise(this,workout);
        adapter = new ExerciseAdapter(this,list);
        mExercises.setAdapter(adapter);
        mExercises.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mExercises.setNestedScrollingEnabled(false);
        mExercises.setHasFixedSize(false);
        mStart.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(WorkoutDetailActivity.this,PlayingActivity.class);
        intent.putExtra("WORKOUT_ID",WORKOUT_ID);
        startActivity(intent);
    }
}
