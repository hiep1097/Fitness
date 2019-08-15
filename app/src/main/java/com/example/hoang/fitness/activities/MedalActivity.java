package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.MedalAdapter;
import com.example.hoang.fitness.decoration.MyItemDecoration;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.Medal;
import com.example.hoang.fitness.utils.DrawableUtil;
import com.example.hoang.fitness.utils.SharedPrefsUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedalActivity extends AppCompatActivity implements ItemOnClick {
    @BindView(R.id.rcv_medal)
    RecyclerView mMedals;
    @BindView(R.id.tv_total_coin)
    TextView mTotalCoin;
    @BindView(R.id.btnChoose)
    Button mChoose;
    @BindView(R.id.btnExit)
    Button mExit;
    MedalAdapter adapter;
    List<Medal> list = new ArrayList<>();
    int totalCoint=0;
    int currentPos=0;
    boolean isInitMedal;
    DatabaseReference myMedalRef, myCoinsRef, myMedalPosRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal);
        ButterKnife.bind(this);
        accessDBFireBase();
        readCoinsFromFireBase();
        readMedalPosFromFireBase();
        isInitMedal = SharedPrefsUtils.getBooleanPreference(this,user.getUid()+"IS_INIT_MEDAL",false);
        if (!isInitMedal){
            isInitMedal = true;
            SharedPrefsUtils.setBooleanPreference(this,user.getUid()+"IS_INIT_MEDAL",true);
            initMedal();
        }
        getListFromFirebase();
        adapter = new MedalAdapter(this,list, this::clickItem);
        mMedals.setAdapter(adapter);
        mMedals.setLayoutManager(new GridLayoutManager(this,3));
        mMedals.addItemDecoration(new MyItemDecoration(this, R.dimen.item_offset));
        mExit.setOnClickListener(l->finish());
        readCoinsFromFireBase();
        readMedalPosFromFireBase();
        mChoose.setOnClickListener(l->{
            int coin = list.get(currentPos).getPrice();
            if (totalCoint>=coin ||  list.get(currentPos).isDaMua()){
                if (!list.get(currentPos).isDaMua()) {
                    totalCoint -= coin;
                    addCoinsToFireBase();
                }
                Medal medal = list.get(currentPos);
                medal.setDaMua(true);
                updateItemAt(currentPos,medal);
                Toast.makeText(MedalActivity.this,"Thành công!",Toast.LENGTH_SHORT).show();
                addMedalPosToFireBase();
                MainActivity.updateMedal(DrawableUtil.getInstance().getDrawable(
                        MedalActivity.this,list.get(currentPos).getImage()));
                mTotalCoin.setText(totalCoint+"");
                SharedPrefsUtils.setStringPreference(this,user.getUid()+"medal",list.get(currentPos).getImage());
            } else {
                Toast.makeText(MedalActivity.this,"Không đủ coin! Hãy tập thêm để nhận coin.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readCoinsFromFireBase(){
        myCoinsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long totalCoint = (long) dataSnapshot.child("NUM_COINS").getValue();
                    MedalActivity.this.totalCoint = (int) totalCoint;
                    mTotalCoin.setText(totalCoint+"");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addCoinsToFireBase(){
        myCoinsRef.child("NUM_COINS").setValue(totalCoint);
    }

    private void readMedalPosFromFireBase(){
        myMedalPosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long currentPos = (long) dataSnapshot.child("MEDAL_POS").getValue();
                    MedalActivity.this.currentPos = (int) currentPos;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMedalPosToFireBase(){
        myMedalPosRef.child("MEDAL_POS").setValue(currentPos);
    }



    private void updateView(List<Medal> list){
        this.list.clear();
        this.list.addAll(list);
        adapter = new MedalAdapter(this,this.list,this::clickItem);
        mMedals.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clickItem(int pos, boolean isChoose) {
        currentPos = pos;
    }

    private void accessDBFireBase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        myMedalRef = currentUserDB.child("medals");
        myCoinsRef = currentUserDB.child("coins");
        myMedalPosRef = currentUserDB.child("medalpos");
    }

    private void getListFromFirebase(){
        List<Medal> list = new ArrayList<>();
        myMedalRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Medal value = dataSnapshot.getValue(Medal.class);
                value.setId(dataSnapshot.getKey());
                list.add(value);
                updateView(list);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Medal value = dataSnapshot.getValue(Medal.class);
                String key = dataSnapshot.getKey();
                int vt = -1;
                for (int i=0;i<list.size();i++)
                    if ((list.get(i).getId()+"").equals(key)){
                        vt = i;
                        break;
                    }
                list.set(vt,value);
                updateView(list);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int vt = -1;
                for (int i=0;i<list.size();i++)
                    if ((list.get(i).getId()+"").equals(key)){
                        vt = i;
                        break;
                    }
                list.remove(vt);
                updateView(list);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addToList(Medal medal){
        myMedalRef.push().setValue(medal);
    }

    private void removeItemAt(int position){
        myMedalRef.child(list.get(position).getId()+"").removeValue();
    }

    private void updateItemAt(int position, Medal medal){
        myMedalRef.child(list.get(position).getId()+"").setValue(medal);
    }

    private void initMedal(){
        readMedalPosFromFireBase();
        addToList(new Medal("ic_bronze_medal",0, true));
        addToList(new Medal("ic_silver_medal",1000, false));
        addToList(new Medal("ic_gold_medal",2000, false));
        addToList(new Medal("ic_ta_dong",500, false));
        addToList(new Medal("ic_ta_bac",1000,false));
        addToList(new Medal("ic_ta_vang",2000, false));
    }
}
