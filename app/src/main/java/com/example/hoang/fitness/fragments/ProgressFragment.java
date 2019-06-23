package com.example.hoang.fitness.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rl_calendar_bar)
    RelativeLayout mRlCalendar;
//    @BindView(R.id.rl_weight_bar)
//    RelativeLayout mRlWeight;
    @BindView(R.id.rl_target_bar)
    RelativeLayout mRlTarget;
    @BindView(R.id.tv_calandar)
    TextView mTvCalendar;
    @BindView(R.id.tv_target)
    TextView mTvTarget;
//    @BindView(R.id.tv_weight)
//    TextView mTvWeight;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this,view);
        getActivity().findViewById(R.id.tv_title_main).setVisibility(View.GONE);
        fragmentManager = getActivity().getSupportFragmentManager();
        mRlCalendar.setSelected(true);
        mTvCalendar.setTextColor(Color.parseColor("#ff0eb664"));
        mRlCalendar.setOnClickListener(this::onClick);
        //mRlWeight.setOnClickListener(this::onClick);
        mRlTarget.setOnClickListener(this::onClick);
        fragmentManager.beginTransaction().replace(R.id.place_holder,new CalendarFragment()).commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v==mRlCalendar){
            mRlCalendar.setSelected(true);
            mRlTarget.setSelected(false);
            //mRlWeight.setSelected(false);
            mTvCalendar.setTextColor(Color.parseColor("#ff0eb664"));
            //mTvWeight.setTextColor(Color.parseColor("#ffffff"));
            mTvTarget.setTextColor(Color.parseColor("#ffffff"));
            fragmentManager.beginTransaction().replace(R.id.place_holder,new CalendarFragment()).commit();
//        } else if (v==mRlWeight) {
//            mRlCalendar.setSelected(false);
//            mRlWeight.setSelected(true);
//            mRlTarget.setSelected(false);
//            mTvCalendar.setTextColor(Color.parseColor("#ffffff"));
//            mTvWeight.setTextColor(Color.parseColor("#ff0eb664"));
//            mTvTarget.setTextColor(Color.parseColor("#ffffff"));
//            fragmentManager.beginTransaction().replace(R.id.place_holder,new WeightFragment()).commit();
        } else {
            mRlCalendar.setSelected(false);
            mRlTarget.setSelected(true);
            //mRlWeight.setSelected(false);
            mTvCalendar.setTextColor(Color.parseColor("#ffffff"));
            //mTvWeight.setTextColor(Color.parseColor("#ffffff"));
            mTvTarget.setTextColor(Color.parseColor("#ff0eb664"));
            fragmentManager.beginTransaction().replace(R.id.place_holder,new TargetFragment()).commit();
        }
    }
}
