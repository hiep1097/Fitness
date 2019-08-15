package com.example.hoang.fitness.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.fragments.TargetFragment;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.utils.AssetsUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogActivity extends Activity {
    private String TARGET_NAME;
    private List<Target> list = new ArrayList<>();
    private Target target;
    private int vt = -1;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //list = FileUtil.docFileTarget(this,"target.txt");
        builder = new AlertDialog.Builder(AlertDialogActivity.this);
        getListTargetFromFireBase();
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

    public void updateListTarget(List<Target> list) {
        try {
            this.list.clear();
            this.list.addAll(list);
            TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
            for (int i=0;i<list.size();i++){
                if (list.get(i).getName().equals(TARGET_NAME)) {
                    target = list.get(i);
                    vt = i;
                    break;
                }
            }
            builder
                    .setTitle("Mục tiêu: "+TARGET_NAME)
                    .setIcon(AssetsUtil.getDrawable(
                            AlertDialogActivity.this,"workout_pic/"+"7m_beginner.webp"))
                    .setMessage("Bạn có muốn bắt đầu luyện tập?")
                    .setCancelable(false)
                    .setPositiveButton("Bắt đầu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(
                                    AlertDialogActivity.this,PlayingActivity.class);
                            intent.putExtra("WORKOUT_ID",target.getWorkout().getId());
                            intent.putExtra("TARGET_NAME",TARGET_NAME);
                            startActivity(intent);
                            dialog.cancel();
                            finish();
                        }
                    })
                    .setNegativeButton("Hủy mục tiêu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            target.setState(-1);
                            list.set(vt,target);
                            //FileUtil.ghiFileTarget(AlertDialogActivity.this,list);
                            addTargetToFireBase(target);
                            try {
                                TargetAdapter.instance.update();
                                TargetFragment.alarmManager.cancel(TargetFragment.pendingIntent[vt]);
                            } catch (Exception e){

                            }
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e){

        }

    }
}