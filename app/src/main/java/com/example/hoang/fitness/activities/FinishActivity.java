package com.example.hoang.fitness.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.fragments.TargetFragment;
import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.FileUtil;
import com.example.hoang.fitness.utils.JsonUtil;
import com.example.hoang.fitness.utils.SharedPrefsUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishActivity extends AppCompatActivity {
    @BindView(R.id.tv_workout_name)
    TextView mName;
    @BindView(R.id.tv_exercise_number)
    TextView mNum;
    @BindView(R.id.tv_minute_number)
    TextView mMin;
    @BindView(R.id.tv_calories_number)
    TextView mCalo;
    @BindView(R.id.rl_save_layout)
    RelativeLayout mSave;
    private int WORKOUT_ID;
    private String WORKOUT_NAME="";
    private String TARGET_NAME;
    private List<Target> list = new ArrayList<>();
    private Target target;
    private int vt = -1;
    int WORKOUT_NUM=0;
    int MINUTES_NUM=0;
    int CALORIES_NUM=0;
    int CUR_STREAK_NUM=0;
    int BEST_STREAK_NUM=0;
    int CUR_STREAK = 0;
    int BEST_STREAK = 0;
    List<CustomWorkout> customWorkouts;
    CustomWorkout workout;
    List<Exercise> list1 = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ButterKnife.bind(this);
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
                    for (int j=0;j<workout.getCircuit();j++) this.list1.addAll(list);
                    break;
                }
        } else {
            workout = new CustomWorkout();
            Workout w = JsonUtil.getInstance().getWorkout(this, WORKOUT_ID);
            list1 = JsonUtil.getInstance().getListExercise(this, w);
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
        mName.setText(workout.getName());
        mNum.setText(workout.getExercises().size()+"");
        mMin.setText(workout.getTime()+"");
        mCalo.setText(workout.getCalorie()+"");
        readResultFromFireBase();
        calCurStreakNum();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WORKOUT_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"WORKOUT_NUM",0);
