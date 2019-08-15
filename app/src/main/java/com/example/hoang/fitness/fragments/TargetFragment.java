package com.example.hoang.fitness.fragments;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.TargetAdapter;
import com.example.hoang.fitness.models.Target;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.receiver.AlarmReceiver;
import com.example.hoang.fitness.utils.JsonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TargetFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rcv_target)
    RecyclerView mTargets;
    @BindView(R.id.btn_floating_target)
    ImageView mAddTarget;
    @BindView(R.id.tv_tap_to_start)
    TextView mTapToStart;
    private TargetAdapter adapter;
    private List<Target> list = new ArrayList<>();
    private Dialog dialogTarget;
    private EditText mName;
    private EditText mNumday;
    private Spinner spinner;
    private TimePicker timePicker;
    private Button mDongy;
    private Button mCancel;
    String arr[] = new String[6];
    int wid[] = new int[]{96, 3, 4, 5, 30, 31};
    public static AlarmManager alarmManager;
    Intent intent;
    public static PendingIntent pendingIntent[] = new PendingIntent[1000];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target, container, false);
        ButterKnife.bind(this, view);
        for (int i = 0; i < 6; i++) {
            arr[i] = JsonUtil.getInstance().getListWorkout(getContext()).get(i).getName();
        }
        setDialogTarget();
        //list = FileUtil.docFileTarget(getActivity(),"target.txt");
        getListTargetFromFireBase();
        adapter = new TargetAdapter(getContext(), list);
        mTargets.setAdapter(adapter);
        mTargets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAddTarget.setOnClickListener(this::onClick);
        if (list.size() == 0) {
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(getContext(), AlarmReceiver.class);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mAddTarget) {
            dialogTarget.show();
        } else if (v == mDongy) {
            addTarget();
            //FileUtil.ghiFileTarget(getActivity(), list);
            dialogTarget.dismiss();
        } else if (v == mCancel) {
            dialogTarget.dismiss();
        }
        if (list.size() == 0) {
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }
    }

    public void setDialogTarget() {
        dialogTarget = new Dialog(getContext());
        dialogTarget.setContentView(R.layout.dialog_target);
        dialogTarget.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mName = dialogTarget.findViewById(R.id.edt_target_name);
        mNumday = dialogTarget.findViewById(R.id.edt_num_day);
        spinner = dialogTarget.findViewById(R.id.spinner);
        timePicker = dialogTarget.findViewById(R.id.time_picker);
        mCancel = dialogTarget.findViewById(R.id.btn_cancel);
        mDongy = dialogTarget.findViewById(R.id.btn_dongy);
        mDongy.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                arr
        );
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void addTarget() {
        try {
            String name = mName.getText().toString();
            Workout workout = JsonUtil.getInstance().getWorkout(getContext(), wid[spinner.getSelectedItemPosition()]);
            int numDay = Integer.parseInt(mNumday.getText().toString());
            String am_pm = null;
            String hour = null;
            int x = timePicker.getHour();
            if (timePicker.getHour() <= 12) {
                am_pm = "AM";
            } else {
                am_pm = "PM";
                x -= 12;
            }

            if (x < 10) {
                hour = "0" + x;
            } else {
                hour = x + "";
            }
            String minute = null;
            if (timePicker.getMinute() < 10) {
                minute = "0" + timePicker.getMinute();
            } else {
                minute = timePicker.getMinute() + "";
            }
            int state = 0;
            Target target = new Target(name, workout, numDay, hour, minute, am_pm, state);
            list.add(target);
            adapter.notifyDataSetChanged();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            calendar.set(Calendar.SECOND, 0);
            intent.putExtra("TARGET_NAME", target.getName());
            pendingIntent[list.size() - 1] = PendingIntent.getBroadcast(getActivity(),
                    list.size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent[list.size() - 1]);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(), 3600 * 24 * 1000, pendingIntent[list.size() - 1]);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent[list.size() - 1]);
            addTargetToFireBase(target);
        } catch (Exception e) {

        }
    }

    public void updateListTarget(List<Target> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter = new TargetAdapter(getContext(), this.list);
        mTargets.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            mTapToStart.setVisibility(View.VISIBLE);
        } else {
            mTapToStart.setVisibility(View.GONE);
        }
    }

    public void addTargetToFireBase(Target target) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myRef = currentUserDB.child("targets");
        myRef.child(target.getName()).setValue(target);
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
}
