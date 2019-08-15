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
import android.widget.LinearLayout;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.SigninActivity;
import com.example.hoang.fitness.adapters.WorkoutAdapter;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.JsonUtil;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutsFragment extends Fragment {
    @BindView(R.id.rcv_workout)
    RecyclerView mWorkOut;
    @BindView(R.id.btn_logout)
    LinearLayout mLogout;
    WorkoutAdapter workoutAdapter;
    List<Workout> list;
    private GoogleSignInClient mGoogleSignInClient;
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
        //logout
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                logoutGoogle();
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void logoutGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
