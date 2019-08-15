package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.PickExerciseAdapter;
import com.example.hoang.fitness.adapters.PickExerciseAdapter2;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.ExercisePick;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.models.WorkoutExercise;
import com.example.hoang.fitness.utils.JsonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickExerciseActivity extends AppCompatActivity implements View.OnClickListener, ItemOnClick {
    @BindView(R.id.tv_pick_exercise_type)
    TextView mType;
    @BindView(R.id.rcv_pick_exercise)
    RecyclerView mPickExercises;
    @BindView(R.id.btn_nextstep)
    Button mNextStep;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.rl2)
    RelativeLayout mRl2;
    @BindView(R.id.et_workout_name)
    EditText mName;
    @BindView(R.id.rl_one_layout)
    RelativeLayout mCycle1;
    @BindView(R.id.rl_two_layout)
    RelativeLayout mCycle2;
    @BindView(R.id.rl_three_layout)
    RelativeLayout mCycle3;
    @BindView(R.id.rl_5seonds_layout)
    RelativeLayout mRest5;
    @BindView(R.id.rl_10seonds_layout)
    RelativeLayout mRest10;
    @BindView(R.id.rl_15seconds_layout)
    RelativeLayout mRest15;
    @BindView(R.id.tv_minutes_number)
    TextView mMinutes;
    @BindView(R.id.tv_calories_number)
    TextView mCalories;
    @BindView(R.id.btn_saveworkout)
    TextView mSaveWorkout;
    List<ExercisePick> list;
    PickExerciseAdapter adapter;
    PickExerciseAdapter2 adapter2;
    private int mStep = 1;
    private int count[] = new int[10];
    private ArrayList<Integer> dd[] = new ArrayList[10];
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();
    TextView tv_title_toolbar;
    TextView tv_cancel_toolbar;
    int min = 0;
    float calo = 0;
    int cycle = 1;
    int rest = 5;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_exercise);
        ButterKnife.bind(this);
        mType.setText(getDescription(mStep));
        list = new ArrayList<>();
        for (int i=1;i<=6;i++) {
            count[i] = 0;
            dd[i] = new ArrayList<Integer>();
        }
        for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
            list.add(new ExercisePick(e,false));
        }
        adapter = new PickExerciseAdapter(this,list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter);
        mPickExercises.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_title_toolbar = mToolbar.findViewById(R.id.tv_title_toolbar);
        tv_cancel_toolbar = mToolbar.findViewById(R.id.tv_cancel_toolbar);
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");
        tv_cancel_toolbar.setText("Cancel");
        mNextStep.setOnClickListener(this::onClick);
        mNextStep.setAlpha((float) 0.5);
        mNextStep.setClickable(false);
        mCycle1.setSelected(true);
        mCycle2.setSelected(false);
        mCycle3.setSelected(false);
        mRest5.setSelected(true);
        mRest10.setSelected(false);
        mRest15.setSelected(false);
        mCycle1.setOnClickListener(this::onClick);
        mCycle2.setOnClickListener(this::onClick);
        mCycle3.setOnClickListener(this::onClick);
        mRest5.setOnClickListener(this::onClick);
        mRest10.setOnClickListener(this::onClick);
        mRest15.setOnClickListener(this::onClick);
        mSaveWorkout.setOnClickListener(this::onClick);
        mSaveWorkout.setAlpha((float)0.5);
        mSaveWorkout.setClickable(false);
        tv_cancel_toolbar.setOnClickListener(l->finish());
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    mSaveWorkout.setAlpha(1);
                    mSaveWorkout.setClickable(true);
                } else {
                    mSaveWorkout.setAlpha((float)0.5);
                    mSaveWorkout.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private String getBodyPart(int i) {
        String str = "TOTAL BODY";
        switch (i) {
            case 1:
                return "TOTAL BODY";
            case 2:
                return "LOWER BODY";
            case 3:
                return "UPPER BODY";
            case 4:
                return "CORE";
            default:
                return str;
        }
    }

    private String getDescription(int i) {
        String str = "";
        switch (i) {
            case 1:
                return "pick 3 total body exercises";
            case 2:
                return "pick 3 lower body exercises";
            case 3:
                return "pick 3 upper body exercises";
            case 4:
                return "pick 3 core exercises";
            case 5:
                return "confirm workout order";
            default:
                return str;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if (mStep>1) {
            mStep--;
            mRl2.setVisibility(View.GONE);
            mRl1.setVisibility(View.VISIBLE);
            if (mStep==5){
                List<ExercisePick> list = new ArrayList<>();
                for (int i=1;i<=4;i++){
                    List<ExercisePick> lists = new ArrayList<>();
                    for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(i))){
                        lists.add(new ExercisePick(e,false));
                    }
                    for (int j=0;j<dd[i].size();j++) list.add(lists.get(dd[i].get(j)));
                }
                updateView2(list);
            } else {
                List<ExercisePick> list = new ArrayList<>();
                for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
                    list.add(new ExercisePick(e,false));
                }
                for (int i=0;i<dd[mStep].size();i++)
                    list.get(dd[mStep].get(i)).setChoose(true);
                updateView(list);
                if (count[mStep]==3){
                    mNextStep.setAlpha(1);
                    mNextStep.setClickable(true);
                } else {
                    mNextStep.setAlpha((float) 0.5);
                    mNextStep.setClickable(false);
                }
            }
        } else {
            finish();
        }
    }

    public void updateView(List<ExercisePick> list){
        this.list.clear();
        this.list.addAll(list);
        adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");

    }

    public void updateView2(List<ExercisePick> list){
        this.list.clear();
        this.list.addAll(list);
        adapter2 = new PickExerciseAdapter2(this,this.list,this::clickItem,count[mStep]);
        mPickExercises.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");
        mType.setText(getDescription(mStep));
        mNextStep.setAlpha(1);
        mNextStep.setClickable(true);
    }

    public void updateView3(List<ExercisePick> list){
        tv_title_toolbar.setText("STEP "+mStep+" OF 6");
        List<Workout> workouts = JsonUtil.getInstance().getListWorkout(this);
        List<WorkoutExercise> exercises = new ArrayList<>();
        for (Workout w: workouts){
            exercises.addAll(w.getExercises());
        }
        for (int i=0;i<list.size();i++){
            int id = list.get(i).getExercise().getId();
            for (WorkoutExercise e: exercises){
                if (e.getId()==id){
                    workoutExercises.add(e);
                    min+= e.getTimeSpan();
                    Exercise exercise = JsonUtil.getInstance().getExercise(this,id);
                    calo += exercise.getCalorie();
                    break;
                }
            }
        }
        updateMin();
        updateCalo();
    }

    @Override
    public void onClick(View v) {
        if (v==mNextStep){
            mNextStep.setClickable(false);
            mNextStep.setAlpha((float) 0.5);
            mStep++;
            if (mStep==6){
                mRl1.setVisibility(View.GONE);
                mRl2.setVisibility(View.VISIBLE);
                List<ExercisePick> list = new ArrayList<>();
                for (int i=1;i<=4;i++){
                    List<ExercisePick> lists = new ArrayList<>();
                    for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(i))){
                        lists.add(new ExercisePick(e,false));
                    }
                    for (int j=0;j<dd[i].size();j++) list.add(lists.get(dd[i].get(j)));
                }
                updateView3(list);
            } else {
                mRl2.setVisibility(View.GONE);
                mRl1.setVisibility(View.VISIBLE);
                if (mStep==5){
                    List<ExercisePick> list = new ArrayList<>();
                    for (int i=1;i<=4;i++){
                        List<ExercisePick> lists = new ArrayList<>();
                        for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(i))){
                            lists.add(new ExercisePick(e,false));
                        }
                        for (int j=0;j<dd[i].size();j++) list.add(lists.get(dd[i].get(j)));
                    }
                    updateView2(list);
                } else {
                    List<ExercisePick> list = new ArrayList<>();
                    for (Exercise e: JsonUtil.getInstance().getListExercise(this,getBodyPart(mStep))){
                        list.add(new ExercisePick(e,false));
                    }
                    for (int i=0;i<dd[mStep].size();i++)
                        list.get(dd[mStep].get(i)).setChoose(true);
                    updateView(list);
                }
            }
        } else if (v==mCycle1){
            mCycle1.setSelected(true);
            mCycle2.setSelected(false);
            mCycle3.setSelected(false);
            cycle = 1;
            updateMin();
            updateCalo();
        } else if (v==mCycle2){
            mCycle2.setSelected(true);
            mCycle1.setSelected(false);
            mCycle3.setSelected(false);
            cycle = 2;
            updateMin();
            updateCalo();

        } else if (v==mCycle3){
            mCycle3.setSelected(true);
            mCycle1.setSelected(false);
            mCycle2.setSelected(false);
            cycle = 3;
            updateMin();
            updateCalo();

        } else if (v==mRest5){
            mRest5.setSelected(true);
            mRest10.setSelected(false);
            mRest15.setSelected(false);
            rest = 5;
            updateMin();

        } else if (v==mRest10){
            mRest10.setSelected(true);
            mRest5.setSelected(false);
            mRest15.setSelected(false);
            rest = 10;
            updateMin();

        } else if (v==mRest15){
            mRest15.setSelected(true);
            mRest10.setSelected(false);
            mRest5.setSelected(false);
            rest = 15;
            updateMin();
        } else if (v==mSaveWorkout){
            addCustomWorkoutToFireBase(createWorkout());
            //List<CustomWorkout> list = FileUtil.docFileCustomWorkout(this,"customworkout.txt");

            //list.add(createWorkout());
            //FileUtil.ghiFileCustomWorkout(this,list);
            try{

            } catch (Exception e){

            }
            finish();
        }
    }

    public CustomWorkout createWorkout(){
        CustomWorkout workout = new CustomWorkout();
        workout.setName(mName.getText().toString());
        workout.setLevel("");
        workout.setPicPad("");
        workout.setPicPhone("");
        workout.setBrief("");
        workout.setOrder(Integer.valueOf(1));
        workout.setTime(Integer.valueOf(mMinutes.getText().toString()));
        workout.setExercises(this.workoutExercises);
        workout.setCalorie(Integer.valueOf(mCalories.getText().toString()));
        workout.setType("Custom");
        workout.setId(Integer.valueOf(0));
        workout.setCircuit(Integer.valueOf(this.cycle));
        workout.setWorkoutRestTime(Integer.valueOf(this.rest));
        return workout;
    }

    public void addCustomWorkoutToFireBase(CustomWorkout workout) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("customWorkouts");
        myRef.child(workout.getName()).setValue(workout);
    }

    void updateMin(){
        int total = min*cycle;
        total += rest*12;
        total = Math.round((float)( total/60));
        mMinutes.setText(total+"");

    }

    void updateCalo(){
        float total = calo*cycle;
        mCalories.setText(Math.round(total)+"");
    }

    @Override
    public void clickItem(int pos, boolean isChoose) {
        if (isChoose) {
            count[mStep]++;
            dd[mStep].add(pos);
        }
        else {
            count[mStep]--;
            for (int i=0;i<dd[mStep].size();i++)
                if (dd[mStep].get(i)==pos){
                    dd[mStep].remove(i);
                    break;
                }

        }
        list.get(pos).setChoose(isChoose);
        if (count[mStep]==3){
            mNextStep.setAlpha(1);
            mNextStep.setClickable(true);
            adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
            mPickExercises.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//            Toast.makeText(PickExerciseActivity.this,"3",Toast.LENGTH_SHORT).show();
        } else {
            mNextStep.setAlpha((float) 0.5);
            mNextStep.setClickable(false);
            adapter = new PickExerciseAdapter(this,this.list,this::clickItem,count[mStep]);
            mPickExercises.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//            Toast.makeText(PickExerciseActivity.this,"<3",Toast.LENGTH_SHORT).show();
        }
    }
}
