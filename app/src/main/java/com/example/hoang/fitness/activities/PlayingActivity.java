package com.example.hoang.fitness.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.sound.SoundManager;
import com.example.hoang.fitness.utils.FileUtil;
import com.example.hoang.fitness.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    @BindView(R.id.tv_count_time)
    TextView mTotalTime;
    @BindView(R.id.gift_view)
    GifImageView mGiftView;
    @BindView(R.id.btn_exit)
    Button mExit;
    @BindView(R.id.btn_detail)
    Button mDetail;
    @BindView(R.id.btn_volume)
    Button mVolume;
    @BindView(R.id.tv_exercise_name)
    TextView mName;
    @BindView(R.id.tv_state)
    TextView mState;
    @BindView(R.id.tv_count_down_time)
    TextView mCountDownTime;
    @BindView(R.id.btn_previous)
    Button mPrevious;
    @BindView(R.id.btn_pause)
    Button mPause;
    @BindView(R.id.btn_play)
    Button mPlay;
    @BindView(R.id.btn_next)
    Button mNext;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    int totalTime = 0;
    int readyTime = 0;
    int spanTime = 0;
    int currentPos = 0;
    boolean isFinish = false;
    boolean isPlaying = true;
    List<Exercise> list = new ArrayList<>();
    private int WORKOUT_ID;
    private String WORKOUT_NAME="";
    private String TARGET_NAME;
    Thread thread;
    SoundManager soundManager;
    MediaPlayer mediaPlayer;
    List<CustomWorkout> customWorkouts;
    CustomWorkout workout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        soundManager = new SoundManager(this);
        soundManager.setup();
        WORKOUT_ID = getIntent().getIntExtra("WORKOUT_ID", 0);
        WORKOUT_NAME = getIntent().getStringExtra("WORKOUT_NAME");
        TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
        try {
            WORKOUT_NAME.isEmpty();
        } catch (NullPointerException e){
            WORKOUT_NAME = "";
        }
        if (!WORKOUT_NAME.isEmpty()){
            customWorkouts = FileUtil.docFileCustomWorkout(this,"customworkout.txt");
            for (int i=0; i<customWorkouts.size();i++)
                if (WORKOUT_NAME.equals(customWorkouts.get(i).getName())) {
                    workout = customWorkouts.get(i);
                    List<Exercise> list = JsonUtil.getInstance().getListExercise(this,workout);
                    for (int j=0;j<workout.getCircuit();j++) this.list.addAll(list);
                    break;
                }
        } else {
            workout = new CustomWorkout();
            Workout w = JsonUtil.getInstance().getWorkout(this, WORKOUT_ID);
            list = JsonUtil.getInstance().getListExercise(this, w);
            workout.setBrief(w.getBrief());
            workout.setCalorie(w.getCalorie());
            workout.setCircuit(1);
            workout.setExercises(w.getExercises());
            workout.setId(w.getId());
            workout.setLevel(w.getLevel());
            workout.setName(w.getName());
            workout.setOrder(w.getOrder());
            workout.setPicPad(w.getPicPad());
            workout.setPicPhone(w.getPicPhone());
            workout.setTime(w.getTime());
            workout.setType(w.getType());
            workout.setWorkoutRestTime(0);
        }
        updateValue();
        updateView();
        mExit.setOnClickListener(this::onClick);
        mDetail.setOnClickListener(this::onClick);
        mVolume.setOnClickListener(this::onClick);
        mPrevious.setOnClickListener(this::onClick);
        mPause.setOnClickListener(this::onClick);
        mPlay.setOnClickListener(this::onClick);
        mNext.setOnClickListener(this::onClick);
        try {
            if (thread==null){
                thread = new Thread(this::run);
                thread.start();
            }
        } catch (Exception e){
            thread = new Thread(this::run);
            thread.start();
        }


    }

    private void updateView() {
        int countdowntime = (readyTime > 0 ? readyTime : spanTime);
        mCountDownTime.setText("00:" + (countdowntime < 10 ? "0" + countdowntime : countdowntime));
        mTotalTime.setText((totalTime / 60 < 10 ? "0" + totalTime / 60 : totalTime / 60) + ":" + (totalTime % 60 < 10 ? "0" + totalTime % 60 : totalTime % 60));
        if (readyTime > 0) {
            mState.setText("Get Ready");
        } else {
            if (currentPos == list.size() - 1) {
                mState.setText("Next up: Workout Complete!");
            } else {
                mState.setText("Next up: " + list.get(currentPos + 1).getName());
            }
        }
        if (readyTime == 0 && spanTime == 0) {   //next
            currentPos++;
            if (currentPos == list.size()) isFinish = true;
            else updateValue();
        }
    }

    public void updateValue() {
        if (workout.getWorkoutRestTime()>0) {
            readyTime = workout.getWorkoutRestTime();
        } else {
            readyTime = workout.getExercises().get(currentPos%12).getGetReady();
        }
        spanTime = workout.getExercises().get(currentPos%12).getTimeSpan();

//        readyTime = 3;
//        spanTime = 5;
        GifDrawable gifFromAssets = null;
        try {
            gifFromAssets = new GifDrawable(getAssets(), "exercise_gif/" + list.get(currentPos).getGifPhone());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mGiftView.setImageDrawable(gifFromAssets);
        mName.setText(list.get(currentPos).getName());
        changeProgress(progressBar, currentPos);
        if (currentPos == 0) {
            soundManager.isPlaying = true;
            soundManager.get_ready.start();
            soundManager.get_ready.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 500);
                    soundManager.nextStep.start();
                    soundManager.nextStep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            initMedia();
                        }
                    });
                }
            });
            soundManager.isPlaying = false;
        } else {
            soundManager.isPlaying = true;
            soundManager.nextStep.start();
            soundManager.nextStep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 500);
                    initMedia();
                }
            });
            soundManager.isPlaying = false;
        }
    }

    private int getProgressValue(int i) {
        return 10000 / list.size() * i;
    }

    private void changeProgress(ProgressBar progressBar, int i) {
        progressBar.setProgress(getProgressValue(i));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                Intent intent1 = new Intent(PlayingActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_detail:
                Intent intent = new Intent(PlayingActivity.this, ExerciseDescriptionActivity.class);
                intent.putExtra("EXERCISE_ID", list.get(currentPos).getId());
                PlayingActivity.this.startActivity(intent);
                break;
            case R.id.btn_volume:
                //TODO
                break;
            case R.id.btn_previous:
                if (currentPos > 0) {
                    currentPos--;
                    updateValue();
                }
                break;
            case R.id.btn_pause:
                isPlaying = false;
                mPlay.setVisibility(View.VISIBLE);
                mPause.setVisibility(View.GONE);
                break;
            case R.id.btn_play:
                isPlaying = true;
                mPause.setVisibility(View.VISIBLE);
                mPlay.setVisibility(View.GONE);
                break;
            case R.id.btn_next:
                if (currentPos < list.size() - 1) {
                    currentPos++;
                    updateValue();
                }
                break;
        }
    }

    @Override
    public void run() {
        try {
            while (!isFinish) {
                totalTime++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPos < list.size()) updateView();
                    }
                });
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isPlaying) {
                    if (readyTime > 0) {
                        readyTime--;
                    } else {
                        spanTime--;
                    }
                    if (readyTime == 3 || spanTime == 3) {
                        soundManager.three.start();
                    } else if (readyTime == 2 || spanTime == 2) {
                        soundManager.two.start();
                    } else if (readyTime == 1 || spanTime == 1) {
                        soundManager.one.start();
                    } else if (readyTime > 0 || spanTime > 0) {
                        if (spanTime > 0) {
                            if (spanTime == workout.getExercises().get(currentPos%12).getTimeSpan() / 2) {
                                soundManager.halfwaythere.start();
                            } else if (readyTime == 0 && spanTime == workout.getExercises().get(currentPos%12).getTimeSpan()) {
                                soundManager.begin.start();
                            } else if (readyTime == 0 && spanTime == workout.getExercises().get(currentPos%12).getTimeSpan()-1) {
                                soundManager.ding.start();
                            } else if (readyTime == 0 && spanTime == workout.getExercises().get(currentPos%12).getTimeSpan()-2) {
                                initMedia();
                            }
                        }
                        if (!soundManager.isPlaying) soundManager.start(soundManager.countdown);
                    }
                }
            }
            soundManager.congratulations.start();
            changeProgress(progressBar, 10000);
            Intent intent = new Intent(PlayingActivity.this, FinishActivity.class);
            intent.putExtra("WORKOUT_ID", WORKOUT_ID);
            intent.putExtra("TARGET_NAME",TARGET_NAME);
            intent.putExtra("WORKOUT_NAME",WORKOUT_NAME);
            startActivity(intent);
            finish();
        } catch (Exception e) {
        }
    }

    private void initMedia(){
        String sound = list.get(currentPos).getSound();
        sound = sound.substring(0, sound.indexOf("."));
        Resources resources = getResources();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPackageName());
        stringBuilder.append(":raw/");
        stringBuilder.append(sound);
        int identifier = resources.getIdentifier(stringBuilder.toString(), null, null);
        mediaPlayer = MediaPlayer.create(this,identifier);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    private void releaseMedia(){
        if (mediaPlayer.isPlaying()){
             mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    protected void onDestroy() {
        try {
            soundManager.releaseAll();
            if (mediaPlayer!=null) releaseMedia();
            Thread.currentThread().interrupt();
        } catch (Exception e){

        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
