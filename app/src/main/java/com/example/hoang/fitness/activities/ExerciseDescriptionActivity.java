package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.ExerciseDescriptionAdapter;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.utils.JsonUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseDescriptionActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    @BindView(R.id.tv_exercise_name)
    TextView mName;
    @BindView(R.id.youtube_player)
    YouTubePlayerView mYoutube;
    @BindView(R.id.rcv_exercise_description)
    RecyclerView mDescriptions;
    private ExerciseDescriptionAdapter adapter;
    private List<String> list;
    private final String GOOGLE_API_KEY = "AIzaSyAMH4PgslRvIsem8jmbjvmRSlf7l3bYEDg";
    private String YOUTUBE_VIDEO_ID;
    private Exercise exercise;
    private int EXERCISE_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_exercise);
        ButterKnife.bind(this);
        EXERCISE_ID  = getIntent().getIntExtra("EXERCISE_ID",0);
        exercise = JsonUtil.getInstance().getExercise(this,EXERCISE_ID);
        mName.setText(exercise.getName());
        YOUTUBE_VIDEO_ID = exercise.getVideoLink().replace("https://www.youtube.com/watch?v=","");
        mYoutube.initialize(GOOGLE_API_KEY,this);
        list = exercise.getDescription();
        adapter = new ExerciseDescriptionAdapter(this,list);
        mDescriptions.setAdapter(adapter);
        mDescriptions.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this,youTubeInitializationResult.toString(),Toast.LENGTH_SHORT).show();
    }
}
