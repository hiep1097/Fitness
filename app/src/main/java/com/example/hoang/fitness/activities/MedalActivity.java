package com.example.hoang.fitness.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.adapters.MedalAdapter;
import com.example.hoang.fitness.decoration.MyItemDecoration;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.Medal;
import com.example.hoang.fitness.utils.DrawableUtil;
import com.example.hoang.fitness.utils.FileUtil;
import com.example.hoang.fitness.utils.SharedPrefsUtils;

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
    List<Medal> list;
    int totalCoint;
    int currentPos;
    boolean isInitMedal = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal);
        ButterKnife.bind(this);
        isInitMedal = SharedPrefsUtils.getBooleanPreference(this,"isinitmedal",false);
        Log.d("INITTTTTTTTT",isInitMedal+"");
        if (!isInitMedal){
            FileUtil.initMedal(this);
            isInitMedal = true;
            SharedPrefsUtils.setBooleanPreference(this,"isinitmedal",true);

        }
        list = FileUtil.docFileMedal(this,"medal.txt");
        adapter = new MedalAdapter(this,list, this::clickItem);
        mMedals.setAdapter(adapter);
        mMedals.setLayoutManager(new GridLayoutManager(this,3));
        mMedals.addItemDecoration(new MyItemDecoration(this, R.dimen.item_offset));
        mExit.setOnClickListener(l->finish());
        totalCoint = SharedPrefsUtils.getIntegerPreference(this,"totalcoin",0);
        mTotalCoin.setText(totalCoint+"");
        totalCoint = SharedPrefsUtils.getIntegerPreference(this,"totalcoin",0);
        currentPos = SharedPrefsUtils.getIntegerPreference(this,"medalpos",0);
        mChoose.setOnClickListener(l->{
            int coin = list.get(currentPos).getPrice();
            if (totalCoint>=coin ||  list.get(currentPos).isDaMua()){
                if (!list.get(currentPos).isDaMua()) {
                    totalCoint -= coin;
                    SharedPrefsUtils.setIntegerPreference(this,"totalcoin",totalCoint);
                }
                List<Medal> ll = new ArrayList<>();
                ll.addAll(list);
                ll.get(currentPos).setDaMua(true);
                Toast.makeText(MedalActivity.this,"Thành công!",Toast.LENGTH_SHORT).show();
                SharedPrefsUtils.setIntegerPreference(MedalActivity.this,"medalpos",currentPos);
                SharedPrefsUtils.setStringPreference(MedalActivity.this,
                        "medal",list.get(currentPos).getImage());
                MainActivity.updateMedal(DrawableUtil.getInstance().getDrawable(
                        MedalActivity.this,list.get(currentPos).getImage()));
                mTotalCoin.setText(totalCoint+"");
                FileUtil.ghiFileMedal(MedalActivity.this,ll);
                updateView(ll);
            } else {
                Toast.makeText(MedalActivity.this,"Không đủ coin! Hãy tập thêm để nhận coin.",Toast.LENGTH_SHORT).show();
            }
        });
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
}
