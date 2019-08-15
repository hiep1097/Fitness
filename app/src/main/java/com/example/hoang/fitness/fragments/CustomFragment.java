package com.example.hoang.fitness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.PickExerciseActivity;
import com.example.hoang.fitness.adapters.CustomWorkoutAdapter;
import com.example.hoang.fitness.models.CustomWorkout;
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

public class CustomFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rcv_custom_workout)
    RecyclerView mCustomWorkouts;
    @BindView(R.id.btn_floating_custom_workout)
    ImageView mAddCustomWorkouts;
    @BindView(R.id.tv_tap_to_start)
    TextView mTapToStart;
    private CustomWorkoutAdapter adapter;
    private List<CustomWorkout> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        ButterKnife.bind(this,view);
        getActivity().findViewById(R.id.tv_title_main).setVisibility(View.GONE);
        getListCustomWorkoutFromFireBase();
        //list = FileUtil.docFileCustomWorkout(this.getContext(),"customworkout.txt");
        adapter = new CustomWorkoutAdapter(getContext(),list);
        mCustomWorkouts.setAdapter(adapter);
        mCustomWorkouts.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAddCustomWorkouts.setOnClickListener(this::onClick);
        if (list.size()==0){
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onStart() {
        //List<CustomWorkout> list = FileUtil.docFileCustomWorkout(this.getContext(),"customworkout.txt");
        getListCustomWorkoutFromFireBase();
        updateListCustomWorkout(list);
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), PickExerciseActivity.class);
        startActivity(intent);
        if (list.size()==0){
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }

    }

    public void getListCustomWorkoutFromFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("customWorkouts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CustomWorkout> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    CustomWorkout value = data.getValue(CustomWorkout.class);
                    list.add(value);
                }
                updateListCustomWorkout(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateListCustomWorkout(List<CustomWorkout> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter = new CustomWorkoutAdapter(getContext(), this.list);
        mCustomWorkouts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }
    }
}
