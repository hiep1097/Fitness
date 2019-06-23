package com.example.hoang.fitness.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.fragments.CustomFragment;
import com.example.hoang.fitness.fragments.ProgressFragment;
import com.example.hoang.fitness.fragments.WorkoutsFragment;
import com.example.hoang.fitness.utils.DrawableUtil;
import com.example.hoang.fitness.utils.SharedPrefsUtils;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    static ImageView mMedal;
    public static final int TAB_WORKOUTS = 0;
    public static final int TAB_CUSTOM = 1;
    public static final int TAB_PROGRESS = 2;
    private List<Fragment> listFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_workouts:
                openTab(TAB_WORKOUTS);
                return true;
            case R.id.navigation_custom:
                openTab(TAB_CUSTOM);
                return true;
            case R.id.navigation_progress:
                openTab(TAB_PROGRESS);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMedal = findViewById(R.id.btn_medal);
        try {
            String medal = SharedPrefsUtils.getStringPreference(this,"medal");
            mMedal.setImageDrawable(DrawableUtil.getInstance().getDrawable(this,medal));
        } catch (Exception e){

        }

        mMedal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MedalActivity.class);
                startActivity(intent);
            }
        });
        FirebaseApp.initializeApp(getApplicationContext());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragmet();
        openTab(TAB_WORKOUTS);
    }

    private void initFragmet(){
        listFragment = new ArrayList<>();
        listFragment.add(new WorkoutsFragment());
        listFragment.add(new CustomFragment());
        listFragment.add(new ProgressFragment());
    }
    private void openTab(int index){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fr_home, listFragment.get(index))
                .commit();
    }

    public static void updateMedal(Drawable drawable){
        mMedal.setImageDrawable(drawable);
    }
}
