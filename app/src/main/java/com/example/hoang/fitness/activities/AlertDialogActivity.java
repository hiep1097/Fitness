package com.example.hoang.fitness.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.fragments.TargetFragment;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.utils.AssetsUtil;
import com.example.hoang.fitness.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogActivity extends Activity {
    private String TARGET_NAME;
    private List<Target> list = new ArrayList<>();
    private Target target;
    private int vt = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = FileUtil.docFileTarget(this,"target.txt");
        TARGET_NAME = getIntent().getStringExtra("TARGET_NAME");
        for (int i=0;i<list.size();i++){
            if (list.get(i).getName().equals(TARGET_NAME)) {
                target = list.get(i);
                vt = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        FileUtil.ghiFileTarget(AlertDialogActivity.this,list);
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