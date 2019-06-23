package com.example.hoang.fitness.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.WorkoutAdapter;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.JsonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutsFragment extends Fragment {
    @BindView(R.id.rcv_workout)
    RecyclerView mWorkOut;
    WorkoutAdapter workoutAdapter;
    List<Workout> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container,false);
        ButterKnife.bind(this,view);
        getActivity().findViewById(R.id.tv_title_main).setVisibility(View.VISIBLE);
        //getActivity().findViewById(R.id.btn_setting).setVisibility(View.VISIBLE);
        list = JsonUtil.getInstance().getListWorkout(getContext());
        workoutAdapter = new WorkoutAdapter(getContext(),list.subList(0,6));
        mWorkOut.setAdapter(workoutAdapter);
        mWorkOut.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mWorkOut.setNestedScrollingEnabled(false);
        mWorkOut.setHasFixedSize(false);
        return view;
    }
}
