package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.models.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTargetActivity extends AppCompatActivity {
    @BindView(R.id.tv_target_name)
    TextView mName;
    @BindView(R.id.tv_target_time)
    TextView mTime;
    @BindView(R.id.tv_day_number)
    TextView mDay;
    @BindView(R.id.tv_minute_number)
    TextView mMin;
    @BindView(R.id.tv_calories_number)
    TextView mCalo;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_state)
    TextView mState;
    @BindView(R.id.rl_save_layout)
    RelativeLayout mSave;
    @BindView(R.id.rl_huy_layout)
    RelativeLayout mHuy;
    private String TARGET_NAME;
    private List<Target> list = new ArrayList<>();
    private Target target;
    private int vt = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_target);
        ButterKnife.bind(this);
        //list = FileUtil.docFileTarget(this,"target.txt");
        getListTargetFromFireBase();
    }

    private void solve() {
        TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
        for (int i=0;i<list.size();i++){
            if (list.get(i).getName().equals(TARGET_NAME)) {
                target = list.get(i);
                vt = i;
                break;
            }
        }
        mName.setText(target.getName());
        mTime.setText(target.getHour()+":"+target.getMinute()+" "+target.getAm_pm());
        if (target.getState()==0){
            mState.setText("Bắt đầu");
            mDay.setText(target.getState()+"/"+target.getNumDay());
            mMin.setText((target.getState()*target.getWorkout().getTime())+"/"+target.getWorkout().getTime()*target.getNumDay());
            mCalo.setText((target.getState()*target.getWorkout().getCalorie())+"/"+target.getWorkout().getCalorie()*target.getNumDay());
            mHuy.setVisibility(View.VISIBLE);
            changeProgress(progressBar, target.getState());
        } else if (target.getState()==-1){
            int state = 0;
            mState.setText("Không hoàn thành");
            mDay.setText(state+"/"+target.getNumDay());
            mMin.setText((state*target.getWorkout().getTime())+"/"+target.getWorkout().getTime()*target.getNumDay());
            mCalo.setText((state*target.getWorkout().getCalorie())+"/"+target.getWorkout().getCalorie()*target.getNumDay());
            mHuy.setVisibility(View.GONE);
            changeProgress(progressBar, state);
        } else if (target.getState()==target.getNumDay()){
            mState.setText("Đã hoàn thành");
            mDay.setText(target.getState()+"/"+target.getNumDay());
            mMin.setText((target.getState()*target.getWorkout().getTime())+"/"+target.getWorkout().getTime()*target.getNumDay());
            mCalo.setText((target.getState()*target.getWorkout().getCalorie())+"/"+target.getWorkout().getCalorie()*target.getNumDay());
            mHuy.setVisibility(View.GONE);
            changeProgress(progressBar, target.getState());
        } else {
            mState.setText("Tiến độ: "+target.getState()+"/"+target.getNumDay());
            mDay.setText(target.getState()+"/"+target.getNumDay());
            mMin.setText((target.getState()*target.getWorkout().getTime())+"/"+target.getWorkout().getTime()*target.getNumDay());
            mCalo.setText((target.getState()*target.getWorkout().getCalorie())+"/"+target.getWorkout().getCalorie()*target.getNumDay());
            mHuy.setVisibility(View.VISIBLE);
            changeProgress(progressBar, target.getState());
        }
        mSave.setOnClickListener(l->finish());
        mHuy.setOnClickListener(l->{
            target.setState(-1);
            list.set(vt,target);
            //FileUtil.ghiFileTarget(this,list);
            addTargetToFireBase(target);
            finish();
            TargetAdapter.instance.update();
        });
    }

    private int getProgressValue(int i) {
        return 10000 / target.getNumDay() * i;
    }

    private void changeProgress(ProgressBar progressBar, int i) {
        progressBar.setProgress(getProgressValue(i));
    }

    public void updateListTarget(List<Target> list) {
        this.list.clear();
        this.list.addAll(list);
        solve();
    }

    public void getListTargetFromFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("targets");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Target> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Target value = data.getValue(Target.class);
                    list.add(value);
                }
                updateListTarget(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTargetToFireBase(Target target) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("targets");
        myRef.child(target.getName()).setValue(target);
    }
}