//                MINUTES_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"MINUTES_NUM",0);
//                CALORIES_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"CALORIES_NUM",0);
//                CUR_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"CUR_STREAK_NUM",0);
//                BEST_STREAK_NUM = SharedPrefsUtils.getIntegerPreference(FinishActivity.this,"BEST_STREAK_NUM",0);
//                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"WORKOUT_NUM",WORKOUT_NUM+1);
//                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"MINUTES_NUM",MINUTES_NUM+workout.getTime());
//                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"CALORIES_NUM",CALORIES_NUM+workout.getCalorie());
//                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"CUR_STREAK_NUM",CUR_STREAK_NUM);
//                SharedPrefsUtils.setIntegerPreference(FinishActivity.this,"BEST_STREAK_NUM",BEST_STREAK_NUM);
                addResultToFireBase();
                if (TARGET_NAME!=null){
                    list = FileUtil.docFileTarget(FinishActivity.this,"target.txt");
                    TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getName().equals(TARGET_NAME)) {
                            target = list.get(i);
                            vt = i;
                            break;
                        }
                    }
                    target.setState(target.getState()+1);
                    list.set(vt,target);
                    FileUtil.ghiFileTarget(FinishActivity.this,list);
                    try {
                        TargetAdapter.instance.update();
                    } catch (Exception e){

                    }
                    if (target.getState()==target.getNumDay()){
                        TargetFragment.alarmManager.cancel(
                                TargetFragment.pendingIntent[vt]
                        );
                        congrateDialog();
                        tangcoinDialog();
                    } else {
                        tangcoinDialog();
                    }
                } else {
                    tangcoinDialog();
                }
            }
        });
    }
    private void calCurStreakNum(){
        CUR_STREAK = 0;
        ArrayList<Integer> listDay = getListDay();
        ArrayList<Integer> listMonth = getListMonth();
        ArrayList<Integer> listYear = getListYear();
        if (listDay.size()!=0){
            CalendarDay calendarDay = CalendarDay.from(listYear.get(listYear.size()-1),
                    listMonth.get(listMonth.size()-1),listDay.get(listDay.size()-1));
            CalendarDay calendarToday = CalendarDay.today();
            if (!calendarDay.equals(calendarToday)) {
                addDate();
                calendarDay = CalendarDay.from(calendarDay.getDate().plusDays(1));
                if (calendarDay.equals(calendarToday)){
                    CUR_STREAK=CUR_STREAK+1;
//                    Log.d("CUR_STREAK",CUR_STREAK+" 1");
                } else {
                    CUR_STREAK = 1;
//                    Log.d("CUR_STREAK",CUR_STREAK+" 2");
                }
            }
        } else {
            CUR_STREAK=CUR_STREAK+1;
//            Log.d("CUR_STREAK",CUR_STREAK+" 3");
            addDate();
        }
    }

    //danh dau ngay tap
    private void addDate(){
        CalendarDay calendarToday = CalendarDay.today();
        addDay(calendarToday.getDay());
        addMonth(calendarToday.getMonth());
        addYear(calendarToday.getYear());
    }

    private ArrayList<Integer> getListDay(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_DAY");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }

        return list;
    }

    private ArrayList<Integer> getListMonth(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_MONTH");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }
        return list;
    }

    private ArrayList<Integer> getListYear(){
        ArrayList<Integer> list = new ArrayList<>();
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_YEAR");
        try {
            String [] s = day.split(" ");
            for (String d: s){
                list.add(Integer.parseInt(d));
            }
        } catch (NullPointerException e){

        }
        return list;
    }

    private void addDay(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_DAY");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_DAY",day_new);
    }

    private void addMonth(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_MONTH");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_MONTH",day_new);
    }

    private void addYear(int d){
        String day = SharedPrefsUtils.getStringPreference(this,"LIST_YEAR");
        String day_new = (day==null?"":day)+d+" ";
        SharedPrefsUtils.setStringPreference(this,"LIST_YEAR",day_new);
    }

    @Override
    public void onBackPressed() {
    }

    public void congrateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Mục tiêu: "+TARGET_NAME)
                .setMessage("Chúc mừng bạn đã hoàn thành mục tiêu "+TARGET_NAME)
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(FinishActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void tangcoinDialog(){
        int totalCoins = SharedPrefsUtils.getIntegerPreference(this,"totalcoin",0);
        SharedPrefsUtils.setIntegerPreference(this,"totalcoin",totalCoins+workout.getCalorie()*10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Congratulate!")
                .setMessage("Chúc mừng bạn đã được tặng "+workout.getCalorie()*10+" coins!")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(FinishActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void readResultFromFireBase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("result");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long WORKOUT_NUM = (long) dataSnapshot.child("WORKOUT_NUM").getValue();
                    long MINUTES_NUM = (long) dataSnapshot.child("MINUTES_NUM").getValue();
                    long CALORIES_NUM = (long) dataSnapshot.child("CALORIES_NUM").getValue();
                    long CUR_STREAK_NUM = (long) dataSnapshot.child("CUR_STREAK_NUM").getValue();
                    long BEST_STREAK_NUM = (long) dataSnapshot.child("BEST_STREAK_NUM").getValue();
                    FinishActivity.this.WORKOUT_NUM = (int) WORKOUT_NUM;
                    FinishActivity.this.MINUTES_NUM = (int) MINUTES_NUM;
                    FinishActivity.this.CALORIES_NUM = (int) CALORIES_NUM;
                    FinishActivity.this.CUR_STREAK_NUM = (int) CUR_STREAK_NUM;
                    FinishActivity.this.BEST_STREAK_NUM = (int) BEST_STREAK_NUM;
                    //Toast.makeText(FinishActivity.this,"Loaded",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addResultToFireBase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("result");
        myRef.child("WORKOUT_NUM").setValue(WORKOUT_NUM+1);
        myRef.child("MINUTES_NUM").setValue(MINUTES_NUM+workout.getTime());
        myRef.child("CALORIES_NUM").setValue(CALORIES_NUM+workout.getCalorie());
        if (CUR_STREAK==1 || CUR_STREAK==3){
            myRef.child("CUR_STREAK_NUM").setValue(CUR_STREAK_NUM+1);
            if (CUR_STREAK_NUM+1>BEST_STREAK) BEST_STREAK= CUR_STREAK_NUM+1;
        } else{
            myRef.child("CUR_STREAK_NUM").setValue(1);
            if (1>BEST_STREAK) BEST_STREAK= 1;
        }
        myRef.child("BEST_STREAK_NUM").setValue(BEST_STREAK);
        Log.d("CUR_STREAK",CUR_STREAK+" 5");
        Toast.makeText(FinishActivity.this,"Added",Toast.LENGTH_SHORT).show();
    }
}
