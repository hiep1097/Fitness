package com.example.hoang.fitness.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hoang.fitness.activities.AlertDialogActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlertDialogActivity.class);
        String TARGET_NAME = intent.getStringExtra("TARGET_NAME");
        i.putExtra("TARGET_NAME",TARGET_NAME);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}

